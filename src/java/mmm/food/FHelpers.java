package mmm.food;


import mmm.core.CRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class FHelpers
{

	public static ItemFood makeBasicMeal( final int amount , final float saturation , final boolean wolfFood ,
			final String... name )
	{
		final ItemFood item = new ItemFood( amount , saturation , wolfFood );
		item.setCreativeTab( CreativeTabs.FOOD );

		final String[] fullName = new String[ name.length + 1 ];
		fullName[ 0 ] = "food";
		System.arraycopy( name , 0 , fullName , 1 , name.length );
		CRegistry.setIdentifiers( item , fullName );

		CRegistry.addItem( item );
		return item;
	}


	public static Item makeIngredient( final String name )
	{
		final Item item = new Item( );
		item.setCreativeTab( CreativeTabs.FOOD );
		CRegistry.setIdentifiers( item , "food" , "ingredient" , name );
		CRegistry.addItem( item );
		return item;
	}


	public static void addCooking( final Item in , final Item out )
	{
		FHelpers.addCooking( in , out , 1 );
	}


	public static void addCooking( final Item in , final Item out , final int amount )
	{
		GameRegistry.addSmelting( in , new ItemStack( out , amount ) , 0.1f );
	}


	public static void addTransform( final Item in , final Item out )
	{
		FHelpers.addTransform( in , out , 1 );
	}


	public static void addTransform( final Item in , final Item out , final int amount )
	{
		GameRegistry.addShapelessRecipe( new ItemStack( out , amount ) , in );
	}
}
