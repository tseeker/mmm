package mmm.utils.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class UGui
{
	public static enum E_Icon {
		RED_CROSS( 26 , 30 , 13 , 13 ),
		//
		;

		public final int x;
		public final int y;
		public final int width;
		public final int height;


		private E_Icon( final int x , final int y , final int w , final int h )
		{
			this.x = x;
			this.y = y;
			this.width = w;
			this.height = h;
		}
	}

	public static final int TAB_TEXTURE_X = 0;
	public static final int TAB_TEXTURE_Y = 0;
	public static final int TAB_WIDTH = 26;
	public static final int TAB_HEIGHT = 26;

	public static final int ABT_TEXTURE_X = 26;
	public static final int ABT_TEXTURE_Y = 0;
	public static final int ABT_WIDTH = 10;
	public static final int ABT_HEIGHT = 15;

	public static final int TAB_BORDER = 4;
	public static final int TAB_ICON_X = 5;
	public static final int TAB_ICON_Y = 5;
	public static final int TAB_ICON_WIDTH = 16;
	public static final int TAB_ICON_HEIGHT = 16;

	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation( "mmm" , "textures/gui/gui-common.png" );


	@SideOnly( Side.CLIENT )
	public static void drawIcon( final Gui gui , final int x , final int y , final E_Icon icon )
	{
		Minecraft.getMinecraft( ).getTextureManager( ).bindTexture( UGui.GUI_TEXTURE );
		gui.drawTexturedModalRect( x , y , icon.x , icon.y , icon.width , icon.height );
	}
}
