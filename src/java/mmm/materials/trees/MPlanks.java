package mmm.materials.trees;


import mmm.core.CRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;



public class MPlanks
		extends Block
{

	public final MTree wood;


	public MPlanks( final MTree wood )
	{
		super( Material.WOOD );
		this.wood = wood;

		this.setCreativeTab( CreativeTabs.BUILDING_BLOCKS );
		this.setHarvestLevel( "axe" , 0 );
		this.setHardness( 2f );
		this.setResistance( 5f );
		this.setSoundType( SoundType.WOOD );

		CRegistry.setIdentifiers( this , "materials" , "planks" , wood.NAME );
	}


	@Override
	public MapColor getMapColor( final IBlockState state )
	{
		return this.wood.getPlankColor( );
	}

}
