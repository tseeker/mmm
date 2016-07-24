package mmm.world.biome;


import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;



public class WBLimestoneMountains
		extends A_WBLimestone
{
	private static final WorldGenTaiga2 SPRUCE_GENERATOR = new WorldGenTaiga2( false );

	private static final IBlockState BS_GRASS = Blocks.GRASS.getDefaultState( );
	private static final IBlockState BS_DIRT = Blocks.DIRT.getDefaultState( );
	private static final IBlockState BS_COARSE_DIRT = Blocks.DIRT.getDefaultState( )//
			.withProperty( BlockDirt.VARIANT , BlockDirt.DirtType.COARSE_DIRT );
	private static final IBlockState BS_GRAVEL = Blocks.GRAVEL.getDefaultState( );
	private static final IBlockState BS_STONE = Blocks.STONE.getDefaultState( );

	private final boolean chaoticAreas;


	public WBLimestoneMountains( final WBBuilder helper )
	{
		super( helper.getProperties( ) );

		if ( helper.hasExtraProperty( "Trees" ) ) {
			this.theBiomeDecorator.treesPerChunk = 2;
			this.theBiomeDecorator.flowersPerChunk = 2;
			this.theBiomeDecorator.grassPerChunk = 2;
		} else {
			this.theBiomeDecorator.treesPerChunk = 0;
			this.theBiomeDecorator.flowersPerChunk = 2;
			this.theBiomeDecorator.grassPerChunk = 2;
		}
		this.theBiomeDecorator.field_189870_A = 0.1f;

		this.chaoticAreas = helper.hasExtraProperty( "Chaos" );
	}


	@Override
	public WorldGenAbstractTree genBigTreeChance( final Random rand )
	{
		return rand.nextInt( 3 ) > 0 ? WBLimestoneMountains.SPRUCE_GENERATOR : super.genBigTreeChance( rand );
	}


	@Override
	public void genTerrainBlocks( final World worldIn , final Random rand , final ChunkPrimer chunkPrimerIn ,
			final int x , final int z , final double noiseVal )
	{
		if ( noiseVal > 1. && ( noiseVal <= 2. || !this.chaoticAreas ) ) {
			this.topBlock = this.fillerBlock = WBLimestoneMountains.BS_STONE;
		} else {
			this.topBlock = WBLimestoneMountains.BS_GRASS;
			this.fillerBlock = WBLimestoneMountains.BS_DIRT;
		}
		this.generateBiomeTerrain( worldIn , rand , chunkPrimerIn , x , z , noiseVal );

		final double gcn = Biome.GRASS_COLOR_NOISE.getValue( z / 32. , x / 32. );
		if ( gcn > .25 && this.chaoticAreas ) {
			this.genChaoticArea( rand , chunkPrimerIn , x , z , (int) ( ( gcn - .25 ) * 6 ) );
		}
	}


	private void genChaoticArea( final Random rand , final ChunkPrimer chunkPrimerIn , final int x , final int z ,
			final int heightMax )
	{
		final int chunkX = x & 15 , chunkZ = z & 15;
		final int groundY = this.findGround( chunkPrimerIn , chunkX , chunkZ );
		if ( groundY == -1 ) {
			return;
		}

		final int stoneY = this.findStone( chunkPrimerIn , chunkX , groundY , chunkZ );
		if ( stoneY == -1 ) {
			return;
		}

		final int topY = groundY + rand.nextInt( heightMax + 1 );
		for ( int y = stoneY ; y <= topY ; y++ ) {
			final int v = rand.nextInt( 20 );
			IBlockState randBlock;
			if ( v < 1 ) {
				randBlock = WBLimestoneMountains.BS_GRAVEL;
			} else if ( v < 3 ) {
				randBlock = WBLimestoneMountains.BS_COARSE_DIRT;
			} else {
				randBlock = WBLimestoneMountains.BS_STONE;
			}
			chunkPrimerIn.setBlockState( chunkX , y , chunkZ , randBlock );
		}
	}


	private int findGround( final ChunkPrimer chunkPrimerIn , final int x , final int z )
	{
		for ( int y = 255 ; y >= 0 ; --y ) {
			if ( chunkPrimerIn.getBlockState( x , y , z ).getMaterial( ) != Material.AIR ) {
				return y;
			}
		}
		return -1;
	}


	private int findStone( final ChunkPrimer chunkPrimerIn , final int x , final int fromY , final int z )
	{
		for ( int y = fromY ; y >= 0 ; --y ) {
			if ( chunkPrimerIn.getBlockState( x , y , z ).getMaterial( ) == Material.AIR ) {
				return -1;
			}
			if ( chunkPrimerIn.getBlockState( x , y , z ) == WBLimestoneMountains.BS_STONE ) {
				return y;
			}
		}
		return -1;
	}
}
