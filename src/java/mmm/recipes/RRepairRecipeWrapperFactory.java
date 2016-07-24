package mmm.recipes;


import java.util.ArrayList;
import java.util.List;

import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_CraftingRecipeWrapperFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeRepairItem;



public class RRepairRecipeWrapperFactory
		implements I_CraftingRecipeWrapperFactory
{

	@Override
	public Class< ? extends IRecipe > getRecipeClass( )
	{
		return RecipeRepairItem.class;
	}


	@Override
	public List< I_CraftingRecipeWrapper > createWrappers( final IRecipe recipe )
	{
		final ArrayList< I_CraftingRecipeWrapper > repairWrappers = new ArrayList<>( );
		for ( final Item item : Item.REGISTRY ) {
			if ( item.isItemTool( new ItemStack( item ) ) && item.isRepairable( ) ) {
				repairWrappers.add( new RRepairRecipeWrapper( recipe , item ) );
			}
		}
		return repairWrappers;
	}

}
