package mmm;


import mmm.materials.MAlloys;
import mmm.materials.MItems;
import mmm.materials.MMetals;
import mmm.materials.MOres;
import mmm.materials.MRocks;
import mmm.materials.MWoods;
import mmm.materials.traps.MTraps;
import mmm.materials.trees.MTrees;
import mmm.materials.trees.MWoodAchievementHandler;
import net.minecraftforge.common.MinecraftForge;



public class MmmMaterials
{
	public static final MRocks ROCK;
	public static final MMetals METAL;
	public static final MAlloys ALLOY;
	public static final MTrees TREE;
	public static final MWoods WOOD;
	public static final MItems ITEM;
	public static final MOres ORE;
	public static final MTraps TRAP;

	static {
		ROCK = new MRocks( );
		METAL = new MMetals( );
		ALLOY = new MAlloys( );
		TREE = new MTrees( );
		WOOD = new MWoods( );
		ITEM = new MItems( );
		ORE = new MOres( );
		TRAP = new MTraps( );

		MinecraftForge.EVENT_BUS.register( new MWoodAchievementHandler( ) );
	}


	public static void preInit( )
	{
		// EMPTY
	}

}
