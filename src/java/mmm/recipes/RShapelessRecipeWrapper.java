package mmm.recipes;


import java.util.ArrayList;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_RecipeRequirements;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;



public class RShapelessRecipeWrapper
		implements I_CraftingRecipeWrapper
{

	private final ShapelessRecipes recipe;
	private final RRequirements requirements;


	public RShapelessRecipeWrapper( final ShapelessRecipes recipe )
	{
		this.recipe = recipe;
		this.requirements = new RRequirements( RShapelessRecipeWrapper.extractInputs( recipe ) );
	}


	private static ArrayList< ItemStack > extractInputs( final ShapelessRecipes recipe )
	{
		final ArrayList< ItemStack > combinedInputs = new ArrayList<>( );
		OUTER: for ( final ItemStack invStack : recipe.recipeItems ) {
			for ( final ItemStack ciStack : combinedInputs ) {
				if ( OreDictionary.itemMatches( ciStack , invStack , true ) ) {
					ciStack.stackSize += invStack.stackSize;
					continue OUTER;
				}
			}
			combinedInputs.add( invStack.copy( ) );
		}
		return combinedInputs;
	}


	@Override
	public String getIdentifier( )
	{
		final StringBuilder sb = new StringBuilder( "SHAPELESS;" );
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
		for ( int i = 0 ; i < this.requirements.size( ) ; i++ ) {
			ItemStack stack = this.requirements.getItemTypes( i ).get( 0 );
			if ( stack.getMetadata( ) == OreDictionary.WILDCARD_VALUE ) {
				stack = new ItemStack( stack.getItem( ) , 1 , 0 );
			} else {
				stack = stack.copy( );
			}
			stack.stackSize = this.requirements.getQuantity( i );
			displayInventory.setInventorySlotContents( i , stack );
		}
	}


	@Override
	public I_RecipeRequirements getRequirements( )
	{
		return this.requirements;
	}

}
