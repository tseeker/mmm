package mmm.deco.slabs;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.deco.DSmoothStone;
import mmm.materials.trees.MTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;



public class DSlabParts
		implements I_RecipeRegistrar
{
	public static DSlabParts fromVanillaSmoothStone( final BlockStone.EnumType type )
	{
		final IBlockState bs = Blocks.STONE.getDefaultState( ).withProperty( BlockStone.VARIANT , type );
		final String name = type.getName( ).replace( "smooth_" , "" );
		return new DSlabParts( bs , name );
	}


	public static DSlabParts fromWood( final MTree materials )
	{
		final DSlabParts slab = new DSlabParts( materials.PLANKS.getDefaultState( ) , materials.NAME );
		if ( materials.getBaseFlammability( ) != 0 ) {
			Blocks.FIRE.setFireInfo( slab.HALF , materials.getBaseFireEncouragement( ) ,
					materials.getBaseFlammability( ) * 4 );
			Blocks.FIRE.setFireInfo( slab.DOUBLE , materials.getBaseFireEncouragement( ) ,
					materials.getBaseFlammability( ) * 4 );
		}
		OreDictionary.registerOre( "slabWood" , slab.HALF );
		return slab;
	}


	public static DSlabParts fromSmoothStone( final DSmoothStone.SmoothStoneBlock stone )
	{
		return new DSlabParts( stone.getDefaultState( ) , stone.rock.name );
	}

	public final DSlabHalf HALF;
	public final DSlabDouble DOUBLE;
	public final ItemSlab ITEM;


	public DSlabParts( final Block modelBlock , final String name )
	{
		this( modelBlock.getDefaultState( ) , name );
	}


	public DSlabParts( final IBlockState modelState , final String name )
	{
		this.HALF = new DSlabHalf( modelState , name );
		this.DOUBLE = new DSlabDouble( this.HALF , name );
		this.ITEM = new ItemSlab( this.HALF , this.HALF , this.DOUBLE );
		CRegistry.setIdentifiers( this.ITEM , "deco" , "slabs" , name );

		CRegistry.addBlock( this.HALF , this.ITEM );
		CRegistry.addBlock( this.DOUBLE , null );
		CRegistry.addRecipeRegistrar( this );
	}


	@Override
	public void registerRecipes( )
	{
		final Block block = this.HALF.modelBlock;
		final IBlockState state = this.HALF.modelState;
		GameRegistry.addShapedRecipe( new ItemStack( this.HALF , 6 ) , //
				"BBB" , //
				'B' , new ItemStack( block , 1 , block.getMetaFromState( state ) ) );
	}
}
