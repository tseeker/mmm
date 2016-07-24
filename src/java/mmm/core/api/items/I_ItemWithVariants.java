package mmm.core.api.items;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public interface I_ItemWithVariants
		extends I_ItemModelProvider
{

	public int getVariantsCount( );


	@SideOnly( Side.CLIENT )
	public ModelResourceLocation getModelResourceLocation( int variant );

}
