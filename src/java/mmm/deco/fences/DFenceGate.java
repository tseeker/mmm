package mmm.deco.fences;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.core.api.blocks.I_StateMapperProvider;
import mmm.materials.MWood;
import mmm.utils.UStateMapper;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class DFenceGate
		extends BlockFenceGate
		implements I_RecipeRegistrar , I_StateMapperProvider
{

	private final MWood woodType;


	public DFenceGate( final MWood woodType )
	{
		super( BlockPlanks.EnumType.OAK );
		this.woodType = woodType;
		this.setHardness( 2.f );
		this.setResistance( 5.f );
		this.setSoundType( SoundType.WOOD );
		this.setHarvestLevel( "axe" , 0 );
		CRegistry.setIdentifiers( this , "deco" , "fence" , "gate" , woodType.getSuffix( ) );
	}


	@Override
	public MapColor getMapColor( final IBlockState state )
	{
		return this.woodType.getMapColor( );
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addShapedRecipe( new ItemStack( this ) , //
				"SPS" , //
				"SPS" , //
				'P' , this.woodType.getPlanksBlock( ) , //
				'S' , Items.STICK );
	}


	@Override
	public IStateMapper getStateMapper( )
	{
		return UStateMapper.ignoreProperties( BlockFenceGate.POWERED );
	}

}
