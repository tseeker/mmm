package mmm.recipes;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_CraftingRecipeWrapperFactory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;



public class RShapedRecipeWrapperFactory
		implements I_CraftingRecipeWrapperFactory
{

	@Override
	public Class< ? extends IRecipe > getRecipeClass( )
	{
		return ShapedRecipes.class;
	}


	@Override
	public List< I_CraftingRecipeWrapper > createWrappers( final IRecipe recipe )
	{
		final ShapedRecipes r = (ShapedRecipes) recipe;
		if ( r.recipeHeight > 3 || r.recipeWidth > 3 ) {
			return Collections.emptyList( );
		}
		return Arrays.asList( new RShapedRecipeWrapper( r ) );
	}

}
