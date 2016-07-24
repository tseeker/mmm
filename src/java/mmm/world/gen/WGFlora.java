package mmm.world.gen;


import java.util.ArrayList;
import java.util.Random;

import mmm.core.CRegistry;
import mmm.core.api.I_FloraRegistrar;
import mmm.core.api.world.I_BiomeWithFlora;
import mmm.core.api.world.I_FloraParameters;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;



public class WGFlora
		extends WorldGenerator
{

	private final ArrayList< I_FloraParameters > allConditions;


	public WGFlora( )
	{
		final ArrayList< I_FloraParameters > conditions = new ArrayList<>( );
		for ( final I_FloraRegistrar registrar : CRegistry.getFloraRegistrars( ) ) {
			registrar.getFloraGeneration( conditions );
		}
		this.allConditions = conditions;
	}


	@Override
	public boolean generate( final World worldIn , final Random rand , final BlockPos position )
	{
		final ArrayList< I_FloraParameters > candidates = this.getValidFlora( worldIn , position );
		final int nCandidates = candidates.size( );
		if ( nCandidates == 0 ) {
			return false;
		}

		for ( int i = 0 ; i < nCandidates ; i++ ) {
			this.tryGenerate( worldIn , rand , position , candidates.get( i ) );
		}
		return true;
	}


	private ArrayList< I_FloraParameters > getValidFlora( final World worldIn , final BlockPos position )
	{
		final ArrayList< I_FloraParameters > output = new ArrayList<>( );
		for ( final I_FloraParameters item : this.allConditions ) {
			if ( item.canPlaceInChunk( worldIn , position ) ) {
				output.add( item );
			}
		}

		final Biome biome = worldIn.getBiomeGenForCoords( position );
		if ( biome instanceof I_BiomeWithFlora ) {
			( (I_BiomeWithFlora) biome ).addFloraParameters( output );
		}

		return output;
	}


	private void tryGenerate( final World worldIn , final Random rand , final BlockPos position ,
			final I_FloraParameters parameters )
	{
		final int pc = parameters.getAmountPerChunk( worldIn , position , rand );
		for ( int i = 0 ; i < pc ; ++i ) {
			final int x = rand.nextInt( 16 ) + 8;
			final int z = rand.nextInt( 16 ) + 8;

			final int height = worldIn.getHeight( position.add( x , 0 , z ) ).getY( ) * 2;
			if ( height <= 0 ) {
				continue;
			}

			this.tryGeneratePlant( worldIn , rand , position.add( x , rand.nextInt( height ) , z ) , parameters );
		}
	}


	private void tryGeneratePlant( final World worldIn , final Random rand , BlockPos position ,
			final I_FloraParameters parameters )
	{
		if ( worldIn.provider.getDimensionType( ) == DimensionType.NETHER ) {
			position = this.findNetherPosition( worldIn , rand , position );
		} else {
			position = this.findOverwordOrEndPosition( worldIn , position );
		}

		final int attempts = parameters.getPlacementAttempts( worldIn , position , rand );
		int successes = parameters.getSuccessfulPlacements( worldIn , position , rand );
		for ( int i = 0 ; i < attempts ; ++i ) {
			final BlockPos blockpos = position.add( //
					rand.nextInt( 8 ) - rand.nextInt( 8 ) , //
					rand.nextInt( 4 ) - rand.nextInt( 4 ) , //
					rand.nextInt( 8 ) - rand.nextInt( 8 ) );
			if ( !parameters.canPlace( worldIn , blockpos , rand ) ) {
				continue;
			}

			worldIn.setBlockState( blockpos , parameters.getBlockState( worldIn , blockpos , rand ) , 2 );
			successes--;
			if ( successes == 0 ) {
				break;
			}
		}
	}


	private BlockPos findNetherPosition( final World worldIn , final Random rand , BlockPos position )
	{
		float keepGoing = 1.6f;
		do {
			keepGoing *= .5f;
			IBlockState bs = worldIn.getBlockState( position );
			while ( ( bs.getBlock( ) == Blocks.NETHERRACK || bs.getBlock( ) == Blocks.BEDROCK
					|| bs.getMaterial( ).isLiquid( ) ) && position.getY( ) > 0 ) {
				position = position.down( );
				bs = worldIn.getBlockState( position );
			}
			while ( bs.getBlock( ).isAir( bs , worldIn , position ) && position.getY( ) > 0 ) {
				position = position.down( );
				bs = worldIn.getBlockState( position );
			}
		} while ( position.getY( ) > 0 && rand.nextFloat( ) < keepGoing );
		return position;
	}


	private BlockPos findOverwordOrEndPosition( final World worldIn , BlockPos position )
	{
		IBlockState bs = worldIn.getBlockState( position );
		while ( ( bs.getBlock( ).isAir( bs , worldIn , position ) //
				|| bs.getBlock( ).isLeaves( bs , worldIn , position ) ) && position.getY( ) > 0 ) {
			position = position.down( );
			bs = worldIn.getBlockState( position );
		}
		return position;
	}

}
