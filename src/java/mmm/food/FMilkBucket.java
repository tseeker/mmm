package mmm.food;


import mmm.core.CRegistry;
import mmm.core.api.items.I_ItemModelProviderBasic;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;



public class FMilkBucket
		extends Item
		implements I_ItemModelProviderBasic
{

	public final FMilkType milkType;


	public FMilkBucket( final FMilkType milkType )
	{
		super( );
		this.milkType = milkType;
		CRegistry.setIdentifiers( this , "food" , "milk" , milkType.name );
		this.setMaxStackSize( 1 );
		this.setCreativeTab( CreativeTabs.MISC );
	}


	@Override
	public ModelResourceLocation getModelResourceLocation( )
	{
		return new ModelResourceLocation( Items.MILK_BUCKET.getRegistryName( ) , "inventory" );
	}

}
