package mmm.core;


import mmm.MmmDeco;
import mmm.MmmFood;
import mmm.MmmMaterials;
import mmm.MmmPlants;
import mmm.MmmTech;
import mmm.MmmWorld;
import mmm.recipes.RCraftingWrappers;
import mmm.recipes.RExtraRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;



public class CProxyCommon
{

	public void preInit( final FMLPreInitializationEvent event )
	{
		RCraftingWrappers.preInit( );
		CRegistry.addRegistrar( new RExtraRecipes( ) );
		CAccessors.preInit( );

		MmmPlants.preInit( );
		MmmMaterials.preInit( );
		MmmWorld.preInit( );
		MmmTech.preInit( );
		MmmFood.preInit( );
		MmmDeco.preInit( );

		CRegistry.registerRecipes( );
	}


	public void init( final FMLInitializationEvent event )
	{
		MmmWorld.init( );
		MmmDeco.init( );
	}


	public void postInit( final FMLPostInitializationEvent event )
	{
		RCraftingWrappers.wrapRecipes( );
	}

}