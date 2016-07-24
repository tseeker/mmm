package mmm.plants;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import mmm.core.CRegistry;
import mmm.core.api.I_FloraRegistrar;
import mmm.core.api.I_RecipeRegistrar;
import mmm.core.api.blocks.I_StateMapperProvider;
import mmm.core.api.world.I_FloraParameters;
import mmm.utils.UMaths;
import mmm.world.WLocation;
import mmm.world.WLocationRainfall;
import mmm.world.WLocationTemperature;
import mmm.world.gen.WGFloraParameters;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class PTomato
		implements I_RecipeRegistrar , I_FloraRegistrar
{
	public static final PropertyInteger AGE = PropertyInteger.create( "age" , 0 , 15 );
	public static final PropertyInteger SIZE = PropertyInteger.create( "size" , 0 , 3 );
	public static final PropertyBool HIGHEST = PropertyBool.create( "highest" );
	public static final PropertyBool WITH_FRUITS = PropertyBool.create( "fruits" );
	private static final AxisAlignedBB[] PLANT_AABB = new AxisAlignedBB[] {
			UMaths.makeBlockAABB( 0 , 0 , 0 , 16 , 4 , 16 ) , //
			UMaths.makeBlockAABB( 0 , 0 , 0 , 16 , 8 , 16 ) , //
			UMaths.makeBlockAABB( 0 , 0 , 0 , 16 , 12 , 16 ) , //
			Block.FULL_BLOCK_AABB
	};

	public class WildPlant
			extends BlockBush
			implements IGrowable , IShearable
	{
		public WildPlant( )
		{
			CRegistry.setIdentifiers( this , "plant" , "block" , "wild_tomato" );

			this.setTickRandomly( true );
			this.setCreativeTab( (CreativeTabs) null );
			this.setHardness( 0f );
			this.setSoundType( SoundType.PLANT );
			this.disableStats( );

			this.setDefaultState( this.blockState.getBaseState( ) //
					.withProperty( PTomato.WITH_FRUITS , false ) );
		}


		@Override
		public AxisAlignedBB getBoundingBox( final IBlockState state , final IBlockAccess source , final BlockPos pos )
		{
			return PTomato.PLANT_AABB[ 1 ];
		}


		@Override
		protected BlockStateContainer createBlockState( )
		{
			return new BlockStateContainer( this , PTomato.WITH_FRUITS );
		}


		@Override
		public IBlockState getStateFromMeta( final int meta )
		{
			return this.getDefaultState( ).withProperty( PTomato.WITH_FRUITS , meta == 1 );
		}


		@Override
		public int getMetaFromState( final IBlockState state )
		{
			return state.getValue( PTomato.WITH_FRUITS ) ? 1 : 0;
		}


		@Override
		public boolean isShearable( final ItemStack item , final IBlockAccess world , final BlockPos pos )
		{
			return !world.getBlockState( pos ).getValue( PTomato.WITH_FRUITS );
		}


		@Override
		public List< ItemStack > onSheared( final ItemStack item , final IBlockAccess world , final BlockPos pos ,
				final int fortune )
		{
			final List< ItemStack > ret = new ArrayList< ItemStack >( );
			ret.add( new ItemStack( PTomato.this.WILD ) );
			return ret;
		}


		@Override
		public boolean canGrow( final World worldIn , final BlockPos pos , final IBlockState state ,
				final boolean isClient )
		{
			return !state.getValue( PTomato.WITH_FRUITS );
		}


		@Override
		public boolean canUseBonemeal( final World worldIn , final Random rand , final BlockPos pos ,
				final IBlockState state )
		{
			return true;
		}


		@Override
		public void grow( final World worldIn , final Random rand , final BlockPos pos , final IBlockState state )
		{
			worldIn.setBlockState( pos , state.withProperty( PTomato.WITH_FRUITS , true ) );
		}


		@Override
		public void updateTick( final World worldIn , final BlockPos pos , final IBlockState state , final Random rand )
		{
			super.updateTick( worldIn , pos , state , rand );
			if ( state.getValue( PTomato.WITH_FRUITS ) || worldIn.getLightFromNeighbors( pos.up( ) ) < 10
					|| rand.nextInt( 500 ) != 0 ) {
				return;
			}
			this.grow( worldIn , rand , pos , state );
		}


		@Override
		public List< ItemStack > getDrops( final IBlockAccess world , final BlockPos pos , final IBlockState state ,
				final int fortune )
		{
			final List< ItemStack > ret = new ArrayList<>( 1 );
			if ( state.getValue( PTomato.WITH_FRUITS ) ) {
				ret.add( new ItemStack( PTomato.this.FRUIT ) );
			}
			return ret;
		}
	}

	public class Plant
			extends BlockBush
			implements IGrowable , I_StateMapperProvider
	{

		public Plant( )
		{
			CRegistry.setIdentifiers( this , "plant" , "block" , "tomato" );

			this.setTickRandomly( true );
			this.setCreativeTab( (CreativeTabs) null );
			this.setHardness( 0f );
			this.setSoundType( SoundType.PLANT );
			this.disableStats( );

			this.setDefaultState( this.blockState.getBaseState( ) //
					.withProperty( PTomato.AGE , 0 ) //
					.withProperty( PTomato.SIZE , 0 ) //
					.withProperty( PTomato.HIGHEST , true ) //
					.withProperty( PTomato.WITH_FRUITS , false ) );
		}


		@Override
		protected BlockStateContainer createBlockState( )
		{
			return new BlockStateContainer( this , PTomato.AGE , PTomato.SIZE , PTomato.HIGHEST , PTomato.WITH_FRUITS );
		}


		@Override
		public IBlockState getStateFromMeta( final int meta )
		{
			return this.withAge( meta );
		}


		@Override
		public int getMetaFromState( final IBlockState state )
		{
			return this.getAge( state );
		}


		@Override
		@SideOnly( Side.CLIENT )
		public IStateMapper getStateMapper( )
		{
			return new StateMap.Builder( ).ignore( PTomato.AGE ).build( );
		}


		@Override
		public IBlockState getActualState( final IBlockState state , final IBlockAccess worldIn , final BlockPos pos )
		{
			final int age = this.getAge( state );
			final int size = ( age & 7 ) >> 1;
			final boolean highest = age < 7 || worldIn.getBlockState( pos.up( ) ).getBlock( ) != this;
			final boolean withFruits = ( age & 8 ) != 0;
			return state.withProperty( PTomato.SIZE , size ) //
					.withProperty( PTomato.HIGHEST , highest ) //
					.withProperty( PTomato.WITH_FRUITS , withFruits );
		}


		@Override
		public AxisAlignedBB getBoundingBox( final IBlockState state , final IBlockAccess source , final BlockPos pos )
		{
			return PTomato.PLANT_AABB[ Math.min( this.getAge( state ) >> 1 , 3 ) ];
		}


		public int getAge( final IBlockState state )
		{
			return state.getValue( PTomato.AGE );
		}


		public IBlockState withAge( final int age )
		{
			return this.getDefaultState( ).withProperty( PTomato.AGE , age );
		}


		public boolean isHighestBlock( final World world , final BlockPos pos )
		{
			return world.getBlockState( pos.offset( EnumFacing.DOWN , 2 ) ).getBlock( ) == this;
		}


		@Override
		protected boolean canSustainBush( final IBlockState state )
		{
			return state.getBlock( ) == Blocks.FARMLAND || state.getBlock( ) == this;
		}


		@Override
		public void updateTick( final World worldIn , final BlockPos pos , final IBlockState state , final Random rand )
		{
			super.updateTick( worldIn , pos , state , rand );

			if ( worldIn.getLightFromNeighbors( pos.up( ) ) < 10 ) {
				return;
			}

			if ( this.getAge( state ) < 15 ) {
				final float growthChance = PPlantsHelper.getGrowthChance( this , worldIn , pos );
				if ( rand.nextInt( (int) ( 25.0F / growthChance ) + 1 ) == 0 ) {
					this.grow( worldIn , pos , state , 1 );
				}
			}
		}


		@Override
		public void breakBlock( final World worldIn , final BlockPos pos , final IBlockState state )
		{
			super.breakBlock( worldIn , pos , state );
			final BlockPos down = pos.down( );
			final IBlockState downState = worldIn.getBlockState( down );
			if ( downState.getBlock( ) == this ) {
				this.dropBlockAsItem( worldIn , down , downState , 0 );
				worldIn.setBlockToAir( down );
			}
		}


		@Override
		public boolean canBlockStay( final World worldIn , final BlockPos pos , final IBlockState state )
		{
			final IBlockState soil = worldIn.getBlockState( pos.down( ) );
			return ( worldIn.getLight( pos ) >= 8 || worldIn.canSeeSky( pos ) ) //
					&& soil.getBlock( ).canSustainPlant( soil , worldIn , pos.down( ) , EnumFacing.UP , this );
		}


		@Override
		public List< ItemStack > getDrops( final IBlockAccess world , final BlockPos pos , final IBlockState state ,
				final int fortune )
		{
			final List< ItemStack > ret = new ArrayList<>( );
			final int age = this.getAge( state );
			final Random rand = world instanceof World ? ( (World) world ).rand : new Random( );
			if ( age >= 10 ) {
				for ( int i = 0 ; i < 3 + fortune ; ++i ) {
					if ( rand.nextInt( 30 ) <= age ) {
						ret.add( new ItemStack( PTomato.this.FRUIT ) );
					}
				}
			}
			return ret;
		}


		@Override
		@Nullable
		public Item getItemDropped( final IBlockState state , final Random rand , final int fortune )
		{
			return this.getAge( state ) >= 10 ? PTomato.this.FRUIT : PTomato.this.SEEDS;
		}


		@Override
		public ItemStack getItem( final World worldIn , final BlockPos pos , final IBlockState state )
		{
			return new ItemStack( PTomato.this.SEEDS );
		}


		@Override
		public boolean canGrow( final World worldIn , final BlockPos pos , final IBlockState state ,
				final boolean isClient )
		{
			if ( worldIn.getBlockState( pos.down( ) ).getBlock( ) != Blocks.FARMLAND ) {
				return false;
			}
			for ( int y = 0 ; y < 3 ; y++ ) {
				final IBlockState s = worldIn.getBlockState( pos.offset( EnumFacing.UP , y ) );
				if ( s.getBlock( ) != this ) {
					return false;
				}
				if ( this.getAge( s ) != 15 ) {
					return true;
				}
			}
			return false;
		}


		@Override
		public boolean canUseBonemeal( final World worldIn , final Random rand , final BlockPos pos ,
				final IBlockState state )
		{
			return true;
		}


		@Override
		public void grow( final World worldIn , final Random rand , final BlockPos pos , final IBlockState state )
		{
			for ( int y = 0 ; y < 3 ; y++ ) {
				final BlockPos bPos = pos.offset( EnumFacing.UP , y );
				final IBlockState s = worldIn.getBlockState( bPos );
				if ( s.getBlock( ) != this ) {
					return;
				}
				if ( this.getAge( s ) != 15 ) {
					this.grow( worldIn , bPos , s , MathHelper.getRandomIntegerInRange( worldIn.rand , 2 , 5 ) );
					this.grow( worldIn , bPos , s , 1 );
				}
			}
		}


		private void grow( final World worldIn , final BlockPos pos , final IBlockState state , final int increase )
		{
			final int newAge = Math.min( 15 , this.getAge( state ) + increase );
			worldIn.setBlockState( pos , this.withAge( newAge ) , 2 );
			if ( newAge > 6 && !this.isHighestBlock( worldIn , pos )
					&& worldIn.getBlockState( pos.up( ) ).getMaterial( ) == Material.AIR ) {
				worldIn.setBlockState( pos.up( ) , this.getDefaultState( ) );
			}
		}
	}

	public final WildPlant WILD;
	public final Plant CROP;
	public final ItemSeeds SEEDS;
	public final ItemFood FRUIT;


	public PTomato( )
	{
		CRegistry.addBlock( this.WILD = new WildPlant( ) );
		CRegistry.addBlock( this.CROP = new Plant( ) , null );
		this.SEEDS = PPlantsHelper.makeSeeds( "tomato" , this.CROP );
		this.FRUIT = PPlantsHelper.makeFruit( "tomato" , 2 , .15f );
		Blocks.FIRE.setFireInfo( this.WILD , 60 , 100 );
		CRegistry.addRegistrar( this );
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addShapelessRecipe( new ItemStack( this.SEEDS , 2 ) , //
				new ItemStack( this.FRUIT ) );
	}


	@Override
	public void getFloraGeneration( final List< I_FloraParameters > output )
	{
		output.add( new WGFloraParameters( this.WILD.getDefaultState( ) , 0.2f ,
				WLocation.inOverworld( )//
						.and( new WLocationTemperature( 0.4f , 1f ) ) //
						.and( new WLocationRainfall( 0.2f , 0.8f ) ) ) );
		output.add( new WGFloraParameters( this.WILD.getDefaultState( ) , 0.6f ,
				WLocation.inOverworld( )//
						.and( new WLocationTemperature( 0.7f , 1f ) ) //
						.and( new WLocationRainfall( 0.3f , 0.5f ) ) ) );
		output.add( new WGFloraParameters( //
				this.WILD.getDefaultState( ).withProperty( PTomato.WITH_FRUITS , true ) , 0.3f ,
				WLocation.inOverworld( )//
						.and( new WLocationTemperature( 0.8f , 0.9f ) ) //
						.and( new WLocationRainfall( 0.4f , 0.5f ) ) ) );
	}

}
