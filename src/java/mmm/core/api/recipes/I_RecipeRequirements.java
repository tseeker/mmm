package mmm.core.api.recipes;


import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;



public interface I_RecipeRequirements
{

	int size( );


	int getQuantity( int pos );


	List< ItemStack > getItemTypes( int pos );


	boolean checkItemStack( int pos , ItemStack stack , World world );


	boolean checkInventory( IInventory inventory , World world );


	boolean checkInventory( IInventory inventory , int amount , World world );


	public int getMaxOutput( IInventory inventory , World world );


	void removeFromInventory( IInventory inventory , int amount , World world );
}