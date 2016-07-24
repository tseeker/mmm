package mmm.materials;


import mmm.Mmm;
import mmm.materials.trees.MTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;



public abstract class MWood
{

	public abstract String getSuffix( );


	public abstract MapColor getMapColor( );


	public abstract Block getPlanksBlock( );


	public abstract Block getSlabBlock( );


	public abstract int getMetaData( );

	// *******************************************************************************************
	// * VANILLA WOOD SUPPORT ********************************************************************
	// *******************************************************************************************

	public static final class Vanilla
			extends MWood
	{
		private final String suffix;
		private final BlockPlanks.EnumType type;


		public Vanilla( final String suffix , final BlockPlanks.EnumType type )
		{
			this.suffix = suffix;
			this.type = type;
		}


		@Override
		public String getSuffix( )
		{
			return this.suffix;
		}


		@Override
		public MapColor getMapColor( )
		{
			return this.type.getMapColor( );
		}


		@Override
		public Block getPlanksBlock( )
		{
			return Blocks.PLANKS;
		}


		@Override
		public Block getSlabBlock( )
		{
			return Blocks.WOODEN_SLAB;
		}


		@Override
		public int getMetaData( )
		{
			return this.type.getMetadata( );
		}
	}

	// *******************************************************************************************
	// * MMM WOOD SUPPORT ************************************************************************
	// *******************************************************************************************

	public static final class MmmTree
			extends MWood
	{
		private final MTree materials;
		private final ResourceLocation slabName;


		public MmmTree( final MTree materials )
		{
			this.materials = materials;
			this.slabName = new ResourceLocation( Mmm.ID , "deco/slabs/" + materials.NAME );
		}


		@Override
		public String getSuffix( )
		{
			return this.materials.NAME;
		}


		@Override
		public MapColor getMapColor( )
		{
			return this.materials.getPlankColor( );
		}


		@Override
		public Block getPlanksBlock( )
		{
			return this.materials.PLANKS;
		}


		@Override
		public Block getSlabBlock( )
		{
			return Block.REGISTRY.getObject( this.slabName );
		}


		@Override
		public int getMetaData( )
		{
			return 0;
		}
	}
}