package mmm.core;


import mmm.Mmm;
import mmm.core.api.I_Message;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;



public class CNetwork
{
	private static final SimpleNetworkWrapper NET = new SimpleNetworkWrapper( Mmm.ID );
	private static int nextPacketDiscriminator = 0;


	public static void addClientMessage( final Class< ? extends I_Message > message )
	{
		CNetwork.NET.registerMessage( //
				( final I_Message m , final MessageContext ctx ) -> {
					final IThreadListener main = Minecraft.getMinecraft( );
					main.addScheduledTask( ( ) -> m.handleOnClient( Minecraft.getMinecraft( ).thePlayer ) );
					return null;
				} , //
				message , CNetwork.nextPacketDiscriminator++ , Side.CLIENT );
	}


	public static void addServerMessage( final Class< ? extends I_Message > message )
	{
		CNetwork.NET.registerMessage( //
				( final I_Message m , final MessageContext ctx ) -> {
					final IThreadListener main = (WorldServer) ctx.getServerHandler( ).playerEntity.worldObj;
					main.addScheduledTask( ( ) -> m.handleOnServer( ctx.getServerHandler( ).playerEntity ) );
					return null;
				} , //
				message , CNetwork.nextPacketDiscriminator++ , Side.SERVER );
	}


	public static void sendToServer( final I_Message msg )
	{
		CNetwork.NET.sendToServer( msg );
	}


	public static void sendTo( final I_Message msg , final EntityPlayerMP player )
	{
		CNetwork.NET.sendTo( msg , player );
	}


	public static void sendToAll( final I_Message msg )
	{
		CNetwork.NET.sendToAll( msg );
	}
}
