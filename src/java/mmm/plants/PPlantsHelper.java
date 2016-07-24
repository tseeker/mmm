package mmm.plants;


import mmm.core.CRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;



public class PPlantsHelper
{

	public static ItemFood makeFruit( final String name , final int food , final float saturation )
	{
		final ItemFood fruit = new ItemFood( food , saturation , false );
		CRegistry.setIdentifiers( fruit , "plant" , "fruit" , name );
		CRegistry.addItem( fruit );
		return fruit;
	}


	public static ItemSeeds makeSeeds( final String name , final Block plant )
	{
		final ItemSeeds seeds = new ItemSeeds( plant , Blocks.FARMLAND );
		CRegistry.setIdentifiers( seeds , "plant" , "seeds" , name );
		CRegistry.addItem( seeds );
		return seeds;
	}


	public static float getGrowthChance( final Block blockIn , final World worldIn , final BlockPos pos )
	{
		float total = 1.0f;
		final BlockPos blockpos = pos.down( );

		for ( int xo = -1 ; xo <= 1 ; ++xo ) {
			for ( int zo = -1 ; zo <= 1 ; ++zo ) {
				float weight = 0f;
				final IBlockState neighbor = worldIn.getBlockState( blockpos.add( xo , 0 , zo ) );
				final Block block = neighbor.getBlock( );
				if ( block.canSustainPlant( neighbor , worldIn , blockpos.add( xo , 0 , zo ) , EnumFacing.UP ,
						(IPlantable) blockIn ) ) {
					weight = 1f;
					if ( block.isFertile( worldIn , blockpos.add( xo , 0 , zo ) ) ) {
						weight = 3f;
					}
				}
				if ( xo != 0 || zo != 0 ) {
					weight /= 4f;
				}
				total += weight;
			}
		}

		final BlockPos bpNorth = pos.north( );
		final BlockPos bpSouth = pos.south( );
		final BlockPos bpWest = pos.west( );
		final BlockPos bpEast = pos.east( );

		final boolean surroundedEW = blockIn == worldIn.getBlockState( bpWest ).getBlock( )
				|| blockIn == worldIn.getBlockState( bpEast ).getBlock( );
		final boolean surroundedNS = blockIn == worldIn.getBlockState( bpNorth ).getBlock( )
				|| blockIn == worldIn.getBlockState( bpSouth ).getBlock( );
		if ( surroundedEW && surroundedNS //
				|| blockIn == worldIn.getBlockState( bpWest.north( ) ).getBlock( )
				|| blockIn == worldIn.getBlockState( bpEast.north( ) ).getBlock( )
				|| blockIn == worldIn.getBlockState( bpEast.south( ) ).getBlock( )
				|| blockIn == worldIn.getBlockState( bpWest.south( ) ).getBlock( ) ) {
			total /= 2f;
		}

		return total;
	}

}
