package mmm.recipes;


import java.util.ArrayList;
import java.util.List;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_RecipeRequirements;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;



public class RShapelessOreRecipeWrapper
		implements I_CraftingRecipeWrapper
{
	private final ShapelessOreRecipe recipe;
	private final RRequirements requirements;


	public RShapelessOreRecipeWrapper( final ShapelessOreRecipe recipe )
	{
		this.recipe = recipe;
		this.requirements = ROreRecipeHelper.extractRequirements( recipe.getInput( ).toArray( ) );
	}


	@Override
	public String getIdentifier( )
	{
		final StringBuilder sb = new StringBuilder( "SHAPELESS_ORE;" );
		final ItemStack recipeOutput = this.recipe.getRecipeOutput( );
		sb.append( recipeOutput.getItem( ).getRegistryName( ) ).append( ',' ).append( recipeOutput.getMetadata( ) );
		final ArrayList< Object > input = this.recipe.getInput( );
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
