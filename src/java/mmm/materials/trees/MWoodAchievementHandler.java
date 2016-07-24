package mmm.materials.trees;


import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class MWoodAchievementHandler
{

	@SubscribeEvent
	public void onEntityItemPickup( final EntityItemPickupEvent event )
	{
		final Item item = event.getItem( ).getEntityItem( ).getItem( );
		if ( item instanceof ItemBlock && ( (ItemBlock) item ).getBlock( ) instanceof MLog ) {
			event.getEntityPlayer( ).addStat( AchievementList.MINE_WOOD );
		}
	}

}
