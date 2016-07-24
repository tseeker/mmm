package mmm.materials.trees;


import mmm.core.CRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class MLog
		extends BlockLog
{

	public final MTree wood;


	public MLog( final MTree wood )
	{
		super( );
		this.wood = wood;

		this.setHarvestLevel( "axe" , 0 );

		this.setDefaultState( this.blockState.getBaseState( )//
				.withProperty( BlockLog.LOG_AXIS , BlockLog.EnumAxis.Y ) );

		CRegistry.setIdentifiers( this , "materials" , "log" , wood.NAME );
	}


	@Override
	public MapColor getMapColor( final IBlockState state )
	{
		if ( state.getValue( BlockLog.LOG_AXIS ) == BlockLog.EnumAxis.Y ) {
			return this.wood.getPlankColor( );
		}
		return this.wood.getBarkColor( );
	}


	@Override
	protected BlockStateContainer createBlockState( )
	{
		return new BlockStateContainer( this , new IProperty< ? >[] {
				BlockLog.LOG_AXIS
		} );
	}


	@Override
	public IBlockState getStateFromMeta( final int meta )
	{
		final IBlockState iblockstate = this.getDefaultState( );

		switch ( meta & 12 ) {
			case 0:
				return iblockstate.withProperty( BlockLog.LOG_AXIS , BlockLog.EnumAxis.Y );

			case 4:
				return iblockstate.withProperty( BlockLog.LOG_AXIS , BlockLog.EnumAxis.X );

			case 8:
				return iblockstate.withProperty( BlockLog.LOG_AXIS , BlockLog.EnumAxis.Z );

			default:
				return iblockstate.withProperty( BlockLog.LOG_AXIS , BlockLog.EnumAxis.NONE );
		}
	}


	@Override
	public int getMetaFromState( final IBlockState state )
	{
		switch ( state.getValue( BlockLog.LOG_AXIS ) ) {
			case X:
				return 4;

			case Y:
				return 0;

			case Z:
				return 8;

			case NONE:
			default:
				return 12;
		}
	}


	@Override
	public AxisAlignedBB getBoundingBox( final IBlockState state , final IBlockAccess source , final BlockPos pos )
	{
		final AxisAlignedBB bb = this.wood.getLogBoundingBox( state.getValue( BlockLog.LOG_AXIS ) );
		if ( bb != null ) {
			return bb;
		}
		return Block.FULL_BLOCK_AABB;
	}


	@Override
	public boolean isOpaqueCube( final IBlockState state )
	{
		return this.wood != null && !this.wood.hasLogBoundingBox( );
	}


	@Override
	public boolean isFullCube( final IBlockState state )
	{
		return !this.wood.hasLogBoundingBox( );
	}


	@Override
	public boolean isFullBlock( final IBlockState state )
	{
		return !this.wood.hasLogBoundingBox( );
	}


	@Override
	@SideOnly( Side.CLIENT )
	public BlockRenderLayer getBlockLayer( )
	{
		return this.wood.hasLogBoundingBox( ) ? BlockRenderLayer.CUTOUT : BlockRenderLayer.SOLID;
	}

}
