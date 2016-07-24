package mmm.deco.fences;


import mmm.core.CRegistry;
import mmm.materials.MWood;



public class DFence
{
	public final DFenceBlock BLOCK;
	public final DFenceGate GATE;


	public DFence( final MWood woodType )
	{
		CRegistry.addBlock( this.BLOCK = new DFenceBlock( woodType ) );
		CRegistry.addBlock( this.GATE = new DFenceGate( woodType ) );
	}

}
