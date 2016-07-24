package mmm;


import mmm.plants.PNetherCoral;
import mmm.plants.PTomato;



public class MmmPlants
{

	public static final PTomato TOMATO;
	public static final PNetherCoral NETHER_CORAL;

	static {
		TOMATO = new PTomato( );
		NETHER_CORAL = new PNetherCoral( );
	}


	public static void preInit( )
	{
		// EMPTY
	}

}
