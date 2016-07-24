package mmm.tech.base.alloy_furnace;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import mmm.MmmTech;
import mmm.core.CGui;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.utils.UMaths;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class TBAFBlock
		extends BlockContainer
		implements I_RecipeRegistrar
{
	private static final AxisAlignedBB AABB_BOTTOM = UMaths.makeBlockAABB( 0 , 0 , 0 , 16 , 10 , 16 );
	private static final AxisAlignedBB AABB_TOP = UMaths.makeBlockAABB( 4 , 10 , 4 , 12 , 16 , 12 );

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool POWERED = PropertyBool.create( "powered" );

	private static boolean keepInventory = false;

	public final boolean active;


	public TBAFBlock( final boolean active )
	{
		super( Material.ROCK );
		this.active = active;

		this.setResistance( 17.5f );
		this.setHardness( 3.5f );
		this.setHarvestLevel( "pickaxe" , 0 );

		this.lightOpacity = 0;
		this.translucent = false;
		this.fullBlock = false;
		this.lightValue = active ? 13 : 0;

		this.setDefaultState( this.blockState.getBaseState( ) //
				.withProperty( TBAFBlock.FACING , EnumFacing.NORTH ) //
				.withProperty( TBAFBlock.POWERED , Boolean.valueOf( false ) ) );

		CRegistry.setIdentifiers( this , "tech" , "base" , "alloy_furnace" , active ? "active" : "inactive" );
	}


	@Override
	public void registerRecipes( )
	{
		if ( !this.active ) {
			GameRegistry.addShapedRecipe( new ItemStack( MmmTech.MACHINES.ALLOY_FURNACE.ITEM ) , //
					"BBB" , //
					"BFB" , //
					"BBB" , //
					'B' , Blocks.BRICK_BLOCK , //
					'F' , Blocks.FURNACE );
		}
	}


	// *************************************************************************************************
	// TILE ENTITY
	// *************************************************************************************************

	@Override
	public TileEntity createNewTileEntity( final World worldIn , final int meta )
	{
		return new TBAFTileEntity( );
	}


	public static void setState( final boolean burning , final World world , final BlockPos pos )
	{
		final IBlockState iblockstate = world.getBlockState( pos );
		final TileEntity tileentity = world.getTileEntity( pos );

		TBAFBlock.keepInventory = true;
		IBlockState nState;
		Block nBlock;
		if ( burning ) {
			nBlock = MmmTech.MACHINES.ALLOY_FURNACE.ACTIVE;
		} else {
			nBlock = MmmTech.MACHINES.ALLOY_FURNACE.INACTIVE;
		}
		nState = nBlock.getDefaultState( ) //
				.withProperty( TBAFBlock.FACING , iblockstate.getValue( TBAFBlock.FACING ) ) //
				.withProperty( TBAFBlock.POWERED , iblockstate.getValue( TBAFBlock.POWERED ) );
		world.setBlockState( pos , nState , 3 );
		TBAFBlock.keepInventory = false;

		if ( tileentity != null ) {
			tileentity.validate( );
			world.setTileEntity( pos , tileentity );
		}
	}


	// *************************************************************************************************
	// RENDERING AND COLLISIONS
	// *************************************************************************************************

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
	public EnumBlockRenderType getRenderType( final IBlockState state )
	{
		return EnumBlockRenderType.MODEL;
	}


	@Override
	@SideOnly( Side.CLIENT )
	public BlockRenderLayer getBlockLayer( )
	{
		return BlockRenderLayer.CUTOUT;
	}


	@Override
	@SideOnly( Side.CLIENT )
	public void randomDisplayTick( final IBlockState stateIn , final World worldIn , final BlockPos pos ,
			final Random rand )
	{
		if ( this.active ) {
			if ( rand.nextDouble( ) < .1 ) {
				worldIn.playSound( pos.getX( ) + .5 , pos.getY( ) , pos.getZ( ) + .5 ,
						SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE , SoundCategory.BLOCKS , 1.f , 1.f , false );
			}

			if ( rand.nextDouble( ) < .25 ) {
				this.spawnFrontParticles( stateIn , worldIn , pos , rand );
			}

			if ( rand.nextDouble( ) < .5 ) {
				for ( int i = 0 ; i < 4 ; i++ ) {
					this.spawnTopParticles( stateIn , worldIn , pos , rand );
				}
			}
		}
	}


	private void spawnTopParticles( final IBlockState stateIn , final World worldIn , final BlockPos pos ,
			final Random rand )
	{
		final double spawnX = pos.getX( ) + .5 + ( rand.nextDouble( ) - .5 ) * 4.5 / 16.;
		final double spawnY = pos.getY( ) + ( 15. + 3. * rand.nextDouble( ) ) / 16.;
		final double spawnZ = pos.getZ( ) + .5 + ( rand.nextDouble( ) - .5 ) * 4.5 / 16.;
		worldIn.spawnParticle( EnumParticleTypes.SMOKE_NORMAL , spawnX , spawnY , spawnZ , 0. , 0. , 0. );
		worldIn.spawnParticle( EnumParticleTypes.FLAME , spawnX , spawnY , spawnZ , 0. , 0. , 0. );
	}


	private void spawnFrontParticles( final IBlockState stateIn , final World worldIn , final BlockPos pos ,
			final Random rand )
	{
		final double cx = pos.getX( ) + .5;
		final double cz = pos.getZ( ) + .5;
		final double frontOffset = .52;
		final double spawnY = pos.getY( ) + ( 4. + rand.nextDouble( ) * 4. ) / 16.;
		final double pSide = ( rand.nextDouble( ) - .5 ) * 10. / 16.;

		double spawnX , spawnZ;
		switch ( stateIn.getValue( TBAFBlock.FACING ) ) {
			case WEST:
				spawnX = cx - frontOffset;
				spawnZ = cz + pSide;
				break;
			case EAST:
				spawnX = cx + frontOffset;
				spawnZ = cz + pSide;
				break;
			case NORTH:
				spawnX = cx + pSide;
				spawnZ = cz - frontOffset;
				break;
			case SOUTH:
				spawnX = cx + pSide;
				spawnZ = cz + frontOffset;
				break;
			default:
				return;
		}

		worldIn.spawnParticle( EnumParticleTypes.SMOKE_NORMAL , spawnX , spawnY , spawnZ , 0. , 0. , 0. );
		worldIn.spawnParticle( EnumParticleTypes.FLAME , spawnX , spawnY , spawnZ , 0. , 0. , 0. );
	}


	@Override
	public AxisAlignedBB getBoundingBox( final IBlockState state , final IBlockAccess source , final BlockPos pos )
	{
		return Block.FULL_BLOCK_AABB;
	}


	@Override
	public void addCollisionBoxToList( final IBlockState state , final World worldIn , final BlockPos pos ,
			final AxisAlignedBB entityBox , final List< AxisAlignedBB > collidingBoxes , final Entity entityIn )
	{
		Block.addCollisionBoxToList( pos , entityBox , collidingBoxes , TBAFBlock.AABB_TOP );
		Block.addCollisionBoxToList( pos , entityBox , collidingBoxes , TBAFBlock.AABB_BOTTOM );
	}


	// *************************************************************************************************
	// ITEM
	// *************************************************************************************************

	@Override
	@Nullable
	public Item getItemDropped( final IBlockState state , final Random rand , final int fortune )
	{
		return MmmTech.MACHINES.ALLOY_FURNACE.ITEM;
	}


	@Override
	public List< ItemStack > getDrops( final IBlockAccess world , final BlockPos pos , final IBlockState state ,
			final int fortune )
	{
		return new ArrayList< ItemStack >( );
	}


	// *************************************************************************************************
	// DIRECTION
	// *************************************************************************************************

	@Override
	protected BlockStateContainer createBlockState( )
	{
		return new BlockStateContainer( this , new IProperty[] {
				TBAFBlock.FACING , TBAFBlock.POWERED
		} );
	}


	@Override
	public IBlockState getStateFromMeta( final int meta )
	{
		final boolean powered = ( meta & 8 ) == 8;
		EnumFacing enumfacing = EnumFacing.getFront( meta & 7 );

		if ( enumfacing.getAxis( ) == EnumFacing.Axis.Y ) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState( ) //
				.withProperty( TBAFBlock.FACING , enumfacing ) //
				.withProperty( TBAFBlock.POWERED , powered );
	}


	@Override
	public int getMetaFromState( final IBlockState state )
	{
		return state.getValue( TBAFBlock.FACING ).getIndex( ) //
				| ( state.getValue( TBAFBlock.POWERED ) ? 8 : 0 );
	}


	@Override
	public void onBlockPlacedBy( final World worldIn , final BlockPos pos , final IBlockState state ,
			final EntityLivingBase placer , final ItemStack stack )
	{
		worldIn.setBlockState( pos , state.withProperty( //
				TBAFBlock.FACING , placer.getHorizontalFacing( ).getOpposite( ) ) , 2 );

		if ( stack.hasDisplayName( ) ) {
			final TileEntity tileentity = worldIn.getTileEntity( pos );
			if ( tileentity instanceof TBAFTileEntity ) {
				( (TBAFTileEntity) tileentity ).setCustomName( stack.getDisplayName( ) );
			}
		}
	}


	@Override
	public IBlockState onBlockPlaced( final World worldIn , final BlockPos pos , final EnumFacing facing ,
			final float hitX , final float hitY , final float hitZ , final int meta , final EntityLivingBase placer )
	{
		return this.getDefaultState( ).withProperty( //
				TBAFBlock.FACING , placer.getHorizontalFacing( ).getOpposite( ) );
	}


	@Override
	public IBlockState withRotation( final IBlockState state , final Rotation rot )
	{
		return state.withProperty( TBAFBlock.FACING , rot.rotate( state.getValue( TBAFBlock.FACING ) ) );
	}


	@Override
	public IBlockState withMirror( final IBlockState state , final Mirror mirrorIn )
	{
		return state.withRotation( mirrorIn.toRotation( state.getValue( TBAFBlock.FACING ) ) );
	}


	// *************************************************************************************************
	// SUPPORT REQUIREMENT
	// *************************************************************************************************

	@Override
	public boolean isSideSolid( final IBlockState base_state , final IBlockAccess world , final BlockPos pos ,
			final EnumFacing side )
	{
		return side == EnumFacing.DOWN;
	}


	// *************************************************************************************************
	// INTERACTION
	// *************************************************************************************************

	@Override
	public boolean onBlockActivated( final World worldIn , final BlockPos pos , final IBlockState state ,
			final EntityPlayer playerIn , final EnumHand hand , final ItemStack heldItem , final EnumFacing side ,
			final float hitX , final float hitY , final float hitZ )
	{
		final TileEntity te = worldIn.getTileEntity( pos );
		if ( ! ( te instanceof TBAFTileEntity ) || playerIn.isSneaking( ) ) {
			return false;
		}
		CGui.openTileEntityGUI( playerIn , worldIn , pos );
		return true;
	}


	@Override
	public void breakBlock( final World worldIn , final BlockPos pos , final IBlockState state )
	{
		if ( !TBAFBlock.keepInventory ) {
			final TileEntity tileEntity = worldIn.getTileEntity( pos );
			if ( tileEntity instanceof TBAFTileEntity ) {
				final TBAFTileEntity afte = (TBAFTileEntity) tileEntity;
				afte.cancelAlloying( );
				InventoryHelper.dropInventoryItems( worldIn , pos , afte.input );
				InventoryHelper.dropInventoryItems( worldIn , pos , afte.fuel );
				InventoryHelper.dropInventoryItems( worldIn , pos , afte.output );

				final ItemStack stack = new ItemStack( MmmTech.MACHINES.ALLOY_FURNACE.ITEM , 1 );
				if ( afte.hasCustomName( ) ) {
					stack.setStackDisplayName( afte.getName( ) );
				}
				InventoryHelper.spawnItemStack( worldIn , pos.getX( ) , pos.getY( ) , pos.getZ( ) , stack );
			}
			worldIn.updateComparatorOutputLevel( pos , this );
		}

		super.breakBlock( worldIn , pos , state );
	}


	// *************************************************************************************************
	// REDSTONE
	// *************************************************************************************************

	@Override
	public boolean hasComparatorInputOverride( final IBlockState state )
	{
		return true;
	}


	@Override
	public int getComparatorInputOverride( final IBlockState blockState , final World worldIn , final BlockPos pos )
	{
		final TileEntity te = worldIn.getTileEntity( pos );
		if ( ! ( te instanceof TBAFTileEntity ) ) {
			return 0;
		}
		return ( (TBAFTileEntity) te ).getComparatorValue( );
	}


	@Override
	public void neighborChanged( final IBlockState state , final World worldIn , final BlockPos pos ,
			final Block blockIn )
	{
		this.checkIfPowered( state , worldIn , pos );
	}


	@Override
	public void onBlockAdded( final World worldIn , final BlockPos pos , final IBlockState state )
	{
		this.checkIfPowered( state , worldIn , pos );
	}


	private void checkIfPowered( final IBlockState state , final World worldIn , final BlockPos pos )
	{
		final boolean powered = worldIn.isBlockPowered( pos );
		if ( powered != state.getValue( TBAFBlock.POWERED ).booleanValue( ) ) {
			worldIn.setBlockState( pos , state.withProperty( TBAFBlock.POWERED , powered ) , 4 );
		}
	}


	public static boolean isPowered( final int meta )
	{
		return ( meta & 8 ) == 8;
	}
}
