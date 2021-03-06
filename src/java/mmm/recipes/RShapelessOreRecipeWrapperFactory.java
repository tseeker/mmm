package mmm.recipes;


import java.util.Arrays;
import java.util.List;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_CraftingRecipeWrapperFactory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;



public class RShapelessOreRecipeWrapperFactory
		implements I_CraftingRecipeWrapperFactory
{

	@Override
	public Class< ? extends IRecipe > getRecipeClass( )
	{
		return ShapelessOreRecipe.class;
	}


	@Override
	public List< I_CraftingRecipeWrapper > createWrappers( final IRecipe recipe )
	{
		return Arrays.asList( new RShapelessOreRecipeWrapper( (ShapelessOreRecipe) recipe ) );
	}

}
