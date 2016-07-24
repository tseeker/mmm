package mmm.core;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import mmm.Mmm;
import mmm.core.api.I_FloraRegistrar;
import mmm.core.api.I_OreGenerationRegistrar;
import mmm.core.api.I_RecipeRegistrar;
import mmm.core.api.I_RequiresClientInit;
import mmm.core.api.I_RequiresClientPreInit;
import mmm.core.api.blocks.I_ColoredBlock;
import mmm.core.api.blocks.I_StateMapperProvider;
import mmm.core.api.blocks.I_TintedBlock;
import mmm.core.api.items.I_ItemModelProvider;
import mmm.core.api.items.I_ItemModelProviderBasic;
import mmm.core.api.items.I_ItemWithVariants;
import mmm.core.api.items.I_TintedItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;



public class CRegistry
{

	private static class FuelHandler
			implements IFuelHandler
	{

		@Override
		public int getBurnTime( final ItemStack fuel )
		{
			final Item item = fuel.getItem( );
			final Object fuelInfo = CRegistry.FUELS.get( item );
			if ( fuelInfo != null ) {
				if ( item.getHasSubtypes( ) ) {
					return ( (int[]) fuelInfo )[ fuel.getItemDamage( ) ];
				}
				return ( (Integer) fuelInfo ).intValue( );
			}
			return 0;
		}

	}

	private static final HashSet< I_RecipeRegistrar > RECIPE_REGISTRARS = new HashSet<>( );
	private static final HashSet< I_OreGenerationRegistrar > ORE_GEN_REGISTRARS = new HashSet<>( );
	private static final HashSet< I_FloraRegistrar > FLORA_REGISTRARS = new HashSet<>( );

	private static final HashSet< Item > ITEMS = new HashSet< Item >( );
	private static final HashSet< Block > BLOCKS = new HashSet< Block >( );

	private static final ArrayList< Block > BLOCKS_CLIENT_PREINIT = new ArrayList<>( );
	private static final ArrayList< Block > BLOCKS_CLIENT_INIT = new ArrayList<>( );
	private static final ArrayList< Item > ITEMS_CLIENT_INIT = new ArrayList<>( );

	private static final HashMap< Item , Object > FUELS = new HashMap<>( );
	private static final FuelHandler FUEL_HANDLER = new FuelHandler( );

	static {
		GameRegistry.registerFuelHandler( CRegistry.FUEL_HANDLER );
	}


	public static void setIdentifiers( final IForgeRegistryEntry< ? > thing , final String... strings )
	{
		final int nStrings = strings.length;
		if ( nStrings == 0 ) {
			throw new IllegalArgumentException( "no identifier specified" );
		}

		final StringBuilder sb = new StringBuilder( );
		for ( int i = 0 ; i < nStrings ; i++ ) {
			if ( i > 0 ) {
				sb.append( '/' );
			}
			sb.append( strings[ i ] );
		}
		thing.setRegistryName( new ResourceLocation( Mmm.ID , sb.toString( ) ) );

		if ( thing instanceof Block || thing instanceof Item ) {
			sb.setLength( 0 );
			sb.append( Mmm.ID );
			for ( int i = 0 ; i < nStrings ; i++ ) {
				sb.append( '.' ).append( strings[ i ] );
			}
			if ( thing instanceof Block ) {
				( (Block) thing ).setUnlocalizedName( sb.toString( ) );
			} else {
				( (Item) thing ).setUnlocalizedName( sb.toString( ) );
			}
		}
	}


	public static void addItem( final Item item )
	{
		if ( CRegistry.ITEMS.add( item ) ) {
			GameRegistry.register( item );
			CRegistry.addRegistrar( item );
			if ( item instanceof I_RequiresClientInit ) {
				CRegistry.ITEMS_CLIENT_INIT.add( item );
			}
		}
	}


	public static Item addBlock( final Block block )
	{
		Item item;
		if ( block instanceof I_ColoredBlock ) {
			item = new ItemCloth( block );
		} else {
			item = new ItemBlock( block );
		}
		item.setRegistryName( block.getRegistryName( ) );
		return CRegistry.addBlock( block , item );
	}


	public static Item addBlock( final Block block , final Item blockItem )
	{
		if ( CRegistry.BLOCKS.add( block ) ) {
			GameRegistry.register( block );
			CRegistry.addRegistrar( block );
			if ( block instanceof I_RequiresClientPreInit ) {
				CRegistry.BLOCKS_CLIENT_PREINIT.add( block );
			}
			if ( block instanceof I_RequiresClientInit ) {
				CRegistry.BLOCKS_CLIENT_INIT.add( block );
			}

			if ( blockItem != null ) {
				CRegistry.addItem( blockItem );
			}
		}
		return blockItem;
	}


	public static void setFuel( final Item item , final int value )
	{
		Object obj;
		if ( item.getHasSubtypes( ) ) {
			final int values[] = new int[ 16 ];
			Arrays.fill( values , value );
			obj = values;
		} else {
			obj = Integer.valueOf( value );
		}
		CRegistry.FUELS.put( item , obj );
	}


	public static void setFuel( final Item item , final int meta , final int value )
	{
		if ( !item.getHasSubtypes( ) ) {
			throw new IllegalArgumentException( "item " + item + " has no subtypes" );
		}

		Object obj = CRegistry.FUELS.get( item );
		if ( obj == null ) {
			obj = new int[ 16 ];
			CRegistry.FUELS.put( item , obj );
		}

		( (int[]) obj )[ meta ] = value;
	}


	public static void addRegistrar( final Object object )
	{
		CRegistry.addRecipeRegistrar( object );
		CRegistry.addOreGenerationRegistrar( object );
		CRegistry.addFloraRegistrar( object );
	}


	public static void addRecipeRegistrar( final Object object )
	{
		if ( object instanceof I_RecipeRegistrar ) {
			CRegistry.RECIPE_REGISTRARS.add( (I_RecipeRegistrar) object );
		}
	}


	public static void addOreGenerationRegistrar( final Object object )
	{
		if ( object instanceof I_OreGenerationRegistrar ) {
			CRegistry.ORE_GEN_REGISTRARS.add( (I_OreGenerationRegistrar) object );
		}
	}


	public static void addFloraRegistrar( final Object object )
	{
		if ( object instanceof I_FloraRegistrar ) {
			CRegistry.FLORA_REGISTRARS.add( (I_FloraRegistrar) object );
		}
	}


	public static Collection< I_OreGenerationRegistrar > getOreGenerationRegistrars( )
	{
		return Collections.unmodifiableCollection( CRegistry.ORE_GEN_REGISTRARS );
	}


	public static Collection< I_FloraRegistrar > getFloraRegistrars( )
	{
		return Collections.unmodifiableCollection( CRegistry.FLORA_REGISTRARS );
	}


	public static void registerRecipes( )
	{
		for ( final I_RecipeRegistrar registrar : CRegistry.RECIPE_REGISTRARS ) {
			registrar.registerRecipes( );
		}
	}


	@SideOnly( Side.CLIENT )
	public static void preInitClient( )
	{
		for ( final Item item : CRegistry.ITEMS ) {
			// Automatic model location unless there's a provider
			if ( ! ( item instanceof I_ItemModelProvider ) ) {
				final ModelResourceLocation location = new ModelResourceLocation( item.getRegistryName( ) ,
						"inventory" );
				ModelLoader.setCustomModelResourceLocation( item , 0 , location );
			}

			if ( ! ( item instanceof I_RequiresClientPreInit ) ) {
				continue;
			}

			// Item models
			if ( item instanceof I_ItemModelProviderBasic ) {
				ModelLoader.setCustomModelResourceLocation( item , 0 ,
						( (I_ItemModelProviderBasic) item ).getModelResourceLocation( ) );
			} else if ( item instanceof I_ItemWithVariants ) {
				final I_ItemWithVariants iwv = (I_ItemWithVariants) item;
				for ( int i = 0 ; i < iwv.getVariantsCount( ) ; i++ ) {
					ModelLoader.setCustomModelResourceLocation( item , i , iwv.getModelResourceLocation( i ) );
				}
			}
		}

		for ( final Block block : CRegistry.BLOCKS_CLIENT_PREINIT ) {
			if ( block instanceof I_StateMapperProvider ) {
				ModelLoader.setCustomStateMapper( block , ( (I_StateMapperProvider) block ).getStateMapper( ) );
			}
		}
	}


	@SideOnly( Side.CLIENT )
	public static void initClient( )
	{
		final BlockColors blockColors = Minecraft.getMinecraft( ).getBlockColors( );
		final ItemColors itemColors = Minecraft.getMinecraft( ).getItemColors( );

		for ( final Block block : CRegistry.BLOCKS_CLIENT_INIT ) {
			// Register tinted blocks
			if ( block instanceof I_TintedBlock ) {
				blockColors.registerBlockColorHandler( ( (I_TintedBlock) block ).getBlockTint( ) , block );
			}

			// Register tinted block items
			if ( block instanceof I_TintedItem ) {
				itemColors.registerItemColorHandler( ( (I_TintedItem) block ).getItemTint( ) , block );
			}
		}

		for ( final Item item : CRegistry.ITEMS_CLIENT_INIT ) {
			// Register tinted items
			if ( item instanceof I_TintedItem ) {
				itemColors.registerItemColorHandler( ( (I_TintedItem) item ).getItemTint( ) , item );
			}
		}
	}
}
