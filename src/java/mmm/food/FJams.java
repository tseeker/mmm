package mmm.food;


import mmm.MmmFood;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;



public class FJams
{

	public final ItemFood APPLE;


	public FJams( )
	{
		this.APPLE = FJams.makeJam( "apple" , Items.APPLE );
	}


	public static FFoodInContainer makeJam( final String name , final Item fruit )
	{
		return new FFoodInContainer( "jam" , name , 5 , 0.9f , MmmFood.INGREDIENT.GLASS_JAR , 16 , //
				fruit , Items.SUGAR , Items.SUGAR );
	}

}
