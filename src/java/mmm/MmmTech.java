package mmm;


import mmm.core.CNetwork;
import mmm.tech.TActivationModeMessage;
import mmm.tech.base.TBMachines;
import mmm.tech.tools.TTArmors;
import mmm.tech.tools.TTTools;



public class MmmTech
{
	public static final TBMachines MACHINES;
	public static final TTTools TOOL;
	public static final TTArmors ARMOR;

	static {
		CNetwork.addServerMessage( TActivationModeMessage.class );
		MACHINES = new TBMachines( );
		TOOL = new TTTools( );
		ARMOR = new TTArmors( );
	}


	public static void preInit( )
	{
		// EMPTY
	}

}
