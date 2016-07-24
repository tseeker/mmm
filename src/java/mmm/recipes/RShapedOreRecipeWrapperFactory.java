package mmm.recipes;


import java.util.Arrays;
import java.util.List;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_CraftingRecipeWrapperFactory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;



public class RShapedOreRecipeWrapperFactory
		implements I_CraftingRecipeWrapperFactory
{

	@Override
	public Class< ? extends IRecipe > getRecipeClass( )
	{
		return ShapedOreRecipe.class;
	}


	@Override
	public List< I_CraftingRecipeWrapper > createWrappers( final IRecipe recipe )
	{
		final ShapedOreRecipe r = (ShapedOreRecipe) recipe;
		return Arrays.asList( new RShapedOreRecipeWrapper( r ) );
	}

}
