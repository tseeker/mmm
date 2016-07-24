package mmm.recipes;


import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_RecipeRequirements;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;



public class RMapCloningRecipeWrapper
		implements I_CraftingRecipeWrapper
{

	private final I_RecipeRequirements requirements;


	public RMapCloningRecipeWrapper( )
	{
		final RRequirements reqs = new RRequirements( 2 );
		reqs.put( 0 , new ItemStack( Items.FILLED_MAP , 1 , OreDictionary.WILDCARD_VALUE ) , 1 );
		reqs.put( 1 , new ItemStack( Items.MAP ) , 1 );
		this.requirements = reqs;
	}


	@Override
	public String getIdentifier( )
	{
		return "MAP_CLONING";
	}


	@Override
	public String getName( )
	{
		return "gui.mmm.recipes.map_cloning";
	}


	@Override
	public ItemStack getOutput( )
	{
		return new ItemStack( Items.FILLED_MAP , 2 , 0 );
	}


	@Override
	public ItemStack getActualOutput( final IInventory input )
	{
		int filledMapSlot = -1;
		for ( int i = 0 ; i < input.getSizeInventory( ) && filledMapSlot == -1 ; i++ ) {
			final ItemStack stack = input.getStackInSlot( i );
			if ( stack != null && stack.getItem( ) == Items.FILLED_MAP ) {
				filledMapSlot = i;
			}
		}
		return new ItemStack( Items.FILLED_MAP , 2 , input.getStackInSlot( filledMapSlot ).getMetadata( ) );
	}


	@Override
	public void addInputsToDisplay( final IInventory displayInventory )
	{
		displayInventory.setInventorySlotContents( 0 , new ItemStack( Items.FILLED_MAP , 1 , 0 ) );
		displayInventory.setInventorySlotContents( 1 , new ItemStack( Items.MAP ) );
	}


	@Override
	public I_RecipeRequirements getRequirements( )
	{
		return this.requirements;
	}

}
