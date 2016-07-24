package mmm.utils.gui;


import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;



public class UGSlotOutput
		extends Slot
{
	public static interface I_OutputHandler
	{
		public void onOutputRemoved( EntityPlayer player , ItemStack stack , int quantity );
	}

	/** Player interacting with this slot */
	private final EntityPlayer player;
	/** Output handler */
	@Nullable
	private final I_OutputHandler outputHandler;
	/** Amount of items removed from the slot */
	private int removed;


	public UGSlotOutput( final EntityPlayer player , final IInventory inventoryIn , final int slotIndex ,
			final int xPosition , final int yPosition )
	{
		this( player , inventoryIn , slotIndex , xPosition , yPosition , null );
	}


	public UGSlotOutput( final EntityPlayer player , final IInventory inventoryIn , final int slotIndex ,
			final int xPosition , final int yPosition , @Nullable final I_OutputHandler handler )
	{
		super( inventoryIn , slotIndex , xPosition , yPosition );
		this.player = player;
		this.outputHandler = handler;
		this.removed = 0;
	}


	/** Players can't add items to output slots */
	@Override
	public boolean isItemValid( @Nullable final ItemStack stack )
	{
		return false;
	}


	@Override
	public ItemStack decrStackSize( final int amount )
	{
		if ( this.getHasStack( ) ) {
			this.removed += Math.min( amount , this.getStack( ).stackSize );
		}
		return super.decrStackSize( amount );
	}


	@Override
	public void onPickupFromSlot( final EntityPlayer playerIn , final ItemStack stack )
	{
		this.onCrafting( stack );
		super.onPickupFromSlot( playerIn , stack );
	}


	@Override
	protected void onCrafting( final ItemStack stack , final int amount )
	{
		this.removed += amount;
		this.onCrafting( stack );
	}


	@Override
	protected void onCrafting( final ItemStack stack )
	{
		stack.onCrafting( this.player.worldObj , this.player , this.removed );
		if ( this.outputHandler != null ) {
			this.outputHandler.onOutputRemoved( this.player , stack , this.removed );
		}
		this.removed = 0;
	}

}
