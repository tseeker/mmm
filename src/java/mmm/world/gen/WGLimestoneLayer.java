package mmm.world.gen;


import java.util.Random;

import mmm.MmmMaterials;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;



public class WGLimestoneLayer
		extends WorldGenerator
{
	private static final IBlockState BS_DIRT = Blocks.DIRT.getDefaultState( );
	private static final IBlockState BS_COARSE_DIRT = Blocks.DIRT.getDefaultState( )//
			.withProperty( BlockDirt.VARIANT , BlockDirt.DirtType.COARSE_DIRT );
	private static final IBlockState BS_GRASS = Blocks.GRASS.getDefaultState( );

	private static final IBlockState BS_STONE = Blocks.STONE.getDefaultState( );
	private static final IBlockState BS_GRANITE = Blocks.STONE.getDefaultState( )//
			.withProperty( BlockStone.VARIANT , BlockStone.EnumType.GRANITE );
	private static final IBlockState BS_DIORITE = Blocks.STONE.getDefaultState( )//
			.withProperty( BlockStone.VARIANT , BlockStone.EnumType.DIORITE );
	private static final IBlockState BS_ANDESITE = Blocks.STONE.getDefaultState( )//
			.withProperty( BlockStone.VARIANT , BlockStone.EnumType.ANDESITE );

	private static final IBlockState BS_GRAVEL = Blocks.GRAVEL.getDefaultState( );

	private static final IBlockState BS_LIMESTONE = MmmMaterials.ROCK.LIMESTONE.getDefaultState( );


	@Override
	public boolean generate( final World worldIn , final Random rand , final BlockPos pos )
	{
		final BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos( pos );
		for ( int i = 0 ; i < 16 ; i++ ) {
			for ( int j = 0 ; j < 16 ; j++ ) {
				mbp.setPos( pos.getX( ) + i , 255 , pos.getZ( ) + j );

				int toY = -1;
				for ( int y = 255 ; y >= 0 ; y-- ) {
					mbp.setY( y );

					final IBlockState bs = worldIn.getBlockState( mbp );
					if ( bs == WGLimestoneLayer.BS_STONE || bs == WGLimestoneLayer.BS_GRANITE
							|| bs == WGLimestoneLayer.BS_DIORITE || bs == WGLimestoneLayer.BS_ANDESITE ) {
						if ( y < toY ) {
							continue;
						}
						this.replaceWithLimestone( worldIn , rand , mbp );
						if ( toY == -1 ) {
							toY = worldIn.getSeaLevel( ) - 15 - rand.nextInt( 5 );
						}
						continue;
					}

					if ( bs == WGLimestoneLayer.BS_DIRT || bs == WGLimestoneLayer.BS_GRAVEL
							|| bs == WGLimestoneLayer.BS_GRASS ) {
						this.replaceWithCoarseDirt( worldIn , rand , mbp );
					}
				}
			}
		}
		return true;
	}


	private void replaceWithLimestone( final World worldIn , final Random rand , final BlockPos.MutableBlockPos mbp )
	{
		if ( rand.nextInt( 8 ) != 0 ) {
			worldIn.setBlockState( mbp , WGLimestoneLayer.BS_LIMESTONE );
		}
	}


	private void replaceWithCoarseDirt( final World worldIn , final Random rand , final BlockPos.MutableBlockPos mbp )
	{
		if ( rand.nextInt( 16 ) == 0 ) {
			worldIn.setBlockState( mbp , WGLimestoneLayer.BS_COARSE_DIRT );
		}
	}

}
