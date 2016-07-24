package mmm.recipes;


import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class RExtraRecipes
		implements I_RecipeRegistrar
{
	// I don't need a pretext to go adventuring, and neither does my kid (actually, it's more like
	// "nah, not going caving today, too tired... please?"), so fuck you and your "but it encourages
	// adventuring!" BS.
	//
	// ...
	//
	// Sorry, this just annoys me.

	@Override
	public void registerRecipes( )
	{
		// "I can make pistons but not saddles because fuck you"
		// ... Well, no. Fuck *you*.
		GameRegistry.addShapedRecipe( new ItemStack( Items.SADDLE ) , //
				"LLL" , //
				"SLS" , //
				"ILI" , //
				'L' , new ItemStack( Items.LEATHER ) , //
				'S' , new ItemStack( Items.STRING ) , //
				'I' , new ItemStack( Items.IRON_INGOT ) );

		// "Oh God, name tags are so hard to make!!!111one one one"
		GameRegistry.addShapelessRecipe( new ItemStack( Items.NAME_TAG ) , //
				Items.PAPER , Items.PAPER , Items.PAPER , Items.STRING , Items.FEATHER ,
				new ItemStack( Items.DYE , 1 , EnumDyeColor.BLACK.getDyeDamage( ) ) );

		// "Holse amor? IMPOSSIBRU!"
		GameRegistry.addShapedRecipe( new ItemStack( Items.IRON_HORSE_ARMOR ) , //
				"LBL" , "LBL" , "LBL" , //
				'L' , Items.LEATHER , //
				'B' , Blocks.IRON_BLOCK );
		GameRegistry.addShapedRecipe( new ItemStack( Items.GOLDEN_HORSE_ARMOR ) , //
				"LBL" , "LBL" , "LBL" , //
				'L' , Items.LEATHER , //
				'B' , Blocks.GOLD_BLOCK );
		GameRegistry.addShapedRecipe( new ItemStack( Items.DIAMOND_HORSE_ARMOR ) , //
				"LBL" , "LBL" , "LBL" , //
				'L' , Items.LEATHER , //
				'B' , Blocks.DIAMOND_BLOCK );

		// Low gain gravel -> flint recipe, because doing it manually is just so bloody annoying.
		GameRegistry.addShapelessRecipe( new ItemStack( Items.FLINT ) , //
				Blocks.GRAVEL , Blocks.GRAVEL , Blocks.GRAVEL , //
				Blocks.GRAVEL , Blocks.GRAVEL , Blocks.GRAVEL , //
				Blocks.GRAVEL , Blocks.GRAVEL , Blocks.GRAVEL );

		// Spinning cobwebs into strings, because my enchanted diamond sword's durability hates you.
		GameRegistry.addShapelessRecipe( new ItemStack( Items.STRING ) , Blocks.WEB );
	}

}
