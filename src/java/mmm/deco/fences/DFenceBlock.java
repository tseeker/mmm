package mmm.deco.fences;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.materials.MWood;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class DFenceBlock
		extends BlockFence
		implements I_RecipeRegistrar
{

	private final MWood woodType;


	public DFenceBlock( final MWood woodType )
	{
		super( Material.WOOD , woodType.getMapColor( ) );
		this.woodType = woodType;
		this.setHardness( 2.f );
		this.setResistance( 5.f );
		this.setSoundType( SoundType.WOOD );
		this.setHarvestLevel( "axe" , 0 );
		CRegistry.setIdentifiers( this , "deco" , "fence" , woodType.getSuffix( ) );
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addShapedRecipe( new ItemStack( this , 3 ) , //
				"PSP" , //
				"PSP" , //
				'P' , this.woodType.getPlanksBlock( ) , //
				'S' , Items.STICK );
	}

}
