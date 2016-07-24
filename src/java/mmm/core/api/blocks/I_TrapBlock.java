package mmm.core.api.blocks;


import net.minecraft.block.state.IBlockState;



public interface I_TrapBlock
{

	public String getTrapType( );


	public IBlockState[] getReplacedBlocks( );

}
