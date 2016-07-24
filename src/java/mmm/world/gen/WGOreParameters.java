package mmm.world.gen;


import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;



public class WGOreParameters
{

	public final IBlockState ore;
	public final int clusters;
	public final int clusterSize;
	public final int minHeight;
	public final int maxHeight;
	public Predicate< IBlockState > matcher;

	private WorldGenMinable generator;


	public WGOreParameters( final Block ore , final int clusters , final int clusterSize )
	{
		this( ore.getDefaultState( ) , clusters , clusterSize , Integer.MIN_VALUE , Integer.MIN_VALUE ,
				BlockMatcher.forBlock( Blocks.STONE ) );
	}


	public WGOreParameters( final Block ore , final int clusters , final int clusterSize , final int minHeight ,
			final int maxHeight )
	{
		this( ore.getDefaultState( ) , clusters , clusterSize , minHeight , maxHeight ,
				BlockMatcher.forBlock( Blocks.STONE ) );
	}


	public WGOreParameters( final Block ore , final int clusters , final int clusterSize ,
			final Predicate< IBlockState > matcher )
	{
		this( ore.getDefaultState( ) , clusters , clusterSize , Integer.MIN_VALUE , Integer.MIN_VALUE , matcher );
	}


	public WGOreParameters( final Block ore , final int clusters , final int clusterSize , final int minHeight ,
			final int maxHeight , final Predicate< IBlockState > matcher )
	{
		this( ore.getDefaultState( ) , clusters , clusterSize , minHeight , maxHeight , matcher );
	}


	public WGOreParameters( final IBlockState ore , final int clusters , final int clusterSize )
	{
		this( ore , clusters , clusterSize , Integer.MIN_VALUE , Integer.MIN_VALUE ,
				BlockMatcher.forBlock( Blocks.STONE ) );
	}


	public WGOreParameters( final IBlockState ore , final int clusters , final int clusterSize , final int minHeight ,
			final int maxHeight )
	{
		this( ore , clusters , clusterSize , minHeight , maxHeight , BlockMatcher.forBlock( Blocks.STONE ) );
	}


	public WGOreParameters( final IBlockState ore , final int clusters , final int clusterSize ,
			final Predicate< IBlockState > matcher )
	{
		this( ore , clusters , clusterSize , Integer.MIN_VALUE , Integer.MIN_VALUE , matcher );
	}


	public WGOreParameters( final IBlockState ore , final int clusters , final int clusterSize , int minHeight ,
			int maxHeight , final Predicate< IBlockState > matcher )
	{
		if ( clusters < 1 ) {
			throw new IllegalArgumentException( "cluster count should be at least 1" );
		}
		if ( clusterSize < 1 ) {
			throw new IllegalArgumentException( "cluster size should be at least 1" );
		}

		if ( minHeight != Integer.MIN_VALUE || maxHeight != Integer.MIN_VALUE ) {
			if ( minHeight > maxHeight ) {
				throw new IllegalArgumentException( "min height should be <= max height" );
			}
			if ( minHeight < 0 ) {
				throw new IllegalArgumentException( "min height should be at least 0" );
			}
			if ( maxHeight > 255 ) {
				throw new IllegalArgumentException( "max height should be at most 255" );
			}
			if ( maxHeight == minHeight ) {
				if ( minHeight < 255 ) {
					++maxHeight;
				} else {
					--minHeight;
				}
			}
		} else {
			minHeight = 0;
			maxHeight = 255;
		}

		this.ore = ore;
		this.clusters = clusters;
		this.clusterSize = clusterSize;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.matcher = matcher;
	}


	public void generate( final World worldIn , final Random random , final int chunkX , final int chunkZ )
	{
		if ( this.generator == null ) {
			this.generator = new WorldGenMinable( this.ore , this.clusterSize , this.matcher );
		}

		for ( int j = 0 ; j < this.clusters ; ++j ) {
			final BlockPos blockpos = new BlockPos( //
					chunkX * 16 + random.nextInt( 16 ) , //
					MathHelper.getRandomIntegerInRange( random , this.minHeight , this.maxHeight ) , //
					chunkZ * 16 + random.nextInt( 16 ) );
			this.generator.generate( worldIn , random , blockpos );
		}
	}

}
