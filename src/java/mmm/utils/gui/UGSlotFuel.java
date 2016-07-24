package mmm.utils.gui;


import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;



public class UGSlotFuel
		extends Slot
{

	public UGSlotFuel( final IInventory inventoryIn , final int slotIndex , final int xPosition , final int yPosition )
	{
		super( inventoryIn , slotIndex , xPosition , yPosition );
	}


	@Override
	public boolean isItemValid( @Nullable final ItemStack stack )
	{
		return TileEntityFurnace.isItemFuel( stack );
	}

}
