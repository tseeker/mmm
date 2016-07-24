package mmm.materials;


import net.minecraft.block.material.MapColor;



public class MAlloy
		extends MMetal
		implements I_MAlloy
{

	private final MMetal[] metals;


	public MAlloy( final String name , final float baseSmeltingXP , final float hardness , final int harvestLevel ,
			final MapColor mapColor , final MMetal... metals )
	{
		super( name , baseSmeltingXP , hardness , harvestLevel , mapColor );
		this.metals = metals.clone( );
	}


	@Override
	public MMetal[] getComponents( )
	{
		return this.metals.clone( );
	}

}
