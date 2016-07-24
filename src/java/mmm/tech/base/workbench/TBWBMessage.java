package mmm.tech.base.workbench;


import io.netty.buffer.ByteBuf;
import mmm.core.api.I_Message;
import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.recipes.RCraftingWrappers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.ByteBufUtils;



public class TBWBMessage
		implements I_Message
{
	private String name;
	private boolean setDefault;


	public TBWBMessage( )
	{
		// EMPTY
	}


	public TBWBMessage( final String name , final boolean setDefault )
	{
		this.name = name;
		this.setDefault = setDefault;
	}


	@Override
	public void fromBytes( final ByteBuf buf )
	{
		if ( buf.readBoolean( ) ) {
			this.name = ByteBufUtils.readUTF8String( buf );
		} else {
			this.name = null;
		}
		this.setDefault = buf.readBoolean( );
	}


	@Override
	public void toBytes( final ByteBuf buf )
	{
		buf.writeBoolean( this.name != null );
		if ( this.name != null ) {
			ByteBufUtils.writeUTF8String( buf , this.name );
		}
		buf.writeBoolean( this.setDefault );
	}


	@Override
	public void handleOnServer( final EntityPlayerMP player )
	{
		final Container curCont = player.openContainer;
		if ( ! ( curCont instanceof TBWBContainer ) ) {
			return;
		}
		final TBWBContainer container = (TBWBContainer) curCont;
		final I_CraftingRecipeWrapper wrapper = RCraftingWrappers.IDENTIFIERS.get( this.name );
		container.setCurrentRecipe( wrapper , this.setDefault );
	}

}
