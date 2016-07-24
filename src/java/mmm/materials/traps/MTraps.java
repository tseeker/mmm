package mmm.materials.traps;


import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;

import mmm.core.CRegistry;
import mmm.core.api.blocks.I_TrapBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;



public class MTraps
{
	public final MQuicksand QUICKSAND;
	public final MQuicksand RED_QUICKSAND;
	public final MSwampPit SWAMP_PIT;
	public final MMud MUD;

	private final ArrayListMultimap< IBlockState , I_TrapBlock > traps = ArrayListMultimap.create( );
	private final ArrayListMultimap< IBlockState , IBlockState > replacements = ArrayListMultimap.create( );
	private final LinkedHashMultimap< IBlockState , String > trapTypes = LinkedHashMultimap.create( );


	public MTraps( )
	{
		// FIXME recipes to convert quicksand into sand or mud into dirt
		this.QUICKSAND = this.register( new MQuicksand( "sand" , //
				Blocks.SAND.getDefaultState( ) //
						.withProperty( BlockSand.VARIANT , BlockSand.EnumType.SAND ) ) );
		this.RED_QUICKSAND = this.register( new MQuicksand( "red_sand" , //
				Blocks.SAND.getDefaultState( ) //
						.withProperty( BlockSand.VARIANT , BlockSand.EnumType.RED_SAND ) ) );
		this.SWAMP_PIT = this.register( new MSwampPit( ) );
		this.MUD = this.register( new MMud( ) );
	}


	public < T extends Block & I_TrapBlock > T register( final T block )
	{
		final Block asMcBlock = block;
		for ( final IBlockState state : block.getReplacedBlocks( ) ) {
			this.traps.put( state , block );
			this.trapTypes.put( state , block.getTrapType( ) );
			this.replacements.put( state , asMcBlock.getDefaultState( ) );
		}
		CRegistry.addBlock( asMcBlock );
		return block;
	}


	public List< IBlockState > getReplacements( final IBlockState blockState )
	{
		return this.replacements.get( blockState );
	}


	public List< IBlockState > getReplacements( final IBlockState blockState , final String trapType )
	{
		final ArrayList< IBlockState > out = Lists.newArrayList( );
		final List< I_TrapBlock > traps = this.traps.get( blockState );
		final int nTraps = traps.size( );
		for ( int i = 0 ; i < nTraps ; i++ ) {
			final I_TrapBlock trap = traps.get( i );
			if ( trap.getTrapType( ).equals( trapType ) ) {
				out.add( ( (Block) trap ).getDefaultState( ) );
			}
		}
		return out;
	}


	public ArrayList< String > getTrapTypes( final IBlockState blockState )
	{
		return Lists.newArrayList( this.trapTypes.get( blockState ) );
	}
}
