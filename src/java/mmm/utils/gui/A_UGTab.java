package mmm.utils.gui;


import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@SideOnly( Side.CLIENT )
public abstract class A_UGTab
		extends Gui
{
	protected A_UGTabbedContainerScreen< ? > parent;
	protected int ordinal;
	protected int[] slotGroups;
	protected ResourceLocation background;
	protected int height;
	protected int iconX;
	protected int iconY;
	protected String tooltip;
	protected List< GuiButton > buttons = Lists.< GuiButton > newArrayList( );
	private boolean selected;


	A_UGTab setParent( final A_UGTabbedContainerScreen< ? > parent , final int ordinal )
	{
		this.parent = parent;
		this.ordinal = ordinal;
		return this;
	}


	public ResourceLocation getBackground( )
	{
		return this.background;
	}


	public A_UGTab setBackground( final ResourceLocation background )
	{
		this.background = background;
		return this;
	}


	public int getHeight( )
	{
		return this.height;
	}


	public A_UGTab setHeight( final int height )
	{
		this.height = height;
		return this;
	}


	public int getIconX( )
	{
		return this.iconX;
	}


	public int getIconY( )
	{
		return this.iconY;
	}


	public A_UGTab setIconPosition( final int x , final int y )
	{
		this.iconX = x;
		this.iconY = y;
		return this;
	}


	public String getTooltip( )
	{
		return this.tooltip;
	}


	public A_UGTab setTooltip( final String tooltip )
	{
		this.tooltip = tooltip;
		return this;
	}


	public A_UGTab setSlotGroups( final int... slotGroups )
	{
		this.slotGroups = slotGroups;
		return this;
	}


	public void initGui( final int x , final int y )
	{
		// EMPTY
	}


	public void drawBackground( final float partialTicks , final int mouseX , final int mouseY )
	{
		GlStateManager.color( 1f , 1f , 1f , 1f );
		GlStateManager.enableBlend( );
		GlStateManager.disableLighting( );
		this.parent.mc.getTextureManager( ).bindTexture( this.background );
		this.drawTexturedModalRect( this.parent.getLeft( ) , this.parent.getTop( ) , 0 , 0 , this.parent.getXSize( ) ,
				this.height );
	}


	public void onSelected( )
	{
		for ( int i = 0 ; i < this.buttons.size( ) ; i++ ) {
			this.buttons.get( i ).visible = true;
		}
		this.selected = true;
	}


	public void onDeselected( )
	{
		for ( int i = 0 ; i < this.buttons.size( ) ; i++ ) {
			this.buttons.get( i ).visible = false;
		}
		this.selected = false;
	}


	public boolean isSelected( )
	{
		return this.selected;
	}


	public void onMouseClicked( final int mouseX , final int mouseY , final int mouseButton )
	{
		// EMPTY
	}


	public boolean onKeyTyped( final char typedChar , final int keyCode )
	{
		return false;
	}


	public boolean onActionPerformed( final GuiButton button )
	{
		return false;
	}
}
