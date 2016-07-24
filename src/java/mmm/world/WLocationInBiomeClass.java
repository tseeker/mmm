package mmm.world;


import mmm.core.api.world.I_LocationCheck;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;



public class WLocationInBiomeClass< T extends Biome >
		implements I_LocationCheck
{
	public final Class< T > biomeType;


	public WLocationInBiomeClass( final Class< T > biomeType )
	{
		this.biomeType = biomeType;
	}


	@Override
	public boolean checkLocation( final World world , final int chunkX , final int chunkZ ,
			final IChunkProvider provider )
	{
		final Biome biome = world.getBiomeGenForCoords( new BlockPos( chunkX * 16 , 0 , chunkZ * 16 ) );
		return this.biomeType.isAssignableFrom( biome.getClass( ) );
	}

}
