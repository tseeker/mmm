package mmm.recipes;


import java.util.List;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_RecipeRequirements;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;



public class RShapedOreRecipeWrapper
		implements I_CraftingRecipeWrapper
{
	private final ShapedOreRecipe recipe;
	private final int width;
	private final int height;
	private final RRequirements requirements;


	public RShapedOreRecipeWrapper( final ShapedOreRecipe recipe )
	{
		this.recipe = recipe;
		this.width = ObfuscationReflectionHelper.getPrivateValue( ShapedOreRecipe.class , this.recipe , "width" );
		this.height = ObfuscationReflectionHelper.getPrivateValue( ShapedOreRecipe.class , this.recipe , "height" );
		this.requirements = ROreRecipeHelper.extractRequirements( recipe.getInput( ) );
	}


	@Override
	public String getIdentifier( )
	{
		final StringBuilder sb = new StringBuilder( "SHAPED_ORE;" );
		final ItemStack recipeOutput = this.recipe.getRecipeOutput( );
		sb.append( recipeOutput.getItem( ).getRegistryName( ) ).append( ',' ).append( recipeOutput.getMetadata( ) );
		final Object[] input = this.recipe.getInput( );
		for ( final Object inObject : input ) {
			ItemStack stack;
			sb.append( ';' );
			if ( inObject instanceof ItemStack ) {
				stack = (ItemStack) inObject;
			} else if ( inObject instanceof List && ! ( (List< ? >) inObject ).isEmpty( ) ) {
				stack = (ItemStack) ( (List< ? >) inObject ).get( 0 );
			} else {
				stack = null;
			}
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
		final Object[] input = this.recipe.getInput( );
		for ( int i = 0 ; i < this.width ; i++ ) {
			for ( int j = 0 ; j < this.height ; j++ ) {
				final Object inObj = input[ i + j * this.width ];

				if ( inObj instanceof ItemStack ) {
					RShapedOreRecipeWrapper.setSlot( displayInventory , i + j * 3 , (ItemStack) inObj );

				} else if ( inObj instanceof List ) {
					@SuppressWarnings( "unchecked" )
					final List< ItemStack > oreList = (List< ItemStack >) inObj;
					if ( oreList.isEmpty( ) ) {
						continue;
					}
					RShapedOreRecipeWrapper.setSlot( displayInventory , i + j * 3 , oreList.get( 0 ) );
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
		inventory.setInventorySlotContents( index , stack );
	}


	@Override
	public I_RecipeRequirements getRequirements( )
	{
		return this.requirements;
	}

}
