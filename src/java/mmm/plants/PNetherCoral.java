package mmm.plants;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import mmm.core.CRegistry;
import mmm.core.api.I_FloraRegistrar;
import mmm.core.api.world.I_FloraParameters;
import mmm.utils.UBlockItemWithVariants;
import mmm.world.WLocation;
import mmm.world.gen.WGFloraParameters;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IShearable;



public class PNetherCoral
		implements I_FloraRegistrar
{

	public static enum E_Color
			implements IStringSerializable {

		YELLOW ,
		ORANGE ,
		BLUE ,
		RED;

		private static final E_Color[] VALUES = E_Color.values( );


		public static E_Color fromMetadata( final int meta )
		{
			return E_Color.VALUES[ meta ];
		}


		public static String[] getNames( )
		{
			final String[] names = new String[ E_Color.VALUES.length ];
			for ( int i = 0 ; i < E_Color.VALUES.length ; i++ ) {
				names[ i ] = E_Color.VALUES[ i ].getName( );
			}
			return names;
		}


		@Override
		public String getName( )
		{
			return this.toString( ).toLowerCase( );
		}


		public int toMetadata( )
		{
			return this.ordinal( );
		}
	}

	private static final PropertyEnum< E_Color > COLOR = PropertyEnum.create( "color" , E_Color.class );

	public class Plant
			extends BlockBush
			implements IShearable
	{

		public Plant( )
		{
			CRegistry.setIdentifiers( this , "plant" , "block" , "nether_coral" );

			this.setTickRandomly( true );
			this.setCreativeTab( CreativeTabs.DECORATIONS );
			this.setHardness( 0f );
			this.setSoundType( SoundType.PLANT );
			this.lightValue = 13;
			this.disableStats( );

			this.setDefaultState( this.blockState.getBaseState( ) //
					.withProperty( PNetherCoral.COLOR , E_Color.YELLOW ) );
		}


		@Override
		public AxisAlignedBB getBoundingBox( final IBlockState state , final IBlockAccess source , final BlockPos pos )
		{
			return Block.FULL_BLOCK_AABB;
		}


		@Override
		protected BlockStateContainer createBlockState( )
		{
			return new BlockStateContainer( this , PNetherCoral.COLOR );
		}


		@Override
		public IBlockState getStateFromMeta( final int meta )
		{
			return this.getDefaultState( ).withProperty( PNetherCoral.COLOR , E_Color.fromMetadata( meta ) );
		}


		@Override
		public int getMetaFromState( final IBlockState state )
		{
			return state.getValue( PNetherCoral.COLOR ).toMetadata( );
		}


		@Override
		protected boolean canSustainBush( final IBlockState state )
		{
			return state.getBlock( ) == Blocks.NETHERRACK || state.getBlock( ) == Blocks.SOUL_SAND;
		}


		@Override
		public EnumPlantType getPlantType( final IBlockAccess world , final BlockPos pos )
		{
			return EnumPlantType.Nether;
		}


		@Override
		public void getSubBlocks( final Item itemIn , final CreativeTabs tab , final List< ItemStack > list )
		{
			for ( final E_Color color : E_Color.VALUES ) {
				list.add( new ItemStack( itemIn , 1 , color.toMetadata( ) ) );
			}
		}


		@Override
		public void onBlockHarvested( final World worldIn , final BlockPos pos , final IBlockState state ,
				final EntityPlayer player )
		{
			if ( !player.isCreative( ) ) {
				final ItemStack heldItemMainhand = player.getHeldItemMainhand( );
				if ( heldItemMainhand == null || heldItemMainhand.getItem( ) != Items.SHEARS ) {
					player.setFire( 5 );
				}
			}
			super.onBlockHarvested( worldIn , pos , state , player );
		}


		@Override
		public boolean isShearable( final ItemStack item , final IBlockAccess world , final BlockPos pos )
		{
			return true;
		}


		@Override
		public List< ItemStack > onSheared( final ItemStack item , final IBlockAccess world , final BlockPos pos ,
				final int fortune )
		{
			final List< ItemStack > ret = new ArrayList< ItemStack >( );
			ret.add( new ItemStack( PNetherCoral.this.ITEM , 1 ,
					world.getBlockState( pos ).getValue( PNetherCoral.COLOR ).toMetadata( ) ) );
			return ret;
		}


		@Override
		public List< ItemStack > getDrops( final IBlockAccess world , final BlockPos pos , final IBlockState state ,
				final int fortune )
		{
			return Collections.emptyList( );
		}


		@Override
		public ItemStack getPickBlock( final IBlockState state , final RayTraceResult target , final World world ,
				final BlockPos pos , final EntityPlayer player )
		{
			return new ItemStack( PNetherCoral.this.ITEM , 1 , state.getValue( PNetherCoral.COLOR ).toMetadata( ) );
		}


		@Override
		public void onEntityCollidedWithBlock( final World worldIn , final BlockPos pos , final IBlockState state ,
				final Entity entityIn )
		{
			final Random rand = worldIn.rand == null ? Block.RANDOM : worldIn.rand;

			if ( entityIn instanceof EntityItem ) {
				final ItemStack stack = ( (EntityItem) entityIn ).getEntityItem( );
				final Item item = stack.getItem( );
				if ( item instanceof ItemFood && ( (ItemFood) item ).isWolfsFavoriteMeat( ) ) {
					entityIn.setDead( );
					this.doSmokeParticles( worldIn , pos , rand );
					this.doFlameParticles( worldIn , pos , rand );
					if ( !worldIn.isRemote ) {
						this.tryGrow( worldIn , pos , state );
					}
				}
			} else if ( entityIn instanceof EntityLivingBase && worldIn.rand.nextFloat( ) < .01f ) {
				this.doFlameParticles( worldIn , pos , rand );
				if ( !entityIn.isImmuneToFire( ) ) {
					entityIn.attackEntityFrom( PNetherCoral.this.DAMAGE , 1f );
				}
			}
		}


		private void tryGrow( final World worldIn , final BlockPos pos , final IBlockState state )
		{
			final ArrayList< BlockPos > positions = new ArrayList<>( 26 );
			int nAround = -1;
			for ( int i = -1 ; i <= 1 ; i++ ) {
				for ( int j = -1 ; j <= 1 ; j++ ) {
					for ( int k = -1 ; k <= 1 ; k++ ) {
						final BlockPos nPos = pos.add( i , j , k );
						if ( worldIn.isAirBlock( nPos ) && this.canBlockStay( worldIn , nPos , state ) ) {
							positions.add( nPos );
						} else if ( worldIn.getBlockState( nPos ).getBlock( ) == this ) {
							nAround++;
						}
					}
				}
			}

			final float limit = .5f + nAround / 8f;
			if ( positions.isEmpty( ) || worldIn.rand.nextFloat( ) < limit ) {
				return;
			}
			final BlockPos growPos = positions.get( worldIn.rand.nextInt( positions.size( ) ) );
			worldIn.setBlockState( growPos , state , 3 );
		}


		private void doSmokeParticles( final World world , final BlockPos pos , final Random rand )
		{
			world.spawnParticle( EnumParticleTypes.SMOKE_LARGE , pos.getX( ) + .5 , pos.getY( ) + .5 ,
					pos.getZ( ) + .5 , 0 , .1 + rand.nextFloat( ) * .2 , 0 );
			for ( int i = 0 ; i < 8 ; i++ ) {
				world.spawnParticle( EnumParticleTypes.SMOKE_NORMAL , //
						pos.getX( ) + .1 + rand.nextFloat( ) * .8 , //
						pos.getY( ) + .4 + rand.nextFloat( ) * .2 , //
						pos.getZ( ) + .1 + rand.nextFloat( ) * .8 , //
						0 , .1 + rand.nextFloat( ) * .2 , 0 );
			}
		}


		private void doFlameParticles( final World world , final BlockPos pos , final Random rand )
		{
			for ( int i = 0 ; i < 50 ; i++ ) {
				world.spawnParticle( EnumParticleTypes.FLAME , //
						pos.getX( ) + rand.nextFloat( ) , //
						pos.getY( ) + .4 + rand.nextFloat( ) * .2 , //
						pos.getZ( ) + rand.nextFloat( ) , //
						0 , 0 , 0 );
			}
		}

	}

	public final DamageSource DAMAGE = new DamageSource( "nether_coral" ).setFireDamage( ).setDamageBypassesArmor( );
	public final Plant PLANT;
	public final UBlockItemWithVariants ITEM;


	public PNetherCoral( )
	{
		CRegistry.addRegistrar( this );
		this.PLANT = new Plant( );
		this.ITEM = new UBlockItemWithVariants( this.PLANT , "plant" , "block" , "nether_coral" ) //
				.setVariants( E_Color.getNames( ) ) //
				.register( );
		CRegistry.addBlock( this.PLANT , this.ITEM );
	}


	@Override
	public void getFloraGeneration( final List< I_FloraParameters > output )
	{
		output.add( new WGFloraParameters( this.getPlant( E_Color.YELLOW ) , //
				0.2f , WLocation.inTheNether( ) ).setSuccessfulPlacements( 16 ) );
		output.add( new WGFloraParameters( this.getPlant( E_Color.RED ) , //
				0.2f , WLocation.inTheNether( ) ).setSuccessfulPlacements( 16 ) );
		output.add( new WGFloraParameters( this.getPlant( E_Color.ORANGE ) , //
				0.1f , WLocation.inTheNether( ) ).setSuccessfulPlacements( 8 ) );
		output.add( new WGFloraParameters( this.getPlant( E_Color.BLUE ) , //
				0.05f , WLocation.inTheNether( ) ).setSuccessfulPlacements( 4 ) );
	}


	private IBlockState getPlant( final E_Color color )
	{
		return this.PLANT.getDefaultState( ).withProperty( PNetherCoral.COLOR , color );
	}
}
