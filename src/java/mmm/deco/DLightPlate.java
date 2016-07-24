package mmm.deco;


import java.util.Random;

import mmm.MmmDeco;
import mmm.MmmMaterials;
import mmm.core.api.I_RecipeRegistrar;
import mmm.core.api.blocks.I_StateMapperProvider;
import mmm.core.api.blocks.I_SupportBlock;
import mmm.utils.UMaths;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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



public class DLightPlate
		extends Block
		implements I_StateMapperProvider , I_RecipeRegistrar
{
	private static final PropertyEnum< EnumFacing > FACING = PropertyEnum.create( "facing" , EnumFacing.class );
	private static final PropertyBool POWERED = PropertyBool.create( "powered" );

	private static final AxisAlignedBB BB_UP = UMaths.makeBlockAABB( 1 , 0 , 1 , 15 , 1 , 15 );
	private static final AxisAlignedBB BB_DOWN = UMaths.makeBlockAABB( 1 , 15 , 1 , 15 , 16 , 15 );
	private static final AxisAlignedBB BB_NORTH = UMaths.makeBlockAABB( 1 , 1 , 15 , 15 , 15 , 16 );
	private static final AxisAlignedBB BB_SOUTH = UMaths.makeBlockAABB( 1 , 1 , 0 , 15 , 15 , 1 );
	private static final AxisAlignedBB BB_WEST = UMaths.makeBlockAABB( 15 , 1 , 1 , 16 , 15 , 15 );
	private static final AxisAlignedBB BB_EAST = UMaths.makeBlockAABB( 0 , 1 , 1 , 1 , 15 , 15 );

	public final DControllableLightSource lightSource;
	public final boolean lit;
	public final boolean button;


	public DLightPlate( final boolean button , final DControllableLightSource cls , final boolean lit )
	{
		super( Material.CIRCUITS );
		this.lightSource = cls;
		this.lit = lit;
		this.button = button;
		this.lightValue = this.lit ? this.button ? 12 : 14 : 1;

		this.setCreativeTab( button ? CreativeTabs.REDSTONE : CreativeTabs.DECORATIONS );
		this.setHardness( 0.25f );
		this.setSoundType( SoundType.METAL );

		this.setDefaultState( this.blockState.getBaseState( ) //
				.withProperty( DLightPlate.POWERED , false ) //
				.withProperty( DLightPlate.FACING , EnumFacing.NORTH ) );
	}


	@Override
	protected BlockStateContainer createBlockState( )
	{
		return new BlockStateContainer( this , DLightPlate.FACING , DLightPlate.POWERED );
	}


	@Override
	public int getMetaFromState( final IBlockState state )
	{
		return state.getValue( DLightPlate.FACING ).getIndex( ) | ( state.getValue( DLightPlate.POWERED ) ? 8 : 0 );
	}


	@Override
	public IBlockState getStateFromMeta( final int meta )
	{
		return this.getDefaultState( ) //
				.withProperty( DLightPlate.FACING , EnumFacing.getFront( meta & 7 ) ) //
				.withProperty( DLightPlate.POWERED , ( meta & 8 ) != 0 );
	}


	@Override
	@SideOnly( Side.CLIENT )
	public IStateMapper getStateMapper( )
	{
		return new StateMap.Builder( ).ignore( DLightPlate.POWERED ).build( );
	}


	@Override
	public Item getItemDropped( final IBlockState state , final Random rand , final int fortune )
	{
		return this.lightSource.ITEM;
	}


	@Override
	public ItemStack getPickBlock( final IBlockState state , final RayTraceResult target , final World world ,
			final BlockPos pos , final EntityPlayer player )
	{
		return new ItemStack( this.lightSource.ITEM );
	}


	@Override
	public EnumPushReaction getMobilityFlag( final IBlockState state )
	{
		return EnumPushReaction.DESTROY;
	}


	@Override
	@SideOnly( Side.CLIENT )
	public BlockRenderLayer getBlockLayer( )
	{
		return BlockRenderLayer.CUTOUT;
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
		switch ( state.getValue( DLightPlate.FACING ) ) {

			default:
			case NORTH:
				return DLightPlate.BB_NORTH;

			case EAST:
				return DLightPlate.BB_EAST;

			case SOUTH:
				return DLightPlate.BB_SOUTH;

			case WEST:
				return DLightPlate.BB_WEST;

			case DOWN:
				return DLightPlate.BB_DOWN;

			case UP:
				return DLightPlate.BB_UP;
		}
	}


	@Override
	public AxisAlignedBB getCollisionBoundingBox( final IBlockState blockState , final World worldIn ,
			final BlockPos pos )
	{
		return Block.NULL_AABB;
	}


	@Override
	public IBlockState withRotation( final IBlockState state , final Rotation rot )
	{
		return state.withProperty( DLightPlate.FACING , rot.rotate( state.getValue( DLightPlate.FACING ) ) );
	}


	@Override
	public IBlockState withMirror( final IBlockState state , final Mirror mirrorIn )
	{
		return state.withProperty( DLightPlate.FACING , mirrorIn.mirror( state.getValue( DLightPlate.FACING ) ) );
	}


	@Override
	public boolean canPlaceBlockAt( final World worldIn , final BlockPos pos )
	{
		for ( final EnumFacing direction : DLightPlate.FACING.getAllowedValues( ) ) {
			if ( I_SupportBlock.check( worldIn , pos , direction ) ) {
				return true;
			}
		}
		return false;
	}


	@Override
	public IBlockState onBlockPlaced( final World worldIn , final BlockPos pos , final EnumFacing facing ,
			final float hitX , final float hitY , final float hitZ , final int meta , final EntityLivingBase placer )
	{
		if ( ! ( worldIn.isRemote || this.button ) ) {
			worldIn.scheduleUpdate( pos , this , 2 );
		}

		final EnumFacing placed = facing;
		if ( I_SupportBlock.check( worldIn , pos , placed.getOpposite( ) ) ) {
			return this.getDefaultState( ).withProperty( DLightPlate.FACING , placed );
		}
		for ( final EnumFacing attempt : DLightPlate.FACING.getAllowedValues( ) ) {
			if ( I_SupportBlock.check( worldIn , pos , attempt.getOpposite( ) ) ) {
				return this.getDefaultState( ).withProperty( DLightPlate.FACING , attempt );
			}
		}
		return this.getDefaultState( );
	}


	@Override
	public boolean onBlockActivated( final World worldIn , final BlockPos pos , final IBlockState state ,
			final EntityPlayer playerIn , final EnumHand hand , final ItemStack heldItem , final EnumFacing side ,
			final float hitX , final float hitY , final float hitZ )
	{
		if ( worldIn.isRemote && ( playerIn.isSneaking( ) || this.button ) ) {
			return true;
		}

		final EnumFacing ownFacing = state.getValue( DLightPlate.FACING );

		if ( playerIn.isSneaking( ) ) {
			final IBlockState newState = ( this.lit ? this.lightSource.UNLIT : this.lightSource.LIT ).getDefaultState( ) //
					.withProperty( DLightPlate.FACING , ownFacing ) //
					.withProperty( DLightPlate.POWERED , state.getValue( DLightPlate.POWERED ) );
			worldIn.setBlockState( pos , newState , 2 );
			return true;
		}

		if ( this.button ) {
			worldIn.setBlockState( pos ,
					( this.lit ? this.lightSource.UNLIT : this.lightSource.LIT ).getDefaultState( ) //
							.withProperty( DLightPlate.FACING , ownFacing ) //
							.withProperty( DLightPlate.POWERED , !state.getValue( DLightPlate.POWERED ) ) ,
					2 );
			worldIn.notifyNeighborsOfStateChange( pos , this );
			worldIn.notifyNeighborsOfStateChange( pos.offset( ownFacing.getOpposite( ) ) , this );
			return true;
		}

		return false;
	}


	@Override
	public void breakBlock( final World worldIn , final BlockPos pos , final IBlockState state )
	{
		if ( this.button && state.getValue( DLightPlate.POWERED ) ) {
			worldIn.notifyNeighborsOfStateChange( pos , this );
			worldIn.notifyNeighborsOfStateChange( pos.offset( state.getValue( DLightPlate.FACING ).getOpposite( ) ) ,
					this );
		}
		super.breakBlock( worldIn , pos , state );
	}


	@Override
	public void neighborChanged( final IBlockState state , final World worldIn , final BlockPos pos ,
			final Block blockIn )
	{
		final EnumFacing opposite = state.getValue( DLightPlate.FACING ).getOpposite( );
		if ( I_SupportBlock.dropIfUnsupported( state , worldIn , pos , this , opposite ) ) {
			return;
		}

		if ( ! ( worldIn.isRemote || this.button ) ) {
			final boolean powered = worldIn.isSidePowered( pos.offset( opposite ) , opposite );
			if ( powered != state.getValue( DLightPlate.POWERED ) ) {
				worldIn.scheduleUpdate( pos , this , 1 );
			}
		}
	}


	@Override
	public void updateTick( final World worldIn , final BlockPos pos , final IBlockState state , final Random rand )
	{
		if ( this.button || worldIn.isRemote ) {
			return;
		}

		final EnumFacing ownFacing = state.getValue( DLightPlate.FACING );
		final EnumFacing powerDir = ownFacing.getOpposite( );
		final boolean powered = worldIn.isSidePowered( pos.offset( powerDir ) , powerDir );
		if ( powered == state.getValue( DLightPlate.POWERED ) ) {
			return;
		}

		final IBlockState newState = ( this.lit ? this.lightSource.UNLIT : this.lightSource.LIT ).getDefaultState( ) //
				.withProperty( DLightPlate.FACING , ownFacing ) //
				.withProperty( DLightPlate.POWERED , powered );
		worldIn.setBlockState( pos , newState , 2 );
	}


	@Override
	public boolean canProvidePower( final IBlockState state )
	{
		return this.button;
	}


	@Override
	public int getWeakPower( final IBlockState blockState , final IBlockAccess blockAccess , final BlockPos pos ,
			final EnumFacing side )
	{
		return this.button && blockState.getValue( DLightPlate.POWERED ) ? 15 : 0;
	}


	@Override
	public int getStrongPower( final IBlockState blockState , final IBlockAccess blockAccess , final BlockPos pos ,
			final EnumFacing side )
	{
		return this.button && blockState.getValue( DLightPlate.POWERED ) //
				? blockState.getValue( DLightPlate.FACING ) == side ? 15 : 0
				: 0;
	}


	@Override
	public void registerRecipes( )
	{
		if ( this.lit ) {
			return;
		}

		if ( this.button ) {
			GameRegistry.addShapelessRecipe( new ItemStack( this.lightSource.ITEM ) , //
					MmmDeco.LIGHT.PLATE.ITEM , Items.REDSTONE , Blocks.STONE_BUTTON );
			GameRegistry.addShapelessRecipe( new ItemStack( this.lightSource.ITEM ) , //
					MmmDeco.LIGHT.PLATE.ITEM , Items.REDSTONE , Blocks.WOODEN_BUTTON );
		} else {
			GameRegistry.addShapedRecipe( new ItemStack( this.lightSource.ITEM , 4 ) , //
					" I " , //
					"IGI" , //
					" I " , //
					'I' , MmmMaterials.ALLOY.BRASS.INGOT , //
					'G' , Blocks.GLOWSTONE );
		}
	}

}