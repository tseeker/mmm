package mmm.core.api.world;


import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;



public interface I_FloraParameters
{

	public IBlockState getBlockState( World world , BlockPos pos , Random random );


	public boolean canPlaceInChunk( World world , BlockPos pos );


	public int getAmountPerChunk( World world , BlockPos pos , Random random );


	public int getPlacementAttempts( World world , BlockPos pos , Random random );


	public int getSuccessfulPlacements( World world , BlockPos pos , Random random );


	public boolean canPlace( World world , BlockPos pos , Random random );
}
