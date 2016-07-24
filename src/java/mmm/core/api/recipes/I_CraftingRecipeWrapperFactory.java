package mmm.core.api.recipes;


import java.util.List;

import net.minecraft.item.crafting.IRecipe;



public interface I_CraftingRecipeWrapperFactory
{
	public Class< ? extends IRecipe > getRecipeClass( );


	public List< I_CraftingRecipeWrapper > createWrappers( IRecipe recipe );

}
