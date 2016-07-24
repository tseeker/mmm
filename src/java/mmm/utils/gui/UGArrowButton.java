package mmm.utils.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@SideOnly( Side.CLIENT )
public class UGArrowButton
		extends GuiButton
{
	public final boolean forward;


	public UGArrowButton( final int buttonID , final int x , final int y , final boolean forward )
	{
		super( buttonID , x , y , UGui.ABT_WIDTH , UGui.ABT_HEIGHT , "" );
		this.forward = forward;
	}


	@Override
	public void drawButton( final Minecraft mc , final int mouseX , final int mouseY )
	{
		if ( !this.visible ) {
			return;
		}
		mc.getTextureManager( ).bindTexture( UGui.GUI_TEXTURE );
		GlStateManager.color( 1f , 1f , 1f , 1f );

		int texX = UGui.ABT_TEXTURE_X;
		if ( !this.enabled ) {
			texX += this.width * 2;
		} else if ( mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
				&& mouseY < this.yPosition + this.height ) {
			texX += this.width;
		}

		int texY = UGui.ABT_TEXTURE_Y;
		if ( !this.forward ) {
			texY += this.height;
		}

		this.drawTexturedModalRect( this.xPosition , this.yPosition , texX , texY , this.width , this.height );
	}

}
