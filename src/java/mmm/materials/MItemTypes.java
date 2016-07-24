package mmm.materials;


import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;



public class MItemTypes
{
	private static final HashMap< String , MItemType > TYPES;

	public static final MItemType ORE;
	public static final MItemType FUEL;
	public static final MItemType NUGGET;
	public static final MItemType INGOT;
	public static final MItemType METAL;

	static {
		TYPES = new HashMap<>( );
		{
			ORE = new MItemType( "ORE" , ( item , metadata ) -> {
				if ( item == Items.COAL && metadata == 0 ) {
					return true;
				}
				if ( item == Items.DYE && metadata == EnumDyeColor.BLUE.getDyeDamage( ) ) {
					return true;
				}
				if ( item == Items.EMERALD || item == Items.DIAMOND || item == Items.REDSTONE ) {
					return true;
				}
				return item instanceof MItem && ( (MItem) item ).itemType == E_MItemType.ORE;
			} );
		}
		{
			FUEL = new MItemType( "FUEL" ,
					( item , metadata ) -> TileEntityFurnace.isItemFuel( new ItemStack( item , 1 , metadata ) ) );
		}
		{
			NUGGET = new MItemType( "NUGGET" , ( item , metadata ) -> {
				return item == Items.GOLD_NUGGET
						|| item instanceof MItem && ( (MItem) item ).itemType == E_MItemType.NUGGET;
			} );
		}
		{
			INGOT = new MItemType( "INGOT" , ( item , metadata ) -> {
				return item == Items.GOLD_INGOT || item == Items.IRON_INGOT
						|| item instanceof MItem && ( (MItem) item ).itemType == E_MItemType.INGOT;
			} );
		}
		{
			METAL = new MItemType( "METAL" , ( item , metadata ) -> {
				return MItemTypes.NUGGET.matches( item , metadata ) || MItemTypes.INGOT.matches( item , metadata );
			} );
		}
	}


	static void add( final MItemType mType )
	{
		if ( MItemTypes.TYPES.containsKey( mType.name ) ) {
			throw new IllegalArgumentException( "duplicate material type " + mType );
		}
		MItemTypes.TYPES.put( mType.name , mType );
	}


	public static MItemType get( final String type )
	{
		return MItemTypes.TYPES.get( type );
	}

}
