package mmm.world.gen;


import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import mmm.MmmMaterials;
import mmm.world.trees.A_WTTreeGenerator;
import mmm.world.trees.WTBamboo;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;



public class WGBambooPatch
		extends WorldGenerator
{
	private static final A_WTTreeGenerator TG_BAMBOO_BIG = new WTBamboo( true , false );
	private static final A_WTTreeGenerator TG_BAMBOO = new WTBamboo( false , false );

	private static final IBlockState BS_WATER = Blocks.WATER.getDefaultState( );


	@Override
	public boolean generate( final World world , final Random rand , final BlockPos position )
	{
		// Find suitable start locations
		final List< BlockPos > locations = Lists.newArrayList( );
		for ( int x = 0 ; x < 16 ; x++ ) {
			for ( int z = 0 ; z < 16 ; z++ ) {
				for ( int y = 255 ; y >= 0 ; y-- ) {
					final BlockPos checkPos = position.add( x , y , z );
					if ( this.checkBlock( world , checkPos ) ) {
						locations.add( checkPos.add( 0 , 1 , 0 ) );
					}
				}
			}
		}
		if ( locations.isEmpty( ) ) {
			return false;
		}

		// Generate placement
		final BlockPos gridCentre = locations.remove( rand.nextInt( locations.size( ) ) );
		int tryPlace = 60 + rand.nextInt( 30 );
		final boolean placing[] = new boolean[ 57 * 57 ];
		int cx = 0 , cz = 0;
		for ( int i = 0 ; i < 4 ; i++ ) {
			final int radius = 2 + rand.nextInt( 4 );
			final int sqRadius = radius * radius + rand.nextInt( 2 );
			for ( int x = -radius ; x <= radius && tryPlace != 0 ; x++ ) {
				for ( int z = -radius ; z <= radius && tryPlace != 0 ; z++ ) {
					if ( x * x + z * z <= sqRadius ) {
						final int offset = ( 28 + cz + z ) * 57 + 28 + cx + x;
						if ( !placing[ offset ] && rand.nextFloat( ) < .8f ) {
							placing[ offset ] = true;
							tryPlace--;
						}
					}
				}
			}
			if ( tryPlace == 0 ) {
				break;
			}
			final float cp = rand.nextFloat( ) * 2f * (float) Math.PI;
			cx = MathHelper.ceiling_float_int( MathHelper.cos( cp ) * radius );
			cz = MathHelper.ceiling_float_int( MathHelper.sin( cp ) * radius );
		}

		// Actually place the bamboos
		for ( int x = -28 , offset = 0 ; x <= 28 ; x++ ) {
			for ( int z = -28 ; z <= 28 ; z++ , offset++ ) {
				if ( !placing[ offset ] ) {
					continue;
				}

				BlockPos l = gridCentre.add( x , 0 , z );
				final IBlockState bs = world.getBlockState( l );
				if ( bs.getMaterial( ) == Material.AIR ) {
					while ( world.getBlockState( l.down( ) ).getMaterial( ) == Material.AIR ) {
						l = l.down( );
					}
				} else {
					do {
						l = l.up( );
					} while ( world.getBlockState( l ).getMaterial( ) != Material.AIR );
				}
				if ( !MmmMaterials.TREE.BAMBOO.canSaplingStay( world , l ) ) {
					continue;
				}

				int wa = 0;
				for ( int i = 0 ; i < 2 ; i++ ) {
					for ( int j = 0 ; j < 2 ; j++ ) {
						final BlockPos wCheck = l.add( i * 2 - 1 , -1 , j * 2 - 1 );
						if ( world.getBlockState( wCheck ) == WGBambooPatch.BS_WATER ) {
							wa++;
						}
					}
				}

				final int rnd = rand.nextInt( 30 );
				if ( rnd <= wa ) {
					WGBambooPatch.TG_BAMBOO_BIG.generate( world , rand , l );
				} else if ( rnd <= 10 ) {
					this.setBlockAndNotifyAdequately( world , l , MmmMaterials.TREE.BAMBOO.SAPLING.getDefaultState( ) );
				} else {
					WGBambooPatch.TG_BAMBOO.generate( world , rand , l );
				}
			}
		}

		return false;
	}


	private boolean checkBlock( final World world , final BlockPos position )
	{
		final IBlockState bs = world.getBlockState( position );
		if ( bs != WGBambooPatch.BS_WATER ) {
			return false;
		}
		for ( int x = -1 ; x <= 1 ; x++ ) {
			for ( int z = -1 ; z <= 1 ; z++ ) {
				if ( ( x != 0 || z != 0 )
						&& MmmMaterials.TREE.BAMBOO.canSaplingStay( world , position.add( x , 1 , z ) ) ) {
					return true;
				}
			}
		}
		return false;
	}

}
