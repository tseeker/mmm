package mmm.core.api.blocks;


import mmm.core.api.I_RequiresClientInit;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



/** For blocks that need to register a tint */
public interface I_TintedBlock
		extends I_RequiresClientInit
{

	@SideOnly( Side.CLIENT )
	public IBlockColor getBlockTint( );

}
