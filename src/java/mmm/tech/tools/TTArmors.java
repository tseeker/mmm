package mmm.tech.tools;


import mmm.MmmMaterials;
import net.minecraft.init.SoundEvents;



public class TTArmors
{
	public final TTArmorSet COPPER;
	public final TTArmorSet BRONZE;
	public final TTArmorSet STEEL;


	public TTArmors( )
	{
		this.COPPER = new TTArmorSet( "copper" , MmmMaterials.METAL.COPPER.INGOT , //
				10 , new int[] {
						1 , 3 , 4 , 1
				} , 15 , SoundEvents.ITEM_ARMOR_EQUIP_GENERIC , 0 );
		this.BRONZE = new TTArmorSet( "bronze" , MmmMaterials.ALLOY.BRONZE.INGOT , //
				13 , new int[] {
						1 , 4 , 5 , 2
				} , 20 , SoundEvents.ITEM_ARMOR_EQUIP_GENERIC , 0 );
		this.STEEL = new TTArmorSet( "steel" , MmmMaterials.ALLOY.STEEL.INGOT , //
				22 , new int[] {
						2 , 6 , 7 , 3
				} , 9 , SoundEvents.ITEM_ARMOR_EQUIP_GENERIC , 1 );
	}

}
