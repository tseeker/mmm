package mmm.tech;


import io.netty.buffer.ByteBuf;
import mmm.core.api.I_Message;
import mmm.core.api.tech.E_ActivationMode;
import mmm.core.api.tech.I_ConfigurableActivation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;



public class TActivationModeMessage
		implements I_Message
{
	private E_ActivationMode mode;


	public TActivationModeMessage( )
	{
		// EMPTY
	}


	public TActivationModeMessage( final E_ActivationMode mode )
	{
		this.mode = mode;
	}


	@Override
	public void fromBytes( final ByteBuf buf )
	{
		switch ( buf.readByte( ) ) {
			default:
				// XXX warning
			case 0:
				this.mode = E_ActivationMode.ALWAYS_ACTIVE;
				break;
			case 1:
				this.mode = E_ActivationMode.POWERED;
				break;
			case 2:
				this.mode = E_ActivationMode.UNPOWERED;
				break;
			case 3:
				this.mode = E_ActivationMode.DISABLED;
				break;
		}
	}


	@Override
	public void toBytes( final ByteBuf buf )
	{
		buf.writeByte( this.mode.ordinal( ) );
	}


	@Override
	public void handleOnServer( final EntityPlayerMP player )
	{
		final Container curCont = player.openContainer;
		if ( curCont instanceof I_ConfigurableActivation ) {
			( (I_ConfigurableActivation) curCont ).setActivationMode( this.mode );
		}
	}

}
