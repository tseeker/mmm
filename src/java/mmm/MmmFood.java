package mmm;


import mmm.food.FIngredients;
import mmm.food.FJams;
import mmm.food.FMeals;
import mmm.food.FMilks;
import mmm.food.FPies;
import mmm.food.FSoups;



public class MmmFood
{
	public static final FMilks MILK;
	public static final FIngredients INGREDIENT;
	public static final FMeals MEAL;
	public static final FPies PIE;
	public static final FSoups SOUP;
	public static final FJams JAM;

	static {
		MILK = new FMilks( );
		INGREDIENT = new FIngredients( );
		MEAL = new FMeals( );
		PIE = new FPies( );
		SOUP = new FSoups( );
		JAM = new FJams( );
	}


	public static void preInit( )
	{
		// EMPTY
	}

}
