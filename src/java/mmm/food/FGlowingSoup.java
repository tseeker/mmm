package mmm.food;


import mmm.MmmPlants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;



public class FGlowingSoup
		extends FFoodInContainer
{

	public FGlowingSoup( )
	{
		super( "soup" , "glowing" , 4 , 0.6f , Items.BOWL , 1 , //
				"CCC" , "CPC" , " B " , //
				'C' , new ItemStack( MmmPlants.NETHER_CORAL.ITEM , 1 , OreDictionary.WILDCARD_VALUE ) , //
				'P' , Items.POTATO );
	}


	@Override
	protected void onFoodEaten( final ItemStack stack , final World worldIn , final EntityPlayer player )
	{
		super.onFoodEaten( stack , worldIn , player );
		if ( !worldIn.isRemote && worldIn.rand.nextFloat( ) < .25f ) {
			final int duration = 300 + worldIn.rand.nextInt( 1200 );
			player.addPotionEffect( new PotionEffect( MobEffects.GLOWING , duration ) );
			player.addPotionEffect( new PotionEffect( MobEffects.NIGHT_VISION , duration ) );
		}
	}

}
