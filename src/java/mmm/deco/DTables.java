package mmm.deco;


import java.util.List;

import mmm.MmmMaterials;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.core.api.blocks.I_SupportBlock;
import mmm.materials.MWood;
import mmm.utils.UMaths;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class DTables
{

	public static class TableBlock
			extends Block
			implements I_RecipeRegistrar
	{

		protected static final AxisAlignedBB COLLISION_TOP = UMaths.makeBlockAABB( 0 , 12 , 0 , 16 , 16 , 16 );
		protected static final AxisAlignedBB COLLISION_LEGS[] = { //
				UMaths.makeBlockAABB( 1 , 0 , 1 , 3 , 12 , 3 ) , //
				UMaths.makeBlockAABB( 13 , 0 , 1 , 15 , 12 , 3 ) , //
				UMaths.makeBlockAABB( 13 , 0 , 13 , 15 , 12 , 15 ) , //
				UMaths.makeBlockAABB( 1 , 0 , 13 , 3 , 12 , 15 ), //
		};

		public static final PropertyBool NORTH = PropertyBool.create( "north" );
		public static final PropertyBool EAST = PropertyBool.create( "east" );
		public static final PropertyBool SOUTH = PropertyBool.create( "south" );
		public static final PropertyBool WEST = PropertyBool.create( "west" );
		public static final PropertyBool NW = PropertyBool.create( "nw" );
		public static final PropertyBool NE = PropertyBool.create( "ne" );
		public static final PropertyBool SW = PropertyBool.create( "sw" );
		public static final PropertyBool SE = PropertyBool.create( "se" );
		private static final PropertyBool[] DIRECTIONS = { //
				TableBlock.NORTH , TableBlock.NE , //
				TableBlock.EAST , TableBlock.SE , //
				TableBlock.SOUTH , TableBlock.SW , //
				TableBlock.WEST , TableBlock.NW//
		};

		public final MWood type;


		public TableBlock( final MWood type )
		{
			super( Material.WOOD , type.getMapColor( ) );
			this.type = type;

			this.setDefaultState( this.blockState.getBaseState( )//
					.withProperty( TableBlock.NORTH , Boolean.valueOf( false ) )//
					.withProperty( TableBlock.EAST , Boolean.valueOf( false ) )//
					.withProperty( TableBlock.SOUTH , Boolean.valueOf( false ) )//
					.withProperty( TableBlock.WEST , Boolean.valueOf( false ) )//
					.withProperty( TableBlock.NW , Boolean.valueOf( false ) )//
					.withProperty( TableBlock.NE , Boolean.valueOf( false ) )//
					.withProperty( TableBlock.SW , Boolean.valueOf( false ) )//
					.withProperty( TableBlock.SE , Boolean.valueOf( false ) ) );

			this.setCreativeTab( CreativeTabs.DECORATIONS );
			CRegistry.setIdentifiers( this , "deco" , "table" , type.getSuffix( ) );

			this.lightOpacity = 0;
			this.translucent = false;
			this.fullBlock = false;
			this.blockHardness = 2.5f;
			this.blockResistance = 12.5f;
			this.blockSoundType = SoundType.LADDER;
			this.enableStats = false;

			this.setHarvestLevel( "axe" , 0 );
		}


		@Override
		public void registerRecipes( )
		{
			GameRegistry.addShapedRecipe( new ItemStack( this ) , //
					"BBB" , //
					"S S" , //
					'B' , new ItemStack( this.type.getPlanksBlock( ) , 1 , this.type.getMetaData( ) ) , //
					'S' , Items.STICK //
			);
		}


		@Override
		public boolean isOpaqueCube( final IBlockState state )
		{
			return false;
		}


		@Override
		public boolean isFullCube( final IBlockState state )
		{
			return false;
		}


		public boolean canConnectTo( final IBlockAccess worldIn , final BlockPos pos )
		{
			return worldIn.getBlockState( pos ).getBlock( ) == this;
		}


		@Override
		protected BlockStateContainer createBlockState( )
		{
			return new BlockStateContainer( this , new IProperty[] {
					TableBlock.NORTH , TableBlock.EAST , TableBlock.WEST , TableBlock.SOUTH , TableBlock.NE ,
					TableBlock.NW , TableBlock.SE , TableBlock.SW
			} );
		}


		@Override
		public int getMetaFromState( final IBlockState state )
		{
			return 0;
		}


		@Override
		public IBlockState getActualState( final IBlockState state , final IBlockAccess worldIn , final BlockPos pos )
		{
			final BlockPos n = pos.north( );
			final BlockPos s = pos.south( );
			final BlockPos w = pos.west( );
			final BlockPos e = pos.east( );
			return state.withProperty( TableBlock.NORTH , Boolean.valueOf( this.canConnectTo( worldIn , n ) ) )//
					.withProperty( TableBlock.EAST , Boolean.valueOf( this.canConnectTo( worldIn , e ) ) )//
					.withProperty( TableBlock.SOUTH , Boolean.valueOf( this.canConnectTo( worldIn , s ) ) )//
					.withProperty( TableBlock.WEST , Boolean.valueOf( this.canConnectTo( worldIn , w ) ) )//
					.withProperty( TableBlock.NW , Boolean.valueOf( this.canConnectTo( worldIn , n.west( ) ) ) )//
					.withProperty( TableBlock.NE , Boolean.valueOf( this.canConnectTo( worldIn , n.east( ) ) ) )//
					.withProperty( TableBlock.SW , Boolean.valueOf( this.canConnectTo( worldIn , s.west( ) ) ) )//
					.withProperty( TableBlock.SE , Boolean.valueOf( this.canConnectTo( worldIn , s.east( ) ) ) )//
			;
		}


		@Override
		public IBlockState withRotation( final IBlockState state , final Rotation rot )
		{
			switch ( rot ) {
				case CLOCKWISE_180:
					return state.withProperty( TableBlock.NORTH , state.getValue( TableBlock.SOUTH ) )//
							.withProperty( TableBlock.EAST , state.getValue( TableBlock.WEST ) )//
							.withProperty( TableBlock.SOUTH , state.getValue( TableBlock.NORTH ) )//
							.withProperty( TableBlock.WEST , state.getValue( TableBlock.EAST ) )//
							.withProperty( TableBlock.NW , state.getValue( TableBlock.SE ) )//
							.withProperty( TableBlock.NE , state.getValue( TableBlock.SW ) )//
							.withProperty( TableBlock.SE , state.getValue( TableBlock.NW ) )//
							.withProperty( TableBlock.SW , state.getValue( TableBlock.NE ) )//
					;
				case COUNTERCLOCKWISE_90:
					return state.withProperty( TableBlock.NORTH , state.getValue( TableBlock.EAST ) )//
							.withProperty( TableBlock.EAST , state.getValue( TableBlock.SOUTH ) )//
							.withProperty( TableBlock.SOUTH , state.getValue( TableBlock.WEST ) )//
							.withProperty( TableBlock.WEST , state.getValue( TableBlock.NORTH ) )//
							.withProperty( TableBlock.NW , state.getValue( TableBlock.NE ) )//
							.withProperty( TableBlock.NE , state.getValue( TableBlock.SE ) )//
							.withProperty( TableBlock.SE , state.getValue( TableBlock.SW ) )//
							.withProperty( TableBlock.SW , state.getValue( TableBlock.NW ) )//
					;
				case CLOCKWISE_90:
					return state.withProperty( TableBlock.NORTH , state.getValue( TableBlock.WEST ) )//
							.withProperty( TableBlock.EAST , state.getValue( TableBlock.NORTH ) )//
							.withProperty( TableBlock.SOUTH , state.getValue( TableBlock.EAST ) )//
							.withProperty( TableBlock.WEST , state.getValue( TableBlock.SOUTH ) )//
							.withProperty( TableBlock.NW , state.getValue( TableBlock.SW ) )//
							.withProperty( TableBlock.NE , state.getValue( TableBlock.NW ) )//
							.withProperty( TableBlock.SE , state.getValue( TableBlock.NE ) )//
							.withProperty( TableBlock.SW , state.getValue( TableBlock.SE ) )//
					;
				default:
					return state;
			}
		}


		@Override
		public IBlockState withMirror( final IBlockState state , final Mirror mirrorIn )
		{
			switch ( mirrorIn ) {
				case LEFT_RIGHT:
					return state.withProperty( TableBlock.NORTH , state.getValue( TableBlock.SOUTH ) )//
							.withProperty( TableBlock.SOUTH , state.getValue( TableBlock.NORTH ) )//
							.withProperty( TableBlock.NW , state.getValue( TableBlock.SW ) )//
							.withProperty( TableBlock.NE , state.getValue( TableBlock.SE ) )//
							.withProperty( TableBlock.SW , state.getValue( TableBlock.NW ) )//
							.withProperty( TableBlock.SE , state.getValue( TableBlock.NE ) )//
					;
				case FRONT_BACK:
					return state.withProperty( TableBlock.EAST , state.getValue( TableBlock.WEST ) )//
							.withProperty( TableBlock.WEST , state.getValue( TableBlock.EAST ) )//
							.withProperty( TableBlock.NW , state.getValue( TableBlock.NE ) )//
							.withProperty( TableBlock.NE , state.getValue( TableBlock.NW ) )//
							.withProperty( TableBlock.SW , state.getValue( TableBlock.SE ) )//
							.withProperty( TableBlock.SE , state.getValue( TableBlock.SW ) )//
					;
				default:
					return state;
			}
		}


		@Override
		public AxisAlignedBB getBoundingBox( final IBlockState state , final IBlockAccess source , final BlockPos pos )
		{
			final IBlockState actual = this.getActualState( state , source , pos );
			for ( int i = 0 , dir = 0 ; i < 4 ; i++ , dir += 2 ) {
				final boolean c0 = actual.getValue( TableBlock.DIRECTIONS[ dir ] );
				final boolean c1 = actual.getValue( TableBlock.DIRECTIONS[ ( dir + 6 ) % 8 ] );
				final boolean c10 = actual.getValue( TableBlock.DIRECTIONS[ ( dir + 7 ) % 8 ] );
				if ( ! ( c0 || c1 ) || c0 && c1 && !c10 ) {
					return Block.FULL_BLOCK_AABB;
				}
			}
			return TableBlock.COLLISION_TOP;
		}


		@Override
		public void addCollisionBoxToList( final IBlockState state , final World worldIn , final BlockPos pos ,
				final AxisAlignedBB container , final List< AxisAlignedBB > output , final Entity entity )
		{
			final IBlockState actual = this.getActualState( state , worldIn , pos );
			Block.addCollisionBoxToList( pos , container , output , TableBlock.COLLISION_TOP );
			for ( int i = 0 , dir = 0 ; i < 4 ; i++ , dir += 2 ) {
				final boolean c0 = actual.getValue( TableBlock.DIRECTIONS[ dir ] );
				final boolean c1 = actual.getValue( TableBlock.DIRECTIONS[ ( dir + 6 ) % 8 ] );
				final boolean c10 = actual.getValue( TableBlock.DIRECTIONS[ ( dir + 7 ) % 8 ] );
				if ( ! ( c0 || c1 ) || c0 && c1 && !c10 ) {
					Block.addCollisionBoxToList( pos , container , output , TableBlock.COLLISION_LEGS[ i ] );
				}
			}
		}


		@Override
		@SideOnly( Side.CLIENT )
		public BlockRenderLayer getBlockLayer( )
		{
			return BlockRenderLayer.CUTOUT;
		}


		@Override
		public boolean isSideSolid( final IBlockState base_state , final IBlockAccess world , final BlockPos pos ,
				final EnumFacing side )
		{
			return side == EnumFacing.UP;
		}


		@Override
		public void neighborChanged( final IBlockState state , final World worldIn , final BlockPos pos ,
				final Block blockIn )
		{
			I_SupportBlock.dropIfUnsupported( state , worldIn , pos , this );
		}


		@Override
		public boolean canPlaceBlockAt( final World worldIn , final BlockPos pos )
		{
			return super.canPlaceBlockAt( worldIn , pos ) && I_SupportBlock.check( worldIn , pos );
		}

	}

	public final TableBlock OAK;
	public final TableBlock BIRCH;
	public final TableBlock SPRUCE;
	public final TableBlock JUNGLE;
	public final TableBlock DARK_OAK;
	public final TableBlock ACACIA;
	public final TableBlock HEVEA;
	public final TableBlock BAMBOO;


	public DTables( )
	{
		CRegistry.addBlock( this.OAK = new TableBlock( MmmMaterials.WOOD.OAK ) );
		CRegistry.addBlock( this.BIRCH = new TableBlock( MmmMaterials.WOOD.BIRCH ) );
		CRegistry.addBlock( this.SPRUCE = new TableBlock( MmmMaterials.WOOD.SPRUCE ) );
		CRegistry.addBlock( this.JUNGLE = new TableBlock( MmmMaterials.WOOD.JUNGLE ) );
		CRegistry.addBlock( this.DARK_OAK = new TableBlock( MmmMaterials.WOOD.DARK_OAK ) );
		CRegistry.addBlock( this.ACACIA = new TableBlock( MmmMaterials.WOOD.ACACIA ) );
		CRegistry.addBlock( this.HEVEA = new TableBlock( MmmMaterials.WOOD.HEVEA ) );
		CRegistry.addBlock( this.BAMBOO = new TableBlock( MmmMaterials.WOOD.BAMBOO ) );
	}
}
