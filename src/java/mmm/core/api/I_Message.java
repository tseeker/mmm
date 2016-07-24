package mmm.core.api;


import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public interface I_Message
		extends IMessage
{

	@SideOnly( Side.CLIENT )
	default void handleOnClient( final EntityPlayerSP player )
	{
		// EMPTY
	}


	default void handleOnServer( final EntityPlayerMP player )
	{
		// EMPTY
	}

}
