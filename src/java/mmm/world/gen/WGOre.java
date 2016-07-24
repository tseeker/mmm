package mmm.world.gen;


import java.util.ArrayList;
import java.util.Random;

import mmm.core.api.world.I_BiomeWithOres;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;



public class WGOre
		implements IWorldGenerator
{

	private final ArrayList< WGOreCondition > conditions;


	public WGOre( final ArrayList< WGOreCondition > conditions )
	{
		this.conditions = conditions;
	}


	@Override
	public void generate( final Random random , final int chunkX , final int chunkZ , final World world ,
			final IChunkGenerator chunkGenerator , final IChunkProvider chunkProvider )
	{
		final Biome biome = world.getBiomeGenForCoords( new BlockPos( chunkX * 16 , 0 , chunkZ * 16 ) );
		if ( biome instanceof I_BiomeWithOres ) {
			for ( final WGOreParameters parameters : ( (I_BiomeWithOres) biome ).getBiomeOres( world ) ) {
				parameters.generate( world , random , chunkX , chunkZ );
			}
		}

		final int n = this.conditions.size( );
		for ( int i = 0 ; i < n ; i++ ) {
			final WGOreCondition cond = this.conditions.get( i );
			if ( cond.conditions.checkLocation( world , chunkX , chunkZ , chunkProvider ) ) {
				cond.parameters.generate( world , random , chunkX , chunkZ );
			}
		}
	}

}
