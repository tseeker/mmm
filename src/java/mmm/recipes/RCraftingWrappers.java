package mmm.recipes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_CraftingRecipeWrapperFactory;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;



public class RCraftingWrappers
{
	private static final HashMap< Class< ? > , I_CraftingRecipeWrapperFactory > FACTORIES = new HashMap<>( );
	public static final ArrayList< I_CraftingRecipeWrapper > RECIPES = new ArrayList<>( );
	public static final HashMap< String , I_CraftingRecipeWrapper > IDENTIFIERS = new HashMap<>( );


	public static void register( final I_CraftingRecipeWrapperFactory factory )
	{
		final Class< ? extends IRecipe > rc = factory.getRecipeClass( );
		if ( RCraftingWrappers.FACTORIES.containsKey( rc ) ) {
			throw new IllegalArgumentException( "duplicate recipe wrapper factory for type " + rc );
		}
		RCraftingWrappers.FACTORIES.put( rc , factory );
	}


	public static void wrapRecipes( )
	{
		if ( !RCraftingWrappers.RECIPES.isEmpty( ) ) {
			throw new IllegalStateException( "already initialized" );
		}

		for ( final IRecipe recipe : CraftingManager.getInstance( ).getRecipeList( ) ) {
			final I_CraftingRecipeWrapperFactory factory = RCraftingWrappers.FACTORIES.get( recipe.getClass( ) );
			if ( factory == null ) {
				System.err.println( "unsupported recipe class " + recipe.getClass( ) );
				continue;
			}
			final List< I_CraftingRecipeWrapper > wrappers = factory.createWrappers( recipe );
			for ( final I_CraftingRecipeWrapper wrapper : wrappers ) {
				final String identifier = wrapper.getIdentifier( );
				if ( RCraftingWrappers.IDENTIFIERS.containsKey( identifier ) ) {
					throw new IllegalStateException( "duplicate wrapper ID " + identifier );
				}
				RCraftingWrappers.IDENTIFIERS.put( identifier , wrapper );
			}
			RCraftingWrappers.RECIPES.addAll( wrappers );
		}

		System.err.println( "generated " + RCraftingWrappers.RECIPES.size( ) + " recipe wrappers" );
	}


	public static void preInit( )
	{
		RCraftingWrappers.register( new RShapelessRecipeWrapperFactory( ) );
		RCraftingWrappers.register( new RShapelessOreRecipeWrapperFactory( ) );
		RCraftingWrappers.register( new RShapedRecipeWrapperFactory( ) );
		RCraftingWrappers.register( new RShapedOreRecipeWrapperFactory( ) );
		RCraftingWrappers.register( new RMapCloningRecipeWrapperFactory( ) );
		RCraftingWrappers.register( new RMapExtendingRecipeWrapperFactory( ) );
		RCraftingWrappers.register( new RRepairRecipeWrapperFactory( ) );
	}
}
