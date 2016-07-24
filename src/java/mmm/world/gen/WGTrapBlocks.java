package mmm.world.gen;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import mmm.MmmMaterials;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;



public class WGTrapBlocks
		extends WorldGenerator
{

	private final float chance;


	public WGTrapBlocks( final boolean notify , final float chance )
	{
		super( notify );
		this.chance = chance;
	}


	@Override
	public boolean generate( final World worldIn , final Random rand , final BlockPos position )
	{
		if ( rand.nextFloat( ) > this.chance ) {
			return false;
		}

		// Select radius and centre
		final int radius = 2 + rand.nextInt( 2 );
		final int space = 16 - radius * 2;
		final BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos(
				position.getX( ) + radius + rand.nextInt( space ) , //
				255 , //
				position.getZ( ) + radius + rand.nextInt( space ) );

		// Find the ground
		final int minDepth = Math.max( 0 , worldIn.getSeaLevel( ) - 20 );
		boolean found = false;
		while ( check.getY( ) > minDepth ) {
			final IBlockState bs = worldIn.getBlockState( check );
			final Material material = bs.getMaterial( );
			if ( material.isLiquid( ) ) {
				return false;
			}
			if ( material != Material.AIR && material != Material.WOOD && material != Material.PLANTS ) {
				found = true;
				break;
			}
			check.setY( check.getY( ) - 1 );
		}
		if ( !found ) {
			return false;
		}

		// Displace the centre vertically
		if ( check.getY( ) > radius / 2 && check.getY( ) < 255 - radius / 2 ) {
			check.setY( check.getY( ) - radius / 2 + rand.nextInt( radius ) );
		}

		// Find possible trap types
		final BlockPos bp = new BlockPos( check );
		final int sqr = radius * radius;
		final ArrayList< String > types = Lists.newArrayList( );
		for ( int x = -radius ; x <= radius ; x++ ) {
			for ( int y = -radius ; y <= radius ; y++ ) {
				for ( int z = -radius ; z <= radius ; z++ ) {
					if ( x * x + y * y + z * z > sqr + 1 ) {
						continue;
					}

					final BlockPos pos = bp.add( x , y , z );
					final IBlockState atPos = worldIn.getBlockState( pos );
					final List< String > trapTypes = MmmMaterials.TRAP.getTrapTypes( atPos );
					for ( final String rep : trapTypes ) {
						if ( !types.contains( rep ) ) {
							types.add( rep );
						}
					}
				}
			}
		}
		if ( types.isEmpty( ) ) {
			return false;
		}

		final String trapType = types.get( rand.nextInt( types.size( ) ) );
		for ( int x = -radius ; x <= radius ; x++ ) {
			for ( int y = -radius ; y <= radius ; y++ ) {
				for ( int z = -radius ; z <= radius ; z++ ) {
					if ( x * x + y * y + z * z > sqr + rand.nextInt( 2 ) ) {
						continue;
					}

					final BlockPos pos = bp.add( x , y , z );
					final IBlockState atPos = worldIn.getBlockState( pos );
					final List< IBlockState > replacements = MmmMaterials.TRAP.getReplacements( atPos , trapType );
					final int nReplacements = replacements.size( );
					if ( nReplacements == 0 ) {
						continue;
					}

					int repIndex;
					if ( nReplacements == 1 ) {
						repIndex = 0;
					} else {
						repIndex = rand.nextInt( nReplacements );
					}
					this.setBlockAndNotifyAdequately( worldIn , pos , replacements.get( repIndex ) );
				}
			}
		}

		return true;
	}

}
