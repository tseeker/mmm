package mmm.world;


import mmm.core.api.world.I_LocationCheck;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;



public class WLocationTemperature
		implements I_LocationCheck
{
	public final float minTemperature;
	public final float maxTemperature;


	public WLocationTemperature( final float minTemperature , final float maxTemperature )
	{
		if ( maxTemperature < minTemperature ) {
			throw new IllegalArgumentException( "minimal temperature is higher than maximal temperature" );
		}
		this.minTemperature = minTemperature;
		this.maxTemperature = maxTemperature;
	}


	@Override
	public boolean checkLocation( final World world , final int chunkX , final int chunkZ ,
			final IChunkProvider provider )
	{
		final Biome biome = world.getBiomeGenForCoords( new BlockPos( chunkX * 16 , 0 , chunkZ * 16 ) );
		final float t = biome.getTemperature( );
		return t >= this.minTemperature && t <= this.maxTemperature;
	}

}
