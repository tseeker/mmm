package mmm.tech.base.alloy_furnace;


import mmm.MmmMaterials;
import mmm.core.api.tech.E_ActivationMode;
import mmm.core.api.tech.I_ConfigurableActivation;
import mmm.materials.MAlloyRecipe;
import mmm.utils.UInventoryGrid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;



public class TBAFTileEntity
		extends TileEntity
		implements ITickable , I_ConfigurableActivation , IWorldNameable
{
	/** Input hopper allows invalid input? */
	public static final int F_IH_INVALID = 1 << 0;
	/** Output hopper collects invalid input? */
	public static final int F_OH_INVALID_INPUT = 1 << 1;
	/** Output hopper collects invalid fuel? */
	public static final int F_OH_INVALID_FUEL = 1 << 2;
	/** Comparators check output? */
	public static final int F_CMP_OUTPUT = 1 << 3;
	/** Comparators check fuel? */
	public static final int F_CMP_FUEL = 1 << 4;
	/** Comparators check valid input? */
	public static final int F_CMP_INPUT_VALID = 1 << 5;
	/** Comparators check invalid input? */
	public static final int F_CMP_INPUT_INVALID = 1 << 6;
	/** Comparators check alloying progress? */
	public static final int F_CMP_ALLOYING = 1 << 7;
	/** Comparators check burn time? */
	public static final int F_CMP_BURN_TIME = 1 << 8;

	private class Inventory
			extends UInventoryGrid
	{

		public Inventory( final String name , final int width , final int height )
		{
			super( name , width , height );
		}


		@Override
		public void markDirty( )
		{
			TBAFTileEntity.this.forceUpdate( );
		}

	}

	private class FuelInventory
			extends Inventory
	{

		public FuelInventory( final String name , final int width , final int height )
		{
			super( name , width , height );
		}


		@Override
		public boolean isItemValidForSlot( final int index , final ItemStack stack )
		{
			return super.isItemValidForSlot( index , stack ) && TileEntityFurnace.isItemFuel( stack );
		}

	}

	public final UInventoryGrid input;
	public final UInventoryGrid fuel;
	public final UInventoryGrid output;

	private String customName;

	public MAlloyRecipe recipe;

	private MAlloyRecipe alloying;
	private int alloyCurrent;
	private int burnCurrent;
	private int burnTotal;
	private E_ActivationMode activationMode;

	public int flags;

	private final IItemHandler inputHopper;
	private final IItemHandler fuelHopper;
	private final IItemHandler outputHopper;


	public TBAFTileEntity( )
	{
		this.input = new Inventory( "Input" , 3 , 5 );
		this.fuel = new FuelInventory( "Fuel" , 2 , 2 );
		this.output = new Inventory( "Output" , 2 , 5 );
		this.recipe = MAlloyRecipe.REGISTRY.getRecipes( ).get( 0 );
		this.activationMode = E_ActivationMode.ALWAYS_ACTIVE;
		this.flags = TBAFTileEntity.F_OH_INVALID_INPUT | TBAFTileEntity.F_OH_INVALID_FUEL
				| TBAFTileEntity.F_CMP_INPUT_VALID;

		this.inputHopper = new TBAFInputHopper( this );
		this.fuelHopper = new InvWrapper( this.fuel );
		this.outputHopper = new TBAFOutputHopper( this );
	}


	@Override
	public String getName( )
	{
		return this.hasCustomName( ) ? this.customName : "tile.mmm.tech.base.alloy_furnace.inactive.name";
	}


	@Override
	public boolean hasCustomName( )
	{
		return this.customName != null && !this.customName.isEmpty( );
	}


	public void setCustomName( final String name )
	{
		this.customName = name;
	}


	@Override
	public boolean shouldRefresh( final World world , final BlockPos pos , final IBlockState oldState ,
			final IBlockState newSate )
	{
		return ! ( newSate.getBlock( ) instanceof TBAFBlock );
	}


	@Override
	public void readFromNBT( final NBTTagCompound compound )
	{
		super.readFromNBT( compound );
		if ( compound.hasKey( "CustomName" , 8 ) ) {
			this.customName = compound.getString( "CustomName" );
		}
		this.input.deserializeNBT( compound.getTagList( "Input" , NBT.TAG_COMPOUND ) );
		this.fuel.deserializeNBT( compound.getTagList( "Fuel" , NBT.TAG_COMPOUND ) );
		this.output.deserializeNBT( compound.getTagList( "Output" , NBT.TAG_COMPOUND ) );
		this.readSyncData( compound );
	}


	@Override
	public NBTTagCompound writeToNBT( final NBTTagCompound compound )
	{
		super.writeToNBT( compound );
		if ( this.hasCustomName( ) ) {
			compound.setString( "CustomName" , this.customName );
		}
		compound.setTag( "Input" , this.input.serializeNBT( ) );
		compound.setTag( "Fuel" , this.fuel.serializeNBT( ) );
		compound.setTag( "Output" , this.output.serializeNBT( ) );
		this.writeSyncData( compound );
		return compound;
	}


	@Override
	public SPacketUpdateTileEntity getUpdatePacket( )
	{
		final NBTTagCompound compound = new NBTTagCompound( );
		this.writeSyncData( compound );
		return new SPacketUpdateTileEntity( this.pos , 0 , compound );
	}


	@Override
	public NBTTagCompound getUpdateTag( )
	{
		final NBTTagCompound compound = new NBTTagCompound( );
		this.writeToNBT( compound );
		return compound;
	}


	@Override
	public void onDataPacket( final NetworkManager net , final SPacketUpdateTileEntity pkt )
	{
		if ( this.worldObj.isRemote ) {
			this.readSyncData( pkt.getNbtCompound( ) );
			final IBlockState state = this.worldObj.getBlockState( this.pos );
			this.worldObj.notifyBlockUpdate( this.pos , state , state , 3 );
		}
	}


	@Override
	public void update( )
	{
		if ( this.worldObj.isRemote ) {
			return;
		}

		final boolean wasBurning = this.isBurning( );
		boolean dirty = false;

		if ( wasBurning ) {
			dirty = true;
			this.burnCurrent--;

			if ( this.alloying != null ) {
				this.alloyCurrent--;
				if ( this.alloyCurrent == 0 ) {
					this.addOutput( this.alloying.output );
					if ( this.alloying.slag != 0 ) {
						this.addOutput( new ItemStack( MmmMaterials.ITEM.SLAG , this.alloying.slag ) );
					}
					this.alloying = null;
				}
			}

			if ( this.burnCurrent == 0 ) {
				if ( this.alloying != null ) {
					if ( ! ( this.isEnabled( ) && this.startBurning( this.alloyCurrent ) ) ) {
						this.cancelAlloying( );
						this.burnCurrent = this.burnTotal = 0;
					}
				} else {
					this.burnTotal = this.burnCurrent = 0;
				}
			}

		}

		if ( this.isEnabled( ) && !this.isAlloying( ) && this.canAlloy( )
				&& ( this.isBurning( ) || this.startBurning( this.recipe.burnTime ) ) ) {
			this.startAlloying( );
			dirty = true;
		}

		if ( this.isBurning( ) != wasBurning ) {
			TBAFBlock.setState( this.isBurning( ) , this.worldObj , this.pos );
		}

		if ( dirty ) {
			this.forceUpdate( );
		}
	}


	public boolean isEnabled( )
	{
		final boolean powered = TBAFBlock.isPowered( this.getBlockMetadata( ) );
		switch ( this.activationMode ) {
			case ALWAYS_ACTIVE:
				return true;
			case POWERED:
				return powered;
			case UNPOWERED:
				return !powered;
			case DISABLED:
				return false;
		}
		System.err.println( "Warning: unsupported activation mode" );
		return false;
	}


	@Override
	public boolean hasCapability( final Capability< ? > capability , final EnumFacing facing )
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != null
				|| super.hasCapability( capability , facing );
	}


	@SuppressWarnings( "unchecked" )
	@Override
	public < T > T getCapability( final Capability< T > capability , final EnumFacing facing )
	{
		if ( facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ) {
			if ( facing == EnumFacing.DOWN ) {
				return (T) this.outputHopper;
			} else if ( facing == EnumFacing.UP ) {
				return (T) this.inputHopper;
			} else {
				return (T) this.fuelHopper;
			}
		}
		return super.getCapability( capability , facing );
	}


	@Override
	public E_ActivationMode getActivationMode( )
	{
		return this.activationMode;
	}


	@Override
	public void setActivationMode( final E_ActivationMode mode )
	{
		this.activationMode = mode;
	}


	public void cancelAlloying( )
	{
		if ( this.alloying != null ) {
			this.addOutput( new ItemStack( MmmMaterials.ITEM.SLAG , this.alloying.getTotalInputItems( ) ) );
			this.alloying = null;
			this.alloyCurrent = 0;
		}
	}


	public void setRecipe( final ResourceLocation location )
	{
		MAlloyRecipe recipe = MAlloyRecipe.REGISTRY.getRecipe( location );
		if ( recipe == null ) {
			recipe = MAlloyRecipe.REGISTRY.getRecipes( ).get( 0 );
		}

		if ( recipe == this.recipe ) {
			return;
		}
		this.recipe = recipe;

		if ( !this.worldObj.isRemote ) {
			this.forceUpdate( );
		}
	}


	public void forceUpdate( )
	{
		this.markDirty( );
		final IBlockState state = this.worldObj.getBlockState( this.pos );
		this.worldObj.notifyBlockUpdate( this.pos , state , state , 3 );
	}


	public boolean isBurning( )
	{
		return this.burnTotal != 0;
	}


	public int getBurnProgress( final int max )
	{
		int t = this.burnTotal;
		if ( t == 0 ) {
			t = 200;
		}
		return Math.min( max , this.burnCurrent * max / t );
	}


	/**
	 * Find the most appropriate fuel for the specified burn time.
	 *
	 * @param requiredBurnTime
	 *            the burn time we need
	 *
	 * @return the fuel slot's index, or -1 if there is no fuel.
	 */
	private int getBestFuel( final int requiredBurnTime )
	{
		int bestStack = -1 , bestDiff = Integer.MAX_VALUE;
		for ( int i = 0 ; i < this.fuel.slotsCount ; i++ ) {
			final ItemStack fuel = this.fuel.inventoryContents[ i ];
			if ( fuel == null || fuel.stackSize == 0 ) {
				continue;
			}
			final int fuelBurnTime = TileEntityFurnace.getItemBurnTime( fuel );
			final int diff = Math.abs( requiredBurnTime - fuelBurnTime );
			if ( diff < bestDiff ) {
				bestStack = i;
				bestDiff = diff;
			}
		}
		return bestStack;
	}


	private boolean startBurning( final int requiredBurnTime )
	{
		final int fuelSlot = this.getBestFuel( requiredBurnTime );
		if ( fuelSlot == -1 ) {
			return false;
		}

		final ItemStack fuelStack = this.fuel.inventoryContents[ fuelSlot ];
		this.burnCurrent = this.burnTotal = TileEntityFurnace.getItemBurnTime( fuelStack );

		fuelStack.stackSize--;
		if ( fuelStack.stackSize == 0 ) {
			final ItemStack replace = fuelStack.getItem( ).getContainerItem( fuelStack );
			this.fuel.inventoryContents[ fuelSlot ] = replace;
		}

		return true;
	}


	/**
	 * Checks if alloying is possible
	 * <p>
	 * In order to do that, we need to check that:
	 * <ul>
	 * <li>the current recipe's requirements are present in the input slot, AND
	 * <li>either there is a free output slot OR there is an output slot with the current output
	 * which can accept enough items.
	 * </ul>
	 *
	 * @return <code>true</code> if alloying is possible
	 */
	public boolean canAlloy( )
	{
		if ( !this.recipe.checkInventory( this.input ) ) {
			return false;
		}

		final ItemStack output = this.recipe.output;
		final int fullStack = Math.min( this.output.getInventoryStackLimit( ) , output.getMaxStackSize( ) );
		int freeSpace = 0;

		for ( int i = 0 ; i < this.output.slotsCount && freeSpace < output.stackSize ; i++ ) {
			final ItemStack outputSlot = this.output.inventoryContents[ i ];
			if ( outputSlot == null ) {
				freeSpace += fullStack;
			} else if ( outputSlot.isItemEqual( output ) ) {
				freeSpace += fullStack - outputSlot.stackSize;
			}
		}

		return freeSpace >= output.stackSize;
	}


	public boolean isAlloying( )
	{
		return this.alloying != null;
	}


	public int getAlloyingProgress( final int max )
	{
		if ( this.alloying == null ) {
			return max;
		}

		int t = this.alloying.burnTime;
		if ( t == 0 ) {
			t = 200;
		}
		return Math.min( max , ( t - this.alloyCurrent ) * max / t );
	}


	private void startAlloying( )
	{
		this.removeRecipeInput( );
		this.alloying = this.recipe;
		this.alloyCurrent = this.recipe.burnTime;
	}


	private void removeRecipeInput( )
	{
		final ItemStack[] rIn = this.recipe.inputs;
		final int inSlots = this.input.slotsCount;
		for ( int i = 0 ; i < rIn.length ; i++ ) {
			final ItemStack inputStack = rIn[ i ];
			int found = 0;
			for ( int slot = 0 ; slot < inSlots ; slot++ ) {
				final ItemStack stackInSlot = this.input.inventoryContents[ slot ];
				if ( stackInSlot == null || !inputStack.isItemEqual( stackInSlot ) ) {
					continue;
				}

				final int take = Math.min( inputStack.stackSize - found , stackInSlot.stackSize );
				found += take;
				stackInSlot.stackSize -= take;
				if ( stackInSlot.stackSize == 0 ) {
					final ItemStack replace = stackInSlot.getItem( ).getContainerItem( stackInSlot );
					this.input.inventoryContents[ slot ] = replace;
				}

				if ( found == inputStack.stackSize ) {
					break;
				}
			}
		}
	}


	private void addOutput( final ItemStack wanted )
	{
		final int maxStackSize = Math.min( this.output.getInventoryStackLimit( ) , wanted.getMaxStackSize( ) );
		int added = 0;
		for ( int i = 0 ; i < this.output.slotsCount ; i++ ) {
			ItemStack outputStack = this.output.inventoryContents[ i ];
			int canAdd;
			if ( outputStack == null ) {
				canAdd = maxStackSize;
				outputStack = new ItemStack( wanted.getItem( ) , 0 , wanted.getItemDamage( ) );
				this.output.inventoryContents[ i ] = outputStack;

			} else if ( outputStack.isItemEqual( wanted ) ) {
				canAdd = maxStackSize - outputStack.stackSize;

			} else {
				canAdd = 0;
			}

			if ( canAdd == 0 ) {
				continue;
			}

			final int doAdd = Math.min( canAdd , wanted.stackSize - added );
			added += doAdd;
			outputStack.stackSize += doAdd;
			if ( added == wanted.stackSize ) {
				break;
			}
		}
	}


	private void readSyncData( final NBTTagCompound compound )
	{
		final String recipeName = compound.getString( "Recipe" );
		this.recipe = MAlloyRecipe.REGISTRY.getRecipe( new ResourceLocation( recipeName ) );
		if ( this.recipe == null ) {
			this.recipe = MAlloyRecipe.REGISTRY.getRecipes( ).get( 0 );
		}

		final byte am = compound.getByte( "ActivationMode" );
		switch ( am ) {
			default:
				// XXX log
			case 0:
				this.activationMode = E_ActivationMode.ALWAYS_ACTIVE;
				break;
			case 1:
				this.activationMode = E_ActivationMode.POWERED;
				break;
			case 2:
				this.activationMode = E_ActivationMode.UNPOWERED;
				break;
			case 3:
				this.activationMode = E_ActivationMode.DISABLED;
				break;
		}
		this.flags = compound.getShort( "Flags" );

		this.burnCurrent = compound.getInteger( "BurnCurrent" );
		this.burnTotal = compound.getInteger( "BurnTotal" );

		final String alloyingRecipeName = compound.getString( "AlloyRecipe" );
		if ( "".equals( alloyingRecipeName ) ) {
			this.alloying = null;
		} else {
			this.alloying = MAlloyRecipe.REGISTRY.getRecipe( new ResourceLocation( alloyingRecipeName ) );
		}
		if ( this.alloying == null ) {
			this.alloyCurrent = 0;
		} else {
			this.alloyCurrent = compound.getInteger( "AlloyCurrent" );
		}
	}


	private void writeSyncData( final NBTTagCompound compound )
	{
		compound.setString( "Recipe" , this.recipe.name.toString( ) );
		compound.setByte( "ActivationMode" , (byte) this.activationMode.ordinal( ) );
		compound.setShort( "Flags" , (short) this.flags );
		if ( this.alloying != null ) {
			compound.setString( "AlloyRecipe" , this.alloying.name.toString( ) );
			compound.setInteger( "AlloyCurrent" , this.alloyCurrent );
		}
		if ( this.burnTotal != 0 ) {
			compound.setInteger( "BurnTotal" , this.burnTotal );
			compound.setInteger( "BurnCurrent" , this.burnCurrent );
		}
	}


	public int getComparatorValue( )
	{
		int nValues = 0;
		int total = 0;

		if ( ( this.flags & TBAFTileEntity.F_CMP_OUTPUT ) != 0 ) {
			nValues++;
			total += Container.calcRedstoneFromInventory( this.output );
		}

		if ( ( this.flags & TBAFTileEntity.F_CMP_FUEL ) != 0 ) {
			nValues++;
			total += Container.calcRedstoneFromInventory( this.fuel );
		}

		if ( ( this.flags & ( TBAFTileEntity.F_CMP_INPUT_INVALID | TBAFTileEntity.F_CMP_INPUT_VALID ) ) != 0 ) {
			int validInput = 0 , invalidInput = 0;
			float validInputSum = 0f , invalidInputSum = 0f;
			for ( int i = 0 ; i < this.input.slotsCount ; i++ ) {
				final ItemStack stack = this.input.getStackInSlot( i );
				if ( stack == null ) {
					continue;
				}
				final float value = stack.stackSize / (float) Math.min( this.input.getInventoryStackLimit( ) , //
						stack.getMaxStackSize( ) );
				if ( this.recipe.hasInput( stack ) ) {
					validInput++;
					validInputSum += value;
				} else {
					invalidInput++;
					invalidInputSum += value;
				}
			}
			if ( ( this.flags & TBAFTileEntity.F_CMP_INPUT_VALID ) != 0 ) {
				validInputSum /= this.input.slotsCount;
				total += MathHelper.floor_float( validInputSum * 14F ) + ( validInput > 0 ? 1 : 0 );
				nValues++;
			}
			if ( ( this.flags & TBAFTileEntity.F_CMP_INPUT_INVALID ) != 0 ) {
				invalidInputSum /= this.input.slotsCount;
				total += MathHelper.floor_float( invalidInputSum * 14F ) + ( invalidInput > 0 ? 1 : 0 );
				nValues++;
			}
		}

		if ( ( this.flags & TBAFTileEntity.F_CMP_ALLOYING ) != 0 ) {
			nValues++;
			if ( this.alloying != null ) {
				int t = this.alloying.burnTime;
				if ( t == 0 ) {
					t = 200;
				}
				total += 1 + Math.min( 14 , ( t - this.alloyCurrent ) * 14 / t );
			}
		}

		if ( ( this.flags & TBAFTileEntity.F_CMP_BURN_TIME ) != 0 ) {
			nValues++;
			if ( this.isBurning( ) ) {
				int t = this.burnTotal;
				if ( t == 0 ) {
					t = 200;
				}
				total += 1 + Math.min( 14 , ( t - this.burnCurrent ) * 14 / t );
			}
		}

		if ( nValues == 0 ) {
			return 0;
		}
		final float ratio = total / ( 15.0f * nValues );
		return MathHelper.floor_float( ratio * 14.0f ) + ( total == 0 ? 0 : 1 );
	}

}
