package mmm.world.gen;


import java.util.Random;

import mmm.MmmMaterials;
import mmm.materials.MBlockTypes;
import mmm.utils.UMaterials;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;



public class WGBasalt
		extends WorldGenerator
{
	private static final IBlockState BS_BASALT = MmmMaterials.ROCK.BASALT.getDefaultState( );


	public WGBasalt( final boolean notify )
	{
		super( notify );
	}


	@Override
	public boolean generate( final World world , final Random rand , final BlockPos position )
	{
		if ( world.provider.getDimensionType( ) != DimensionType.OVERWORLD ) {
			return false;
		}

		// TODO: prevent basalt from generating in some biomes

		// Find all rock blocks that are close to some lava
		final boolean rockNearLava[] = new boolean[ 16 * 16 * 256 ];
		final BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos( );
		for ( int x = 0 ; x < 16 ; x++ ) {
			for ( int z = 0 ; z < 16 ; z++ ) {
				mbp.setPos( position.getX( ) + x , 0 , position.getZ( ) + z );
				for ( int y = 0 ; y < 256 ; y++ ) {
					mbp.setY( y );
					final IBlockState bs = world.getBlockState( mbp );
					if ( bs.getBlock( ) != Blocks.LAVA ) {
						continue;
					}
					this.scanForRocksAround( world , rockNearLava , mbp , x , y , z );
				}
			}
		}

		// Replace some of them with basalt
		int offset = 0;
		for ( int x = 0 ; x < 16 ; x++ ) {
			for ( int z = 0 ; z < 16 ; z++ ) {
				mbp.setPos( position.getX( ) + x , 0 , position.getZ( ) + z );
				for ( int y = 0 ; y < 256 ; y++ ) {
					if ( rockNearLava[ offset++ ] && rand.nextInt( 5 ) != 4 ) {
						mbp.setY( y );
						world.setBlockState( mbp , WGBasalt.BS_BASALT );
					}
				}
			}
		}

		return true;
	}


	private void scanForRocksAround( final World world , final boolean[] rockNearLava ,
			final BlockPos.MutableBlockPos mbp , final int x , final int y , final int z )
	{
		final BlockPos.MutableBlockPos mbps = new BlockPos.MutableBlockPos( );
		for ( int i = -2 ; i <= 2 ; i++ ) {
			final int xb = x + i;

			for ( int j = -2 ; j <= 2 ; j++ ) {
				final int zb = z + j;

				for ( int k = -2 ; k <= 2 ; k++ ) {
					final int yb = y + k;
					if ( i == 0 && j == 0 && k == 0 || xb < 0 || xb > 15 || zb < 0 || zb > 15 || yb < 0 || yb > 255 ) {
						continue;
					}

					final int offset = ( xb * 16 + zb ) * 256 + yb;
					if ( rockNearLava[ offset ] ) {
						continue;
					}

					mbps.setPos( mbp.getX( ) + i , yb , mbp.getZ( ) + j );
					// System.err.println( "pos = " + mbps );
					if ( UMaterials.hasType( MBlockTypes.ROCK , world.getBlockState( mbps ) ) ) {
						rockNearLava[ offset ] = true;
					}
				}
			}
		}
	}
}
