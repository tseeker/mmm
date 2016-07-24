package mmm.world.trees;


import java.util.Random;

import mmm.MmmMaterials;
import mmm.materials.trees.MTree;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;



public class WTBamboo
		extends A_WTTreeGenerator
{
	public static WTBamboo createSaplingGen( final boolean big )
	{
		return new WTBamboo( big , true , null );
	}

	private final int minHeight;
	private final int randomHeight;
	private final int maxRingRadius;


	public WTBamboo( final boolean big , final boolean notify )
	{
		this( big , notify , MmmMaterials.TREE.BAMBOO );
	}


	private WTBamboo( final boolean big , final boolean notify , final MTree materials )
	{
		super( notify , materials );
		this.minHeight = big ? 13 : 5;
		this.randomHeight = big ? 15 : 8;
		this.maxRingRadius = big ? 2 : 1;
	}


	@Override
	protected RuntimeData determineTreeSize( final BlockPos position , final Random rand )
	{
		return new RuntimeData( position , 2 , this.minHeight + rand.nextInt( this.randomHeight ) );
	}


	@Override
	protected void generateTreeBlocks( final RuntimeData rtd , final Random rand )
	{
		// Trunk
		for ( int y = 0 ; y < rtd.height ; y++ ) {
			rtd.setBlock( 2 , y , 2 , this.getTreeMaterials( ).LOG , E_BlockRequirement.VANILLA );
			rtd.setRequirement( 2 , y , 2 , E_BlockRequirement.VANILLA );
		}

		// Don't share the top space
		for ( int x = 1 ; x <= 3 ; x++ ) {
			for ( int z = 1 ; z <= 3 ; z++ ) {
				rtd.setRequirement( x , rtd.height - 1 , z , E_BlockRequirement.SOFT );
			}
		}

		// Leaves
		final IBlockState leaves = this.getTreeMaterials( ).LEAVES.getDefaultState( ) //
				.withProperty( BlockLeaves.CHECK_DECAY , false );
		int ringY = 1 + rand.nextInt( 3 );
		while ( ringY < rtd.height ) {
			final int radius = 1 + rand.nextInt( this.maxRingRadius );
			final int sqRadius = radius * radius;
			for ( int x = -2 ; x <= 2 ; x++ ) {
				for ( int z = -2 ; z <= 2 ; z++ ) {
					if ( ( x != 0 || z != 0 ) && x * x + z * z <= sqRadius + rand.nextInt( 2 ) ) {
						rtd.setBlock( x + 2 , ringY , z + 2 , leaves , E_BlockRequirement.SOFT );
					}
				}
			}
			ringY += 1 + rand.nextInt( 3 );
		}

	}

}
