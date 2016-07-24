package mmm.deco;


import java.util.List;

import mmm.Mmm;
import mmm.core.api.blocks.I_SeatBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;



public class DSeatEntity
		extends Entity
{

	public static void register( final Mmm mmm )
	{
		EntityRegistry.registerModEntity( DSeatEntity.class , "Seat" , 0 , mmm , 80 , 1 , false );
	}


	public static boolean sit( final World world , final BlockPos pos , final EntityPlayer player ,
			final double yOffset )
	{
		if ( world.isRemote ) {
			return true;
		}

		final List< DSeatEntity > seats = world.getEntitiesWithinAABB( DSeatEntity.class ,
				new AxisAlignedBB( pos ).expand( 1 , 1 , 1 ) );
		DSeatEntity seat = null;
		for ( final DSeatEntity existingSeat : seats ) {
			if ( !existingSeat.pos.equals( pos ) ) {
				continue;
			}
			if ( existingSeat.getPassengers( ).isEmpty( ) ) {
				seat = existingSeat;
				break;
			}
			return false;
		}

		if ( seat == null ) {
			seat = new DSeatEntity( world , pos , yOffset );
			world.spawnEntityInWorld( seat );
		}
		player.startRiding( seat );
		return true;
	}

	public BlockPos pos;
	public int x;
	public int y;
	public int z;
	public double yOffset;


	public DSeatEntity( final World world )
	{
		super( world );

		this.setInvisible( true );
		this.setEntityInvulnerable( true );
		this.noClip = true;
		this.width = .01f;
		this.height = .01f;
	}


	public DSeatEntity( final World world , final BlockPos pos , final double yOffset )
	{
		this( world );

		this.x = pos.getX( );
		this.y = pos.getY( );
		this.z = pos.getZ( );
		this.pos = new BlockPos( this.x , this.y , this.z );

		this.yOffset = yOffset;

		this.setPosition( this.x + .5 , this.y + this.yOffset , this.z + .5 );
	}


	@Override
	protected void entityInit( )
	{
		// EMPTY
	}


	@Override
	protected void readEntityFromNBT( final NBTTagCompound compound )
	{
		if ( this.worldObj.isRemote ) {
			return;
		}
		this.x = compound.getInteger( "x" );
		this.y = compound.getInteger( "y" );
		this.z = compound.getInteger( "z" );
		this.pos = new BlockPos( this.x , this.y , this.z );
	}


	@Override
	protected void writeEntityToNBT( final NBTTagCompound compound )
	{
		if ( this.worldObj.isRemote ) {
			return;
		}
		compound.setInteger( "BlockX" , this.x );
		compound.setInteger( "BlockY" , this.y );
		compound.setInteger( "BlockZ" , this.z );
	}


	@Override
	public double getMountedYOffset( )
	{
		return 0;
	}


	@Override
	protected boolean shouldSetPosAfterLoading( )
	{
		return false;
	}


	@Override
	protected boolean canBeRidden( final Entity entityIn )
	{
		return true;
	}


	@Override
	public void onEntityUpdate( )
	{
		if ( this.worldObj.isRemote ) {
			return;
		}

		final Block block = this.worldObj.getBlockState( this.pos ).getBlock( );
		if ( ! ( block instanceof I_SeatBlock ) ) {
			this.setDead( );
			return;
		}

		boolean hasLiveRider = false;
		for ( final Entity entity : this.getPassengers( ) ) {
			hasLiveRider = !entity.isDead;
			if ( hasLiveRider ) {
				return;
			}
		}
		this.setDead( );
	}

}
