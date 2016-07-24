package mmm.materials;


import mmm.MmmMaterials;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class MItems
		implements I_RecipeRegistrar
{
	public final Item SLAG;
	public final Item COKE;
	public final Item PIG_IRON_INGOT;
	public final Item MALACHITE;
	public final Item CUPRITE;
	public final Item CASSITERITE;
	public final Item SPHALERITE;
	public final Item ROCK_SALT;
	public final Item SULPHUR_POWDER;
	public final Item SALTPETER;


	public MItems( )
	{
		CRegistry.addRecipeRegistrar( this );

		// Items that do not correspond to metals or ores
		this.SLAG = MItem.makeItem( E_MItemType.MISC , "slag" );
		this.COKE = MItem.makeFuel( "coke" , 9600 );
		this.PIG_IRON_INGOT = MItem.makeItem( E_MItemType.INGOT , "pig_iron" );

		// Ore drops
		this.MALACHITE = MItem.makeItem( E_MItemType.ORE , "malachite" );
		this.CUPRITE = MItem.makeItem( E_MItemType.ORE , "cuprite" );
		this.CASSITERITE = MItem.makeItem( E_MItemType.ORE , "cassiterite" );
		this.SPHALERITE = MItem.makeItem( E_MItemType.ORE , "sphalerite" );
		this.ROCK_SALT = MItem.makeItem( E_MItemType.ORE , "rock_salt" );
		this.SULPHUR_POWDER = MItem.makeItem( E_MItemType.ORE , "sulphur_powder" );
		this.SALTPETER = MItem.makeItem( E_MItemType.ORE , "saltpeter_powder" );
	}


	@Override
	public void registerRecipes( )
	{
		// Green dye from malachite
		GameRegistry.addShapelessRecipe( new ItemStack( Items.DYE , 1 , 2 ) ,
				new ItemStack( MmmMaterials.ITEM.MALACHITE ) );

		// Gunpowder from saltpeter, sulphur and charcoal
		GameRegistry.addShapelessRecipe( new ItemStack( Items.GUNPOWDER ) , //
				new ItemStack( MmmMaterials.ITEM.SALTPETER ) , //
				new ItemStack( MmmMaterials.ITEM.SULPHUR_POWDER ) , //
				new ItemStack( Items.COAL , 1 , 1 ) );
	}

}
