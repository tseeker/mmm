package mmm.tech.base.alloy_furnace;


import io.netty.buffer.ByteBuf;
import mmm.core.api.I_Message;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;



public class TBAFMessage
		implements I_Message
{
	private static final int MT_RECIPE = 0;
	private static final int MT_SET_RECIPE = 1;
	private static final int MT_SET_FLAGS = 2;

	private int type;
	private ResourceLocation recipe;
	private int flags;


	public TBAFMessage( )
	{
		// EMPTY
	}


	public TBAFMessage( final ResourceLocation selected , final boolean confirm )
	{
		this.type = confirm ? TBAFMessage.MT_SET_RECIPE : TBAFMessage.MT_RECIPE;
		this.recipe = selected;
	}


	public TBAFMessage( final int flags )
	{
		this.type = TBAFMessage.MT_SET_FLAGS;
		this.flags = flags;
	}


	@Override
	public void fromBytes( final ByteBuf buf )
	{
		this.type = buf.readByte( );
		switch ( this.type ) {

			case MT_RECIPE:
			case MT_SET_RECIPE:
				if ( buf.readBoolean( ) ) {
					this.recipe = new ResourceLocation( ByteBufUtils.readUTF8String( buf ) ,
							ByteBufUtils.readUTF8String( buf ) );
				} else {
					this.recipe = null;
				}
				break;

			case MT_SET_FLAGS:
				this.flags = buf.readShort( );
				break;

		}
	}


	@Override
	public void toBytes( final ByteBuf buf )
	{
		buf.writeByte( this.type );
		switch ( this.type ) {

			case MT_RECIPE:
			case MT_SET_RECIPE:
				buf.writeBoolean( this.recipe != null );
				if ( this.recipe != null ) {
					ByteBufUtils.writeUTF8String( buf , this.recipe.getResourceDomain( ) );
					ByteBufUtils.writeUTF8String( buf , this.recipe.getResourcePath( ) );
				}
				break;

			case MT_SET_FLAGS:
				buf.writeShort( this.flags );
				break;

		}
	}


	@Override
	public void handleOnServer( final EntityPlayerMP player )
	{
		final Container curCont = player.openContainer;
		if ( ! ( curCont instanceof TBAFContainer ) ) {
			return;
		}

		final TBAFContainer container = (TBAFContainer) curCont;

		switch ( this.type ) {

			case MT_RECIPE:
			case MT_SET_RECIPE:
				container.setCurrentRecipe( this.recipe , this.type == TBAFMessage.MT_SET_RECIPE );
				break;

			case MT_SET_FLAGS:
				container.setFlags( this.flags );
				break;

		}
	}

}
