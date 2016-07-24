package mmm.core.api.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;



public interface I_SupportBlock
{

	public static boolean check( final IBlockAccess worldIn , final BlockPos pos )
	{
		return I_SupportBlock.check( worldIn , pos , EnumFacing.DOWN );
	}


	public static boolean check( final IBlockAccess worldIn , final BlockPos pos , final EnumFacing direction )
	{
		final BlockPos checkPos = pos.offset( direction );
		final IBlockState checkedState = worldIn.getBlockState( checkPos );
		if ( checkedState.isSideSolid( worldIn , checkPos , direction.getOpposite( ) ) ) {
			return true;
		}

		final Block checkedBlock = checkedState.getBlock( );
		return checkedBlock == Blocks.GLASS || checkedBlock == Blocks.STAINED_GLASS
				|| checkedBlock instanceof I_SupportBlock;
	}


	public static boolean dropIfUnsupported( final IBlockState state , final World worldIn , final BlockPos pos ,
			final Block block )
	{
		return I_SupportBlock.dropIfUnsupported( state , worldIn , pos , block , EnumFacing.DOWN );
	}


	public static boolean dropIfUnsupported( final IBlockState state , final World worldIn , final BlockPos pos ,
			final Block block , final EnumFacing direction )
	{
		final boolean rv = !I_SupportBlock.check( worldIn , pos , direction );
		if ( rv ) {
			block.dropBlockAsItem( worldIn , pos , state , 0 );
			worldIn.setBlockToAir( pos );
		}
		return rv;
	}
}
