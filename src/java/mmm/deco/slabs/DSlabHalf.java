package mmm.deco.slabs;


import net.minecraft.block.state.IBlockState;



public class DSlabHalf
		extends A_DSlabBlock
{

	public DSlabHalf( final IBlockState modelState , final String name )
	{
		super( modelState , name );
	}


	@Override
	public boolean isDouble( )
	{
		return false;
	}

}