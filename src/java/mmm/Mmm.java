package mmm;


import mmm.core.CProxyCommon;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;



@Mod( modid = Mmm.ID , name = Mmm.NAME , version = Mmm.VERSION )
public class Mmm
{

	public static final String ID = "mmm";
	public static final String NAME = "MMM!";
	public static final String VERSION = "@VERSION@";
	public static final String PREFIX = Mmm.ID + ".";

	@Mod.Instance( Mmm.ID )
	private static Mmm instance;

	@SidedProxy( clientSide = "mmm.core.CProxyClient" , serverSide = "mmm.core.CProxyCommon" )
	private static CProxyCommon proxy = null;


	public static Mmm get( )
	{
		return Mmm.instance;
	}


	@Mod.EventHandler
	public void preInit( final FMLPreInitializationEvent event )
	{
		Mmm.proxy.preInit( event );
	}


	@Mod.EventHandler
	public void init( final FMLInitializationEvent event )
	{
		Mmm.proxy.init( event );
	}


	@Mod.EventHandler
	public void postInit( final FMLPostInitializationEvent event )
	{
		Mmm.proxy.postInit( event );
	}

}
