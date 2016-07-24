package mmm.tech.tools;


import mmm.core.CRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;



public class TTToolSet
{

	public final ToolMaterial MATERIAL;

	public final ItemSpade SHOVEL;
	public final ItemAxe AXE;
	public final ItemPickaxe PICKAXE;
	public final ItemHoe HOE;

	public final ItemSword SWORD;


	public TTToolSet( final String name , final Item material , final int harvestLevel , final int maxUses ,
			final float efficiency , final float damage , final int enchantability , final float axeDamage ,
			final float axeSpeed )
	{
		this.MATERIAL = EnumHelper
				.addToolMaterial( name.toUpperCase( ) , harvestLevel , maxUses , efficiency , damage , enchantability )//
				.setRepairItem( new ItemStack( material ) );

		CRegistry.addItem( this.SHOVEL = new TTShovel( this.MATERIAL ) );
		CRegistry.addItem( this.AXE = new TTAxe( this.MATERIAL , axeDamage , axeSpeed ) );
		CRegistry.addItem( this.PICKAXE = new TTPickaxe( this.MATERIAL ) );
		CRegistry.addItem( this.HOE = new TTHoe( this.MATERIAL ) );

		CRegistry.addItem( this.SWORD = new TTSword( this.MATERIAL ) );
	}
}
