package mmm.world.biome;


import java.util.Random;

import mmm.MmmMaterials;
import mmm.core.api.world.I_BiomeWithOres;
import mmm.core.api.world.I_TrappedBiome;
import mmm.world.gen.WGBambooPatch;
import mmm.world.gen.WGOreParameters;
import mmm.world.trees.A_WTTreeGenerator;
import mmm.world.trees.WTBamboo;
import mmm.world.trees.WTHevea;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenFossils;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;



public class WBTropicalSwamp
		extends Biome
		implements I_BiomeWithOres , I_TrappedBiome
{
	private static final IBlockState OAK_LOG = Blocks.LOG.getDefaultState( ) //
			.withProperty( BlockOldLog.VARIANT , BlockPlanks.EnumType.OAK );
	private static final IBlockState OAK_LEAF = Blocks.LEAVES.getDefaultState( ) //
			.withProperty( BlockOldLeaf.VARIANT , BlockPlanks.EnumType.OAK ) //
			.withProperty( BlockLeaves.CHECK_DECAY , false );

	private static final IBlockState WATER_LILY = Blocks.WATERLILY.getDefaultState( );

	private static final A_WTTreeGenerator TG_BAMBOO_BIG = new WTBamboo( true , false );
	private static final A_WTTreeGenerator TG_BAMBOO = new WTBamboo( false , false );
	private static final A_WTTreeGenerator TG_HEVEA = new WTHevea( false );

	private static final WorldGenShrub TG_SHRUB = new WorldGenShrub( //
			WBTropicalSwamp.OAK_LOG , WBTropicalSwamp.OAK_LEAF );


	public WBTropicalSwamp( final WBBuilder helper )
	{
		super( helper.getProperties( ) );

		this.theBiomeDecorator.treesPerChunk = 10;
		this.theBiomeDecorator.flowersPerChunk = 3;
		this.theBiomeDecorator.deadBushPerChunk = 1;
		this.theBiomeDecorator.mushroomsPerChunk = 2;
		this.theBiomeDecorator.reedsPerChunk = 10;
		this.theBiomeDecorator.clayPerChunk = 2;
		this.theBiomeDecorator.waterlilyPerChunk = 4;
		this.theBiomeDecorator.sandPerChunk2 = 0;
		this.theBiomeDecorator.sandPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 5;

		this.spawnableMonsterList.add( new Biome.SpawnListEntry( EntitySlime.class , 1 , 1 , 1 ) );
	}


	@Override
	public WorldGenAbstractTree genBigTreeChance( final Random rand )
	{
		final int treeTypes = rand.nextInt( 30 );
		if ( treeTypes < 3 ) {
			return WBTropicalSwamp.TG_SHRUB;
		} else if ( treeTypes < 7 ) {
			return new WorldGenTrees( false , 2 + rand.nextInt( 5 ) , WBTropicalSwamp.OAK_LOG ,
					WBTropicalSwamp.OAK_LEAF , true );
		} else if ( treeTypes < 27 ) {
			return WBTropicalSwamp.TG_HEVEA;
		} else if ( treeTypes < 29 ) {
			return WBTropicalSwamp.TG_BAMBOO;
		} else {
			return WBTropicalSwamp.TG_BAMBOO_BIG;
		}
	}


	@Override
	public void genTerrainBlocks( final World worldIn , final Random rand , final ChunkPrimer chunkPrimerIn ,
			final int x , final int z , final double noiseVal )
	{
		final double d0 = Biome.GRASS_COLOR_NOISE.getValue( x * 0.25D , z * 0.25D );
		if ( d0 > 0.0D ) {
			final int i = x & 15;
			final int j = z & 15;

			for ( int k = 255 ; k >= 0 ; --k ) {
				if ( chunkPrimerIn.getBlockState( j , k , i ).getMaterial( ) != Material.AIR ) {
					if ( k == 62 && chunkPrimerIn.getBlockState( j , k , i ).getBlock( ) != Blocks.WATER ) {
						chunkPrimerIn.setBlockState( j , k , i , Biome.WATER );
						if ( d0 < 0.12D ) {
							chunkPrimerIn.setBlockState( j , k + 1 , i , WBTropicalSwamp.WATER_LILY );
						}
					}
					break;
				}
			}
		}

		this.generateBiomeTerrain( worldIn , rand , chunkPrimerIn , x , z , noiseVal );
	}


	@Override
	public WGOreParameters[] getBiomeOres( final World world )
	{
		return new WGOreParameters[] {
				new WGOreParameters( MmmMaterials.ROCK.SLATE , 20 , 60 )
		};
	}


	@Override
	public WorldGenerator getRandomWorldGenForGrass( final Random rand )
	{
		return rand.nextInt( 4 ) == 0
				? new WorldGenTallGrass( BlockTallGrass.EnumType.FERN )
				: new WorldGenTallGrass( BlockTallGrass.EnumType.GRASS );
	}


	@Override
	public void decorate( final World worldIn , final Random rand , final BlockPos pos )
	{
		super.decorate( worldIn , rand , pos );

		if ( rand.nextInt( 10 ) == 0 ) {
			new WGBambooPatch( ).generate( worldIn , rand , pos );
		}

		final WorldGenVines worldgenvines = new WorldGenVines( );
		for ( int i = 0 ; i < 50 ; i++ ) {
			final int x = rand.nextInt( 16 ) + 8;
			final int z = rand.nextInt( 16 ) + 8;
			worldgenvines.generate( worldIn , rand , pos.add( x , 128 , z ) );
		}

		if ( rand.nextInt( 64 ) == 0 ) {
			new WorldGenFossils( ).generate( worldIn , rand , pos );
		}
	}


	@Override
	public void addDefaultFlowers( )
	{
		this.addFlower( Blocks.RED_FLOWER.getDefaultState( ).withProperty( Blocks.RED_FLOWER.getTypeProperty( ) ,
				BlockFlower.EnumFlowerType.BLUE_ORCHID ) , 10 );
	}


	@Override
	public BlockFlower.EnumFlowerType pickRandomFlower( final Random rand , final BlockPos pos )
	{
		return BlockFlower.EnumFlowerType.BLUE_ORCHID;
	}


	@Override
	public float getTrapBlockChance( )
	{
		return .05f;
	}

}
