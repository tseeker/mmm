package mmm.utils;


import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;



public class UItemId
{
	private static final ThreadLocal< WeakHashMap< UItemId , WeakReference< UItemId > > > TABLE //
			= new ThreadLocal< WeakHashMap< UItemId , WeakReference< UItemId > > >( ) {
				@Override
				protected WeakHashMap< UItemId , WeakReference< UItemId > > initialValue( )
				{
					return new WeakHashMap<>( );
				}
			};


	public static UItemId fromItemStack( final ItemStack stack )
	{
		return UItemId.create( stack.getItem( ) , stack.getItemDamage( ) );
	}


	public static UItemId create( final Item item )
	{
		return UItemId.create( item , 0 );
	}


	public static UItemId create( final Item item , final int meta )
	{
		final UItemId id = new UItemId( item.getRegistryName( ) , meta );
		final WeakHashMap< UItemId , WeakReference< UItemId > > table = UItemId.TABLE.get( );
		if ( table.containsKey( id ) ) {
			return table.get( id ).get( );
		}
		table.put( id , new WeakReference< UItemId >( id ) );
		return id;
	}

	public final ResourceLocation name;
	public final int meta;


	private UItemId( final ResourceLocation name , final int meta )
	{
		this.name = name;
		this.meta = meta;
	}


	@Override
	public int hashCode( )
	{
		final int prime = 31;
		return prime * ( prime + this.meta ) + ( this.name == null ? 0 : this.name.hashCode( ) );
	}


	@Override
	public boolean equals( final Object obj )
	{
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( this.getClass( ) != obj.getClass( ) ) {
			return false;
		}
		final UItemId other = (UItemId) obj;
		if ( this.meta != other.meta ) {
			return false;
		}
		if ( this.name == null ) {
			if ( other.name != null ) {
				return false;
			}
		} else if ( !this.name.equals( other.name ) ) {
			return false;
		}
		return true;
	}

}
