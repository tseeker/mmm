package mmm.materials.trees;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

import mmm.core.CRegistry;
import mmm.core.api.blocks.I_TintedBlock;
import mmm.core.api.items.I_TintedItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class MLeaves
		extends BlockLeaves
		implements I_TintedBlock , I_TintedItem
{

	private final MTree wood;


	public MLeaves( final MTree wood )
	{
		this.wood = wood;

		this.setDefaultState( this.blockState.getBaseState( ) //
				.withProperty( BlockLeaves.CHECK_DECAY , true )//
				.withProperty( BlockLeaves.DECAYABLE , true ) );

		CRegistry.setIdentifiers( this , "materials" , "leaves" , wood.NAME );
	}


	@Override
	public IBlockState onBlockPlaced( final World worldIn , final BlockPos pos , final EnumFacing facing ,
			final float hitX , final float hitY , final float hitZ , final int meta , final EntityLivingBase placer )
	{
		return this.getDefaultState( ) //
				.withProperty( BlockLeaves.CHECK_DECAY , false ) //
				.withProperty( BlockLeaves.DECAYABLE , false );
	}


	@Override
	public Item getItemDropped( final IBlockState state , final Random rand , final int fortune )
	{
		return Item.getItemFromBlock( this.wood.SAPLING );
	}


	@Override
	protected void dropApple( final World worldIn , final BlockPos pos , final IBlockState state , final int chance )
	{
		final Item fruit = this.wood.getFruit( );
		if ( fruit != null && worldIn.rand.nextInt( chance ) < this.wood.getFruitDropChance( ) ) {
			Block.spawnAsEntity( worldIn , pos , new ItemStack( fruit ) );
		}
	}


	@Override
	protected int getSaplingDropChance( final IBlockState state )
	{
		return this.wood.getSaplingDropChance( );
	}


	@Override
	public List< ItemStack > onSheared( final ItemStack item , final IBlockAccess world , final BlockPos pos ,
			final int fortune )
	{
		return Arrays.asList( new ItemStack( this ) );
	}


	@Override
	public BlockPlanks.EnumType getWoodType( final int meta )
	{
		// Fuck you and fuck this useless fuckery. For fuck's sake.
		return null;
	}


	@Override
	protected BlockStateContainer createBlockState( )
	{
		return new BlockStateContainer( this , new IProperty[] {
				BlockLeaves.CHECK_DECAY , BlockLeaves.DECAYABLE
		} );
	}


	@Override
	public IBlockState getStateFromMeta( final int meta )
	{
		return this.getDefaultState( ) //
				.withProperty( BlockLeaves.DECAYABLE , Boolean.valueOf( ( meta & 4 ) == 0 ) ) //
				.withProperty( BlockLeaves.CHECK_DECAY , Boolean.valueOf( ( meta & 8 ) > 0 ) );
	}


	@Override
	public int getMetaFromState( final IBlockState state )
	{
		int meta = 0;
		if ( !state.getValue( BlockLeaves.DECAYABLE ) ) {
			meta |= 4;
		}
		if ( state.getValue( BlockLeaves.CHECK_DECAY ) ) {
			meta |= 8;
		}
		return meta;
	}


	@Override
	@SideOnly( Side.CLIENT )
	public BlockRenderLayer getBlockLayer( )
	{
		return Blocks.LEAVES.getBlockLayer( );
	}


	@Override
	public boolean isOpaqueCube( final IBlockState state )
	{
		return Blocks.LEAVES.isOpaqueCube( state );
	}


	@Override
	@SideOnly( Side.CLIENT )
	public boolean shouldSideBeRendered( final IBlockState state , final IBlockAccess world , final BlockPos pos ,
			final EnumFacing side )
	{
		return Blocks.LEAVES.shouldSideBeRendered( state , world , pos , side );
	}


	@Override
	@SideOnly( Side.CLIENT )
	public IBlockColor getBlockTint( )
	{
		return new IBlockColor( ) {

			@Override
			public int colorMultiplier( final IBlockState state , final IBlockAccess worldIn , final BlockPos pos ,
					final int tintIndex )
			{
				final boolean inWorld = worldIn != null && pos != null;
				final int baseTint = inWorld
						? BiomeColorHelper.getFoliageColorAtPos( worldIn , pos )
						: ColorizerFoliage.getFoliageColorBasic( );
				return baseTint;
			}
		};
	}


	@Override
	@SideOnly( Side.CLIENT )
	public IItemColor getItemTint( )
	{
		final IBlockColor bTint = this.getBlockTint( );
		final IBlockState ds = this.getDefaultState( );
		return new IItemColor( ) {

			@Override
			public int getColorFromItemstack( final ItemStack stack , final int tintIndex )
			{
				return bTint.colorMultiplier( ds , null , null , tintIndex );
			}

		};
	}

}
