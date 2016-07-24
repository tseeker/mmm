package mmm.materials;


import java.util.HashMap;
import java.util.HashSet;

import mmm.materials.trees.MLog;
import mmm.materials.trees.MPlanks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;



public class MBlockTypes
{
	private static final HashMap< String , MBlockType > TYPES;

	public static final MBlockType ROCK;
	public static final MBlockType LOG;
	public static final MBlockType PLANKS;
	public static final MBlockType WOOD;
	public static final MBlockType ORE;
	public static final MBlockType METAL;

	static {
		TYPES = new HashMap<>( );
		{
			ROCK = new MBlockType( "ROCK" , ( bs ) -> {
				final Block block = bs.getBlock( );
				if ( block instanceof BlockStone ) {
					final BlockStone.EnumType variant = bs.getValue( BlockStone.VARIANT );
					return variant == BlockStone.EnumType.ANDESITE || variant == BlockStone.EnumType.DIORITE
							|| variant == BlockStone.EnumType.GRANITE || variant == BlockStone.EnumType.STONE;
				}
				if ( block == Blocks.COBBLESTONE ) {
					return true;
				}
				return block instanceof I_MRock;
			} );
		}
		{
			LOG = new MBlockType( "LOG" , ( bs ) -> {
				final Block block = bs.getBlock( );
				return block == Blocks.LOG || block == Blocks.LOG2 || block instanceof MLog;
			} );
		}
		{
			PLANKS = new MBlockType( "PLANKS" , ( bs ) -> {
				final Block block = bs.getBlock( );
				return block == Blocks.PLANKS || block instanceof MPlanks;
			} );
		}
		{
			WOOD = new MBlockType( "WOOD" ,
					( bs ) -> MBlockTypes.LOG.matches( bs ) || MBlockTypes.PLANKS.matches( bs ) );
		}
		{
			final HashSet< Block > builtinOres = new HashSet<>( );
			builtinOres.add( Blocks.COAL_ORE );
			builtinOres.add( Blocks.IRON_ORE );
			builtinOres.add( Blocks.GOLD_ORE );
			builtinOres.add( Blocks.DIAMOND_ORE );
			builtinOres.add( Blocks.LAPIS_ORE );
			builtinOres.add( Blocks.REDSTONE_ORE );
			builtinOres.add( Blocks.EMERALD_ORE );
			ORE = new MBlockType( "ORE" , ( bs ) -> {
				final Block block = bs.getBlock( );
				return builtinOres.contains( block ) || block instanceof MOre;
			} );
		}
		{
			METAL = new MBlockType( "METAL" , ( bs ) -> {
				final Block block = bs.getBlock( );
				return block == Blocks.IRON_BLOCK || block == Blocks.GOLD_BLOCK || block instanceof MMetalBlock;
			} );
		}
	}


	static void add( final MBlockType mType )
	{
		if ( MBlockTypes.TYPES.containsKey( mType.name ) ) {
			throw new IllegalArgumentException( "duplicate material type " + mType );
		}
		MBlockTypes.TYPES.put( mType.name , mType );
	}


	public static MBlockType get( final String type )
	{
		return MBlockTypes.TYPES.get( type );
	}

}
