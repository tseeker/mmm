package mmm.tech.base.alloy_furnace;


import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;



public class TBAFInputHopper
		extends InvWrapper
{

	private final TBAFTileEntity tileEntity;


	public TBAFInputHopper( final TBAFTileEntity tileEntity )
	{
		super( tileEntity.input );
		this.tileEntity = tileEntity;
	}


	@Override
	public ItemStack insertItem( final int slot , final ItemStack stack , final boolean simulate )
	{
		if ( stack != null && ( this.tileEntity.flags & TBAFTileEntity.F_IH_INVALID ) == 0
				&& !this.tileEntity.recipe.hasInput( stack ) ) {
			return stack;
		}
		return super.insertItem( slot , stack , simulate );
	}

}
