package mmm.food;


import mmm.MmmMaterials;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class FSausage
		extends FCookableMeal
{
	public final ItemFood DRIED;


	public FSausage( )
	{
		super( "sausage" , 3 , 0.7f , 1.3f , true , 4 , //
				Items.PORKCHOP , MmmMaterials.ITEM.ROCK_SALT , Items.LEATHER );
		this.DRIED = FHelpers.makeBasicMeal( 4 , 0.9f , true , "meal" , "sausage" , "dried" );
	}


	@Override
	public void registerRecipes( )
	{
		super.registerRecipes( );
		GameRegistry.addShapelessRecipe( new ItemStack( this.DRIED ) , this.RAW , MmmMaterials.ITEM.SALTPETER );
	}

}
