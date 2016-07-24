package mmm.food;


import java.util.Set;

import io.netty.buffer.ByteBuf;
import mmm.Mmm;
import mmm.MmmFood;
import mmm.core.CNetwork;
import mmm.core.api.I_Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class FMilkable
{

	@CapabilityInject( FMilkable.class )
	public static final Capability< FMilkable > CAPABILITY = null;

	public static final ResourceLocation ID = new ResourceLocation( Mmm.ID , "Milkable" );

	// *************************************************************************************************************

	public static class Storage
			implements Capability.IStorage< FMilkable >
	{

		@Override
		public NBTBase writeNBT( final Capability< FMilkable > capability , final FMilkable instance ,
				final EnumFacing side )
		{
			return new NBTTagLong( instance.lastMilking );
		}


		@Override
		public void readNBT( final Capability< FMilkable > capability , final FMilkable instance ,
				final EnumFacing side , final NBTBase nbt )
		{
			instance.lastMilking = ( (NBTTagLong) nbt ).getLong( );
		}

	}

	// *************************************************************************************************************

	public static class Provider
			implements ICapabilitySerializable< NBTBase >
	{

		private final FMilkable instance = FMilkable.CAPABILITY.getDefaultInstance( );


		@Override
		public boolean hasCapability( final Capability< ? > capability , final EnumFacing facing )
		{
			return capability == FMilkable.CAPABILITY;
		}


		@Override
		public < T > T getCapability( final Capability< T > capability , final EnumFacing facing )
		{
			return capability == FMilkable.CAPABILITY ? FMilkable.CAPABILITY.cast( this.instance ) : null;
		}


		@Override
		public NBTBase serializeNBT( )
		{
			return FMilkable.CAPABILITY.writeNBT( this.instance , null );
		}


		@Override
		public void deserializeNBT( final NBTBase nbt )
		{
			FMilkable.CAPABILITY.readNBT( this.instance , null , nbt );
		}

	}

	// *************************************************************************************************************

	public static class Message
			implements I_Message
	{
		private int entityId;
		private long lastMilking;


		public Message( )
		{
			// EMPTY
		}


		public Message( final Entity entity )
		{
			this.entityId = entity.getEntityId( );
			this.lastMilking = entity.getCapability( FMilkable.CAPABILITY , null ).getLastMilking( );
		}


		@Override
		public void fromBytes( final ByteBuf buf )
		{
			this.entityId = buf.readInt( );
			this.lastMilking = buf.readLong( );
		}


		@Override
		public void toBytes( final ByteBuf buf )
		{
			buf.writeInt( this.entityId );
			buf.writeLong( this.lastMilking );
		}


		@Override
		@SideOnly( Side.CLIENT )
		public void handleOnClient( final EntityPlayerSP player )
		{
			final Entity entity = player.getEntityWorld( ).getEntityByID( this.entityId );
			if ( entity == null || !entity.hasCapability( FMilkable.CAPABILITY , null ) ) {
				return;
			}
			entity.getCapability( FMilkable.CAPABILITY , null ).lastMilking = this.lastMilking;
		}

	}

	// *************************************************************************************************************

	public static class EventHandler
	{
		@SubscribeEvent
		public void attachCapability( final AttachCapabilitiesEvent.Entity event )
		{
			if ( MmmFood.MILK.TYPES.containsKey( event.getEntity( ).getClass( ) ) ) {
				event.addCapability( FMilkable.ID , new Provider( ) );
			}
		}


		@SubscribeEvent
		public void startTracking( final PlayerEvent.StartTracking event )
		{
			final EntityPlayer player = event.getEntityPlayer( );
			if ( player.getEntityWorld( ).isRemote ) {
				return;
			}
			final Entity entity = event.getTarget( );
			if ( entity.hasCapability( FMilkable.CAPABILITY , null ) ) {
				CNetwork.sendTo( new Message( entity ) , (EntityPlayerMP) player );
			}
		}


		@SubscribeEvent
		public void handleMilking( final PlayerInteractEvent.EntityInteract event )
		{
			// We must be holding a bucket
			final ItemStack heldStack = event.getItemStack( );
			if ( heldStack == null || heldStack.getItem( ) != Items.BUCKET ) {
				return;
			}

			// Are we targeting a potentially milk-producing animal?
			final Entity target = event.getTarget( );
			if ( !target.hasCapability( FMilkable.CAPABILITY , null ) ) {
				return;
			}

			// We can't milk baby animals
			final EntityAnimal animal = (EntityAnimal) target;
			if ( animal.isChild( ) ) {
				return;
			}

			// Find actual milk type
			final Set< FMilkType > milkTypes = MmmFood.MILK.TYPES.get( animal.getClass( ) );
			FMilkType milkType = null;
			for ( final FMilkType type : milkTypes ) {
				if ( type.check( animal ) ) {
					milkType = type;
					break;
				}
			}
			if ( milkType == null ) {
				return;
			}

			// So yeah, we're definitely trying to milk something. Don't process any further, even
			// if milking fails.
			event.setCanceled( true );

			// Make sure it's been long enough since the animal's last milking
			final EntityPlayer player = event.getEntityPlayer( );
			final FMilkable milkable = animal.getCapability( FMilkable.CAPABILITY , null );
			final long curTime = player.worldObj.getTotalWorldTime( );
			if ( curTime - milkable.lastMilking < milkType.getPeriod( ) ) {
				return;
			}
			milkable.lastMilking = curTime;

			// Send updates to players
			if ( !player.worldObj.isRemote ) {
				CNetwork.sendToAll( new Message( animal ) );
			}

			// Actually milk the animal
			if ( !milkType.isVanilla || player.capabilities.isCreativeMode ) {
				final ItemStack bucket = new ItemStack( milkType.bucket );
				if ( --heldStack.stackSize == 0 ) {
					player.setHeldItem( event.getHand( ) , bucket );
				} else if ( !player.inventory.addItemStackToInventory( bucket ) ) {
					player.dropItem( bucket , false );
				}
				player.playSound( SoundEvents.ENTITY_COW_MILK , 1.0F , 1.0F );
			}
		}
	}

	// *************************************************************************************************************

	private long lastMilking = -500000;


	public long getLastMilking( )
	{
		return this.lastMilking;
	}


	public void setLastMilking( final long lastMilking )
	{
		this.lastMilking = lastMilking;
	}
}
