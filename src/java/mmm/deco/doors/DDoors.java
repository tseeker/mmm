package mmm.deco.doors;


import mmm.MmmMaterials;



public class DDoors
{
	public final DDoor BAMBOO;
	public final DDoor HEVEA;


	public DDoors( )
	{
		this.BAMBOO = new DDoor( MmmMaterials.WOOD.BAMBOO );
		this.HEVEA = new DDoor( MmmMaterials.WOOD.HEVEA );
	}

}
