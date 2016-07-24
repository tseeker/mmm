package mmm.utils.gui;


import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@SideOnly( Side.CLIENT )
public abstract class A_UGContainerScreen< CTYPE extends UGContainer >
		extends GuiContainer
{
	public final CTYPE container;


	public A_UGContainerScreen( final CTYPE inventorySlotsIn )
	{
		super( inventorySlotsIn );
		this.container = inventorySlotsIn;
	}


	public int getLeft( )
	{
		return this.guiLeft;
	}


	public int getTop( )
	{
		return this.guiTop;
	}


	public int getXSize( )
	{
		return this.xSize;
	}


	public int getYSize( )
	{
		return this.ySize;
	}


	public FontRenderer getFontRenderer( )
	{
		return this.fontRendererObj;
	}

}
