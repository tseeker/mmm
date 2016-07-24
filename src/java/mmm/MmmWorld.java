package mmm;


import java.util.ArrayList;

import mmm.core.CRegistry;
import mmm.core.api.I_OreGenerationRegistrar;
import mmm.world.WDefaultGenWatcher;
import mmm.world.biome.WBiomes;
import mmm.world.gen.WGOre;
import mmm.world.gen.WGOreCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class MmmWorld
{
	public static final WBiomes BIOME;

	static {
		final WDefaultGenWatcher dgw = new WDefaultGenWatcher( );
		MinecraftForge.TERRAIN_GEN_BUS.register( dgw );
		MinecraftForge.EVENT_BUS.register( dgw );

		BIOME = new WBiomes( );
	}


	public static void preInit( )
	{
		// EMPTY
	}


	public static void init( )
	{
		final ArrayList< WGOreCondition > conditions = new ArrayList<>( );
		for ( final I_OreGenerationRegistrar registrar : CRegistry.getOreGenerationRegistrars( ) ) {
			registrar.addConditions( conditions );
		}
		GameRegistry.registerWorldGenerator( new WGOre( conditions ) , 0 );
	}

}
