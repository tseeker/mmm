package mmm.world;


import mmm.core.api.world.I_LocationCheck;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;



public class WLocationRainfall
		implements I_LocationCheck
{
	public final float minRainfall;
	public final float maxRainfall;


	public WLocationRainfall( final float minRainfall , final float maxRainfall )
	{
		if ( maxRainfall < minRainfall ) {
			throw new IllegalArgumentException( "minimal rainfall is higher than maximal rainfall" );
		}
		this.minRainfall = minRainfall;
		this.maxRainfall = maxRainfall;
	}


	@Override
	public boolean checkLocation( final World world , final int chunkX , final int chunkZ ,
			final IChunkProvider provider )
	{
		final Biome biome = world.getBiomeGenForCoords( new BlockPos( chunkX * 16 , 0 , chunkZ * 16 ) );
		final float rf = biome.getRainfall( );
		return rf >= this.minRainfall && rf <= this.maxRainfall;
	}

}
