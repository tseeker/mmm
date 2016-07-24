package mmm.world.biome;


import java.util.Random;

import mmm.world.gen.WGLimestoneChaos;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;



public class WBLimestonePlateau
		extends A_WBLimestone
{
	private static final WorldGenTaiga1 PINE_GENERATOR = new WorldGenTaiga1( );
	private static final WorldGenTaiga2 SPRUCE_GENERATOR = new WorldGenTaiga2( false );

	private final int chaosChance;


	public WBLimestonePlateau( final WBBuilder helper )
	{
		super( helper.getProperties( ) );

		final boolean trees = helper.hasExtraProperty( "Trees" );
		if ( trees ) {
			this.theBiomeDecorator.treesPerChunk = 5;
			this.theBiomeDecorator.flowersPerChunk = 4;
			this.theBiomeDecorator.grassPerChunk = 1;
		} else {
			this.theBiomeDecorator.treesPerChunk = 0;
			this.theBiomeDecorator.flowersPerChunk = 8;
			this.theBiomeDecorator.grassPerChunk = 3;
		}
		this.theBiomeDecorator.field_189870_A = 0.1f;
		this.topBlock = Blocks.GRASS.getDefaultState( );
		this.fillerBlock = Blocks.DIRT.getDefaultState( );
		this.chaosChance = helper.getExtraProperty( "ChaosChance" , Integer.class , 32 );
	}


	@Override
	public WorldGenAbstractTree genBigTreeChance( final Random rand )
	{
		final int rnd = rand.nextInt( 20 );
		if ( rnd < 1 ) {
			return Biome.BIG_TREE_FEATURE;
		} else if ( rnd < 5 ) {
			return Biome.TREE_FEATURE;
		} else if ( rnd < 7 ) {
			return WBLimestonePlateau.PINE_GENERATOR;
		} else {
			return WBLimestonePlateau.SPRUCE_GENERATOR;
		}
	}


	@Override
	public void decorate( final World worldIn , final Random rand , final BlockPos pos )
	{
		if ( rand.nextInt( this.chaosChance ) == 0 ) {
			new WGLimestoneChaos( ).generate( worldIn , rand , pos );
		}
		super.decorate( worldIn , rand , pos );
	}

}
