package mmm.food;


import com.google.common.collect.HashMultimap;

import mmm.core.CNetwork;
import mmm.food.FMilkable.EventHandler;
import mmm.food.FMilkable.Message;
import mmm.food.FMilkable.Storage;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.HorseType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;



public class FMilks
{
	public final HashMultimap< Class< ? extends EntityAnimal > , FMilkType > TYPES;

	public final FMilkType COW;
	public final FMilkType SHEEP;
	public final FMilkType PIG;
	public final FMilkType HORSE;
	public final FMilkType DONKEY;


	public FMilks( )
	{
		this.TYPES = HashMultimap.create( );

		this.COW = this.addMilk( new FMilkType( ) );
		this.SHEEP = this.addMilk( new FMilkType( "sheep" , EntitySheep.class ) );
		this.PIG = this.addMilk( new FMilkType( "pig" , EntityPig.class ) //
				.setPeriod( 48000 ) );
		this.HORSE = this.addMilk( new FMilkType( "horse" , EntityHorse.class ) //
				.setExtraCheck( a -> ( (EntityHorse) a ).getType( ) == HorseType.HORSE ) //
				.setPeriod( 24000 ) );
		this.DONKEY = this.addMilk( new FMilkType( "donkey" , EntityHorse.class )
				.setExtraCheck( a -> ( (EntityHorse) a ).getType( ) == HorseType.DONKEY ) //
				.setPeriod( 36000 ) );

		CapabilityManager.INSTANCE.register( FMilkable.class , new Storage( ) , FMilkable::new );
		MinecraftForge.EVENT_BUS.register( new EventHandler( ) );
		CNetwork.addClientMessage( Message.class );
	}


	private FMilkType addMilk( final FMilkType type )
	{
		this.TYPES.put( type.animal , type );
		return type;
	}
}
