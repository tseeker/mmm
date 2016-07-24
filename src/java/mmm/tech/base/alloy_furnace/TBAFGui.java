package mmm.tech.base.alloy_furnace;


import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import mmm.Mmm;
import mmm.core.CNetwork;
import mmm.materials.MAlloyRecipe;
import mmm.utils.gui.A_UGTab;
import mmm.utils.gui.A_UGTabbedContainerScreen;
import mmm.utils.gui.UGArrowButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



@SideOnly( Side.CLIENT )
public class TBAFGui
		extends A_UGTabbedContainerScreen< TBAFContainer >
{
	private static final ResourceLocation TEXTURES[] = {
			new ResourceLocation( Mmm.ID , "textures/gui/alloy_furnace_1.png" ) ,
			new ResourceLocation( Mmm.ID , "textures/gui/alloy_furnace_2.png" ) ,
			new ResourceLocation( Mmm.ID , "textures/gui/alloy_furnace_3.png" ) ,
	};

	@SideOnly( Side.CLIENT )
	private static class MainTab
			extends A_UGTab
	{

		private MainTab( )
		{
			super( );
			this.setBackground( TBAFGui.TEXTURES[ 0 ] ) //
					.setHeight( 194 ) //
					.setIconPosition( 191 , 0 )//
					.setSlotGroups( 0 , 1 ) //
					.setTooltip( "container.mmm.alloy_furnace.contents" );
		}


		@Override
		public void drawBackground( final float partialTicks , final int mouseX , final int mouseY )
		{
			super.drawBackground( partialTicks , mouseX , mouseY );

			final int x = ( this.parent.width - this.parent.getXSize( ) ) / 2;
			final int y = ( this.parent.height - this.height ) / 2;

			final TBAFContainer container = (TBAFContainer) this.parent.container;
			final TileEntity atPos = container.world.getTileEntity( container.position );
			if ( atPos instanceof TBAFTileEntity ) {
				final TBAFTileEntity te = (TBAFTileEntity) atPos;
				// Burn
				if ( te.isBurning( ) ) {
					final int burn = te.getBurnProgress( 13 );
					this.drawTexturedModalRect( x + 89 , y + 38 + 13 - burn , 176 , 28 - burn , 14 , burn + 1 );
				}

				// Alloying progress
				if ( te.isAlloying( ) ) {
					final int alloy = te.getAlloyingProgress( 47 );
					this.drawTexturedModalRect( x + 73 , y + 17 , 176 , 29 , alloy + 1 , 16 );
				}
			}
		}

	}

	@SideOnly( Side.CLIENT )
	private static class RecipeTab
			extends A_UGTab
	{

		private UGArrowButton bPrevious;
		private UGArrowButton bNext;
		private GuiButton bConfirm;
		private GuiTextField tfSearch;


		private RecipeTab( )
		{
			super( );
			this.setBackground( TBAFGui.TEXTURES[ 1 ] ) //
					.setHeight( 194 ) //
					.setIconPosition( 207 , 0 )//
					.setSlotGroups( 0 , 2 ) //
					.setTooltip( "container.mmm.alloy_furnace.recipe" );
		}


		@Override
		public void initGui( final int x , final int y )
		{
			super.initGui( x , y );

			this.bPrevious = new UGArrowButton( 1 , 8 + x , 47 + y , false );
			this.bNext = new UGArrowButton( 2 , 156 + x , 47 + y , true );
			this.bConfirm = new GuiButton( 3 , 24 + x , 84 + y , 128 , 20 ,
					I18n.format( "container.mmm.alloy_furnace.select" ) );

			this.tfSearch = new GuiTextField( 4 , this.parent.getFontRenderer( ) , 24 + x , 9 + y , 128 , 20 );
			this.tfSearch.setTextColor( 0xffffff );
			this.tfSearch.setDisabledTextColour( 0x7f7f7f );
			this.tfSearch.setEnableBackgroundDrawing( true );
			this.tfSearch.setMaxStringLength( 30 );
			this.tfSearch.setVisible( this.isSelected( ) );

			this.buttons.add( this.bNext );
			this.buttons.add( this.bPrevious );
			this.buttons.add( this.bConfirm );

			this.enableButtons( );
		}


		@Override
		public void drawBackground( final float partialTicks , final int mouseX , final int mouseY )
		{
			super.drawBackground( partialTicks , mouseX , mouseY );
			GlStateManager.disableBlend( );
			this.tfSearch.drawTextBox( );
		}


		@Override
		public void onSelected( )
		{
			super.onSelected( );
			this.tfSearch.setVisible( true );
			this.enableButtons( );
		}


		@Override
		public void onDeselected( )
		{
			super.onDeselected( );
			this.tfSearch.setVisible( false );
		}


		@Override
		public void onMouseClicked( final int mouseX , final int mouseY , final int mouseButton )
		{
			this.tfSearch.mouseClicked( mouseX , mouseY , mouseButton );
		}


		@Override
		public boolean onKeyTyped( final char typedChar , final int keyCode )
		{
			if ( this.tfSearch.isFocused( ) && keyCode == Keyboard.KEY_ESCAPE ) {
				this.tfSearch.setFocused( false );
				return true;

			} else if ( this.tfSearch.textboxKeyTyped( typedChar , keyCode ) ) {
				( (TBAFGui) this.parent ).handleFiltering( this.tfSearch.getText( ) );
				return true;

			} else if ( typedChar == '/' ) {
				this.tfSearch.setFocused( true );
				return true;

			} else if ( keyCode == Keyboard.KEY_LEFT && this.bPrevious.enabled ) {
				this.onActionPerformed( this.bPrevious );
				return true;

			} else if ( keyCode == Keyboard.KEY_RIGHT && this.bNext.enabled ) {
				this.onActionPerformed( this.bNext );
				return true;
			}

			return false;
		}


		@Override
		public boolean onActionPerformed( final GuiButton button )
		{
			final TBAFGui gui = (TBAFGui) this.parent;
			if ( button == this.bNext ) {
				gui.setRecipe( gui.currentRecipe + 1 );
			} else if ( button == this.bPrevious ) {
				gui.setRecipe( gui.currentRecipe - 1 );
			} else if ( button == this.bConfirm ) {
				final MAlloyRecipe recipe = gui.recipes.get( gui.currentRecipe );
				CNetwork.sendToServer( new TBAFMessage( recipe.name , true ) );
				gui.container.tileEntity.recipe = recipe;
			} else {
				return false;
			}

			this.enableButtons( );
			return true;
		}


		private void enableButtons( )
		{
			if ( this.bNext == null ) {
				return;
			}

			final TBAFGui gui = (TBAFGui) this.parent;
			if ( gui.recipes.isEmpty( ) ) {
				this.bNext.enabled = this.bPrevious.enabled = this.bConfirm.enabled = false;
			} else {
				this.bNext.enabled = gui.currentRecipe < gui.recipes.size( ) - 1;
				this.bPrevious.enabled = gui.currentRecipe > 0;
				this.bConfirm.enabled = gui.currentRecipe != gui.getRecipeIndex( gui.container.tileEntity.recipe );
			}
		}

	}

	@SideOnly( Side.CLIENT )
	private static class ConfigTab
			extends A_UGTab
	{
		private GuiButton bActivationMode;
		private GuiCheckBox bFlags[];


		private ConfigTab( )
		{
			super( );
			this.setBackground( TBAFGui.TEXTURES[ 2 ] ) //
					.setHeight( 194 ) //
					.setIconPosition( 223 , 0 )//
					.setSlotGroups( ) //
					.setTooltip( "gui.mmm.configure" );
		}


		@Override
		public void initGui( final int x , final int y )
		{
			super.initGui( x , y );

			int id = 100;
			this.bActivationMode = new GuiButton( id++ , x + 10 , y + 10 , this.parent.getXSize( ) - 20 , 20 , "" );
			this.buttons.add( this.bActivationMode );

			this.bFlags = new GuiCheckBox[ 9 ];
			this.bFlags[ 0 ] = this.makeCheckbox( id++ , x , y + 55 , 0 , "hoppers_input_invalid" );
			this.bFlags[ 1 ] = this.makeCheckbox( id++ , x , y + 70 , 0 , "hoppers_output_invalid" );
			this.bFlags[ 2 ] = this.makeCheckbox( id++ , x , y + 85 , 0 , "hoppers_output_fuel" );
			this.bFlags[ 3 ] = this.makeCheckbox( id++ , x , y + 125 , 0 , "cmp_output" );
			this.bFlags[ 4 ] = this.makeCheckbox( id++ , x , y + 125 , 1 , "cmp_fuel" );
			this.bFlags[ 5 ] = this.makeCheckbox( id++ , x , y + 140 , 0 , "cmp_valid_input" );
			this.bFlags[ 6 ] = this.makeCheckbox( id++ , x , y + 140 , 1 , "cmp_invalid_input" );
			this.bFlags[ 7 ] = this.makeCheckbox( id++ , x , y + 155 , 0 , "cmp_alloying" );
			this.bFlags[ 8 ] = this.makeCheckbox( id++ , x , y + 155 , 1 , "cmp_burning" );
		}


		private GuiCheckBox makeCheckbox( final int id , final int x , final int y , final int column ,
				final String text )
		{
			final GuiCheckBox checkBox = new GuiCheckBox( id , x + 10 + column * ( this.parent.getXSize( ) / 2 - 8 ) ,
					y , I18n.format( "container.mmm.alloy_furnace.cfg." + text ) , false );
			checkBox.packedFGColour = 0xffffff;
			this.buttons.add( checkBox );
			return checkBox;
		}


		@Override
		public void drawBackground( final float partialTicks , final int mouseX , final int mouseY )
		{
			this.bActivationMode.displayString = this.getActivationText( );

			final int flags = ( (TBAFContainer) this.parent.container ).getFlags( );
			for ( int i = 0 ; i < this.bFlags.length ; i++ ) {
				this.bFlags[ i ].setIsChecked( ( flags & 1 << i ) != 0 );
			}

			super.drawBackground( partialTicks , mouseX , mouseY );

			final int x = ( this.parent.width - this.parent.getXSize( ) ) / 2;
			final int y = ( this.parent.height - this.parent.getYSize( ) ) / 2;
			this.drawString( this.parent.getFontRenderer( ) , I18n.format( "gui.mmm.configure.hoppers" ) , x + 10 ,
					y + 40 , 0xffffff );
			this.drawString( this.parent.getFontRenderer( ) , I18n.format( "gui.mmm.configure.comparator" ) , x + 10 ,
					y + 110 , 0xffffff );
		}


		@Override
		public boolean onActionPerformed( final GuiButton button )
		{
			final TBAFContainer cont = (TBAFContainer) this.parent.container;
			if ( button == this.bActivationMode ) {
				cont.setActivationMode( cont.getActivationMode( ).next( ) );
				return true;
			}

			if ( button.id > 100 && button.id <= 100 + this.bFlags.length ) {
				final int flags = cont.getFlags( );
				if ( this.bFlags[ button.id - 101 ].isChecked( ) ) {
					cont.setFlags( flags | 1 << button.id - 101 );
				} else {
					cont.setFlags( flags & ~ ( 1 << button.id - 101 ) );
				}
				return true;
			}

			return super.onActionPerformed( button );
		}


		private String getActivationText( )
		{
			return I18n.format( ( (TBAFContainer) this.parent.container ).getActivationMode( ).getDisplayName( ) );
		}

	}

	private ArrayList< MAlloyRecipe > recipes;
	private int currentRecipe = 0;
	private String searchString = "";


	public TBAFGui( final InventoryPlayer inventoryPlayer , final TBAFTileEntity tileEntity )
	{
		super( new TBAFContainer( inventoryPlayer , tileEntity ) , TBAFGui.TEXTURES[ 0 ] , //
				new MainTab( ) , //
				new RecipeTab( ) , //
				new ConfigTab( ) );
		this.xSize = 176;
		this.recipes = MAlloyRecipe.REGISTRY.getRecipes( );
		this.setRecipe( this.getRecipeIndex( tileEntity.recipe ) );
	}


	private void setRecipe( int index )
	{
		if ( index == -1 && !this.recipes.isEmpty( ) ) {
			index = 0;
		}
		this.currentRecipe = index;

		ResourceLocation rName;
		if ( index != -1 ) {
			final MAlloyRecipe recipe = this.recipes.get( index );
			rName = recipe.name;
		} else {
			rName = null;
		}

		this.container.setCurrentRecipe( rName , false );
		CNetwork.sendToServer( new TBAFMessage( rName , false ) );
		( (RecipeTab) this.tabs[ 1 ] ).enableButtons( );
	}


	private int getRecipeIndex( final MAlloyRecipe recipe )
	{
		return this.recipes.indexOf( recipe );
	}


	private void handleFiltering( final String input )
	{
		final String newText = input.trim( ).toLowerCase( );
		if ( this.searchString.equals( newText ) ) {
			return;
		}

		final MAlloyRecipe selected = this.currentRecipe == -1 ? null : this.recipes.get( this.currentRecipe );
		final ArrayList< MAlloyRecipe > fullList = MAlloyRecipe.REGISTRY.getRecipes( );
		this.searchString = newText;
		if ( "".equals( newText ) ) {
			this.recipes = fullList;
		} else {
			if ( this.recipes == fullList ) {
				this.recipes = new ArrayList<>( );
			} else {
				this.recipes.clear( );
			}

			final int nRecipes = fullList.size( );
			for ( int i = 0 ; i < nRecipes ; i++ ) {
				final MAlloyRecipe recipe = fullList.get( i );
				if ( recipe.getLocalizedName( ).toLowerCase( ).contains( newText ) ) {
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

}
