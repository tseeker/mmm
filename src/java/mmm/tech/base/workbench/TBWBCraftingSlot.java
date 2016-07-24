package mmm.tech.base.workbench;


import javax.annotation.Nullable;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.utils.UAchievements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;



public class TBWBCraftingSlot
		extends Slot
{

	private final EntityPlayer player;
	private final TBWBContainer container;


	public TBWBCraftingSlot( final EntityPlayer player , final TBWBContainer container , final IInventory inventory ,
			final int index , final int x , final int y )
	{
		super( inventory , index , x , y );
		this.player = player;
		this.container = container;
	}


	@Override
	public boolean isItemValid( @Nullable final ItemStack stack )
	{
		return false;
	}


	@Override
	public boolean canTakeStack( EntityPlayer playerIn )
	{
		if ( !this.getHasStack( ) ) {
			return false;
		}
		final I_CraftingRecipeWrapper wrapper = this.container.getCurrentRecipe( );
		if ( wrapper == null ) {
			return false;
		}
		final IInventory storage = this.container.getStorage( );
		return storage != null && wrapper.getRequirements( ).checkInventory( storage , 1 , this.container.world );
	}


	@Override
	public ItemStack decrStackSize( final int amount )
	{
		if ( !this.getHasStack( ) ) {
			return null;
		}

		final I_CraftingRecipeWrapper wrapper = this.container.getCurrentRecipe( );
		if ( wrapper == null ) {
			return null;
		}

		final ItemStack stack = this.getStack( );
		final int qt = Math.max( amount - amount % stack.stackSize , stack.stackSize ) / stack.stackSize;
		final IInventory storage = this.container.getStorage( );
		if ( storage == null || !wrapper.getRequirements( ).checkInventory( storage , qt , this.container.world ) ) {
			return null;
		}
		return this.handleCrafting( qt );
	}


	public ItemStack handleCrafting( final int quantity )
	{
		if ( !this.getHasStack( ) ) {
			return null;
		}

		final I_CraftingRecipeWrapper wrapper = this.container.getCurrentRecipe( );
		if ( wrapper == null ) {
			return null;
		}

		final IInventory storage = this.container.getStorage( );
		if ( storage == null ) {
			return null;
		}

		final ItemStack stack = this.container.getCurrentRecipe( ).getActualOutput( storage );
		stack.stackSize *= quantity;

		FMLCommonHandler.instance( ).firePlayerCraftingEvent( this.player , stack , this.inventory );
		stack.onCrafting( this.player.worldObj , this.player , quantity );
		UAchievements.checkCraftingAchievements( this.player , stack.getItem( ) );
		wrapper.getRequirements( ).removeFromInventory( storage , quantity , this.container.world );
		return stack;
	}
}
