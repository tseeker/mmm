package mmm.materials;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class MMetal
		implements I_RecipeRegistrar
{

	public final float SMELTING_XP;
	public final Block BLOCK;
	public final Item INGOT;
	public final Item NUGGET;


	public MMetal( final String name , final float baseSmeltingXP , final float hardness , final int harvestLevel ,
			final MapColor mapColor )
	{
		this( baseSmeltingXP , new MMetalBlock( name , hardness , harvestLevel , mapColor ) , //
				new MItem( E_MItemType.INGOT , name ) , //
				new MItem( E_MItemType.NUGGET , name ) );
	}


	public MMetal( final Block block , final Item ingot , final Item nugget )
	{
		this( 0 , block , ingot , nugget );
	}


	public MMetal setBlockResistance( final float resistance )
	{
		this.BLOCK.setResistance( resistance );
		return this;
	}


	protected MMetal( final float baseSmeltingXP , final Block block , final Item ingot , final Item nugget )
	{
		this.SMELTING_XP = baseSmeltingXP;
		this.BLOCK = block;
		this.INGOT = ingot;
		this.NUGGET = nugget;

		if ( ingot instanceof MItem ) {
			final MItem miIngot = (MItem) ingot;
			if ( miIngot.itemType != E_MItemType.INGOT ) {
				throw new IllegalArgumentException( "invalid ingot item" );
			}
			miIngot.setExtraInfo( this );
		}

		if ( nugget instanceof MItem ) {
			final MItem miNugget = (MItem) nugget;
			if ( miNugget.itemType != E_MItemType.NUGGET ) {
				throw new IllegalArgumentException( "invalid nugget item" );
			}
			miNugget.setExtraInfo( this );
		}

		if ( block instanceof MMetalBlock ) {
			( (MMetalBlock) block ).setMetal( this );
		}

		this.register( );
	}


	protected MMetal register( )
	{
		boolean custom = false;
		if ( this.BLOCK instanceof MMetalBlock ) {
			CRegistry.addBlock( this.BLOCK );
			custom = true;
		}
		if ( this.INGOT instanceof MItem ) {
			CRegistry.addItem( this.INGOT );
			custom = true;
		}
		if ( this.NUGGET instanceof MItem ) {
			CRegistry.addItem( this.NUGGET );
			custom = true;
		}
		if ( custom ) {
			CRegistry.addRecipeRegistrar( this );
		}
		return this;
	}


	@Override
	public void registerRecipes( )
	{
		if ( this.INGOT instanceof MItem || this.NUGGET instanceof MItem ) {
			GameRegistry.addShapelessRecipe( new ItemStack( this.NUGGET , 9 ) , this.INGOT );
			GameRegistry.addRecipe( new ItemStack( this.INGOT ) , //
					"NNN" , "NNN" , "NNN" , //
					'N' , this.NUGGET );
		}
		if ( this.INGOT instanceof MItem || this.BLOCK instanceof MMetalBlock ) {
			GameRegistry.addShapelessRecipe( new ItemStack( this.INGOT , 9 ) , this.BLOCK );
			GameRegistry.addRecipe( new ItemStack( this.BLOCK ) , //
					"III" , "III" , "III" , //
					'I' , this.INGOT );
		}
	}

}
