package mmm.world.biome;


import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import mmm.MmmMaterials;
import mmm.core.api.world.I_BiomeWithOres;
import mmm.core.api.world.I_DefaultPopulateHandler;
import mmm.world.gen.WGLimestoneLayer;
import mmm.world.gen.WGOreParameters;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;



public abstract class A_WBLimestone
		extends Biome
		implements I_DefaultPopulateHandler , I_BiomeWithOres
{

	public A_WBLimestone( final BiomeProperties properties )
	{
		super( properties );
	}


	@Override
	public void decorate( final World worldIn , final Random rand , final BlockPos pos )
	{
		super.decorate( worldIn , rand , pos );
		new WGLimestoneLayer( ).generate( worldIn , rand , pos );
	}


	@Override
	public boolean onDefaultPopulate( final PopulateChunkEvent.Populate event )
	{
		// No lava lakes inside the limestone
		if ( event.getType( ) == PopulateChunkEvent.Populate.EventType.LAVA ) {
			final Random rand = event.getRand( );
			final World world = event.getWorld( );
			final int x = rand.nextInt( 16 ) + 8;
			final int y = rand.nextInt( rand.nextInt( 248 ) + 8 );
			final int z = rand.nextInt( 16 ) + 8;

			if ( y < world.getSeaLevel( ) - 20 ) {
				new WorldGenLakes( Blocks.LAVA ).generate( world , rand ,
						new BlockPos( event.getChunkX( ) * 16 + x , y , event.getChunkZ( ) * 16 + z ) );
			}

			return false;
		}

		return true;
	}


	@Override
	public WGOreParameters[] getBiomeOres( final World worldIn )
	{
		final Predicate< IBlockState > stoneOrLimestone = Predicates.or( BlockMatcher.forBlock( Blocks.STONE ) ,
				BlockMatcher.forBlock( MmmMaterials.ROCK.LIMESTONE ) );
		return new WGOreParameters[] {
				new WGOreParameters( MmmMaterials.ROCK.CHALK.getDefaultState( ) , 15 , 20 , //
						worldIn.getSeaLevel( ) - 20 , 255 , //
						stoneOrLimestone ) ,
				new WGOreParameters( MmmMaterials.ORE.BAUXITE.getDefaultState( ) , 10 , 9 , 45 , 255 ,
						stoneOrLimestone )
		};
	}

}
