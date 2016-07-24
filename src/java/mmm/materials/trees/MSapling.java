package mmm.materials.trees;


import java.util.Random;

import mmm.core.CRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class MSapling
		extends Block
		implements IGrowable
{
	private static final PropertyInteger STAGE = PropertyInteger.create( "stage" , 0 , 15 );

	public final MTree wood;


	public MSapling( final MTree wood )
	{
		super( Material.PLANTS );
		this.wood = wood;

		this.setTickRandomly( true );
		this.setHardness( 0f );
		this.setSoundType( SoundType.PLANT );
		this.setCreativeTab( CreativeTabs.DECORATIONS );

		this.setDefaultState( this.blockState.getBaseState( ).withProperty( MSapling.STAGE , 0 ) );

		CRegistry.setIdentifiers( this , "materials" , "sapling" , wood.NAME );
	}


	@Override
	protected BlockStateContainer createBlockState( )
	{
		return new BlockStateContainer( this , MSapling.STAGE );
	}


	@Override
	public IBlockState getStateFromMeta( final int meta )
	{
		return this.getDefaultState( ).withProperty( MSapling.STAGE , meta );
	}


	@Override
	public int getMetaFromState( final IBlockState state )
	{
		return state.getValue( MSapling.STAGE );
	}


	@Override
	public int damageDropped( final IBlockState state )
	{
		return 0;
	}


	@Override
	public boolean canGrow( final World worldIn , final BlockPos pos , final IBlockState state ,
			final boolean isClient )
	{
		return this.wood.getBonemealChance( ) > 0;
	}


	@Override
	public boolean canUseBonemeal( final World worldIn , final Random rand , final BlockPos pos ,
			final IBlockState state )
	{
		return worldIn.rand.nextFloat( ) < this.wood.getBonemealChance( );
	}


	@Override
	public void grow( final World worldIn , final Random rand , final BlockPos pos , final IBlockState state )
	{
		final int stage = state.getValue( MSapling.STAGE ) + 1;
		if ( stage >= this.wood.getSaplingGrowthStages( ) ) {
			this.generate( worldIn , rand , pos , state );
		} else {
			worldIn.setBlockState( pos , state.withProperty( MSapling.STAGE , stage ) );
		}
	}


	private void generate( final World worldIn , final Random rand , final BlockPos pos , final IBlockState state )
	{
		if ( !TerrainGen.saplingGrowTree( worldIn , rand , pos ) ) {
			return;
		}

		if ( this.wood.canGenerateMega( ) ) {
			for ( int x = -1 ; x < 1 ; x++ ) {
				for ( int z = -1 ; z < 1 ; z++ ) {
					final BlockPos basePos = pos.add( x , 0 , z );
					if ( this.isSameSapling( worldIn , basePos ) //
							&& this.isSameSapling( worldIn , basePos.add( 1 , 0 , 0 ) ) //
							&& this.isSameSapling( worldIn , basePos.add( 0 , 0 , 1 ) )
							&& this.isSameSapling( worldIn , basePos.add( 1 , 0 , 1 ) )
							&& this.generateMega( worldIn , basePos , rand ) ) {
						return;
					}
				}
			}
		}

		if ( this.wood.canGenerateBigOrSmall( ) ) {
			final IBlockState air = Blocks.AIR.getDefaultState( );
			worldIn.setBlockState( pos , air , 4 );
			if ( !this.wood.generateNormalOrBig( worldIn , pos , rand ) ) {
				worldIn.setBlockState( pos , state , 4 );
			}
		}
	}


	private boolean generateMega( final World worldIn , final BlockPos pos , final Random rand )
	{
		final IBlockState air = Blocks.AIR.getDefaultState( );

		final IBlockState saved[] = new IBlockState[ 4 ];
		for ( int i = 0 , o = 0 ; i < 2 ; i++ ) {
			for ( int j = 0 ; j < 2 ; j++ , o++ ) {
				final BlockPos bPos = pos.add( i , 0 , j );
				saved[ o ] = worldIn.getBlockState( bPos );
				worldIn.setBlockState( bPos , air , 4 );
			}
		}

		final boolean generated = this.wood.generateMega( worldIn , pos , rand );
		if ( !generated ) {
			for ( int i = 0 , o = 0 ; i < 2 ; i++ ) {
				for ( int j = 0 ; j < 2 ; j++ , o++ ) {
					final BlockPos bPos = pos.add( i , 0 , j );
					worldIn.setBlockState( bPos , saved[ o ] , 4 );
				}
			}
		}
		return generated;
	}


	private boolean isSameSapling( final World worldIn , final BlockPos pos )
	{
		return worldIn.getBlockState( pos ).getBlock( ) == this;
	}


	@Override
	public boolean canReplace( final World world , final BlockPos pos , final EnumFacing side , final ItemStack stack )
	{
		return world.getBlockState( pos ).getBlock( ).isReplaceable( world , pos )
				&& this.wood.canSaplingStay( world , pos );
	}


	@Override
	public void neighborChanged( final IBlockState state , final World world , final BlockPos pos ,
			final Block neighborBlock )
	{
		this.checkUproot( world , pos , state );
	}


	@Override
	public void updateTick( final World worldIn , final BlockPos pos , final IBlockState state , final Random rand )
	{
		if ( !worldIn.isRemote && this.checkUproot( worldIn , pos , state )
				&& this.wood.checkGrowthLight( worldIn.getLightFromNeighbors( pos.up( ) ) )
				&& rand.nextFloat( ) <= this.wood.getGrowthChance( ) ) {
			this.grow( worldIn , rand , pos , state );
		}
	}


	private boolean checkUproot( final World worldIn , final BlockPos pos , final IBlockState state )
	{
		if ( this.wood.canSaplingStay( worldIn , pos ) ) {
			return true;
		}
		this.dropBlockAsItem( worldIn , pos , state , 0 );
		worldIn.setBlockState( pos , Blocks.AIR.getDefaultState( ) , 3 );
		return false;
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
	@SideOnly( Side.CLIENT )
	public Block.EnumOffsetType getOffsetType( )
	{
		return this.wood.mustOffsetSapling( ) ? Block.EnumOffsetType.XZ : Block.EnumOffsetType.NONE;
	}


	@Override
	public AxisAlignedBB getCollisionBoundingBox( final IBlockState state , final World world , final BlockPos pos )
	{
		return Block.NULL_AABB;
	}
}
