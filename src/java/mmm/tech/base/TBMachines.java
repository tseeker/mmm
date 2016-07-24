package mmm.tech.base;


import mmm.tech.base.alloy_furnace.TBAlloyFurnace;
import mmm.tech.base.workbench.TBWorkbench;



public class TBMachines
{
	public final TBAlloyFurnace ALLOY_FURNACE;
	public final TBWorkbench WORKBENCH;


	public TBMachines( )
	{
		this.ALLOY_FURNACE = new TBAlloyFurnace( );
		this.WORKBENCH = new TBWorkbench( );
	}


	public static void preInit( )
	{
		// EMPTY
	}

}
