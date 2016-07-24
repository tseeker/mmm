package mmm.world.biome;


import java.util.Random;

import mmm.MmmMaterials;
import mmm.world.trees.A_WTTreeGenerator;
import mmm.world.trees.WTHevea;
import mmm.world.trees.WTHeveaBig;
import mmm.world.trees.WTHeveaMega;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTrees;



public class WBTropicalForest
		extends WBBambooForest
{

	private static final IBlockState JUNGLE_LOG = Blocks.LOG.getDefaultState( ) //
			.withProperty( BlockOldLog.VARIANT , BlockPlanks.EnumType.JUNGLE );
	private static final IBlockState JUNGLE_LEAF = Blocks.LEAVES.getDefaultState( )
			.withProperty( BlockOldLeaf.VARIANT , BlockPlanks.EnumType.JUNGLE )
			.withProperty( BlockLeaves.CHECK_DECAY , false );

	private static final IBlockState HEVEA_LOG = MmmMaterials.TREE.HEVEA.LOG.getDefaultState( );
	private static final IBlockState HEVEA_LEAF = MmmMaterials.TREE.HEVEA.LEAVES.getDefaultState( ) //
			.withProperty( BlockLeaves.CHECK_DECAY , false );

	private static final A_WTTreeGenerator TG_HEVEA_GIANT = new WTHeveaMega( false );
	private static final A_WTTreeGenerator TG_HEVEA_BIG = new WTHeveaBig( false );
	private static final A_WTTreeGenerator TG_HEVEA = new WTHevea( false );
	protected static final WorldGenShrub TG_HEVEA_SHRUB = new WorldGenShrub( //
			WBTropicalForest.HEVEA_LOG , WBTropicalForest.HEVEA_LEAF );

	private static final WorldGenMegaJungle TG_JUNGLE = new WorldGenMegaJungle( false , 10 , 20 ,
			WBTropicalForest.JUNGLE_LOG , WBTropicalForest.JUNGLE_LEAF );
	protected static final WorldGenShrub TG_JUNGLE_SHRUB = new WorldGenShrub( //
			WBTropicalForest.JUNGLE_LOG , WBTropicalForest.JUNGLE_LEAF );


	public WBTropicalForest( final WBBuilder helper )
	{
		super( helper );
		this.theBiomeDecorator.treesPerChunk = 100;
		this.spawnableMonsterList.add( new Biome.SpawnListEntry( EntityOcelot.class , 2 , 1 , 1 ) );
	}


	@Override
	public WorldGenAbstractTree genBigTreeChance( final Random rand )
	{
		final int r = rand.nextInt( 60 );
		if ( r < 1 ) {
			return WBTropicalForest.TG_HEVEA_GIANT;
		} else if ( r < 5 ) {
			return WBTropicalForest.TG_JUNGLE;
		} else if ( r < 10 ) {
			return WBBambooForest.TG_BAMBOO_BIG;
		} else if ( r < 15 ) {
			return WBTropicalForest.TG_HEVEA_BIG;
		} else if ( r < 25 ) {
			return new WorldGenTrees( false , 4 + rand.nextInt( 7 ) , WBTropicalForest.JUNGLE_LOG ,
					WBTropicalForest.JUNGLE_LEAF , true );
		} else if ( r < 35 ) {
			return WBTropicalForest.TG_HEVEA;
		} else if ( r < 45 ) {
			return WBBambooForest.TG_BAMBOO;
		} else if ( r < 50 ) {
			return WBBambooForest.TG_BAMBOO_SHRUB;
		} else if ( r < 55 ) {
			return WBTropicalForest.TG_JUNGLE_SHRUB;
		} else {
			return WBTropicalForest.TG_HEVEA_SHRUB;
		}
	}

}
