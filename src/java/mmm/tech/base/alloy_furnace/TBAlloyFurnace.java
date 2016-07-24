package mmm.tech.base.alloy_furnace;


import mmm.core.CGui;
import mmm.core.CNetwork;
import mmm.core.CRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class TBAlloyFurnace
{
	public final CreativeTabs CREATIVE_TAB = CreativeTabs.DECORATIONS;

	public final TBAFBlock INACTIVE;
	public final TBAFBlock ACTIVE;
	public final Item ITEM;


	public TBAlloyFurnace( )
	{
		this.INACTIVE = new TBAFBlock( false );
		this.INACTIVE.setCreativeTab( this.CREATIVE_TAB );

		this.ACTIVE = new TBAFBlock( true );
		this.ACTIVE.setCreativeTab( this.CREATIVE_TAB );

		this.ITEM = new ItemBlock( this.INACTIVE )//
				.setMaxStackSize( 16 )//
				.setCreativeTab( this.CREATIVE_TAB );
		CRegistry.setIdentifiers( this.ITEM , "tech" , "base" , "alloy_furnace" );

		CRegistry.addBlock( this.INACTIVE , this.ITEM );
		CRegistry.addBlock( this.ACTIVE , null );

		GameRegistry.registerTileEntity( TBAFTileEntity.class , "mmm:tech/base/alloy_furnace" );
		CGui.registerTileEntityGUI( TBAFTileEntity.class , //
				"mmm.tech.base.alloy_furnace.TBAFContainer" , //
				"mmm.tech.base.alloy_furnace.TBAFGui" );
		CNetwork.addServerMessage( TBAFMessage.class );
	}

}
