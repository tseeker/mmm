package mmm.materials;


import mmm.core.CRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;



public class MMetalBlock
		extends Block
{

	private MMetal metal;


	public MMetalBlock( final String name , final float hardness , final int harvestLevel , final MapColor mapColor )
	{
		super( Material.IRON , mapColor );
		this.setHardness( hardness );
		this.setResistance( 10f );
		this.setSoundType( SoundType.METAL );
		this.setCreativeTab( CreativeTabs.BUILDING_BLOCKS );
		this.setHarvestLevel( "pickaxe" , harvestLevel );
		CRegistry.setIdentifiers( this , "materials" , "block" , name );
	}


	void setMetal( final MMetal metal )
	{
		this.metal = metal;
	}


	public MMetal getMetal( )
	{
		return this.metal;
	}

}
