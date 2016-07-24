package mmm.utils.gui;


import mmm.utils.UInventoryGrid;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;



public abstract class UGContainer
		extends Container
{

	public static interface I_SlotFactory
	{
		public Slot createSlot( IInventory inv , int index , int x , int y );
	}

	public final UGSlotGroups slotGroups;


	public UGContainer( )
	{
		super( );
		this.slotGroups = new UGSlotGroups( this.inventorySlots );
		this.slotGroups.nextGroup( );
	}


	public void addPlayerInventory( final I_SlotFactory slotAdder , final IInventory inv , final int x , final int y )
	{
		this.addPlayerInventory( slotAdder , inv , x , y , 4 );
	}


	public void addGrid( final I_SlotFactory slotAdder , final UInventoryGrid inv , final int x , final int y )
	{
		this.addGrid( slotAdder , inv , x , y , 2 , 2 );
	}


	public void addGrid( final I_SlotFactory slotAdder , final UInventoryGrid inv , final int x , final int y ,
			final int xSpacing , final int ySpacing )
	{
		for ( int row = 0 , i = 0 ; row < inv.height ; ++row ) {
			for ( int column = 0 ; column < inv.width ; ++column , ++i ) {
				this.addSlotToContainer( slotAdder.createSlot( inv , i , //
						x + column * ( 16 + xSpacing ) , y + row * ( 16 + ySpacing ) ) );
			}
		}
	}


	public void addPlayerInventory( final I_SlotFactory slotAdder , final IInventory inv , final int x , final int y ,
			final int spacing )
	{
		this.addGrid( slotAdder , inv , 9 , 3 , 9 , x , y ); // Main inventory
		this.addGrid( slotAdder , inv , 9 , 1 , 0 , x , y + spacing + 54 ); // Quick bar
	}


	public void addGrid( final I_SlotFactory slotAdder , final IInventory inv , final int columns , final int rows ,
			final int index , final int x , final int y )
	{
		this.addGrid( slotAdder , inv , columns , rows , index , x , y , 2 , 2 );
	}


	public void addGrid( final I_SlotFactory slotAdder , final IInventory inv , final int columns , final int rows ,
			final int index , final int x , final int y , final int xSpacing , final int ySpacing )
	{
		for ( int row = 0 , i = 0 ; row < rows ; ++row ) {
			for ( int column = 0 ; column < columns ; ++column , ++i ) {
				this.addSlotToContainer( slotAdder.createSlot( inv , index + i , //
						x + column * ( 16 + xSpacing ) , y + row * ( 16 + ySpacing ) ) );
			}
		}
	}

}
