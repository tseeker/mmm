package mmm.deco;


import java.util.List;

import mmm.MmmMaterials;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.core.api.blocks.I_SeatBlock;
import mmm.core.api.blocks.I_SupportBlock;
import mmm.materials.MWood;
import mmm.utils.UMaths;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class DChairs
{

	public static class ChairBlock
			extends Block
			implements I_RecipeRegistrar , I_SeatBlock
	{
		public static final PropertyDirection FACING = BlockHorizontal.FACING;

		private static final AxisAlignedBB BOUNDING_BOX = UMaths.makeBlockAABB( 1 , 0 , 1 , 15 , 16 , 15 );
		private static final AxisAlignedBB COLLISION_BOTTOM = UMaths.makeBlockAABB( 1 , 0 , 1 , 15 , 8 , 15 );
		private static final AxisAlignedBB COLLISION_TOP_NORTH = UMaths.makeBlockAABB( 2 , 8 , 2 , 14 , 16 , 4 );
		private static final AxisAlignedBB COLLISION_TOP_EAST = UMaths.makeBlockAABB( 12 , 8 , 2 , 14 , 16 , 14 );
		private static final AxisAlignedBB COLLISION_TOP_SOUTH = UMaths.makeBlockAABB( 2 , 8 , 12 , 14 , 16 , 14 );
		private static final AxisAlignedBB COLLISION_TOP_WEST = UMaths.makeBlockAABB( 2 , 8 , 2 , 4 , 16 , 14 );

		public final MWood type;


		public ChairBlock( final MWood type )
		{
			super( Material.WOOD , type.getMapColor( ) );
			this.type = type;
			this.setDefaultState(
					this.blockState.getBaseState( ).withProperty( ChairBlock.FACING , EnumFacing.NORTH ) );

			this.setCreativeTab( CreativeTabs.DECORATIONS );
			CRegistry.setIdentifiers( this , "deco" , "chair" , type.getSuffix( ) );

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
		public AxisAlignedBB getBoundingBox( final IBlockState state , final IBlockAccess source , final BlockPos pos )
		{
			return ChairBlock.BOUNDING_BOX;
		}


		@Override
		public void addCollisionBoxToList( final IBlockState state , final World worldIn , final BlockPos pos ,
				final AxisAlignedBB container , final List< AxisAlignedBB > output , final Entity entity )
		{
			Block.addCollisionBoxToList( pos , container , output , ChairBlock.COLLISION_BOTTOM );

			AxisAlignedBB back;
			switch ( state.getValue( ChairBlock.FACING ) ) {
				case EAST:
					back = ChairBlock.COLLISION_TOP_EAST;
					break;
				case NORTH:
					back = ChairBlock.COLLISION_TOP_NORTH;
					break;
				case SOUTH:
					back = ChairBlock.COLLISION_TOP_SOUTH;
					break;
				case WEST:
					back = ChairBlock.COLLISION_TOP_WEST;
					break;
				default:
					// TODO log problem
					return;

			}
			Block.addCollisionBoxToList( pos , container , output , back );
		}


		@Override
		public EnumPushReaction getMobilityFlag( final IBlockState state )
		{
			return EnumPushReaction.DESTROY;
		}


		@Override
		public void registerRecipes( )
		{
			GameRegistry.addShapedRecipe( new ItemStack( this ) , //
					"B " , //
					"BB" , //
					"SS" , //
					'B' , new ItemStack( this.type.getSlabBlock( ) , 1 , this.type.getMetaData( ) ) , //
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


		@Override
		@SideOnly( Side.CLIENT )
		public BlockRenderLayer getBlockLayer( )
		{
			return BlockRenderLayer.CUTOUT;
		}


		@Override
		protected BlockStateContainer createBlockState( )
		{
			return new BlockStateContainer( this , new IProperty[] {
					ChairBlock.FACING
			} );
		}


		@Override
		public IBlockState withRotation( final IBlockState state , final Rotation rot )
		{
			return state.withProperty( ChairBlock.FACING , rot.rotate( state.getValue( ChairBlock.FACING ) ) );
		}


		@Override
		public IBlockState withMirror( final IBlockState state , final Mirror mirrorIn )
		{
			return state.withRotation( mirrorIn.toRotation( state.getValue( ChairBlock.FACING ) ) );
		}


		@Override
		public IBlockState getStateFromMeta( final int meta )
		{
			EnumFacing enumfacing = EnumFacing.getFront( meta );

			if ( enumfacing.getAxis( ) == EnumFacing.Axis.Y ) {
				enumfacing = EnumFacing.NORTH;
			}

			return this.getDefaultState( ).withProperty( ChairBlock.FACING , enumfacing );
		}


		@Override
		public int getMetaFromState( final IBlockState state )
		{
			return state.getValue( ChairBlock.FACING ).getIndex( );
		}


		@Override
		public IBlockState onBlockPlaced( final World worldIn , final BlockPos pos , final EnumFacing facing ,
				final float hitX , final float hitY , final float hitZ , final int meta ,
				final EntityLivingBase placer )
		{
			return this.getDefaultState( ).withProperty( ChairBlock.FACING , placer.getHorizontalFacing( ) );
		}


		@Override
		public boolean onBlockActivated( final World worldIn , final BlockPos pos , final IBlockState state ,
				final EntityPlayer playerIn , final EnumHand hand , final ItemStack heldItem , final EnumFacing side ,
				final float hitX , final float hitY , final float hitZ )
		{
			return DSeatEntity.sit( worldIn , pos , playerIn , 0.25 );
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

	public final ChairBlock OAK;
	public final ChairBlock BIRCH;
	public final ChairBlock SPRUCE;
	public final ChairBlock JUNGLE;
	public final ChairBlock DARK_OAK;
	public final ChairBlock ACACIA;
	public final ChairBlock HEVEA;
	public final ChairBlock BAMBOO;


	public DChairs( )
	{
		CRegistry.addBlock( this.OAK = new ChairBlock( MmmMaterials.WOOD.OAK ) );
		CRegistry.addBlock( this.BIRCH = new ChairBlock( MmmMaterials.WOOD.BIRCH ) );
		CRegistry.addBlock( this.SPRUCE = new ChairBlock( MmmMaterials.WOOD.SPRUCE ) );
		CRegistry.addBlock( this.JUNGLE = new ChairBlock( MmmMaterials.WOOD.JUNGLE ) );
		CRegistry.addBlock( this.DARK_OAK = new ChairBlock( MmmMaterials.WOOD.DARK_OAK ) );
		CRegistry.addBlock( this.ACACIA = new ChairBlock( MmmMaterials.WOOD.ACACIA ) );
		CRegistry.addBlock( this.HEVEA = new ChairBlock( MmmMaterials.WOOD.HEVEA ) );
		CRegistry.addBlock( this.BAMBOO = new ChairBlock( MmmMaterials.WOOD.BAMBOO ) );
	}
}
