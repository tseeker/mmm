package mmm.tech.tools;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class TTArmor
		extends ItemArmor
		implements I_RecipeRegistrar
{

	public TTArmor( final ArmorMaterial materialIn , final EntityEquipmentSlot equipmentSlotIn )
	{
		super( materialIn , materialIn.ordinal( ) , equipmentSlotIn );
		CRegistry.setIdentifiers( this , "tech" , "tools" , materialIn.toString( ).toLowerCase( ) , "armor" ,
				equipmentSlotIn.getName( ) );
	}


	@Override
	public void registerRecipes( )
	{
		String l0 , l1 , l2;

		switch ( this.armorType ) {

			case CHEST:
				l0 = "I I";
				l1 = "III";
				l2 = "III";
				break;
			case FEET:
				l0 = "I I";
				l1 = "I I";
				l2 = null;
				break;
			case HEAD:
				l0 = "III";
				l1 = "I I";
				l2 = null;
				break;
			case LEGS:
				l0 = "III";
				l1 = "I I";
				l2 = "I I";
				break;

			default:
				throw new IllegalArgumentException( "unsupported armor slot '" + this.getEquipmentSlot( ) + "'" );

		}

		final ItemStack self = new ItemStack( this );
		final ItemStack ingredient = new ItemStack( this.getArmorMaterial( ).getRepairItem( ) );
		if ( l2 == null ) {
			GameRegistry.addShapedRecipe( self , l0 , l1 , 'I' , ingredient );
		} else {
			GameRegistry.addShapedRecipe( self , l0 , l1 , l2 , 'I' , ingredient );
		}
	}

}
