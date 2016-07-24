package mmm.deco.thrones;


import mmm.MmmMaterials;



public class DThrones
{
	public final DThrone OAK;
	public final DThrone BIRCH;
	public final DThrone SPRUCE;
	public final DThrone JUNGLE;
	public final DThrone DARK_OAK;
	public final DThrone ACACIA;
	public final DThrone HEVEA;
	public final DThrone BAMBOO;


	public DThrones( )
	{
		this.OAK = new DThrone( MmmMaterials.WOOD.OAK );
		this.BIRCH = new DThrone( MmmMaterials.WOOD.BIRCH );
		this.SPRUCE = new DThrone( MmmMaterials.WOOD.SPRUCE );
		this.JUNGLE = new DThrone( MmmMaterials.WOOD.JUNGLE );
		this.DARK_OAK = new DThrone( MmmMaterials.WOOD.DARK_OAK );
		this.ACACIA = new DThrone( MmmMaterials.WOOD.ACACIA );
		this.HEVEA = new DThrone( MmmMaterials.WOOD.HEVEA );
		this.BAMBOO = new DThrone( MmmMaterials.WOOD.BAMBOO );
	}
}
