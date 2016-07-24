package mmm.food;


import mmm.MmmMaterials;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class FIngredients
		implements I_RecipeRegistrar
{
	public final Item FLOUR;
	public final Item DOUGH;
	public final Item GLASS_JAR;


	public FIngredients( )
	{
		CRegistry.addRegistrar( this );

		this.FLOUR = FHelpers.makeIngredient( "flour" );
		this.DOUGH = FHelpers.makeIngredient( "dough" );
		this.GLASS_JAR = FHelpers.makeIngredient( "glass_jar" );
	}


	@Override
	public void registerRecipes( )
	{
		FHelpers.addTransform( Items.WHEAT , this.FLOUR );
		FHelpers.addCooking( this.FLOUR , Items.BREAD );
		GameRegistry.addShapedRecipe( new ItemStack( Items.CAKE ) , //
				"MMM" , //
				"SES" , //
				" F " , //
				'M' , Items.MILK_BUCKET , //
				'S' , Items.SUGAR , //
				'E' , Items.EGG , //
				'F' , this.FLOUR );

		GameRegistry.addShapelessRecipe( new ItemStack( this.DOUGH ) , //
				this.FLOUR , Items.WATER_BUCKET , MmmMaterials.ITEM.ROCK_SALT );

		GameRegistry.addShapedRecipe( new ItemStack( this.GLASS_JAR , 16 ) , //
				"G G" , //
				"G G" , //
				"GGG" , //
				'G' , Blocks.GLASS );
		GameRegistry.addShapedRecipe( new ItemStack( this.GLASS_JAR , 16 ) , //
				"G G" , //
				"G G" , //
				"GGG" , //
				'G' , Blocks.STAINED_GLASS );
	}

}
