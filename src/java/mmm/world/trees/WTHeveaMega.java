package mmm.world.trees;


import java.util.Random;

import mmm.MmmMaterials;
import mmm.materials.trees.MTree;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;



public class WTHeveaMega
		extends A_WTTreeGenerator
{
	public static WTHeveaMega createSaplingGen( )
	{
		return new WTHeveaMega( );
	}

	private static class Data
			extends RuntimeData
	{

		private final int trunkBottom;
		private final int trunkX;
		private final int trunkZ;
		private final int leavesHeight;

		private final IBlockState leaves;
		private final IBlockState logVertical;
		private final IBlockState logXAxis;
		private final IBlockState logZAxis;


		public Data( final MTree materials , final BlockPos position , final int leavesHeight , final int totalHeight ,
				final int extraXSpaceN , final int extraXSpaceP , final int extraZSpaceN , final int extraZSpaceP ,
				final int trunkBottom , final int extraHeight )
		{
			super( position.add( -leavesHeight - extraXSpaceN - 1 , 0 , -leavesHeight - extraZSpaceN - 1 ) ,
					leavesHeight * 2 + 3 + extraXSpaceN + extraXSpaceP , //
					leavesHeight * 2 + 3 + extraZSpaceN + extraZSpaceP ,
					trunkBottom + leavesHeight * 5 / 2 + extraHeight );

			this.leavesHeight = leavesHeight;
			this.trunkBottom = trunkBottom;
			this.trunkX = extraXSpaceN + leavesHeight + 1;
			this.trunkZ = extraZSpaceN + leavesHeight + 1;

			this.leaves = materials.LEAVES.getDefaultState( ).withProperty( BlockLeaves.CHECK_DECAY , false );
			this.logVertical = materials.LOG.getDefaultState( );
			this.logXAxis = this.logVertical.withProperty( BlockLog.LOG_AXIS , BlockLog.EnumAxis.X );
			this.logZAxis = this.logVertical.withProperty( BlockLog.LOG_AXIS , BlockLog.EnumAxis.Z );
		}

	}


	public WTHeveaMega( final boolean notify )
	{
		super( notify , MmmMaterials.TREE.HEVEA );
	}


	// Used for sapling gen
	protected WTHeveaMega( )
	{
		super( true , null );
	}


	@Override
	protected RuntimeData determineTreeSize( final BlockPos position , final Random rand )
	{
		final int leavesHeight = rand.nextInt( 4 ) + 4;
		final int trunkBottom = rand.nextInt( 2 ) + 3;
		final int totalHeight = trunkBottom + leavesHeight * 5 / 2;
		return new Data( this.getTreeMaterials( ) , position , leavesHeight , totalHeight , rand.nextInt( 4 ) ,
				rand.nextInt( 4 ) , rand.nextInt( 4 ) , rand.nextInt( 4 ) , 4 + rand.nextInt( 4 ) , rand.nextInt( 4 ) );
	}


	@Override
	protected void generateTreeBlocks( final RuntimeData rtd , final Random rand )
	{
		final Data data = (Data) rtd;

		this.makeLeaves( data , rand );
		this.makeTrunk( data , rand );
		this.makeBranches( data , rand );
		data.removeCornerLeaves( rand , data.leaves , data.trunkBottom , data.height - 1 , .4f );
		data.fixer( data.logVertical , data.trunkX , data.trunkZ );
	}


	private void makeLeaves( final Data data , final Random rand )
	{
		final int lowerPart = data.trunkBottom + data.leavesHeight * 2;
		// System.err.println( "SZ " + rtd.xSize + " / " + rtd.height + " / " + rtd.zSize );
		// System.err.println( "Trunk " + rtd.trunkX + " / " + rtd.trunkZ );

		// Lower part of the leaves
		final IBlockState leaves = this.getTreeMaterials( ).LEAVES.getDefaultState( )//
				.withProperty( BlockLeaves.CHECK_DECAY , false );
		for ( int y = data.trunkBottom , i = 0 ; y < lowerPart ; y++ , i++ ) {
			final int radius = ( i >> 1 ) + 2;
			final int rSquare = radius * radius + ( i & 1 );
			for ( int x = -radius ; x <= radius + 1 ; x++ ) {
				for ( int z = -radius ; z <= radius + 1 ; z++ ) {
					if ( x * x + z * z <= rSquare ) {
						data.setBlock( data.trunkX + x , y , data.trunkZ + z , leaves , E_BlockRequirement.SOFT );
						data.setRequirement( data.trunkX + x , y , data.trunkZ + z , E_BlockRequirement.VANILLA );
					}
				}
			}
		}

		// Higher part of the leaves
		final int totalHeight = lowerPart + data.leavesHeight / 2;
		for ( int y = lowerPart , radius = data.leavesHeight ; y < totalHeight ; y++ , radius -= 2 ) {
			if ( radius <= 0 ) {
				radius = 2;
			}
			final int rSquare = radius * radius + 1;
			for ( int x = -radius ; x <= radius + 1 ; x++ ) {
				for ( int z = -radius ; z <= radius + 1 ; z++ ) {
					if ( x * x + z * z <= rSquare ) {
						data.setBlock( data.trunkX + x , y , data.trunkZ + z , leaves , E_BlockRequirement.SOFT );
					}
				}
			}
		}
	}


	private void makeTrunk( final Data data , final Random rand )
	{
		final int minTrunkHeight = data.trunkBottom + 7 * data.leavesHeight / 4;
		// System.err.println( "Min Trunk Height " + minTrunkHeight );
		for ( int x = 0 ; x < 2 ; x++ ) {
			for ( int z = 0 ; z < 2 ; z++ ) {
				final int height = Math.min( data.trunkBottom + 5 * data.leavesHeight / 2 - 1 ,
						minTrunkHeight + rand.nextInt( data.leavesHeight ) );
				// System.err.println( " XZ " + x + " / " + z + " > " + height );
				for ( int y = 0 ; y < height ; y++ ) {
					data.setBlock( data.trunkX + x , y , data.trunkZ + z , data.logVertical ,
							E_BlockRequirement.VANILLA );
				}
			}
		}
	}


	private void makeBranches( final Data data , final Random rand )
	{
		int y = data.trunkBottom - 1 + rand.nextInt( 4 );
		final int mth = data.trunkBottom + 5 * data.leavesHeight / 2 - 1;
		boolean xAxis = rand.nextBoolean( );
		boolean positive = rand.nextBoolean( );
		while ( y < mth ) {
			final int x = data.trunkX + ( xAxis ? positive ? 2 : -1 : rand.nextInt( 2 ) );
			final int z = data.trunkZ + ( xAxis ? rand.nextInt( 2 ) : positive ? 2 : -1 );
			boolean needsRotation;

			// Make sure the trunk is still there
			final IBlockState bs = data.getBlockState( x + ( xAxis ? positive ? -1 : 1 : 0 ) , y ,
					z + ( xAxis ? 0 : positive ? -1 : 1 ) );
			if ( bs != data.logVertical ) {
				needsRotation = true;
				y++;
			} else {
				// Try gen'ing the branch
				final int xDir = xAxis ? positive ? 1 : -1 : 0;
				final int zDir = xAxis ? 0 : positive ? 1 : -1;
				needsRotation = this.tryBranch( data , rand , x , y , z , xDir , zDir ,
						xAxis ? data.logXAxis : data.logZAxis );
				if ( needsRotation ) {
					y += rand.nextInt( 2 );
				} else {
					y++;
				}
			}

			// Rotate around the trunk
			if ( needsRotation ) {
				positive = ! ( xAxis || positive ) || xAxis && positive;
				xAxis = !xAxis;
			}
		}
	}


	private boolean tryBranch( final Data rtd , final Random rand , final int x , final int y , final int z ,
			final int xDir , final int zDir , final IBlockState log )
	{
		int rx = x , ry = y , rz = z;
		boolean generated = false;
		boolean useVertical = false;
		while ( this.canPlaceBranch( rtd , rx , ry , rz ) ) {
			generated = true;

			rtd.setBlock( rx , ry , rz , useVertical ? rtd.logVertical : log , E_BlockRequirement.VANILLA );
			rtd.setRequirement( rx , ry , rz , E_BlockRequirement.VANILLA );
			this.placeLeavesAround( rtd , rx , ry , rz );

			final int rdy = rand.nextInt( 8 );
			if ( rdy <= 6 ) {
				ry++;
			}
			useVertical = rdy == 0;

			if ( !useVertical ) {
				rx += xDir;
				rz += zDir;

				if ( rand.nextInt( 2 ) == 0 ) {
					final int rDir = rand.nextInt( 2 ) * 2 - 1;
					if ( zDir == 0 ) {
						rz += rDir;
					} else {
						rx += rDir;
					}
				}
			}
		}
		return generated;
	}


	private void placeLeavesAround( final Data rtd , final int rx , final int ry , final int rz )
	{
		for ( int i = -1 ; i <= 1 ; i++ ) {
			for ( int j = -1 ; j <= 1 ; j++ ) {
				for ( int k = -1 ; k <= 1 ; k++ ) {
					if ( i * i + j * j + k * k >= 3 ) {
						continue;
					}

					final int cx = rx + i;
					final int cy = ry + j;
					final int cz = rz + k;
					if ( cx < 0 || cy < 0 || cz < 0 || cx >= rtd.xSize || cy >= rtd.height || cz >= rtd.zSize ) {
						continue;
					}

					final IBlockState bs = rtd.getBlockState( cx , cy , cz );
					if ( bs == null ) {
						rtd.setBlock( cx , cy , cz , rtd.leaves , E_BlockRequirement.SOFT );
					}
				}
			}
		}
	}


	private boolean canPlaceBranch( final RuntimeData rtd , final int rx , final int ry , final int rz )
	{
		for ( int i = -1 ; i <= 1 ; i++ ) {
			for ( int j = -1 ; j <= 1 ; j++ ) {
				for ( int k = -1 ; k <= 1 ; k++ ) {
					final int cx = rx + i;
					final int cy = ry + j;
					final int cz = rz + k;
					if ( cx < 1 || cy < 1 || cz < 1 || cx >= rtd.xSize - 1 || cy >= rtd.height - 1
							|| cz >= rtd.zSize - 1 ) {
						return false;
					}

					final IBlockState bs = rtd.getBlockState( cx , cy , cz );
					if ( i == 0 && j == 0 && k == 0 && bs != null
							&& bs.getBlock( ) != this.getTreeMaterials( ).LEAVES ) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
