package mmm.materials;


import mmm.core.CRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;



public class MRock
		extends Block
		implements I_MRock
{

	public final int rockHarvestLevel;
	public final String name;


	public MRock( final String name , final MapColor mapColor )
	{
		this( name , mapColor , 0 , 1.5f , 10f );
	}


	public MRock( final String name , final MapColor mapColor , final int harvestLevel )
	{
		this( name , mapColor , harvestLevel , 1.5f , 10f );
	}


	public MRock( final String name , final MapColor mapColor , final int harvestLevel , final float hardness ,
			final float resistance )
	{
		super( Material.ROCK , mapColor );
		this.rockHarvestLevel = harvestLevel;
		this.name = name;

		this.setCreativeTab( CreativeTabs.BUILDING_BLOCKS );
		this.setHardness( hardness );
		this.setResistance( resistance );
		this.setSoundType( SoundType.STONE );
		this.setHarvestLevel( "pickaxe" , harvestLevel );

		CRegistry.setIdentifiers( this , "materials" , "rock" , name );
		CRegistry.addBlock( this );
	}

}
