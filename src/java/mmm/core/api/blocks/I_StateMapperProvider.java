package mmm.core.api.blocks;


import mmm.core.api.I_RequiresClientPreInit;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public interface I_StateMapperProvider
		extends I_RequiresClientPreInit
{

	@SideOnly( Side.CLIENT )
	IStateMapper getStateMapper( );

}
