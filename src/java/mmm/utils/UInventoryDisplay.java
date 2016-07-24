package mmm.utils;


import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;



public class UInventoryDisplay
		implements IInventory
{
	public final String name;
	public final int size;
	private final ItemStack[] contents;


	public UInventoryDisplay( final String name , final int size )
	{
		this.name = name;
		this.size = size;
		this.contents = new ItemStack[ size ];
	}


	@Override
	public String getName( )
	{
		return this.name;
	}


	@Override
	public boolean hasCustomName( )
	{
		return false;
	}


	@Override
	public ITextComponent getDisplayName( )
	{
		return new TextComponentTranslation( this.name );
	}


	@Override
	public int getSizeInventory( )
	{
		return this.size;
	}


	@Override
	@Nullable
	public ItemStack getStackInSlot( final int index )
	{
		return index >= 0 && index < this.size ? this.contents[ index ] : null;
	}


	@Override
	public ItemStack decrStackSize( final int index , final int count )
	{
		return null;
	}


	@Override
	public ItemStack removeStackFromSlot( final int index )
	{
		return null;
	}


	@Override
	public void setInventorySlotContents( final int index , final ItemStack stack )
	{
		this.contents[ index ] = stack;
		this.markDirty( );
	}


	@Override
	public int getInventoryStackLimit( )
	{
		return 64;
	}


	@Override
	public boolean isUseableByPlayer( final EntityPlayer player )
	{
		return false;
	}


	@Override
	public void openInventory( final EntityPlayer player )
	{
		// EMPTY
	}


	@Override
	public void closeInventory( final EntityPlayer player )
	{
		// EMPTY
	}


	@Override
	public boolean isItemValidForSlot( final int index , final ItemStack stack )
	{
		return true;
	}


	@Override
	public int getField( final int id )
	{
		return 0;
	}


	@Override
	public void setField( final int id , final int value )
	{
		// EMPTY
	}


	@Override
	public int getFieldCount( )
	{
		return 0;
	}


	@Override
	public void clear( )
	{
		Arrays.fill( this.contents , null );
	}


	@Override
	public void markDirty( )
	{
		// EMPTY
	}

}
