package mmm.deco.slabs;


import mmm.MmmDeco;
import mmm.MmmMaterials;
import net.minecraft.block.BlockStone;



public class DSlabs
{
	public final DSlabParts GRANITE;
	public final DSlabParts DIORITE;
	public final DSlabParts ANDESITE;

	public final DSlabParts LIMESTONE;
	public final DSlabParts SLATE;
	public final DSlabParts BASALT;

	public final DSlabParts HEVEA;
	public final DSlabParts BAMBOO;


	public DSlabs( )
	{
		this.GRANITE = DSlabParts.fromVanillaSmoothStone( BlockStone.EnumType.GRANITE_SMOOTH );
		this.DIORITE = DSlabParts.fromVanillaSmoothStone( BlockStone.EnumType.DIORITE_SMOOTH );
		this.ANDESITE = DSlabParts.fromVanillaSmoothStone( BlockStone.EnumType.ANDESITE_SMOOTH );

		this.LIMESTONE = DSlabParts.fromSmoothStone( MmmDeco.STONE.LIMESTONE );
		this.SLATE = DSlabParts.fromSmoothStone( MmmDeco.STONE.SLATE );
		this.BASALT = DSlabParts.fromSmoothStone( MmmDeco.STONE.BASALT );

		this.HEVEA = DSlabParts.fromWood( MmmMaterials.TREE.HEVEA );
		this.BAMBOO = DSlabParts.fromWood( MmmMaterials.TREE.BAMBOO );
	}
}
