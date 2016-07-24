package mmm.world.gen;


import java.util.Random;
import java.util.function.BiPredicate;

import mmm.core.api.world.I_FloraParameters;
import mmm.core.api.world.I_LocationCheck;
import mmm.world.WLocation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;



public class WGFloraParameters
		implements I_FloraParameters
{

	private static BiPredicate< World , BlockPos > getDefaultPlacementCheck( final IBlockState bs )
	{
		final Block block = bs.getBlock( );
		if ( block instanceof BlockBush ) {
			return ( w , p ) -> w.isAirBlock( p ) && ( (BlockBush) block ).canBlockStay( w , p , bs );
		}
		return ( w , p ) -> w.isAirBlock( p ) && !w.isAirBlock( p.down( ) );
	}

	public final IBlockState floraType;
	public final float perChunk;
	public final I_LocationCheck location;
	public final BiPredicate< World , BlockPos > placementCheck;
	private int attempts = 128;
	private int successes = 1;


	public WGFloraParameters( final IBlockState floraType , final float perChunk )
	{
		this( floraType , perChunk , WLocation.anywhere( ) , WGFloraParameters.getDefaultPlacementCheck( floraType ) );
	}


	public WGFloraParameters( final IBlockState floraType , final float perChunk ,
			final BiPredicate< World , BlockPos > placementCheck )
	{
		this( floraType , perChunk , WLocation.anywhere( ) , placementCheck );
	}


	public WGFloraParameters( final IBlockState floraType , final float perChunk , final I_LocationCheck location )
	{
		this( floraType , perChunk , location , WGFloraParameters.getDefaultPlacementCheck( floraType ) );
	}


	public WGFloraParameters( final IBlockState floraType , final float perChunk , final I_LocationCheck location ,
			final BiPredicate< World , BlockPos > placementCheck )
	{
		this.floraType = floraType;
		this.perChunk = perChunk;
		this.location = location;
		this.placementCheck = placementCheck;
	}


	public WGFloraParameters setPlacementAttempts( final int attempts )
	{
		if ( attempts < 1 ) {
			throw new IllegalArgumentException( "invalid placement attempts count" );
		}
		this.attempts = attempts;
		return this;
	}


	public WGFloraParameters setSuccessfulPlacements( final int successes )
	{
		if ( successes < 1 ) {
			throw new IllegalArgumentException( "invalid successful placement count" );
		}
		this.successes = successes;
		return this;
	}


	@Override
	public IBlockState getBlockState( final World world , final BlockPos pos , final Random random )
	{
		return this.floraType;
	}


	@Override
	public boolean canPlaceInChunk( final World world , final BlockPos pos )
	{
		return this.location.checkLocation( world , pos.getX( ) >> 4 , pos.getZ( ) >> 4 , world.getChunkProvider( ) );
	}


	@Override
	public int getAmountPerChunk( final World world , final BlockPos pos , final Random random )
	{
		int pc = MathHelper.floor_float( this.perChunk );
		final float remainder = this.perChunk - pc;
		if ( remainder != 0 && random.nextFloat( ) < remainder ) {
			pc++;
		}
		return pc;
	}


	@Override
	public int getPlacementAttempts( final World world , final BlockPos pos , final Random random )
	{
		return this.attempts;
	}


	@Override
	public int getSuccessfulPlacements( final World world , final BlockPos pos , final Random random )
	{
		return this.successes;
	}


	@Override
	public boolean canPlace( final World world , final BlockPos pos , final Random random )
	{
		return this.placementCheck.test( world , pos );
	}

}
