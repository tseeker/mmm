package mmm.utils.gui;


import java.util.List;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.inventory.Slot;



public class UGSlotGroups
{
	private final List< Slot > slots;
	private final IntArrayList firstSlots;
	private int[] slotX;
	private int[] slotY;


	public UGSlotGroups( final List< Slot > slots )
	{
		this.slots = slots;
		this.firstSlots = new IntArrayList( );
	}


	public int nextGroup( )
	{
		this.firstSlots.add( this.slots.size( ) );
		return this.firstSlots.size( ) - 1;
	}


	public void endGroups( )
	{
		final int nSlots = this.slots.size( );
		this.slotX = new int[ nSlots ];
		this.slotY = new int[ nSlots ];
		for ( int i = 0 ; i < nSlots ; i++ ) {
			final Slot slot = this.slots.get( i );
			this.slotX[ i ] = slot.xDisplayPosition;
			this.slotY[ i ] = slot.yDisplayPosition;
			slot.xDisplayPosition = slot.yDisplayPosition = -4000;
		}
	}


	public void hideAll( )
	{
		for ( int i = 0 ; i < this.slotX.length ; i++ ) {
			final Slot slot = this.slots.get( i );
			slot.xDisplayPosition = slot.yDisplayPosition = -4000;
		}
	}


	public void showAll( )
	{
		for ( int i = 0 ; i < this.slotX.length ; i++ ) {
			final Slot slot = this.slots.get( i );
			slot.xDisplayPosition = this.slotX[ i ];
			slot.yDisplayPosition = this.slotY[ i ];
		}
	}


	public UGSlotGroups showGroup( final int index )
	{
		final int first = this.firstSlots.getInt( index );
		final int last;
		if ( index == this.firstSlots.size( ) - 1 ) {
			last = this.slotX.length;
		} else {
			last = this.firstSlots.getInt( index + 1 );
		}

		for ( int i = first ; i < last ; i++ ) {
			final Slot slot = this.slots.get( i );
			slot.xDisplayPosition = this.slotX[ i ];
			slot.yDisplayPosition = this.slotY[ i ];
		}

		return this;
	}


	public UGSlotGroups hideGroup( final int index )
	{
		final int first = this.firstSlots.getInt( index );
		final int last;
		if ( index == this.firstSlots.size( ) - 1 ) {
			last = this.slotX.length;
		} else {
			last = this.firstSlots.getInt( index + 1 );
		}

		for ( int i = first ; i < last ; i++ ) {
			final Slot slot = this.slots.get( i );
			slot.xDisplayPosition = slot.yDisplayPosition = -4000;
		}

		return this;
	}


	public int getGroup( final int slotIndex )
	{
		final int nGroups = this.firstSlots.size( );
		for ( int i = 1 ; i < nGroups ; i++ ) {
			if ( slotIndex < this.firstSlots.getInt( i ) ) {
				return i - 1;
			}
		}
		return nGroups - 1;
	}
}
