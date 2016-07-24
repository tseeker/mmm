package mmm.materials;


import mmm.core.CRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;



public class MItem
		extends Item
{

	public static Item makeItem( final E_MItemType type , final String name )
	{
		final Item item = new MItem( type , name );
		CRegistry.addItem( item );
		return item;
	}


	public static Item makeFuel( final String name , final int burnTime )
	{
		final Item fuel = MItem.makeItem( E_MItemType.FUEL , name );
		CRegistry.setFuel( fuel , burnTime );
		return fuel;
	}

	public final E_MItemType itemType;
	private Object extraInfo;


	public MItem( final E_MItemType type , final String name )
	{
		this( type , name , null );
	}


	public MItem( final E_MItemType type , final String name , final Object extraInfo )
	{
		super( );
		this.setCreativeTab( CreativeTabs.MATERIALS );
		this.itemType = type;
		this.setExtraInfo( extraInfo );
		CRegistry.setIdentifiers( this , "materials" , type.path , name );
	}


	public void setExtraInfo( final Object extraInfo )
	{
		if ( extraInfo != null && !this.itemType.extraInfoType.isAssignableFrom( extraInfo.getClass( ) ) ) {
			throw new IllegalArgumentException( "invalid extra info type" );
		}
		this.extraInfo = extraInfo;
	}


	public < T > T getExtraInfo( final Class< T > cls )
	{
		return cls.cast( this.extraInfo );
	}
}
