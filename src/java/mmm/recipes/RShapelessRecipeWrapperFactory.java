package mmm.recipes;


import java.util.Arrays;
import java.util.List;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_CraftingRecipeWrapperFactory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;



public class RShapelessRecipeWrapperFactory
		implements I_CraftingRecipeWrapperFactory
{

	@Override
	public Class< ShapelessRecipes > getRecipeClass( )
	{
		return ShapelessRecipes.class;
	}


	@Override
	public List< I_CraftingRecipeWrapper > createWrappers( final IRecipe recipe )
	{
		return Arrays.asList( new RShapelessRecipeWrapper( (ShapelessRecipes) recipe ) );
	}

}
