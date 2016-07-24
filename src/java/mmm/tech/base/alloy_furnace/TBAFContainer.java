package mmm.tech.base.alloy_furnace;


import mmm.core.CNetwork;
import mmm.core.api.tech.E_ActivationMode;
import mmm.core.api.tech.I_ConfigurableActivation;
import mmm.materials.MAlloyRecipe;
import mmm.tech.TActivationModeMessage;
import mmm.utils.UInventoryDisplay;
import mmm.utils.UInventoryGrid;
import mmm.utils.gui.UGContainer;
import mmm.utils.gui.UGSlotDisplay;
import mmm.utils.gui.UGSlotFuel;
import mmm.utils.gui.UGSlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;



public class TBAFContainer
		extends UGContainer
		implements I_ConfigurableActivation
{
	public final TBAFTileEntity tileEntity;
	public final World world;
	public final BlockPos position;

	public final UInventoryGrid input;
	public final UInventoryGrid fuel;
	public final UInventoryGrid output;
	public final UInventoryDisplay recipe;


	public TBAFContainer( final InventoryPlayer playerInv , final TBAFTileEntity tileEntity )
	{
		this.tileEntity = tileEntity;
		this.world = tileEntity.getWorld( );
		this.position = tileEntity.getPos( );

		this.addPlayerInventory( Slot::new , playerInv , 8 , 112 );
		this.slotGroups.nextGroup( );

		this.input = tileEntity.input;
		this.addGrid( Slot::new , this.input , //
				this.input.width , this.input.height , 0 , 12 , 8 );
		this.fuel = tileEntity.fuel;
		this.addGrid( UGSlotFuel::new , this.fuel , //
				this.fuel.width , this.fuel.height , 0 , 80 , 62 );
		this.output = tileEntity.output;
		this.addGrid( //
				( inv , i , x , y ) -> new UGSlotOutput( playerInv.player , inv , i , x , y ) , //
				this.output , this.output.width , this.output.height , 0 , 130 , 8 );

		this.recipe = new UInventoryDisplay( "Recipe" , 7 );
		this.slotGroups.nextGroup( );
		this.addGrid( UGSlotDisplay::new , this.recipe , //
				3 , 2 , 0 , 25 , 37 , 8 , 8 );
		this.addSlotToContainer( new UGSlotDisplay( this.recipe , 6 , 131 , 49 ) );

		this.slotGroups.endGroups( );
	}


	@Override
	public boolean canInteractWith( final EntityPlayer player )
	{
		return this.world.getBlockState( this.position ).getBlock( ) instanceof TBAFBlock
				&& player.getDistanceSq( this.position.getX( ) + .5 , this.position.getY( ) + .5 ,
						this.position.getZ( ) + .5 ) <= 64.;
	}


	@Override
	public ItemStack transferStackInSlot( final EntityPlayer playerIn , final int index )
	{
		final Slot slot = this.inventorySlots.get( index );
		if ( slot == null || !slot.getHasStack( ) ) {
			return null;
		}

		final ItemStack slotStack = slot.getStack( );
		final ItemStack slotStackCopy = slotStack.copy( );
		if ( slot.inventory == playerIn.inventory ) {
			boolean checkInput;
			if ( TileEntityFurnace.isItemFuel( slotStack ) ) {
				checkInput = !this.mergeItemStack( slotStackCopy , 51 , 55 , false );
			} else {
				checkInput = true;
			}
			if ( checkInput ) {
				if ( this.tileEntity.recipe.hasInput( slotStack ) ) {
					if ( !this.mergeItemStack( slotStackCopy , 36 , 51 , false ) ) {
						return null;
					}
				} else {
					return null;
				}
			}
		} else {
			if ( !this.mergeItemStack( slotStackCopy , 0 , 36 , false ) ) {
				return null;
			}
			if ( slot.inventory == this.output ) {
				slot.onSlotChange( slotStackCopy , slotStack );
			}
		}

		if ( slotStackCopy.stackSize == 0 ) {
			slot.putStack( null );
		} else {
			slot.onSlotChanged( );
		}

		if ( slotStackCopy.stackSize == slotStack.stackSize ) {
			return null;
		}
		slot.onPickupFromSlot( playerIn , slotStackCopy );

		return slotStack;
	}


	public void setCurrentRecipe( final ResourceLocation name , final boolean confirm )
	{
		final MAlloyRecipe recipe;
		if ( name == null ) {
			recipe = null;
		} else {
			recipe = MAlloyRecipe.REGISTRY.getRecipe( name );
		}

		this.recipe.clear( );
		if ( recipe == null ) {
			// XXX log if confirm is set
			return;
		}

		for ( int i = 0 ; i < recipe.inputs.length ; i++ ) {
			this.recipe.setInventorySlotContents( i , recipe.inputs[ i ] );
		}
		this.recipe.setInventorySlotContents( 6 , recipe.output );

		if ( confirm ) {
			this.tileEntity.setRecipe( name );
		}
	}


	@Override
	public E_ActivationMode getActivationMode( )
	{
		final TileEntity te = this.world.getTileEntity( this.position );
		if ( te instanceof TBAFTileEntity ) {
			return ( (TBAFTileEntity) te ).getActivationMode( );
		}
		return E_ActivationMode.DISABLED;
	}


	@Override
	public void setActivationMode( final E_ActivationMode mode )
	{
		if ( this.world.isRemote ) {
			final TileEntity te = this.world.getTileEntity( this.position );
			if ( te instanceof TBAFTileEntity ) {
				( (TBAFTileEntity) te ).setActivationMode( mode );
			}
			CNetwork.sendToServer( new TActivationModeMessage( mode ) );
		} else {
			this.tileEntity.setActivationMode( mode );
			this.tileEntity.forceUpdate( );
		}
	}


	public int getFlags( )
	{
		final TileEntity te = this.world.getTileEntity( this.position );
		if ( te instanceof TBAFTileEntity ) {
			return ( (TBAFTileEntity) te ).flags;
		}
		return 0;
	}


	public void setFlags( final int flags )
	{
		if ( this.world.isRemote ) {
			final TileEntity te = this.world.getTileEntity( this.position );
			if ( te instanceof TBAFTileEntity ) {
				( (TBAFTileEntity) te ).flags = flags;
				CNetwork.sendToServer( new TBAFMessage( flags ) );
			}
		} else {
			this.tileEntity.flags = flags;
			this.tileEntity.forceUpdate( );
		}
	}

}
