package mmm.world.biome;


import java.util.Random;

import mmm.MmmMaterials;
import mmm.world.gen.WGBambooPatch;
import mmm.world.trees.A_WTTreeGenerator;
import mmm.world.trees.WTBamboo;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;



public class WBBambooForest
		extends Biome
{
	protected static final A_WTTreeGenerator TG_BAMBOO_BIG = new WTBamboo( true , false );
	protected static final A_WTTreeGenerator TG_BAMBOO = new WTBamboo( false , false );
	protected static final WorldGenShrub TG_BAMBOO_SHRUB = new WorldGenShrub( //
			MmmMaterials.TREE.BAMBOO.LOG.getDefaultState( ) , //
			MmmMaterials.TREE.BAMBOO.LEAVES.getDefaultState( ).withProperty( BlockLeaves.CHECK_DECAY , false ) );

	private final int bigThreshold;


	public WBBambooForest( final WBBuilder helper )
	{
		super( helper.getProperties( ) );

		this.theBiomeDecorator.treesPerChunk = 10;
		this.theBiomeDecorator.grassPerChunk = 2;
		this.theBiomeDecorator.flowersPerChunk = 5;
		this.spawnableCreatureList.add( new Biome.SpawnListEntry( EntityRabbit.class , 4 , 2 , 3 ) );

		if ( helper.hasExtraProperty( "Dense" ) ) {
			this.theBiomeDecorator.treesPerChunk *= 2;
			this.bigThreshold = 10;
		} else {
			this.bigThreshold = 2;
		}
	}


	@Override
	public WorldGenAbstractTree genBigTreeChance( final Random rand )
	{
		final int r = rand.nextInt( 50 );
		if ( r < this.bigThreshold ) {
			return WBBambooForest.TG_BAMBOO_BIG;
		} else if ( r < this.bigThreshold + 5 ) {
			return WBBambooForest.TG_BAMBOO_SHRUB;
		} else {
			return WBBambooForest.TG_BAMBOO;
		}
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
		for ( int cx = pos.getX( ) ; cx < pos.getX( ) + 16 ; cx++ ) {
			for ( int cz = pos.getZ( ) ; cz < pos.getZ( ) + 16 ; cz++ ) {
				final double noise = Biome.GRASS_COLOR_NOISE.getValue( cz * 0.25D , cx * 0.25D );
				if ( noise > .75 ) {
					for ( int y = 255 ; y >= 0 ; y-- ) {
						final BlockPos bp = new BlockPos( cx , y , cz );
						if ( this.checkGround( worldIn , bp ) ) {
							if ( this.checkGround( worldIn , bp.north( ) ) //
									&& this.checkGround( worldIn , bp.south( ) ) //
									&& this.checkGround( worldIn , bp.east( ) ) //
									&& this.checkGround( worldIn , bp.west( ) )
									&& this.checkGround( worldIn , bp.down( ) ) ) {
								worldIn.setBlockState( bp , Biome.WATER );
							}
							break;
						}
					}
				}
			}
		}

		super.decorate( worldIn , rand , pos );

		if ( rand.nextInt( 20 ) == 0 ) {
			new WGBambooPatch( ).generate( worldIn , rand , pos );
		}

		final WorldGenVines worldgenvines = new WorldGenVines( );
		for ( int i = 0 ; i < 50 ; i++ ) {
			final int x = rand.nextInt( 16 ) + 8;
			final int z = rand.nextInt( 16 ) + 8;
			worldgenvines.generate( worldIn , rand , pos.add( x , 128 , z ) );
		}
	}


	@Override
	public void addDefaultFlowers( )
	{
		this.addFlower( Blocks.RED_FLOWER.getDefaultState( ) , 10 );
		this.addFlower(
				Blocks.RED_FLOWER.getDefaultState( ) //
						.withProperty( Blocks.RED_FLOWER.getTypeProperty( ) , BlockFlower.EnumFlowerType.ALLIUM ) ,
				10 );
		this.addFlower(
				Blocks.RED_FLOWER.getDefaultState( ) //
						.withProperty( Blocks.RED_FLOWER.getTypeProperty( ) , BlockFlower.EnumFlowerType.BLUE_ORCHID ) ,
				1 );
		this.addFlower( Blocks.YELLOW_FLOWER.getDefaultState( ) , 5 );
	}


	private boolean checkGround( final World world , final BlockPos pos )
	{
		return world.getBlockState( pos ).getBlock( ) == Blocks.GRASS
				|| world.getBlockState( pos ).getBlock( ) == Blocks.DIRT
				|| world.getBlockState( pos ).getBlock( ) == Blocks.WATER;
	}
}
