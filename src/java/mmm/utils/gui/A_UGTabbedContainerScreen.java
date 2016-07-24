package mmm.utils.gui;


import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@SideOnly( Side.CLIENT )
public abstract class A_UGTabbedContainerScreen< CTYPE extends UGContainer >
		extends A_UGContainerScreen< CTYPE >
{
	protected final ResourceLocation mainTexture;
	protected final A_UGTab[] tabs;
	protected int currentTab;


	public A_UGTabbedContainerScreen( final CTYPE inventorySlotsIn , final ResourceLocation mainTexture ,
			final A_UGTab... tabs )
	{
		super( inventorySlotsIn );

		int height = 0;
		for ( int i = 0 ; i < tabs.length ; i++ ) {
			tabs[ i ].setParent( this , i );
			if ( tabs[ i ].getHeight( ) > height ) {
				height = tabs[ i ].getHeight( );
			}
		}

		this.tabs = tabs;
		this.mainTexture = mainTexture;
		this.ySize = height;
		this.currentTab = -1;
		this.selectTab( 0 );
	}


	@Override
	public void initGui( )
	{
		super.initGui( );

		final int x = ( this.width - this.xSize ) / 2;
		final int y = ( this.height - this.ySize ) / 2;

		for ( int tabIndex = 0 ; tabIndex < this.tabs.length ; tabIndex++ ) {
			final A_UGTab tab = this.tabs[ tabIndex ];
			tab.buttons.clear( );
			tab.initGui( x , y );
			for ( int i = 0 ; i < tab.buttons.size( ) ; i++ ) {
				tab.buttons.get( i ).visible = this.currentTab == tabIndex;
			}
			this.buttonList.addAll( tab.buttons );
		}
	}


	@Override
	public void drawScreen( final int mouseX , final int mouseY , final float partialTicks )
	{
		super.drawScreen( mouseX , mouseY , partialTicks );

		for ( int tabIndex = 0 ; tabIndex < this.tabs.length ; tabIndex++ ) {
			if ( this.isInTab( tabIndex , mouseX , mouseY ) ) {
				final List< String > list = Lists.< String > newArrayList( );
				list.add( TextFormatting.WHITE + I18n.format( this.tabs[ tabIndex ].tooltip ) );
				this.drawHoveringText( list , mouseX , mouseY );
				break;
			}
		}
	}


	@Override
	protected void drawGuiContainerBackgroundLayer( final float partialTicks , final int mouseX , final int mouseY )
	{
		// Inactive tabs
		GlStateManager.color( 1f , 1f , 1f , 1f );
		GlStateManager.enableBlend( );
		GlStateManager.disableLighting( );
		this.mc.getTextureManager( ).bindTexture( UGui.GUI_TEXTURE );
		for ( int tabIndex = 0 ; tabIndex < this.tabs.length ; tabIndex++ ) {
			if ( tabIndex != this.currentTab ) {
				this.drawTabBackground( tabIndex );
			}
		}

		// Tab panel
		this.tabs[ this.currentTab ].drawBackground( partialTicks , mouseX , mouseY );
		GlStateManager.color( 1f , 1f , 1f , 1f );
		GlStateManager.enableBlend( );
		GlStateManager.disableLighting( );

		// Active tab
		this.mc.getTextureManager( ).bindTexture( UGui.GUI_TEXTURE );
		this.drawTabBackground( this.currentTab );

		// Tab icons
		this.mc.getTextureManager( ).bindTexture( this.mainTexture );
		this.zLevel = 100f;
		for ( int tabIndex = 0 ; tabIndex < this.tabs.length ; tabIndex++ ) {
			this.drawTabIcon( tabIndex );
		}
	}


	@Override
	protected void mouseClicked( final int mouseX , final int mouseY , final int mouseButton )
			throws IOException
	{
		super.mouseClicked( mouseX , mouseY , mouseButton );

		for ( int i = 0 ; i < this.tabs.length ; i++ ) {
			if ( this.currentTab != i && this.isInTab( i , mouseX , mouseY ) ) {
				this.selectTab( i );
				return;
			}
		}

		this.tabs[ this.currentTab ].onMouseClicked( mouseX , mouseY , mouseButton );
	}


	@Override
	protected void keyTyped( final char typedChar , final int keyCode )
			throws IOException
	{
		if ( !this.tabs[ this.currentTab ].onKeyTyped( typedChar , keyCode ) ) {
			super.keyTyped( typedChar , keyCode );
		}
	}


	@Override
	protected void actionPerformed( final GuiButton button )
			throws IOException
	{
		if ( !this.tabs[ this.currentTab ].onActionPerformed( button ) ) {
			this.onContainerActionPerformed( button );
		}
	}


	protected void onContainerActionPerformed( final GuiButton button )
	{
		// EMPTY
	}


	protected boolean isInTab( final int tab , final int mouseX , final int mouseY )
	{
		final int tabOffsetX = tab * UGui.TAB_WIDTH;
		final int tabX = this.guiLeft + tabOffsetX + UGui.TAB_BORDER;
		final int tabY = this.guiTop - UGui.TAB_HEIGHT + UGui.TAB_BORDER;
		return mouseX >= tabX && mouseY >= tabY && mouseX <= tabX + UGui.TAB_WIDTH && mouseY <= tabY + UGui.TAB_HEIGHT;
	}


	protected void drawTabBackground( final int tab )
	{
		final boolean selected = this.currentTab == tab;
		final int tabOffsetX = tab * UGui.TAB_WIDTH;
		final int tabX = this.guiLeft + tabOffsetX + UGui.TAB_BORDER;
		final int tabY = this.guiTop - UGui.TAB_HEIGHT + UGui.TAB_BORDER;
		this.drawTexturedModalRect( tabX , tabY , //
				UGui.TAB_TEXTURE_X , UGui.TAB_TEXTURE_Y + ( selected ? UGui.TAB_HEIGHT : 0 ) , //
				UGui.TAB_WIDTH , UGui.TAB_HEIGHT );
	}


	protected void drawTabIcon( final int tab )
	{
		final int tabOffsetX = tab * UGui.TAB_WIDTH;
		final int tabX = this.guiLeft + tabOffsetX + UGui.TAB_BORDER;
		final int tabY = this.guiTop - UGui.TAB_HEIGHT + UGui.TAB_BORDER;
		this.drawTexturedModalRect( tabX + UGui.TAB_ICON_X , tabY + UGui.TAB_ICON_Y , //
				this.tabs[ tab ].getIconX( ) , this.tabs[ tab ].getIconY( ) , //
				UGui.TAB_ICON_WIDTH , UGui.TAB_ICON_HEIGHT );
	}


	protected void selectTab( final int tabIndex )
	{
		if ( this.currentTab >= 0 ) {
			this.hideCurrentTab( );
		}

		this.currentTab = tabIndex;
		final A_UGTab tab = this.tabs[ tabIndex ];
		if ( tab.slotGroups != null ) {
			for ( final int sg : tab.slotGroups ) {
				this.container.slotGroups.showGroup( sg );
			}
		}
		tab.onSelected( );
	}


	private void hideCurrentTab( )
	{
		final A_UGTab tab = this.tabs[ this.currentTab ];
		if ( tab.slotGroups != null ) {
			for ( final int sg : tab.slotGroups ) {
				this.container.slotGroups.hideGroup( sg );
			}
		}
		tab.onDeselected( );
	}
}
