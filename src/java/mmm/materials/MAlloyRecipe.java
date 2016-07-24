package mmm.materials;


import java.util.ArrayList;
import java.util.HashMap;

import mmm.Mmm;
import mmm.utils.UItemId;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;



public class MAlloyRecipe
{

	public static final int MAX_ALLOY_INPUTS = 6;
	public static final MAlloyRecipe.I_Registry REGISTRY = new MAlloyRecipe.Registry( );

	public static interface I_Registry
	{
		public MAlloyRecipe getRecipe( ResourceLocation name );


		public ArrayList< MAlloyRecipe > getRecipes( );
	}

	private static class Registry
			implements MAlloyRecipe.I_Registry
	{
		private final HashMap< ResourceLocation , MAlloyRecipe > byName = new HashMap<>( );
		private final ArrayList< MAlloyRecipe > list = new ArrayList<>( );


		@Override
		public MAlloyRecipe getRecipe( final ResourceLocation name )
		{
			return this.byName.get( name );
		}


		@Override
		public ArrayList< MAlloyRecipe > getRecipes( )
		{
			return this.list;
		}

	}

	public static class Builder
	{
		private ResourceLocation name;
		private int burnTime;
		private ItemStack output;
		private int slag;
		private final ItemStack[] inputs = new ItemStack[ MAlloyRecipe.MAX_ALLOY_INPUTS ];
		private int nInputs;


		private Builder( )
		{
			// EMPTY
		}


		public MAlloyRecipe.Builder setName( final String name )
		{
			this.name = new ResourceLocation( Mmm.ID , name );
			return this;
		}


		public MAlloyRecipe.Builder setBurnTime( final int burnTime )
		{
			if ( burnTime <= 0 ) {
				throw new IllegalArgumentException( "invalid burn time" );
			}
			this.burnTime = burnTime;
			return this;
		}


		public MAlloyRecipe.Builder setOutput( final Item item )
		{
			this.output = new ItemStack( item );
			return this;
		}


		public MAlloyRecipe.Builder setOutput( final Item item , final int amount )
		{
			this.output = new ItemStack( item , amount );
			return this;
		}


		public MAlloyRecipe.Builder setOutput( final Block block )
		{
			this.output = new ItemStack( block );
			return this;
		}


		public MAlloyRecipe.Builder setOutput( final Block block , final int amount )
		{
			this.output = new ItemStack( block , amount );
			return this;
		}


		public MAlloyRecipe.Builder setOutput( final ItemStack stack )
		{
			this.output = stack;
			return this;
		}


		public MAlloyRecipe.Builder setSlag( final int slag )
		{
			if ( slag < 0 ) {
				throw new IllegalArgumentException( "invalid amount of slag" );
			}
			this.slag = slag;
			return this;
		}


		public MAlloyRecipe.Builder addInput( final Item item )
		{
			return this.addInput( new ItemStack( item ) );
		}


		public MAlloyRecipe.Builder addInput( final Item item , final int amount )
		{
			return this.addInput( new ItemStack( item , amount ) );
		}


		public MAlloyRecipe.Builder addInput( final Block block , final int amount )
		{
			return this.addInput( new ItemStack( block , amount ) );
		}


		public MAlloyRecipe.Builder addInput( final Block block )
		{
			return this.addInput( new ItemStack( block ) );
		}


		public MAlloyRecipe.Builder addInput( final ItemStack itemStack )
		{
			final int n = this.findItemType( UItemId.fromItemStack( itemStack ) );
			if ( n != -1 ) {
				this.inputs[ n ].stackSize += itemStack.stackSize;
			} else {
				this.inputs[ this.nInputs++ ] = itemStack;
			}
			return this;
		}


		public MAlloyRecipe.Builder register( )
		{
			final MAlloyRecipe.Registry registry = (MAlloyRecipe.Registry) MAlloyRecipe.REGISTRY;

			if ( this.name == null ) {
				throw new IllegalStateException( "alloy recipe has no identifier" );
			} else if ( registry.byName.containsKey( this.name ) ) {
				throw new IllegalStateException( "duplicate alloy recipe identifier " + this.name );
			}
			if ( this.output == null ) {
				throw new IllegalStateException( "alloy recipe has no output" );
			}
			if ( this.burnTime == 0 ) {
				throw new IllegalStateException( "alloy recipe has no burn time" );
			}
			if ( this.nInputs == 0 ) {
				throw new IllegalStateException( "alloy recipe has no inputs" );
			}
			if ( this.findItemType( UItemId.fromItemStack( this.output ) ) != -1 ) {
				throw new IllegalStateException( "alloy recipe needs its output to create itself" );
			}

			final ItemStack[] inputs = new ItemStack[ this.nInputs ];
			for ( int i = 0 ; i < inputs.length ; i++ ) {
				inputs[ i ] = this.inputs[ i ].copy( );
			}

			final MAlloyRecipe recipe = new MAlloyRecipe( this.name , this.burnTime , this.slag , this.output.copy( ) ,
					inputs );
			registry.byName.put( this.name , recipe );
			registry.list.add( recipe );
			this.name = null;

			return this;
		}


		private int findItemType( final UItemId id )
		{
			for ( int i = 0 ; i < this.nInputs ; i++ ) {
				if ( UItemId.fromItemStack( this.inputs[ i ] ).equals( id ) ) {
					return i;
				}
			}
			return -1;
		}
	}


	public static MAlloyRecipe.Builder build( )
	{
		return new MAlloyRecipe.Builder( );
	}

	public final ResourceLocation name;
	public final int burnTime;
	public final int slag;
	public final ItemStack output;
	public final ItemStack[] inputs;


	private MAlloyRecipe( final ResourceLocation name , final int burnTime , final int slag , final ItemStack output ,
			final ItemStack[] inputs )
	{
		this.name = name;
		this.burnTime = burnTime;
		this.slag = slag;
		this.output = output;
		this.inputs = inputs;
	}


	public String getLocalizedName( )
	{
		return this.output.getItem( ).getItemStackDisplayName( this.output );
	}


	public boolean checkInventory( final IInventory input )
	{
		return this.checkInventory( input , 0 , input.getSizeInventory( ) );
	}


	public boolean checkInventory( final IInventory input , final int first , final int last )
	{
		for ( int i = 0 ; i < this.inputs.length ; i++ ) {
			final ItemStack inputStack = this.inputs[ i ];
			int found = 0;
			for ( int slot = first ; slot < last ; slot++ ) {
				final ItemStack stackInSlot = input.getStackInSlot( slot );
				if ( stackInSlot == null || !inputStack.isItemEqual( stackInSlot ) ) {
					continue;
				}
				found += stackInSlot.stackSize;
				if ( found >= inputStack.stackSize ) {
					break;
				}
			}
			if ( found < inputStack.stackSize ) {
				return false;
			}
		}
		return true;
	}


	public int getTotalInputItems( )
	{
		int sum = 0;
		for ( int i = 0 ; i < this.inputs.length ; i++ ) {
			sum += this.inputs[ i ].stackSize;
		}
		return sum;
	}


	public boolean hasInput( final ItemStack stack )
	{
		for ( int i = 0 ; i < this.inputs.length ; i++ ) {
			if ( this.inputs[ i ].isItemEqual( stack ) ) {
				return true;
			}
		}
		return false;
	}

}
