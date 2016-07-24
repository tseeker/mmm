package mmm.materials.traps;


import mmm.core.api.blocks.I_TintedBlock;
import mmm.core.api.items.I_TintedItem;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class MSwampPit
		extends MQuicksand
		implements I_TintedBlock , I_TintedItem
{

	public MSwampPit( )
	{
		super( "swamp_pit" , Blocks.GRASS.getDefaultState( ) );
	}


	@Override
	public IBlockState[] getReplacedBlocks( )
	{
		return new IBlockState[] {
				this.forType , Blocks.DIRT.getDefaultState( )
		};
	}


	@Override
	public boolean canSustainPlant( final IBlockState state , final IBlockAccess world , final BlockPos pos ,
			final EnumFacing direction , final IPlantable plantable )
	{
		return false;
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
						? BiomeColorHelper.getGrassColorAtPos( worldIn , pos )
						: ColorizerGrass.getGrassColor( .5f , .5f );
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


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addSmelting( this ,
				new ItemStack( Blocks.DIRT , 1 , BlockDirt.DirtType.COARSE_DIRT.getMetadata( ) ) , .01f );
	}

}
