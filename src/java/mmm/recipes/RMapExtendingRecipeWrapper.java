package mmm.recipes;


import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.core.api.recipes.I_RecipeRequirements;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.oredict.OreDictionary;



public class RMapExtendingRecipeWrapper
		implements I_CraftingRecipeWrapper
{
	private final RRequirements requirements = new RRequirements( 2 ) {

		@Override
		public boolean checkItemStack( final int pos , final ItemStack stack , final World world )
		{
			if ( !super.checkItemStack( pos , stack , world ) ) {
				return false;
			}

			if ( stack.getItem( ) == Items.FILLED_MAP ) {
				final MapData mapdata = Items.FILLED_MAP.getMapData( stack , world );
				return mapdata == null ? false : mapdata.scale < 4;
			}
			return true;
		}

	};


	public RMapExtendingRecipeWrapper( )
	{
		this.requirements.put( 0 , new ItemStack( Items.FILLED_MAP , 1 , OreDictionary.WILDCARD_VALUE ) , 1 );
		this.requirements.put( 1 , new ItemStack( Items.PAPER ) , 8 );
	}


	@Override
	public String getIdentifier( )
	{
		return "MAP_EXTENDING";
	}


	@Override
	public String getName( )
	{
		return "gui.mmm.recipes.map_extending";
	}


	@Override
	public ItemStack getOutput( )
	{
		return new ItemStack( Items.FILLED_MAP , 1 , 0 );
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

		final ItemStack stack = input.getStackInSlot( filledMapSlot ).copy( );
		stack.stackSize = 1;
		if ( stack.getTagCompound( ) == null ) {
			stack.setTagCompound( new NBTTagCompound( ) );
		}
		stack.getTagCompound( ).setInteger( "map_scale_direction" , 1 );
		return stack;
	}


	@Override
	public boolean canShiftClick( )
	{
		return false;
	}


	@Override
	public void addInputsToDisplay( final IInventory displayInventory )
	{
		for ( int i = 0 ; i < 9 ; i++ ) {
			displayInventory.setInventorySlotContents( i , i == 4
					? new ItemStack( Items.FILLED_MAP , 1 , 0 ) //
					: new ItemStack( Items.PAPER ) );
		}
	}


	@Override
	public I_RecipeRequirements getRequirements( )
	{
		return this.requirements;
	}

}
