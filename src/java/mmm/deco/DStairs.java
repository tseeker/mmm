package mmm.deco;


import mmm.MmmDeco;
import mmm.MmmMaterials;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.materials.trees.MTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;



public class DStairs
{

	public static class StairsBlock
			extends BlockStairs
			implements I_RecipeRegistrar
	{
		public static StairsBlock fromVanillaSmoothStone( final BlockStone.EnumType type )
		{
			final IBlockState bs = Blocks.STONE.getDefaultState( ).withProperty( BlockStone.VARIANT , type );
			final StairsBlock stairs = new StairsBlock( bs , type.getName( ).replace( "smooth_" , "" ) );
			CRegistry.addBlock( stairs );
			return stairs;
		}


		public static StairsBlock fromSmoothStone( final DSmoothStone.SmoothStoneBlock type )
		{
			final StairsBlock stairs = new StairsBlock( type , type.rock.name );
			CRegistry.addBlock( stairs );
			return stairs;
		}


		public static StairsBlock fromWood( final MTree materials )
		{
			final StairsBlock stairs = new StairsBlock( materials.PLANKS.getDefaultState( ) , materials.NAME );
			CRegistry.addBlock( stairs );
			if ( materials.getBaseFlammability( ) != 0 ) {
				Blocks.FIRE.setFireInfo( stairs , materials.getBaseFireEncouragement( ) ,
						materials.getBaseFlammability( ) * 4 );
			}
			OreDictionary.registerOre( "stairWood" , stairs );
			return stairs;
		}

		public final IBlockState modelState;
		public final Block modelBlock;


		public StairsBlock( final Block modelBlock , final String name )
		{
			this( modelBlock.getDefaultState( ) , name );
		}


		public StairsBlock( final IBlockState modelState , final String name )
		{
			super( modelState );
			this.modelState = modelState;
			this.modelBlock = modelState.getBlock( );
			CRegistry.setIdentifiers( this , "deco" , "stairs" , name );
		}


		@Override
		public void registerRecipes( )
		{
			GameRegistry.addShapedRecipe( new ItemStack( this , 4 ) , //
					"B  " , //
					"BB " , //
					"BBB" , //
					'B' , new ItemStack( this.modelBlock , 1 , this.modelBlock.getMetaFromState( this.modelState ) ) );
		}

	}

	public final StairsBlock GRANITE;
	public final StairsBlock DIORITE;
	public final StairsBlock ANDESITE;

	public final StairsBlock LIMESTONE;
	public final StairsBlock SLATE;
	public final StairsBlock BASALT;

	public final StairsBlock HEVEA;
	public final StairsBlock BAMBOO;


	public DStairs( )
	{
		this.GRANITE = StairsBlock.fromVanillaSmoothStone( BlockStone.EnumType.GRANITE_SMOOTH );
		this.DIORITE = StairsBlock.fromVanillaSmoothStone( BlockStone.EnumType.DIORITE_SMOOTH );
		this.ANDESITE = StairsBlock.fromVanillaSmoothStone( BlockStone.EnumType.ANDESITE_SMOOTH );

		this.LIMESTONE = StairsBlock.fromSmoothStone( MmmDeco.STONE.LIMESTONE );
		this.SLATE = StairsBlock.fromSmoothStone( MmmDeco.STONE.SLATE );
		this.BASALT = StairsBlock.fromSmoothStone( MmmDeco.STONE.BASALT );

		this.HEVEA = StairsBlock.fromWood( MmmMaterials.TREE.HEVEA );
		this.BAMBOO = StairsBlock.fromWood( MmmMaterials.TREE.BAMBOO );
	}
}
