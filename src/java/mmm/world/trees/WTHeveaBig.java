package mmm.world.trees;


import java.util.Random;

import mmm.materials.trees.MLog;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;



public class WTHeveaBig
		extends WTHevea
{

	public static WTHeveaBig createSaplingGen( )
	{
		return new WTHeveaBig( );
	}


	public WTHeveaBig( final boolean notify )
	{
		super( notify );
	}


	// Used for sapling gen
	protected WTHeveaBig( )
	{
		super( );
	}


	@Override
	protected RuntimeData determineTreeSize( final BlockPos position , final Random rand )
	{
		final int leavesHeight = rand.nextInt( 4 ) + 4;
		final int trunkBottom = rand.nextInt( 2 ) + 3;
		final int totalHeight = trunkBottom + leavesHeight * 5 / 2;
		return new RuntimeData( position , leavesHeight , totalHeight );
	}


	@Override
	protected void generateTreeBlocks( final RuntimeData rtd , final Random rand )
	{
		super.generateTreeBlocks( rtd , rand );
		final int centre = ( rtd.xSize - 1 ) / 2;
		rtd.fixer( this.getTreeMaterials( ).LOG.getDefaultState( ) , centre , centre );
	}


	@Override
	protected void makeTrunk( final RuntimeData rtd , final Random rand , final int leavesHeight ,
			final int trunkHeight )
	{
		final MLog logVertical = this.getTreeMaterials( ).LOG;
		final IBlockState logXAxis = logVertical.getDefaultState( ).withProperty( BlockLog.LOG_AXIS ,
				BlockLog.EnumAxis.X );
		final IBlockState logZAxis = logVertical.getDefaultState( ).withProperty( BlockLog.LOG_AXIS ,
				BlockLog.EnumAxis.Z );

		// Trunk
		for ( int y = 0 ; y < trunkHeight ; y++ ) {
			rtd.setBlock( leavesHeight , y , leavesHeight , logVertical , E_BlockRequirement.VANILLA );
			rtd.setRequirement( leavesHeight , y , leavesHeight , E_BlockRequirement.VANILLA );
		}

		// Branches
		final int totalLeavesHeight = leavesHeight * 5 / 2;
		int y = rtd.height - totalLeavesHeight + 2 + rand.nextInt( 2 );
		boolean xAxis = rand.nextBoolean( );
		boolean positive = rand.nextBoolean( );
		while ( y < rtd.height - 1 ) {
			final int x = xAxis ? positive ? leavesHeight + 1 : leavesHeight - 1 : leavesHeight;
			final int z = xAxis ? leavesHeight : positive ? leavesHeight + 1 : leavesHeight - 1;
			final int xDir = xAxis ? positive ? 1 : -1 : 0;
			final int zDir = xAxis ? 0 : positive ? 1 : -1;
			if ( this.tryBranch( rtd , rand , x , y , z , xDir , zDir , xAxis ? logXAxis : logZAxis ) ) {
				y += rand.nextInt( 2 );
				positive = ! ( xAxis || positive ) || xAxis && positive;
				xAxis = !xAxis;
			}
			y++;
		}
	}


	private boolean tryBranch( final RuntimeData rtd , final Random rand , final int x , final int y , final int z ,
			final int xDir , final int zDir , final IBlockState log )
	{
		int rx = x , ry = y , rz = z;
		boolean generated = false;
		while ( this.canPlaceBranch( rtd , rx , ry , rz ) ) {
			generated = true;

			rtd.setBlock( rx , ry , rz , log , E_BlockRequirement.VANILLA );
			rtd.setRequirement( rx , ry , rz , E_BlockRequirement.VANILLA );

			rx += xDir;
			if ( rand.nextInt( 4 ) != 0 ) {
				ry++;
			}
			rz += zDir;

			if ( rand.nextInt( 10 ) == 0 ) {
				final int rDir = rand.nextInt( 2 ) * 2 - 1;
				if ( zDir == 0 ) {
					rz += rDir;
				} else {
					rx += rDir;
				}
			}
		}
		return generated;
	}


	private boolean canPlaceBranch( final RuntimeData rtd , final int rx , final int ry , final int rz )
	{
		return ! ( rtd.isEmpty( rx - 1 , ry , rz ) //
				|| rtd.isEmpty( rx + 1 , ry , rz ) //
				|| rtd.isEmpty( rx , ry - 1 , rz ) //
				|| rtd.isEmpty( rx , ry + 1 , rz ) //
				|| rtd.isEmpty( rx , ry , rz - 1 ) //
				|| rtd.isEmpty( rx , ry , rz + 1 ) );
	}

}
