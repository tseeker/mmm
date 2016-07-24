package mmm.tech.base.workbench;


import mmm.core.CGui;
import mmm.core.CNetwork;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class TBWorkbench
		extends BlockContainer
		implements I_RecipeRegistrar
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;


	public TBWorkbench( )
	{
		super( Material.WOOD );

		this.setCreativeTab( CreativeTabs.DECORATIONS );
		this.setHardness( 2.5f );
		this.setSoundType( SoundType.WOOD );
		this.setHarvestLevel( "axe" , 0 );

		this.setDefaultState( this.blockState.getBaseState( ) //
				.withProperty( TBWorkbench.FACING , EnumFacing.NORTH ) );

		CRegistry.setIdentifiers( this , "tech" , "base" , "workbench" );
		CRegistry.addBlock( this );
		GameRegistry.registerTileEntity( TBWBTileEntity.class , "mmm:tech/base/workbench" );
		CGui.registerTileEntityGUI( TBWBTileEntity.class , //
				"mmm.tech.base.workbench.TBWBContainer" , //
				"mmm.tech.base.workbench.TBWBGui" );
		CNetwork.addServerMessage( TBWBMessage.class );
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addShapelessRecipe( new ItemStack( this ) , //
				Blocks.CRAFTING_TABLE , //
				Blocks.CHEST , //
				Items.BOOK );
	}


	@Override
	protected BlockStateContainer createBlockState( )
	{
		return new BlockStateContainer( this , TBWorkbench.FACING );
	}


	@Override
	public IBlockState getStateFromMeta( final int meta )
	{
		return this.getDefaultState( ) //
				.withProperty( TBWorkbench.FACING , EnumFacing.getHorizontal( meta ) );
	}


	@Override
	public int getMetaFromState( final IBlockState state )
	{
		return state.getValue( TBWorkbench.FACING ).getHorizontalIndex( );
	}


	@Override
	public void onBlockPlacedBy( final World worldIn , final BlockPos pos , final IBlockState state ,
			final EntityLivingBase placer , final ItemStack stack )
	{
		worldIn.setBlockState( pos , state.withProperty( //
				TBWorkbench.FACING , placer.getHorizontalFacing( ).getOpposite( ) ) , 2 );
	}


	@Override
	public IBlockState withRotation( final IBlockState state , final Rotation rot )
	{
		return state.withProperty( TBWorkbench.FACING , rot.rotate( state.getValue( TBWorkbench.FACING ) ) );
	}


	@Override
	public IBlockState withMirror( final IBlockState state , final Mirror mirrorIn )
	{
		return state.withRotation( mirrorIn.toRotation( state.getValue( TBWorkbench.FACING ) ) );
	}


	@Override
	public TileEntity createNewTileEntity( final World worldIn , final int meta )
	{
		return new TBWBTileEntity( );
	}


	@Override
	public EnumBlockRenderType getRenderType( final IBlockState state )
	{
		return EnumBlockRenderType.MODEL;
	}


	@Override
	public boolean onBlockActivated( final World worldIn , final BlockPos pos , final IBlockState state ,
			final EntityPlayer playerIn , final EnumHand hand , final ItemStack heldItem , final EnumFacing side ,
			final float hitX , final float hitY , final float hitZ )
	{
		final TileEntity te = worldIn.getTileEntity( pos );
		if ( ! ( te instanceof TBWBTileEntity ) || playerIn.isSneaking( ) ) {
			return false;
		}
		CGui.openTileEntityGUI( playerIn , worldIn , pos );
		return true;
	}


	@Override
	public void breakBlock( final World worldIn , final BlockPos pos , final IBlockState state )
	{
		final TileEntity tileEntity = worldIn.getTileEntity( pos );
		if ( tileEntity instanceof TBWBTileEntity ) {
			final TBWBTileEntity wbte = (TBWBTileEntity) tileEntity;
			InventoryHelper.dropInventoryItems( worldIn , pos , wbte.storage );
		}
		super.breakBlock( worldIn , pos , state );
	}
}
