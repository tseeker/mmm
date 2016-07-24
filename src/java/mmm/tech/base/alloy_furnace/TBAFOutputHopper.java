package mmm.tech.base.alloy_furnace;


import mmm.utils.UInventoryGrid;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;



public class TBAFOutputHopper
		implements IItemHandler
{
	private final TBAFTileEntity tileEntity;
	private final int nSlots;


	public TBAFOutputHopper( final TBAFTileEntity tileEntity )
	{
		this.tileEntity = tileEntity;
		this.nSlots = this.tileEntity.input.getSizeInventory( ) + this.tileEntity.fuel.getSizeInventory( )
				+ this.tileEntity.output.getSizeInventory( );
	}


	@Override
	public int getSlots( )
	{
		return this.nSlots;
	}


	@Override
	public ItemStack getStackInSlot( final int slot )
	{
		final int nInputSlots = this.tileEntity.input.getSizeInventory( );
		if ( slot < nInputSlots ) {
			return this.tileEntity.input.getStackInSlot( slot );
		} else {
			final int nFuelSlots = this.tileEntity.fuel.getSizeInventory( );
			if ( slot < nInputSlots + nFuelSlots ) {
				return this.tileEntity.fuel.getStackInSlot( slot - nInputSlots );
			} else {
				return this.tileEntity.output.getStackInSlot( slot - nInputSlots - nFuelSlots );
			}
		}
	}


	@Override
	public ItemStack insertItem( final int slot , final ItemStack stack , final boolean simulate )
	{
		return null;
	}


	@Override
	public ItemStack extractItem( final int slot , final int amount , final boolean simulate )
	{
		if ( amount == 0 ) {
			return null;
		}

		final ItemStack stackInSlot = this.getStackInSlot( slot );

		if ( stackInSlot == null ) {
			return null;
		}

		// Extracting invalid input items
		final int nInputSlots = this.tileEntity.input.getSizeInventory( );
		if ( slot < nInputSlots ) {
			if ( ( this.tileEntity.flags & TBAFTileEntity.F_OH_INVALID_INPUT ) == 0
					|| this.tileEntity.recipe.hasInput( stackInSlot ) ) {
				return null;
			}
			return this.doExtractItems( this.tileEntity.input , stackInSlot , slot , amount , simulate );
		}

		// Extracting invalid fuel items
		final int nFuelSlots = this.tileEntity.fuel.getSizeInventory( );
		if ( slot < nInputSlots + nFuelSlots ) {
			if ( ( this.tileEntity.flags & TBAFTileEntity.F_OH_INVALID_FUEL ) == 0
					|| TileEntityFurnace.isItemFuel( stackInSlot ) ) {
				return null;
			}
			return this.doExtractItems( this.tileEntity.fuel , stackInSlot , slot - nInputSlots , amount , simulate );
		}

		return this.doExtractItems( this.tileEntity.output , stackInSlot , slot - ( nInputSlots + nFuelSlots ) ,
				amount , simulate );
	}


	private ItemStack doExtractItems( final UInventoryGrid inventory , final ItemStack stackInSlot , final int slot ,
			final int amount , final boolean simulate )
	{
		if ( simulate ) {
			if ( stackInSlot.stackSize < amount ) {
				return stackInSlot.copy( );
			} else {
				final ItemStack copy = stackInSlot.copy( );
				copy.stackSize = amount;
				return copy;
			}
		} else {
			final int m = Math.min( stackInSlot.stackSize , amount );

			final ItemStack decrStackSize = inventory.decrStackSize( slot , m );
			inventory.markDirty( );
			return decrStackSize;
		}
	}

}
