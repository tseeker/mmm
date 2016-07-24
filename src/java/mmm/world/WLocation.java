package mmm.world;


import mmm.core.api.world.I_LocationCheck;
import net.minecraft.world.biome.Biome;



public class WLocation
{
	private static final I_LocationCheck ANYWHERE = ( w , x , z , p ) -> true;

	private static I_LocationCheck inOverworld;
	private static I_LocationCheck inTheNether;


	public static I_LocationCheck inDimension( final int dimension )
	{
		return new WLocationInDimension( dimension );
	}


	public static I_LocationCheck inOverworld( )
	{
		if ( WLocation.inOverworld == null ) {
			WLocation.inOverworld = WLocation.inDimension( 0 );
		}
		return WLocation.inOverworld;
	}


	public static I_LocationCheck inTheNether( )
	{
		if ( WLocation.inTheNether == null ) {
			WLocation.inTheNether = WLocation.inDimension( -1 );
		}
		return WLocation.inTheNether;
	}


	public static < T extends Biome > WLocationInBiomeClass< T > inBiome( final Class< T > biomeType )
	{
		return new WLocationInBiomeClass<>( biomeType );
	}


	public static I_LocationCheck anywhere( )
	{
		return WLocation.ANYWHERE;
	}
}
