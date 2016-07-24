package mmm.materials;


import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;



public class MBlockType
{

	public final String name;
	private final Predicate< IBlockState > check;


	public MBlockType( final String name , final Predicate< IBlockState > check )
	{
		this.name = name.intern( );
		this.check = check;
		MBlockTypes.add( this );
	}


	public boolean matches( final IBlockState blockState )
	{
		return this.check.test( blockState );
	}


	@Override
	public String toString( )
	{
		return "MBlockType{" + this.name + "}";
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
