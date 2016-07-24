package mmm.materials;


import mmm.MmmMaterials;
import net.minecraft.block.BlockPlanks;



public class MWoods
{
	public final MWood OAK;
	public final MWood BIRCH;
	public final MWood SPRUCE;
	public final MWood JUNGLE;
	public final MWood DARK_OAK;
	public final MWood ACACIA;

	public final MWood HEVEA;
	public final MWood BAMBOO;


	public MWoods( )
	{
		this.OAK = new MWood.Vanilla( "oak" , BlockPlanks.EnumType.OAK );
		this.BIRCH = new MWood.Vanilla( "birch" , BlockPlanks.EnumType.BIRCH );
		this.SPRUCE = new MWood.Vanilla( "spruce" , BlockPlanks.EnumType.SPRUCE );
		this.JUNGLE = new MWood.Vanilla( "jungle" , BlockPlanks.EnumType.JUNGLE );
		this.DARK_OAK = new MWood.Vanilla( "dark_oak" , BlockPlanks.EnumType.DARK_OAK );
		this.ACACIA = new MWood.Vanilla( "acacia" , BlockPlanks.EnumType.ACACIA );

		this.HEVEA = new MWood.MmmTree( MmmMaterials.TREE.HEVEA );
		this.BAMBOO = new MWood.MmmTree( MmmMaterials.TREE.BAMBOO );
	}

}
