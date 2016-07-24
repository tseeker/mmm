package mmm.deco.thrones;


import java.util.List;
import java.util.Random;

import mmm.core.api.I_RecipeRegistrar;
import mmm.core.api.blocks.I_ColoredBlock;
import mmm.core.api.blocks.I_SeatBlock;
import mmm.core.api.blocks.I_SupportBlock;
import mmm.deco.DSeatEntity;
import mmm.materials.MWood;
import mmm.utils.UMaths;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class DThroneBlock
		extends Block
		implements I_ColoredBlock , I_SeatBlock , I_RecipeRegistrar
{

	public static final PropertyDirection FACING;
	public static final PropertyEnum< EnumDyeColor > COLOR;

	private static final AxisAlignedBB COLLISION_TOP_NORTH = UMaths.makeBlockAABB( 0 , 0 , 0 , 16 , 13 , 3 );
	private static final AxisAlignedBB COLLISION_TOP_EAST = UMaths.makeBlockAABB( 13 , 0 , 0 , 16 , 13 , 16 );
	private static final AxisAlignedBB COLLISION_TOP_SOUTH = UMaths.makeBlockAABB( 0 , 0 , 13 , 16 , 13 , 16 );
	private static final AxisAlignedBB COLLISION_TOP_WEST = UMaths.makeBlockAABB( 0 , 0 , 0 , 3 , 13 , 16 );

	private static boolean dropping = false;

	static {
		FACING = BlockHorizontal.FACING;
		COLOR = PropertyEnum.< EnumDyeColor > create( "color" , EnumDyeColor.class );
	}

	public final MWood woodType;
	public final DThrone parts;
	public final boolean isTop;


	public DThroneBlock( final DThrone throne , final MWood woodType , final boolean isTop )
	{
		super( Material.WOOD );
		this.woodType = woodType;
		this.parts = throne;
		this.isTop = isTop;

		this.setCreativeTab( CreativeTabs.DECORATIONS );
		this.setRegistryName( "mmm:deco/throne/" + woodType.getSuffix( ) + "/" + ( isTop ? "top" : "bottom" ) );
		this.setUnlocalizedName( "mmm.deco.throne." + woodType.getSuffix( ) );

		if ( this.isTop ) {
			this.setDefaultState( this.blockState.getBaseState( ) //
					.withProperty( DThroneBlock.FACING , EnumFacing.NORTH ) );
		} else {
			this.setDefaultState( this.blockState.getBaseState( ) //
					.withProperty( DThroneBlock.COLOR , EnumDyeColor.WHITE ) );
		}

		this.lightOpacity = 0;
		this.translucent = false;
		this.fullBlock = false;
		this.blockHardness = 2.5f;
		this.blockResistance = 12.5f;
		this.blockSoundType = SoundType.LADDER;
		this.enableStats = false;

		this.setHarvestLevel( "axe" , 0 );
	}

	// *************************************************************************************************
	// RENDERING AND SHAPE
	// *************************************************************************************************


	@Override
	@SideOnly( Side.CLIENT )
	public BlockRenderLayer getBlockLayer( )
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
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
	public AxisAlignedBB getBoundingBox( final IBlockState state , final IBlockAccess source , final BlockPos pos )
	{
		if ( this.isTop ) {
			switch ( state.getValue( DThroneBlock.FACING ) ) {
				case EAST:
					return DThroneBlock.COLLISION_TOP_EAST;
				case NORTH:
					return DThroneBlock.COLLISION_TOP_NORTH;
				case SOUTH:
					return DThroneBlock.COLLISION_TOP_SOUTH;
				case WEST:
					return DThroneBlock.COLLISION_TOP_WEST;
				default:
					// TODO log problem
					return Block.FULL_BLOCK_AABB;
			}
		}
		return Block.FULL_BLOCK_AABB;
	}


	// *************************************************************************************************
	// BLOCK STATE
	// *************************************************************************************************

	@Override
	protected BlockStateContainer createBlockState( )
	{
		return new BlockStateContainer( this , new IProperty[] {
				DThroneBlock.FACING , DThroneBlock.COLOR
		} );
	}


	@Override
	public IBlockState getStateFromMeta( final int meta )
	{
		IBlockState bs = this.blockState.getBaseState( );
		if ( this.isTop ) {
			bs = bs.withProperty( DThroneBlock.FACING , EnumFacing.getHorizontal( meta ) );
		} else {
			bs = bs.withProperty( DThroneBlock.COLOR , EnumDyeColor.byMetadata( meta ) );
		}
		return bs;
	}


	@Override
	public int getMetaFromState( final IBlockState state )
	{
		if ( this.isTop ) {
			return state.getValue( DThroneBlock.FACING ).getHorizontalIndex( );
		} else {
			return state.getValue( DThroneBlock.COLOR ).getMetadata( );
		}
	}


	@Override
	public IBlockState getActualState( final IBlockState state , final IBlockAccess worldIn , final BlockPos pos )
	{
		if ( this.isTop ) {
			final IBlockState bottom = worldIn.getBlockState( pos.down( ) );
			if ( bottom.getBlock( ) instanceof DThroneBlock ) {
				return state.withProperty( DThroneBlock.COLOR , bottom.getValue( DThroneBlock.COLOR ) );
			}
		} else {
			final IBlockState top = worldIn.getBlockState( pos.up( ) );
			if ( top.getBlock( ) instanceof DThroneBlock ) {
				return state.withProperty( DThroneBlock.FACING , top.getValue( DThroneBlock.FACING ) );
			}
		}
		return state;
	}


	@Override
	public IBlockState withRotation( final IBlockState state , final Rotation rot )
	{
		return state.withProperty( DThroneBlock.FACING , rot.rotate( state.getValue( DThroneBlock.FACING ) ) );
	}


	@Override
	public IBlockState withMirror( final IBlockState state , final Mirror mirrorIn )
	{
		return state.withRotation( mirrorIn.toRotation( state.getValue( DThroneBlock.FACING ) ) );
	}


	// *************************************************************************************************
	// BLOCK PLACEMENT
	// *************************************************************************************************

	@Override
	public boolean canPlaceBlockAt( final World worldIn , final BlockPos pos )
	{
		return pos.getY( ) < worldIn.getHeight( ) - 1 && super.canPlaceBlockAt( worldIn , pos )
				&& super.canPlaceBlockAt( worldIn , pos.up( ) ) && I_SupportBlock.check( worldIn , pos );
	}


	@Override
	public void onBlockPlacedBy( final World worldIn , final BlockPos pos , final IBlockState state ,
			final EntityLivingBase placer , final ItemStack stack )
	{
		if ( this.isTop ) {
			return;
		}

		final int dmg = stack.getItemDamage( );
		final EnumDyeColor color = EnumDyeColor.byMetadata( dmg );

		worldIn.setBlockState( pos , state.withProperty( DThroneBlock.COLOR , color ) , 6 );
		worldIn.setBlockState( pos.up( ) , //
				this.parts.TOP.getDefaultState( ).withProperty( DThroneBlock.FACING , placer.getHorizontalFacing( ) ) ,
				6 );
	}


	// *************************************************************************************************
	// BLOCK REMOVAL AND PICKING
	// *************************************************************************************************

	@Override
	public Item getItemDropped( final IBlockState state , final Random rand , final int fortune )
	{
		return this.parts.ITEM;
	}


	@Override
	public ItemStack getPickBlock( final IBlockState state , final RayTraceResult target , final World world ,
			final BlockPos pos , final EntityPlayer player )
	{
		return new ItemStack( this.parts.ITEM , 1 , this.damageDropped( state.getActualState( world , pos ) ) );
	}


	@Override
	public boolean removedByPlayer( final IBlockState state , final World world , final BlockPos pos ,
			final EntityPlayer player , final boolean willHarvest )
	{
		if ( willHarvest ) {
			return true;
		}

		this.removeOtherBlock( state , world , pos );
		return super.removedByPlayer( state , world , pos , player , willHarvest );
	}


	@Override
	public void dropBlockAsItemWithChance( final World worldIn , final BlockPos pos , final IBlockState state ,
			final float chance , final int fortune )
	{
		if ( !worldIn.isRemote && !worldIn.restoringBlockSnapshots ) {
			super.dropBlockAsItemWithChance( worldIn , pos , state , chance , fortune );
			this.removeOtherBlock( state , worldIn , pos );
		}
	}


	@Override
	public void harvestBlock( final World worldIn , final EntityPlayer player , final BlockPos pos ,
			final IBlockState state , final TileEntity te , final ItemStack tool )
	{
		super.harvestBlock( worldIn , player , pos , state , te , tool );
		this.removedByPlayer( state , worldIn , pos , player , false );
	}


	private void removeOtherBlock( final IBlockState state , final World world , final BlockPos pos )
	{
		DThroneBlock.dropping = true;
		final BlockPos otherPos = this.isTop ? pos.down( ) : pos.up( );
		final Block otherBlock = world.getBlockState( otherPos ).getBlock( );
		if ( otherBlock instanceof DThroneBlock && ( (DThroneBlock) otherBlock ).isTop == !this.isTop ) {
			world.setBlockToAir( otherPos );
		}
		DThroneBlock.dropping = false;
	}


	@Override
	public List< ItemStack > getDrops( final IBlockAccess world , final BlockPos pos , final IBlockState state ,
			final int fortune )
	{
		return super.getDrops( world , pos , state.getActualState( world , pos ) , fortune );
	}


	@Override
	public int damageDropped( final IBlockState state )
	{
		return state.getValue( DThroneBlock.COLOR ).getMetadata( );
	}


	// *************************************************************************************************
	// AUTOMATIC REMOVAL
	// *************************************************************************************************

	@Override
	public void neighborChanged( final IBlockState state , final World worldIn , final BlockPos pos ,
			final Block blockIn )
	{
		if ( ! ( this.isTop || DThroneBlock.dropping ) ) {
			I_SupportBlock.dropIfUnsupported( state , worldIn , pos , this );
		}
	}


	@Override
	public EnumPushReaction getMobilityFlag( final IBlockState state )
	{
		return EnumPushReaction.DESTROY;
	}


	// *************************************************************************************************
	// INTERACTION
	// *************************************************************************************************

	@Override
	public boolean onBlockActivated( final World worldIn , final BlockPos pos , final IBlockState state ,
			final EntityPlayer playerIn , final EnumHand hand , final ItemStack heldItem , final EnumFacing side ,
			final float hitX , final float hitY , final float hitZ )
	{
		BlockPos p;
		if ( this.isTop ) {
			p = pos.down( );
		} else {
			p = pos;
		}
		return DSeatEntity.sit( worldIn , p , playerIn , 0.25 );
	}


	@Override
	@SideOnly( Side.CLIENT )
	public void getSubBlocks( final Item itemIn , final CreativeTabs tab , final List< ItemStack > list )
	{
		for ( int i = 0 ; i < 16 ; ++i ) {
			list.add( new ItemStack( itemIn , 1 , i ) );
		}
	}


	// *************************************************************************************************
	// CRAFTING
	// *************************************************************************************************

	@Override
	public void registerRecipes( )
	{
		if ( this.isTop ) {
			return;
		}
		for ( final EnumDyeColor dyeColor : EnumDyeColor.values( ) ) {
			GameRegistry.addShapedRecipe( new ItemStack( this , 1 , dyeColor.getMetadata( ) ) , //
					" E " , //
					"GWG" , //
					"BBB" , //
					'W' , new ItemStack( Blocks.WOOL , 1 , dyeColor.getMetadata( ) ) , //
					'B' , new ItemStack( this.woodType.getPlanksBlock( ) , 1 , this.woodType.getMetaData( ) ) , //
					'G' , Items.GOLD_INGOT , //
					'E' , Items.EMERALD //
			);
			GameRegistry.addShapelessRecipe( new ItemStack( this , 1 , dyeColor.getMetadata( ) ) , //
					new ItemStack( this , 1 , 32767 ) , //
					new ItemStack( Items.DYE , 1 , dyeColor.getDyeDamage( ) ) );
		}

	}

}
