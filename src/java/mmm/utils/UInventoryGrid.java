package mmm.utils;


import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.INBTSerializable;



public class UInventoryGrid
		implements IInventory , INBTSerializable< NBTTagList >
{
	public final String name;
	public final int width;
	public final int height;
	public final int slotsCount;
	public final ItemStack[] inventoryContents;


	public UInventoryGrid( final String name , final int width , final int height )
	{
		this.name = name;
		this.width = width;
		this.height = height;
		this.slotsCount = width * height;
		this.inventoryContents = new ItemStack[ width * height ];
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
		return this.slotsCount;
	}


	@Override
	@Nullable
	public ItemStack getStackInSlot( final int index )
	{
		return index >= 0 && index < this.inventoryContents.length ? this.inventoryContents[ index ] : null;
	}


	@Nullable
	public ItemStack getStackInCell( final int x , final int y )
	{
		return this.getStackInSlot( x + y * this.width );
	}


	@Override
	@Nullable
	public ItemStack decrStackSize( final int index , final int count )
	{
		final ItemStack itemstack = ItemStackHelper.getAndSplit( this.inventoryContents , index , count );
		if ( itemstack != null ) {
			this.markDirty( );
		}
		return itemstack;
	}


	@Override
	@Nullable
	public ItemStack removeStackFromSlot( final int index )
	{
		if ( this.inventoryContents[ index ] != null ) {
			final ItemStack itemstack = this.inventoryContents[ index ];
			this.inventoryContents[ index ] = null;
			return itemstack;
		}
		return null;
	}


	@Override
	public void setInventorySlotContents( final int index , final ItemStack stack )
	{
		this.inventoryContents[ index ] = stack;
		if ( stack != null && stack.stackSize > this.getInventoryStackLimit( ) ) {
			stack.stackSize = this.getInventoryStackLimit( );
		}
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
		return true;
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
		Arrays.fill( this.inventoryContents , null );
	}


	@Override
	public void markDirty( )
	{
		// EMPTY
	}


	@Override
	public NBTTagList serializeNBT( )
	{
		final NBTTagList list = new NBTTagList( );
		for ( int i = 0 ; i < this.slotsCount ; i++ ) {
			final ItemStack stack = this.inventoryContents[ i ];
			NBTTagCompound tag;
			if ( stack == null ) {
				tag = new NBTTagCompound( );
			} else {
				tag = stack.serializeNBT( );
			}
			list.appendTag( tag );
		}
		return list;
	}


	@Override
	public void deserializeNBT( final NBTTagList nbt )
	{
		final int n = Math.min( nbt.tagCount( ) , this.slotsCount );
		Arrays.fill( this.inventoryContents , null );
		for ( int i = 0 ; i < n ; i++ ) {
			final NBTTagCompound tag = nbt.getCompoundTagAt( i );
			if ( tag.hasKey( "id" ) ) {
				this.inventoryContents[ i ] = ItemStack.loadItemStackFromNBT( tag );
			}
		}
	}

}
