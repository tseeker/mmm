package mmm.core.api.items;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public interface I_ItemModelProviderBasic
		extends I_ItemModelProvider
{

	@SideOnly( Side.CLIENT )
	public ModelResourceLocation getModelResourceLocation( );

}
