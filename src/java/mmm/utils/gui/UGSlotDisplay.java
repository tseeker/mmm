package mmm.utils.gui;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;



/** An inventory slot that cannot be interacted with */
public class UGSlotDisplay
		extends Slot
{

	public UGSlotDisplay( final IInventory inventoryIn , final int index , final int xPosition , final int yPosition )
	{
		super( inventoryIn , index , xPosition , yPosition );
	}


	@Override
	public boolean isItemValid( final ItemStack stack )
	{
		return false;
	}


	@Override
	public boolean canTakeStack( final EntityPlayer playerIn )
	{
		return false;
	}

}
