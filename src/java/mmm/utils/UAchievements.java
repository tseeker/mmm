package mmm.utils;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.AchievementList;



public class UAchievements
{

	public static void checkCraftingAchievements( final EntityPlayer playerIn , final Item item )
	{
		if ( item == Item.getItemFromBlock( Blocks.CRAFTING_TABLE ) ) {
			playerIn.addStat( AchievementList.BUILD_WORK_BENCH );

		} else if ( item instanceof ItemPickaxe ) {
			playerIn.addStat( AchievementList.BUILD_PICKAXE );
			if ( ( (ItemPickaxe) item ).getToolMaterial( ) != Item.ToolMaterial.WOOD ) {
				playerIn.addStat( AchievementList.BUILD_BETTER_PICKAXE );
			}

		} else if ( item == Item.getItemFromBlock( Blocks.FURNACE ) ) {
			playerIn.addStat( AchievementList.BUILD_FURNACE );

		} else if ( item instanceof ItemHoe ) {
			playerIn.addStat( AchievementList.BUILD_HOE );

		} else if ( item == Items.BREAD ) {
			playerIn.addStat( AchievementList.MAKE_BREAD );

		} else if ( item == Items.CAKE ) {
			playerIn.addStat( AchievementList.BAKE_CAKE );

		} else if ( item instanceof ItemSword ) {
			playerIn.addStat( AchievementList.BUILD_SWORD );

		} else if ( item == Item.getItemFromBlock( Blocks.ENCHANTING_TABLE ) ) {
			playerIn.addStat( AchievementList.ENCHANTMENTS );

		} else if ( item == Item.getItemFromBlock( Blocks.BOOKSHELF ) ) {
			playerIn.addStat( AchievementList.BOOKCASE );
		}
	}

}
