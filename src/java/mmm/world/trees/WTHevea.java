package mmm.world.trees;


import java.util.Random;

import mmm.MmmMaterials;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;



public class WTHevea
		extends A_WTTreeGenerator
{

	public static WTHevea createSaplingGen( )
	{
		return new WTHevea( );
	}


	public WTHevea( final boolean notify )
	{
		super( notify , MmmMaterials.TREE.HEVEA );
	}


	// Used for sapling gen
	protected WTHevea( )
	{
		super( true , null );
	}


	@Override
	protected RuntimeData determineTreeSize( final BlockPos position , final Random rand )
	{
		final int leavesHeight = rand.nextInt( 2 ) + 2;
		final int totalHeight = 1 + rand.nextInt( 2 ) + leavesHeight * 5 / 2;
		return new RuntimeData( position , leavesHeight , totalHeight );
	}


	@Override
	protected void generateTreeBlocks( final RuntimeData rtd , final Random rand )
	{
		final int leavesHeight = ( rtd.xSize - 1 ) / 2;
		final int totalLeavesHeight = leavesHeight * 5 / 2;
		final int trunkBottom = rtd.height - totalLeavesHeight;
		final int trunkHeight = Math.max( trunkBottom + 1 , rtd.height - ( 1 + rand.nextInt( 2 ) ) );

		this.makeLeaves( rtd , rand , leavesHeight , trunkBottom );
		this.makeTrunk( rtd , rand , leavesHeight , trunkHeight );
	}


	protected void makeLeaves( final RuntimeData rtd , final Random rand , final int leavesHeight ,
			final int trunkBottom )
	{
		final int lowerPart = trunkBottom + leavesHeight * 2;

		// Lower part of the leaves
		final IBlockState leaves = this.getTreeMaterials( ).LEAVES.getDefaultState( )//
				.withProperty( BlockLeaves.CHECK_DECAY , false );
		for ( int y = trunkBottom , i = 0 ; y < lowerPart ; y++ , i++ ) {
			final int radius = ( i >> 1 ) + 1;
			final int rSquare = radius * radius + ( i & 1 );
			for ( int x = -radius ; x <= radius ; x++ ) {
				for ( int z = -radius ; z <= radius ; z++ ) {
					if ( x * x + z * z <= rSquare ) {
						rtd.setBlock( leavesHeight + x , y , leavesHeight + z , leaves , E_BlockRequirement.SOFT );
					}
				}
			}
		}

		// Higher part of the leaves
		for ( int y = lowerPart , radius = leavesHeight - 1 ; y < rtd.height ; y++ , radius -= 2 ) {
			if ( radius <= 0 ) {
				radius = 1;
			}
			final int rSquare = radius * radius + 1;
			for ( int x = -radius ; x <= radius ; x++ ) {
				for ( int z = -radius ; z <= radius ; z++ ) {
					if ( x * x + z * z <= rSquare ) {
						rtd.setBlock( leavesHeight + x , y , leavesHeight + z , leaves , E_BlockRequirement.SOFT );
					}
				}
			}
		}

		// Randomly remove leaves from corners
		rtd.removeCornerLeaves( rand , leaves , trunkBottom , rtd.height - 1 , .4f );
	}


	protected void makeTrunk( final RuntimeData rtd , final Random rand , final int leavesHeight ,
			final int trunkHeight )
	{
		// Trunk
		for ( int y = 0 ; y < trunkHeight ; y++ ) {
			rtd.setBlock( leavesHeight , y , leavesHeight , this.getTreeMaterials( ).LOG , E_BlockRequirement.VANILLA );
			rtd.setRequirement( leavesHeight , y , leavesHeight , E_BlockRequirement.VANILLA );
		}
	}

}
