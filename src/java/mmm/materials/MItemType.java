package mmm.materials;


import net.minecraft.item.Item;



public class MItemType
{
	public static interface I_ItemCheck
	{
		public boolean test( Item item , int meta );
	}

	public final String name;
	private final I_ItemCheck check;


	public MItemType( final String name , final I_ItemCheck check )
	{
		this.name = name.intern( );
		this.check = check;
		MItemTypes.add( this );
	}


	public boolean matches( final Item item , final int meta )
	{
		return this.check.test( item , meta );
	}


	@Override
	public String toString( )
	{
		return "MItemType{" + this.name + "}";
	}


	@Override
	public int hashCode( )
	{
		return this.name.hashCode( );
	}


	@Override
	public boolean equals( final Object obj )
	{
		return this == obj;
	}

}
