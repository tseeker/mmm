package mmm.recipes;


import java.util.ArrayList;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_RecipeRequirements;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.OreDictionary;



public class RShapedRecipeWrapper
		implements I_CraftingRecipeWrapper
{
	private final ShapedRecipes recipe;
	private final RRequirements requirements;


	public RShapedRecipeWrapper( final ShapedRecipes recipe )
	{
		this.recipe = recipe;
		this.requirements = new RRequirements( RShapedRecipeWrapper.extractInputs( recipe ) );
	}


	private static ArrayList< ItemStack > extractInputs( final ShapedRecipes recipe )
	{
		final ArrayList< ItemStack > inputs = new ArrayList<>( );
		for ( int i = 0 ; i < recipe.recipeWidth ; i++ ) {
			RECIPE: for ( int j = 0 ; j < recipe.recipeHeight ; j++ ) {
				ItemStack itemStack = recipe.recipeItems[ i + j * recipe.recipeWidth ];
				if ( itemStack == null ) {
					continue;
				}

				for ( int k = 0 ; k < inputs.size( ) ; k++ ) {
					final ItemStack input = inputs.get( k );
					if ( OreDictionary.itemMatches( input , itemStack , true ) ) {
						input.stackSize++;
						continue RECIPE;
					}
				}

				itemStack = itemStack.copy( );
				itemStack.stackSize = 1;
				inputs.add( itemStack );
			}
		}
		return inputs;
	}


	@Override
	public String getIdentifier( )
	{
		final StringBuilder sb = new StringBuilder( "SHAPED;" );
		final ItemStack recipeOutput = this.recipe.getRecipeOutput( );
		sb.append( recipeOutput.getItem( ).getRegistryName( ) ).append( ',' ).append( recipeOutput.getMetadata( ) );
		for ( final ItemStack stack : this.recipe.recipeItems ) {
			sb.append( ';' );
			if ( stack != null ) {
				sb.append( stack.getItem( ).getRegistryName( ) ).append( ',' ).append( stack.getMetadata( ) );
			}
		}
		return sb.toString( );
	}


	@Override
	public String getName( )
	{
		return this.recipe.getRecipeOutput( ).getUnlocalizedName( ) + ".name";
	}


	@Override
	public ItemStack getOutput( )
	{
		return this.recipe.getRecipeOutput( );
	}


	@Override
	public void addInputsToDisplay( final IInventory displayInventory )
	{
		for ( int i = 0 ; i < this.recipe.recipeWidth ; i++ ) {
			for ( int j = 0 ; j < this.recipe.recipeHeight ; j++ ) {
				final ItemStack itemStack = this.recipe.recipeItems[ i + j * this.recipe.recipeWidth ];
				if ( itemStack != null ) {
					RShapedRecipeWrapper.setSlot( displayInventory , i + 3 * j , itemStack );
				}
			}
		}
	}


	private static void setSlot( final IInventory inventory , final int index , ItemStack stack )
	{
		if ( stack.getMetadata( ) == OreDictionary.WILDCARD_VALUE ) {
			stack = new ItemStack( stack.getItem( ) , 1 , 0 );
		} else {
			stack = stack.copy( );
		}
		stack.stackSize = 1;
		inventory.setInventorySlotContents( index , stack );
	}


	@Override
	public I_RecipeRequirements getRequirements( )
	{
		return this.requirements;
	}

}
