package mmm.core;


import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;



public class CProxyClient
		extends CProxyCommon
{

	@Override
	public void preInit( final FMLPreInitializationEvent event )
	{
		super.preInit( event );
		CRegistry.preInitClient( );
	}


	@Override
	public void init( final FMLInitializationEvent event )
	{
		super.init( event );
		CRegistry.initClient( );
	}

}