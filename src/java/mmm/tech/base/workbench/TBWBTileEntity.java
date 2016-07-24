package mmm.tech.base.workbench;


import mmm.MmmTech;
import mmm.utils.UInventoryGrid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;



public class TBWBTileEntity
		extends TileEntity
		implements IWorldNameable
{
	private class Inventory
			extends UInventoryGrid
	{

		public Inventory( final String name , final int width , final int height )
		{
			super( name , width , height );
		}


		@Override
		public void markDirty( )
		{
			TBWBTileEntity.this.markDirty( );
		}

	}

	public final UInventoryGrid storage = new Inventory( "Storage" , 3 , 5 );
	private String customName = "";
	private String defaultRecipe = "";

	private IItemHandler itemHandler;


	@Override
	public String getName( )
	{
		return this.hasCustomName( ) ? this.customName : "tile.mmm.tech.base.workbench.name";
	}


	@Override
	public boolean hasCustomName( )
	{
		return this.customName != null && !this.customName.isEmpty( );
	}


	public void setCustomName( final String name )
	{
		this.customName = name;
	}


	@Override
	public boolean hasCapability( final Capability< ? > capability , final EnumFacing facing )
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null
				|| super.hasCapability( capability , facing );
	}


	@Override
	@SuppressWarnings( "unchecked" )
	public < T > T getCapability( final net.minecraftforge.common.capabilities.Capability< T > capability ,
			final net.minecraft.util.EnumFacing facing )
	{
		if ( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
			return (T) ( this.itemHandler == null
					? ( this.itemHandler = new InvWrapper( this.storage ) )
					: this.itemHandler );
		}
		return super.getCapability( capability , facing );
	}


	@Override
	public boolean shouldRefresh( final World world , final BlockPos pos , final IBlockState oldState ,
			final IBlockState newSate )
	{
		return newSate.getBlock( ) != MmmTech.MACHINES.WORKBENCH;
	}


	@Override
	public SPacketUpdateTileEntity getUpdatePacket( )
	{
		final NBTTagCompound compound = new NBTTagCompound( );
		this.writeSyncData( compound );
		return new SPacketUpdateTileEntity( this.pos , 0 , compound );
	}


	@Override
	public NBTTagCompound getUpdateTag( )
	{
		final NBTTagCompound compound = new NBTTagCompound( );
		this.writeToNBT( compound );
		return compound;
	}


	@Override
	public void onDataPacket( final NetworkManager net , final SPacketUpdateTileEntity pkt )
	{
		if ( this.worldObj.isRemote ) {
			this.readSyncData( pkt.getNbtCompound( ) );
			final IBlockState state = this.worldObj.getBlockState( this.pos );
			this.worldObj.notifyBlockUpdate( this.pos , state , state , 3 );
		}
	}


	@Override
	public void readFromNBT( final NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		if ( compound.hasKey( "CustomName" , 8 ) ) {
			this.customName = compound.getString( "CustomName" );
		}
		this.storage.deserializeNBT( compound.getTagList( "Storage" , NBT.TAG_COMPOUND ) );
		this.readSyncData( compound );
	}


	private void readSyncData( final NBTTagCompound compound )
	{
		this.defaultRecipe = compound.getString( "DefaultRecipe" );
	}


	@Override
	public NBTTagCompound writeToNBT( final NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if ( this.hasCustomName( ) ) {
			compound.setString( "CustomName" , this.customName );
		}
		compound.setTag( "Storage" , this.storage.serializeNBT( ) );
		this.writeSyncData( compound );
		return compound;
	}


	private void writeSyncData( final NBTTagCompound compound )
	{
		compound.setString( "DefaultRecipe" , this.defaultRecipe );
	}


	public String getDefaultRecipe( )
	{
		return this.defaultRecipe;
	}


	public void setDefaultRecipe( final String defaultRecipe )
	{
		this.defaultRecipe = defaultRecipe;
		this.markDirty( );
	}

}
