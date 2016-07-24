package mmm.world;


import mmm.core.api.world.I_LocationCheck;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;



public class WLocationInDimension
		implements I_LocationCheck
{

	public final int dimension;


	public WLocationInDimension( final int dimension )
	{
		this.dimension = dimension;
	}


	@Override
	public boolean checkLocation( final World world , final int chunkX , final int chunkZ ,
			final IChunkProvider provider )
	{
		return world.provider.getDimension( ) == this.dimension;
	}

}
