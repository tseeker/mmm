package mmm.deco.fences;


import mmm.MmmMaterials;



public class DFences
{
	public final DFence BAMBOO;
	public final DFence HEVEA;


	public DFences( )
	{
		this.BAMBOO = new DFence( MmmMaterials.WOOD.BAMBOO );
		this.HEVEA = new DFence( MmmMaterials.WOOD.HEVEA );
	}

}
