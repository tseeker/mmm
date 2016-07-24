package mmm.recipes;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mmm.core.api.recipes.I_RecipeRequirements;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;



class RRequirements
		implements I_RecipeRequirements
{

	private final List< ItemStack >[] itemTypes;
	private final int[] quantities;


	@SuppressWarnings( "unchecked" )
	RRequirements( final int nInputs )
	{
		this.itemTypes = new List[ nInputs ];
		this.quantities = new int[ nInputs ];
	}


	RRequirements( final ArrayList< ItemStack > inputs )
	{
		this( inputs.size( ) );
		for ( int i = 0 ; i < this.quantities.length ; i++ ) {
			final ItemStack input = inputs.get( i );
			this.put( i , input , input.stackSize );
		}
	}


	void put( final int pos , final ItemStack stack , final int quantity )
	{
		this.itemTypes[ pos ] = Arrays.asList( stack );
		this.quantities[ pos ] = quantity;
	}


	void put( final int pos , final List< ItemStack > stacks , final int quantity )
	{
		assert stacks != null;
		this.itemTypes[ pos ] = stacks;
		this.quantities[ pos ] = quantity;
	}


	@Override
	public int size( )
	{
		return this.quantities.length;
	}


	@Override
	public int getQuantity( final int pos )
	{
		return this.quantities[ pos ];
	}


	@Override
	public List< ItemStack > getItemTypes( final int pos )
	{
		return this.itemTypes[ pos ];
	}


	@Override
	public boolean checkItemStack( final int pos , final ItemStack stack , final World world )
	{
		if ( stack != null ) {
			for ( final ItemStack target : this.getItemTypes( pos ) ) {
				if ( OreDictionary.itemMatches( target , stack , false ) ) {
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public boolean checkInventory( final IInventory inventory , final World world )
	{
		return this.checkInventory( inventory , 1 , world );
	}


	@Override
	public boolean checkInventory( final IInventory inventory , final int amount , final World world )
	{
		final int nInvItems = inventory.getSizeInventory( );
		for ( int i = 0 ; i < this.quantities.length ; i++ ) {
			int nLeft = this.quantities[ i ] * amount;

			for ( int j = 0 ; j < nInvItems && nLeft > 0 ; j++ ) {
				final ItemStack invStack = inventory.getStackInSlot( j );
				if ( this.checkItemStack( i , invStack , world ) ) {
					nLeft -= invStack.stackSize;
				}
			}

			if ( nLeft > 0 ) {
				return false;
			}
		}
		return true;
	}


	@Override
	public int getMaxOutput( final IInventory inventory , final World world )
	{
		final int nInvItems = inventory.getSizeInventory( );
		int maxQuantity = Integer.MAX_VALUE;
		for ( int i = 0 ; i < this.quantities.length && maxQuantity > 0 ; i++ ) {
			int nFound = 0;

			for ( int j = 0 ; j < nInvItems ; j++ ) {
				final ItemStack invStack = inventory.getStackInSlot( j );
				if ( this.checkItemStack( i , invStack , world ) ) {
					nFound += invStack.stackSize;
				}
			}

			maxQuantity = Math.min( maxQuantity , nFound / this.quantities[ i ] );
		}
		return maxQuantity;
	}


	@Override
	public void removeFromInventory( final IInventory inventory , final int amount , final World world )
	{
		final int nInvItems = inventory.getSizeInventory( );
		for ( int i = 0 ; i < this.quantities.length ; i++ ) {
			int nLeft = this.quantities[ i ] * amount;
			for ( int j = 0 ; j < nInvItems && nLeft > 0 ; j++ ) {
				final ItemStack invStack = inventory.getStackInSlot( j );
				if ( this.checkItemStack( i , invStack , world ) ) {
					final int used = Math.min( nLeft , invStack.stackSize );
					nLeft -= used;
					if ( invStack.stackSize == used ) {
						inventory.setInventorySlotContents( j , invStack.getItem( ).getContainerItem( invStack ) );
					} else {
						invStack.stackSize -= used;
					}
				}
			}
			assert nLeft == 0;
		}
	}
}
