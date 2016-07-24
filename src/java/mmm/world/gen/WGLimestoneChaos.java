package mmm.world.gen;


import java.util.Random;

import mmm.MmmMaterials;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;



public class WGLimestoneChaos
		extends WorldGenerator
{

	private static final IBlockState BS_LIMESTONE = MmmMaterials.ROCK.LIMESTONE.getDefaultState( );
	private static final IBlockState BS_STONE = Blocks.STONE.getDefaultState( );
	private static final IBlockState BS_AIR = Blocks.AIR.getDefaultState( );
	private static final IBlockState BS_COARSE_DIRT = Blocks.DIRT.getDefaultState( ).withProperty( BlockDirt.VARIANT ,
			BlockDirt.DirtType.COARSE_DIRT );
	private static final IBlockState BS_GRAVEL = Blocks.GRAVEL.getDefaultState( );
	private static final IBlockState BS_WATER = Blocks.WATER.getDefaultState( );

	private static final int SB_IGNORE = 0;
	private static final int SB_LIMESTONE = 1;
	private static final int SB_DIRT = 2;
	private static final int SB_GRAVEL = 3;
	private static final int SB_WATER = 4;
	private static final int SB_HOLE = 5;
	private static final int SB_MAYBE = 8;


	@Override
	public boolean generate( final World worldIn , final Random rand , final BlockPos position )
	{
		final BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos(
				position.add( rand.nextInt( 16 ) , 0 , rand.nextInt( 16 ) ) );

		// Find stone
		int lastAirY = 255;
		int lastGroundY = 255;
		for ( int y = 255 ; y >= worldIn.getSeaLevel( ) - 20 ; y-- ) {
			mbp.setY( y );
			final IBlockState blockState = worldIn.getBlockState( mbp );
			if ( blockState == WGLimestoneChaos.BS_STONE || blockState == WGLimestoneChaos.BS_LIMESTONE ) {
				lastGroundY = y;
				break;
			}
			if ( blockState.getMaterial( ) == Material.AIR ) {
				lastAirY = y;
			}
		}
		if ( lastGroundY == 255 ) {
			return false;
		}

		final int shapeHeight = lastAirY - mbp.getY( ) + 3;
		final int[] shape = this.generateShape( rand , shapeHeight );

		if ( !this.checkLiquids( worldIn , position , mbp , lastGroundY , shapeHeight , shape ) ) {
			return false;
		}

		final int airCount = this.checkAirBlocks( worldIn , position , mbp , lastGroundY , shapeHeight , shape );
		if ( airCount * 2 > 16 * 16 * shapeHeight ) {
			return false;
		}

		this.digPit( rand , shapeHeight , shape );
		this.makePillars( rand , shapeHeight , shape );
		this.addWaterHoles( rand , shapeHeight , shape );

		this.build( worldIn , position , mbp , lastGroundY , shapeHeight , shape );

		return true;
	}


	private int[] generateShape( final Random rand , final int shapeHeight )
	{
		final int shape[] = new int[ 16 * 16 * shapeHeight ];
		final int nCylinders = 8 + rand.nextInt( 8 );
		for ( int c = 0 ; c < nCylinders ; c++ ) {
			final double xSize = rand.nextDouble( ) * 6.0D + 3.0D;
			final double zSize = rand.nextDouble( ) * 6.0D + 3.0D;
			final double xCenter = rand.nextDouble( ) * ( 16.0D - xSize - 2.0D ) + 1.0D + xSize / 2.0D;
			final double zCenter = rand.nextDouble( ) * ( 16.0D - zSize - 2.0D ) + 1.0D + zSize / 2.0D;
			final double xDelta = ( 8 - xCenter ) * .5f / shapeHeight;
			final double zDelta = ( 8 - zCenter ) * .5f / shapeHeight;

			for ( int x = 1 ; x < 15 ; ++x ) {
				for ( int z = 1 ; z < 15 ; ++z ) {
					double xc = xCenter , zc = zCenter;
					for ( int y = 1 ; y < shapeHeight - 1 ; ++y ) {
						final double dx = ( x - xc ) / ( xSize / 2.0D );
						final double dz = ( z - zc ) / ( zSize / 2.0D );
						final double sqd = dx * dx + dz * dz;

						if ( sqd < 1.0D ) {
							final int rnd = rand.nextInt( 20 );
							int out;
							switch ( rnd ) {
								case 0:
									out = WGLimestoneChaos.SB_DIRT;
									break;
								case 1:
									out = WGLimestoneChaos.SB_GRAVEL;
									break;
								default:
									out = WGLimestoneChaos.SB_LIMESTONE;
									break;
							}
							shape[ ( x * 16 + z ) * shapeHeight + y ] = out;
						}

						xc += xDelta;
						zc += zDelta;
					}
				}
			}
		}
		return shape;
	}


	private boolean checkLiquids( final World worldIn , final BlockPos position , final BlockPos.MutableBlockPos mbp ,
			final int lastGroundY , final int shapeHeight , final int[] shape )
	{
		for ( int x = 0 ; x < 16 ; ++x ) {
			for ( int z = 0 ; z < 16 ; ++z ) {
				mbp.setPos( position.getX( ) + x - 8 , 0 , position.getZ( ) + z - 8 );
				for ( int y = 0 ; y < shapeHeight ; y++ ) {
					mbp.setY( lastGroundY + y );
					if ( shape[ ( x * 16 + z ) * shapeHeight + y ] != WGLimestoneChaos.SB_IGNORE
							&& worldIn.getBlockState( mbp ).getMaterial( ).isLiquid( ) ) {
						return false;
					}
				}
			}
		}
		return true;
	}


	private int checkAirBlocks( final World worldIn , final BlockPos position , final BlockPos.MutableBlockPos mbp ,
			final int lastGroundY , final int shapeHeight , final int[] shape )
	{
		int airCount = 0;
		for ( int x = 1 ; x < 15 ; ++x ) {
			for ( int z = 1 ; z < 15 ; ++z ) {
				mbp.setPos( position.getX( ) + x - 8 , 0 , position.getZ( ) + z - 8 );
				boolean inAir = true;
				for ( int y = shapeHeight ; y > 1 ; y-- ) {
					mbp.setY( lastGroundY + y );
					if ( worldIn.getBlockState( mbp ).getMaterial( ) != Material.AIR ) {
						inAir = false;
					} else {
						airCount++;
					}
					if ( inAir ) {
						final int v = shape[ ( x * 16 + z ) * shapeHeight + y ];
						if ( v != 0 ) {
							shape[ ( x * 16 + z ) * shapeHeight + y ] += WGLimestoneChaos.SB_MAYBE;
						}
					}
				}
			}
		}
		return airCount;
	}


	private void digPit( final Random rand , final int shapeHeight , final int[] shape )
	{
		for ( int x = 2 ; x < 14 ; ++x ) {
			for ( int z = 2 ; z < 14 ; ++z ) {
				if ( rand.nextInt( 32 ) == 0 ) {
					continue;
				}

				for ( int y = shapeHeight - 2 ; y > 1 + rand.nextInt( 3 ) ; --y ) {
					final boolean digOk = WGLimestoneChaos.isSet( shape , shapeHeight , x , y , z ) //
							&& WGLimestoneChaos.isSet( shape , shapeHeight , x + 1 , y , z + 0 ) //
							&& WGLimestoneChaos.isSet( shape , shapeHeight , x + 1 , y , z + 1 ) //
							&& WGLimestoneChaos.isSet( shape , shapeHeight , x + 0 , y , z + 1 ) //
							&& WGLimestoneChaos.isSet( shape , shapeHeight , x - 1 , y , z + 1 ) //
							&& WGLimestoneChaos.isSet( shape , shapeHeight , x - 1 , y , z + 1 ) //
							&& WGLimestoneChaos.isSet( shape , shapeHeight , x - 1 , y , z - 1 ) //
							&& WGLimestoneChaos.isSet( shape , shapeHeight , x + 0 , y , z - 1 ) //
							&& WGLimestoneChaos.isSet( shape , shapeHeight , x + 1 , y , z - 1 );
					if ( digOk ) {
						shape[ ( ( x + 0 ) * 16 + z + 0 ) * shapeHeight + y ] = WGLimestoneChaos.SB_HOLE;
						if ( rand.nextInt( 64 ) == 0 ) {
							break;
						}
					}
				}
			}
		}
	}


	private void makePillars( final Random rand , final int shapeHeight , final int[] shape )
	{
		for ( int x = 1 ; x < 15 ; ++x ) {
			for ( int z = 1 ; z < 15 ; ++z ) {
				for ( int y = 1 ; y <= shapeHeight ; y++ ) {
					final int v = shape[ ( x * 16 + z ) * shapeHeight + y ];
					if ( v == WGLimestoneChaos.SB_IGNORE || v == WGLimestoneChaos.SB_HOLE ) {
						break;
					}
					if ( v > WGLimestoneChaos.SB_MAYBE ) {
						if ( rand.nextInt( 5 ) < 3 ) {
							break;
						}
						shape[ ( x * 16 + z ) * shapeHeight + y ] = v - WGLimestoneChaos.SB_MAYBE;
					}
				}
			}
		}
	}


	private void addWaterHoles( final Random rand , final int shapeHeight , final int[] shape )
	{
		for ( int x = 1 ; x < 15 ; ++x ) {
			for ( int z = 1 ; z < 15 ; ++z ) {
				for ( int y = 1 ; y <= shapeHeight ; y++ ) {
					final boolean air = WGLimestoneChaos.isAir( shape , shapeHeight , x , y , z );
					if ( air ) {
						final boolean waterOk = rand.nextInt( 3 ) == 0 //
								&& WGLimestoneChaos.canHoldWater( shape , shapeHeight , x + 1 , y , z ) //
								&& WGLimestoneChaos.canHoldWater( shape , shapeHeight , x - 1 , y , z ) //
								&& WGLimestoneChaos.canHoldWater( shape , shapeHeight , x , y , z + 1 ) //
								&& WGLimestoneChaos.canHoldWater( shape , shapeHeight , x , y , z - 1 ) //
								&& WGLimestoneChaos.canHoldWater( shape , shapeHeight , x , y - 1 , z );
						if ( waterOk ) {
							shape[ ( x * 16 + z ) * shapeHeight + y ] = WGLimestoneChaos.SB_WATER;
						}
						break;
					}
				}
			}
		}
	}


	private static boolean isSet( final int[] shape , final int shapeHeight , final int x , final int y , final int z )
	{
		final int value = shape[ ( x * 16 + z ) * shapeHeight + y ];
		return value != WGLimestoneChaos.SB_IGNORE;
	}


	private static boolean canHoldWater( final int[] shape , final int shapeHeight , final int x , final int y ,
			final int z )
	{
		final int value = shape[ ( x * 16 + z ) * shapeHeight + y ];
		return value == WGLimestoneChaos.SB_DIRT || value == WGLimestoneChaos.SB_LIMESTONE;
	}


	private static boolean isAir( final int[] shape , final int shapeHeight , final int x , final int y , final int z )
	{
		final int value = shape[ ( x * 16 + z ) * shapeHeight + y ];
		return value == WGLimestoneChaos.SB_IGNORE || value == WGLimestoneChaos.SB_HOLE
				|| value > WGLimestoneChaos.SB_MAYBE;
	}


	private void build( final World worldIn , final BlockPos position , final BlockPos.MutableBlockPos mbp ,
			final int lastGroundY , final int shapeHeight , final int[] shape )
	{
		for ( int x = 0 ; x < 16 ; ++x ) {
			for ( int z = 0 ; z < 16 ; ++z ) {
				mbp.setPos( position.getX( ) + x - 8 , 0 , position.getZ( ) + z - 8 );
				for ( int y = 0 ; y < shapeHeight ; y++ ) {
					mbp.setY( lastGroundY + y );
					switch ( shape[ ( x * 16 + z ) * shapeHeight + y ] ) {
						default:
							break;

						case SB_LIMESTONE:
							worldIn.setBlockState( mbp , WGLimestoneChaos.BS_LIMESTONE , 2 );
							break;
						case SB_DIRT:
							worldIn.setBlockState( mbp , WGLimestoneChaos.BS_COARSE_DIRT , 2 );
							break;
						case SB_GRAVEL:
							worldIn.setBlockState( new BlockPos( mbp ) , WGLimestoneChaos.BS_GRAVEL , 2 );
							break;
						case SB_WATER:
							worldIn.setBlockState( mbp , WGLimestoneChaos.BS_WATER , 2 );
							break;
						case SB_HOLE:
							worldIn.setBlockState( mbp , WGLimestoneChaos.BS_AIR , 2 );
							break;
					}
				}
			}
		}
	}

}
