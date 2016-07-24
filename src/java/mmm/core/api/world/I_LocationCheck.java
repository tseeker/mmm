package mmm.core.api.world;


import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;



public interface I_LocationCheck
{

	public boolean checkLocation( World world , int chunkX , int chunkZ , IChunkProvider provider );


	default I_LocationCheck and( final I_LocationCheck other )
	{
		return ( w , x , z , p ) -> this.checkLocation( w , x , z , p ) && other.checkLocation( w , x , z , p );
	}


	default I_LocationCheck or( final I_LocationCheck other )
	{
		return ( w , x , z , p ) -> this.checkLocation( w , x , z , p ) || other.checkLocation( w , x , z , p );
	}


	default I_LocationCheck invert( )
	{
		return ( w , x , z , p ) -> !this.checkLocation( w , x , z , p );
	}

}
