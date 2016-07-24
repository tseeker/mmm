package mmm.core.api.recipes;


import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;



public interface I_CraftingRecipeWrapper
{
	public String getIdentifier( );


	public String getName( );


	public ItemStack getOutput( );


	default ItemStack getActualOutput( final IInventory input )
	{
		return this.getOutput( ).copy( );
	}


	default boolean canShiftClick( )
	{
		return true;
	}


	public void addInputsToDisplay( IInventory displayInventory );


	public I_RecipeRequirements getRequirements( );

}
