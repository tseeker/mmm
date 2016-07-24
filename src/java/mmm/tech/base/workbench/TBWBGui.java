package mmm.tech.base.workbench;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.lwjgl.input.Keyboard;

import mmm.Mmm;
import mmm.core.CNetwork;
import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_RecipeRequirements;
import mmm.recipes.RCraftingWrappers;
import mmm.utils.gui.A_UGContainerScreen;
import mmm.utils.gui.UGArrowButton;
import mmm.utils.gui.UGui;
import mmm.utils.gui.UGui.E_Icon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@SideOnly( Side.CLIENT )
public class TBWBGui
		extends A_UGContainerScreen< TBWBContainer >
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation( Mmm.ID , "textures/gui/workbench.png" );
	private GuiTextField tfSearch;
	private UGArrowButton bPrevious;
	private UGArrowButton bNext;
	private GuiButton bSetDefault;

	private String searchString;
	private List< I_CraftingRecipeWrapper > recipes;
	private int currentRecipe;


	public TBWBGui( final InventoryPlayer inventoryPlayer , final TBWBTileEntity tileEntity )
	{
		super( new TBWBContainer( inventoryPlayer , tileEntity ) );
		this.container.slotGroups.showAll( );
		this.xSize = 216;
		this.ySize = 200;

		this.searchString = "";
		this.recipes = RCraftingWrappers.RECIPES;
		int index = this.recipes.indexOf( this.container.getCurrentRecipe( ) );
		if ( index == -1 && !this.recipes.isEmpty( ) ) {
			index = 0;
		}
		this.setRecipe( index );
	}


	@Override
	public void initGui( )
	{
		super.initGui( );

		final int x = ( this.width - this.xSize ) / 2;
		final int y = ( this.height - this.ySize ) / 2;

		this.tfSearch = new GuiTextField( 0 , this.getFontRenderer( ) , 71 + x , 7 + y , 138 , 20 );
		this.tfSearch.setTextColor( 0xffffff );
		this.tfSearch.setDisabledTextColour( 0x7f7f7f );
		this.tfSearch.setEnableBackgroundDrawing( true );
		this.tfSearch.setMaxStringLength( 30 );
		this.tfSearch.setVisible( true );

		this.bPrevious = new UGArrowButton( 1 , 71 + x , 50 + y , false );
		this.bNext = new UGArrowButton( 2 , 199 + x , 50 + y , true );
		this.bSetDefault = new GuiButton( 3 , 71 + x , 89 + y , 138 , 20 ,
				I18n.format( "container.mmm.workbench.default" ) );

		this.buttonList.add( this.bPrevious );
		this.buttonList.add( this.bNext );
		this.buttonList.add( this.bSetDefault );

		this.enableButtons( );
	}


	@Override
	protected void drawGuiContainerBackgroundLayer( final float partialTicks , final int mouseX , final int mouseY )
	{
		GlStateManager.color( 1f , 1f , 1f , 1f );
		GlStateManager.enableBlend( );
		GlStateManager.disableLighting( );
		this.mc.getTextureManager( ).bindTexture( TBWBGui.BACKGROUND );
		this.drawTexturedModalRect( this.guiLeft , this.guiTop , 0 , 0 , this.xSize , this.ySize );

		boolean canCraft;
		if ( this.currentRecipe == -1 ) {
			canCraft = false;
		} else {
			final TileEntity te = this.container.world.getTileEntity( this.container.position );
			final I_RecipeRequirements requirements = this.recipes.get( this.currentRecipe ).getRequirements( );
			canCraft = te instanceof TBWBTileEntity
					&& requirements.checkInventory( ( (TBWBTileEntity) te ).storage , this.container.world );
		}
		if ( !canCraft ) {
			UGui.drawIcon( this , this.guiLeft + 147 , this.guiTop + 51 , E_Icon.RED_CROSS );
		}

		GlStateManager.disableBlend( );
		this.tfSearch.drawTextBox( );
	}


	@Override
	protected void mouseClicked( final int mouseX , final int mouseY , final int mouseButton )
			throws IOException
	{
		super.mouseClicked( mouseX , mouseY , mouseButton );
		this.tfSearch.mouseClicked( mouseX , mouseY , mouseButton );
	}


	@Override
	protected void keyTyped( final char typedChar , final int keyCode )
			throws IOException
	{
		if ( this.tfSearch.isFocused( ) && keyCode == Keyboard.KEY_ESCAPE ) {
			this.tfSearch.setFocused( false );

		} else if ( this.tfSearch.textboxKeyTyped( typedChar , keyCode ) ) {
			this.handleFiltering( this.tfSearch.getText( ) );

		} else if ( typedChar == '/' ) {
			this.tfSearch.setFocused( true );

		} else if ( keyCode == Keyboard.KEY_LEFT && this.bPrevious.enabled ) {
			this.actionPerformed( this.bPrevious );

		} else if ( keyCode == Keyboard.KEY_RIGHT && this.bNext.enabled ) {
			this.actionPerformed( this.bNext );

		} else {
			super.keyTyped( typedChar , keyCode );
		}
	}


	@Override
	protected void actionPerformed( final GuiButton button )
			throws IOException
	{
		if ( button == this.bNext && this.currentRecipe < this.recipes.size( ) - 1 ) {
			this.setRecipe( this.currentRecipe + 1 );
		} else if ( button == this.bPrevious && this.currentRecipe > 0 ) {
			this.setRecipe( this.currentRecipe - 1 );
		} else if ( button == this.bSetDefault ) {
			final I_CraftingRecipeWrapper wrapper = this.currentRecipe == -1
					? null
					: this.recipes.get( this.currentRecipe );
			this.container.setCurrentRecipe( wrapper , true );
			CNetwork.sendToServer( new TBWBMessage( wrapper == null ? null : wrapper.getIdentifier( ) , true ) );
			this.enableButtons( );
		}
	}


	private void setRecipe( int index )
	{
		if ( index == -1 && !this.recipes.isEmpty( ) ) {
			index = 0;
		}
		this.currentRecipe = index;
		final I_CraftingRecipeWrapper wrapper = index == -1 ? null : this.recipes.get( index );
		this.container.setCurrentRecipe( wrapper , false );
		CNetwork.sendToServer( new TBWBMessage( wrapper == null ? null : wrapper.getIdentifier( ) , false ) );
		this.enableButtons( );
	}


	private void enableButtons( )
	{
		if ( this.bNext == null ) {
			return;
		}

		this.bNext.enabled = !this.recipes.isEmpty( ) && this.currentRecipe < this.recipes.size( ) - 1;
		this.bPrevious.enabled = !this.recipes.isEmpty( ) && this.currentRecipe > 0;
		this.bSetDefault.enabled = false;
		if ( this.currentRecipe == -1 ) {
			return;
		}

		final TileEntity te = this.container.world.getTileEntity( this.container.position );
		if ( te instanceof TBWBTileEntity ) {
			final I_CraftingRecipeWrapper recipe = RCraftingWrappers.IDENTIFIERS
					.get( ( (TBWBTileEntity) te ).getDefaultRecipe( ) );
			this.bSetDefault.enabled = recipe != this.recipes.get( this.currentRecipe );
		}
	}


	private void handleFiltering( final String input )
	{
		final String newText = input.trim( ).toLowerCase( );
		if ( this.searchString.equals( newText ) ) {
			return;
		}

		final I_CraftingRecipeWrapper selected = this.currentRecipe == -1
				? null
				: this.recipes.get( this.currentRecipe );
		final ArrayList< I_CraftingRecipeWrapper > fullList = RCraftingWrappers.RECIPES;
		this.searchString = newText;
		if ( "".equals( newText ) ) {
			this.recipes = fullList;
		} else {
			if ( this.recipes == fullList ) {
				this.recipes = new ArrayList<>( );
			} else {
				this.recipes.clear( );
			}

			Predicate< String > matcher;
			if ( newText.charAt( 0 ) == '=' && newText.length( ) > 1 ) {
				final String searchText = newText.substring( 1 );
				matcher = ( x ) -> x.equals( searchText );
			} else {
				Pattern pattern;
				if ( newText.charAt( 0 ) == '/' && newText.length( ) > 1 ) {
					try {
						pattern = Pattern.compile( newText.substring( 1 ) ,
								Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE );
					} catch ( final PatternSyntaxException e ) {
						pattern = null;
					}
				} else {
					pattern = null;
				}

				if ( pattern != null ) {
					matcher = pattern.asPredicate( );
				} else {
					final String[] parts = newText.split( "\\s+" );
					matcher = ( x ) -> {
						for ( int i = 0 ; i < parts.length ; i++ ) {
							if ( !x.contains( parts[ i ] ) ) {
								return false;
							}
						}
						return true;
					};
				}
			}

			final int nRecipes = fullList.size( );
			for ( int i = 0 ; i < nRecipes ; i++ ) {
				final I_CraftingRecipeWrapper recipe = fullList.get( i );
				if ( matcher.test( I18n.format( recipe.getName( ) ).toLowerCase( ) ) ) {
					this.recipes.add( recipe );
				}
			}
		}

		if ( selected == null && !this.recipes.isEmpty( ) ) {
			this.setRecipe( 0 );
		} else if ( selected != null ) {
			this.setRecipe( this.getRecipeIndex( selected ) );
		} else {
			this.setRecipe( -1 );
		}
	}


	private int getRecipeIndex( final I_CraftingRecipeWrapper selected )
	{
		return this.recipes.indexOf( selected );
	}

}
