package mmm.recipes;


import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_RecipeRequirements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;



public class RRepairRecipeWrapper
		implements I_CraftingRecipeWrapper
{
	private class RepairRequirements
			extends RRequirements
	{

		RepairRequirements( )
		{
			super( 2 );
			this.put( 0 , new ItemStack( RRepairRecipeWrapper.this.item ) , 2 );
		}


		@Override
		public boolean checkItemStack( final int pos , final ItemStack stack , final World world )
		{
			return stack != null && stack.getItem( ) == RRepairRecipeWrapper.this.item && stack.isItemDamaged( );
		}

	}

	private final Item item;
	private final RepairRequirements requirements;
	private final IRecipe originalRecipe;
	private static InventoryCrafting tempInventory = null;


	public RRepairRecipeWrapper( final IRecipe originalRecipe , final Item item )
	{
		this.item = item;
		this.requirements = new RepairRequirements( );
		this.originalRecipe = originalRecipe;
	}


	@Override
	public String getIdentifier( )
	{
		return "REPAIR:" + this.item.getRegistryName( ).toString( );
	}


	@Override
	public String getName( )
	{
		return this.item.getUnlocalizedName( ) + ".name";
	}


	@Override
	public ItemStack getOutput( )
	{
		return new ItemStack( this.item );
	}


	@Override
	public ItemStack getActualOutput( final IInventory input )
	{
		if ( RRepairRecipeWrapper.tempInventory == null ) {
			RRepairRecipeWrapper.tempInventory = new InventoryCrafting( new Container( ) {

				@Override
				public boolean canInteractWith( final EntityPlayer playerIn )
				{
					return false;
				}

			} , 3 , 3 );
		}

		RRepairRecipeWrapper.tempInventory.clear( );
		for ( int i = 0 , j = 0 ; i < input.getSizeInventory( ) && j != 2 ; i++ ) {
			final ItemStack stack = input.getStackInSlot( i );
			if ( this.requirements.checkItemStack( 0 , stack , null ) ) {
				RRepairRecipeWrapper.tempInventory.setInventorySlotContents( j++ , stack );
			}
		}
		return this.originalRecipe.getCraftingResult( RRepairRecipeWrapper.tempInventory );
	}


	@Override
	public void addInputsToDisplay( final IInventory displayInventory )
	{
		final ItemStack stack = new ItemStack( this.item );
		stack.setItemDamage( this.item.getMaxDamage( ) - 1 );
		displayInventory.setInventorySlotContents( 0 , stack.copy( ) );
		displayInventory.setInventorySlotContents( 1 , stack );
	}


	@Override
	public I_RecipeRequirements getRequirements( )
	{
		return this.requirements;
	}

}
