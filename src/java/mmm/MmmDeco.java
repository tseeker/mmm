package mmm;


import mmm.deco.DChairs;
import mmm.deco.DLights;
import mmm.deco.DSeatEntity;
import mmm.deco.DSmoothStone;
import mmm.deco.DStairs;
import mmm.deco.DTables;
import mmm.deco.doors.DDoors;
import mmm.deco.fences.DFences;
import mmm.deco.slabs.DSlabs;
import mmm.deco.thrones.DThrones;
import net.minecraftforge.fml.common.registry.EntityRegistry;



public class MmmDeco
{
	public static final DSmoothStone STONE;
	public static final DStairs STAIRS;
	public static final DSlabs SLAB;
	public static final DFences FENCE;
	public static final DDoors DOOR;
	public static final DTables TABLE;
	public static final DChairs CHAIR;
	public static final DThrones THRONE;
	public static final DLights LIGHT;

	static {
		STONE = new DSmoothStone( );
		STAIRS = new DStairs( );
		SLAB = new DSlabs( );
		FENCE = new DFences( );
		DOOR = new DDoors( );
		TABLE = new DTables( );
		CHAIR = new DChairs( );
		THRONE = new DThrones( );
		LIGHT = new DLights( );
	}


	public static void preInit( )
	{
		// EMPTY
	}


	public static void init( )
	{
		EntityRegistry.registerModEntity( DSeatEntity.class , "Seat" , 0 , Mmm.get( ) , 80 , 1 , false );
	}

}
