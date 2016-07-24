package mmm.food;


import mmm.MmmPlants;
import net.minecraft.init.Items;



public class FSoups
{
	public final FFoodInContainer TOMATO;
	public final FFoodInContainer GLOWING;


	public FSoups( )
	{
		this.TOMATO = new FFoodInContainer( "soup" , "tomato" , 8 , 1.1f , Items.BOWL , 1 , //
				"TTT" , " B " , 'T' , MmmPlants.TOMATO.FRUIT );
		this.GLOWING = new FGlowingSoup( );
	}
}
