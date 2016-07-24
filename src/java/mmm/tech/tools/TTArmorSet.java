package mmm.tech.tools;


import mmm.core.CRegistry;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;



public class TTArmorSet
{

	public final ArmorMaterial ARMOR_MATERIAL;

	public final TTArmor HELMET;
	public final TTArmor CHESTPLATE;
	public final TTArmor LEGGINGS;
	public final TTArmor BOOTS;


	public TTArmorSet( final String name , final Item material , final int durability , final int[] reductionAmounts ,
			final int enchantability , final SoundEvent soundOnEquip , final float toughness )
	{
		this.ARMOR_MATERIAL = EnumHelper.addArmorMaterial( name.toUpperCase( ) , "mmm:" + name , durability ,
				reductionAmounts , enchantability , soundOnEquip , toughness );
		this.ARMOR_MATERIAL.customCraftingMaterial = material;

		CRegistry.addItem( this.HELMET = new TTArmor( this.ARMOR_MATERIAL , EntityEquipmentSlot.HEAD ) );
		CRegistry.addItem( this.CHESTPLATE = new TTArmor( this.ARMOR_MATERIAL , EntityEquipmentSlot.CHEST ) );
		CRegistry.addItem( this.LEGGINGS = new TTArmor( this.ARMOR_MATERIAL , EntityEquipmentSlot.LEGS ) );
		CRegistry.addItem( this.BOOTS = new TTArmor( this.ARMOR_MATERIAL , EntityEquipmentSlot.FEET ) );
	}
}
