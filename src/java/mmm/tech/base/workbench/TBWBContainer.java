package mmm.tech.base.workbench;


import javax.annotation.Nullable;

import mmm.MmmTech;
import mmm.core.api.recipes.I_CraftingRecipeWrapper;
import mmm.recipes.RCraftingWrappers;
import mmm.utils.UInventoryDisplay;
import mmm.utils.gui.UGContainer;
import mmm.utils.gui.UGSlotDisplay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;



public class TBWBContainer
		extends UGContainer
{
	public final TBWBTileEntity tileEntity;
	public final World world;
	public final BlockPos position;
	public final UInventoryDisplay recipeDisplay;
	public final InventoryCraftResult recipeOutput;
	private I_CraftingRecipeWrapper recipeWrapper;


	public TBWBContainer( final InventoryPlayer playerInv , final TBWBTileEntity tileEntity )
	{
		this.tileEntity = tileEntity;
		this.world = tileEntity.getWorld( );
		this.position = tileEntity.getPos( );

		this.addPlayerInventory( Slot::new , playerInv , 28 , 119 );

		this.slotGroups.nextGroup( );
		this.addGrid( Slot::new , tileEntity.storage , 8 , 15 );

		this.slotGroups.nextGroup( );
		this.recipeDisplay = new UInventoryDisplay( "Recipe" , 10 );
		this.addGrid( UGSlotDisplay::new , this.recipeDisplay , //
				3 , 3 , 0 , 86 , 32 );

		this.slotGroups.nextGroup( );
		this.recipeOutput = new InventoryCraftResult( );
		this.addSlotToContainer( new TBWBCraftingSlot( playerInv.player , this , //
				this.recipeOutput , 0 , 174 , 50 ) );

		this.slotGroups.endGroups( );

		this.recipeWrapper = RCraftingWrappers.IDENTIFIERS.get( tileEntity.getDefaultRecipe( ) );
		if ( this.recipeWrapper == null ) {
			this.recipeWrapper = RCraftingWrappers.RECIPES.get( 0 );
		}
		this.onRecipeChanged( );
	}


	@Override
	public boolean canInteractWith( final EntityPlayer player )
	{
		return this.world.getBlockState( this.position ).getBlock( ) == MmmTech.MACHINES.WORKBENCH
				&& player.getDistanceSq( this.position.getX( ) + .5 , this.position.getY( ) + .5 ,
						this.position.getZ( ) + .5 ) <= 64.;
	}


	@Override
	@Nullable
	public ItemStack transferStackInSlot( final EntityPlayer playerIn , final int index )
	{
		final Slot slot = this.inventorySlots.get( index );
		if ( slot == null || !slot.getHasStack( ) ) {
			return null;
		}

		final ItemStack slotStack = slot.getStack( );

		final int group = this.slotGroups.getGroup( index );
		if ( group == 3 ) {
			// Craft as many as possible
			if ( this.recipeWrapper == null || !this.recipeWrapper.canShiftClick( ) ) {
				return null;
			}
			final IInventory storage = this.getStorage( );
			if ( storage == null ) {
				return null;
			}

			// Can we?
			if ( !this.recipeWrapper.getRequirements( ).checkInventory( storage , this.world ) ) {
				return null;
			}

			// Merge it and remove ingredients
			final ItemStack outStack = this.getCurrentRecipe( ).getActualOutput( storage );
			if ( !this.mergeItemStack( outStack , 0 , 36 , false ) ) {
				return null;
			}
			( (TBWBCraftingSlot) slot ).handleCrafting( 1 );

			// Drop any items that didn't fit in the inventory
			if ( outStack.stackSize != 0 ) {
				playerIn.dropItem( outStack , false );
				return null;
			}

			return slotStack;

		}

		final ItemStack outStack = slotStack.copy( );
		if ( group == 0 ) {
			// Player inventory to storage
			if ( !this.mergeItemStack( slotStack , 36 , 51 , false ) ) {
				return null;
			}
		} else if ( group == 1 ) {
			// Storage to player inventory
			if ( !this.mergeItemStack( slotStack , 0 , 36 , false ) ) {
				return null;
			}
		} else {
			return null;
		}

		if ( slotStack.stackSize == 0 ) {
			slot.putStack( (ItemStack) null );
		} else {
			slot.onSlotChanged( );
		}

		if ( slotStack.stackSize == outStack.stackSize ) {
			return null;
		}
		slot.onPickupFromSlot( playerIn , slotStack );
		return outStack;
	}


	@Override
	public boolean canMergeSlot( final ItemStack stack , final Slot slotIn )
	{
		return slotIn.inventory != this.recipeOutput && super.canMergeSlot( stack , slotIn );
	}


	public void setCurrentRecipe( final I_CraftingRecipeWrapper wrapper , final boolean setDefault )
	{
		this.recipeWrapper = wrapper;
		this.onRecipeChanged( );
		if ( setDefault ) {
			final TileEntity te = this.world.getTileEntity( this.position );
			if ( te instanceof TBWBTileEntity ) {
				( (TBWBTileEntity) te ).setDefaultRecipe( wrapper == null ? "" : wrapper.getIdentifier( ) );
			}
		}
	}


	private void onRecipeChanged( )
	{
		this.recipeDisplay.clear( );
		this.recipeOutput.clear( );
		if ( this.recipeWrapper == null ) {
			// XXX log if confirm is set
			return;
		}
		this.recipeWrapper.addInputsToDisplay( this.recipeDisplay );
		this.recipeOutput.setInventorySlotContents( 0 , this.recipeWrapper.getOutput( ) );
	}


	public IInventory getStorage( )
	{
		final TileEntity te = this.world.getTileEntity( this.position );
		if ( te instanceof TBWBTileEntity ) {
			return ( (TBWBTileEntity) te ).storage;
		}
		return null;
	}


	public I_CraftingRecipeWrapper getCurrentRecipe( )
	{
		return this.recipeWrapper;
	}

}
