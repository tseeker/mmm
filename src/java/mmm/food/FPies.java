package mmm.food;


import mmm.MmmFood;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class FPies
		implements I_RecipeRegistrar
{
	public final ItemFood APPLE;


	public FPies( )
	{
		this.APPLE = FHelpers.makeBasicMeal( 8 , 0.5f , false , "pie" , "apple" );
	}


	@Override
	public void registerRecipes( )
	{
		CRegistry.addRegistrar( this );

		GameRegistry.addShapelessRecipe( new ItemStack( this.APPLE ) , //
				MmmFood.INGREDIENT.DOUGH , Items.SUGAR , Items.APPLE );
	}
}
