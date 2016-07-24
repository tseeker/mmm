package mmm.deco.thrones;


import mmm.core.CRegistry;
import mmm.materials.MWood;
import mmm.utils.UBlockItemWithVariants;
import net.minecraft.item.Item;



public class DThrone
{

	public final DThroneBlock TOP;
	public final DThroneBlock BOTTOM;
	public final Item ITEM;


	public DThrone( final MWood woodType )
	{
		CRegistry.addBlock( this.BOTTOM = new DThroneBlock( this , woodType , false ) , null );
		CRegistry.addBlock( this.TOP = new DThroneBlock( this , woodType , true ) , null );
		this.ITEM = new UBlockItemWithVariants( this.BOTTOM , "deco" , "throne" , woodType.getSuffix( ) ) //
				.useColorVariants( ) //
				.register( );
	}

}
