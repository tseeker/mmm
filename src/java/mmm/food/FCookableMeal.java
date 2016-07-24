package mmm.food;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class FCookableMeal
		implements I_RecipeRegistrar
{

	public final ItemFood RAW;
	public final ItemFood COOKED;

	private final int quantity;
	private Object[] recipe;


	public FCookableMeal( final String name , final int amount , final float saturation , float danger ,
			final boolean wolfLikes , final int quantity , final Object... recipe )
	{
		this.RAW = new ItemFood( Math.max( 1 , amount / 3 ) , saturation / 3f , wolfLikes );
		if ( danger > 0 ) {
			Potion effect;
			if ( danger > 1 ) {
				if ( danger > 2 ) {
					effect = MobEffects.POISON;
					danger -= 2;
				} else {
					effect = MobEffects.NAUSEA;
					danger -= 1;
				}
			} else {
				effect = MobEffects.HUNGER;

			}
			this.RAW.setPotionEffect( new PotionEffect( effect , 600 , 0 ) , Math.min( 1f , danger ) );
		}
		CRegistry.setIdentifiers( this.RAW , "food" , "meal" , name , "raw" );
		CRegistry.addItem( this.RAW );

		this.COOKED = new ItemFood( amount , saturation , wolfLikes );
		CRegistry.setIdentifiers( this.COOKED , "food" , "meal" , name , "cooked" );
		CRegistry.addItem( this.COOKED );

		this.quantity = quantity;
		this.recipe = recipe.clone( );
		CRegistry.addRecipeRegistrar( this );
	}


	@Override
	public void registerRecipes( )
	{
		if ( this.quantity > 0 && this.recipe.length > 0 ) {
			if ( this.recipe[ 0 ] instanceof String ) {
				GameRegistry.addShapedRecipe( new ItemStack( this.RAW , this.quantity ) , this.recipe );
			} else {
				GameRegistry.addShapelessRecipe( new ItemStack( this.RAW , this.quantity ) , this.recipe );
			}
			this.recipe = null;
		}
		FHelpers.addCooking( this.RAW , this.COOKED );
	}

}
