package mmm.food;


import mmm.MmmFood;
import net.minecraft.init.Items;



public class FMeals
{

	public final FSausage SAUSAGE;
	public final FCookableMeal PASTA;


	public FMeals( )
	{
		this.SAUSAGE = new FSausage( );
		this.PASTA = new FCookableMeal( "pasta" , 2 , 0.7f , 0.5f , false , 1 , //
				Items.EGG , MmmFood.INGREDIENT.FLOUR );
	}

}
