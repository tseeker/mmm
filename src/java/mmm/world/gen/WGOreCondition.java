package mmm.world.gen;


import mmm.core.api.world.I_LocationCheck;



public class WGOreCondition
{

	public final I_LocationCheck conditions;
	public final WGOreParameters parameters;


	public WGOreCondition( final I_LocationCheck conditions , final WGOreParameters parameters )
	{
		this.conditions = conditions;
		this.parameters = parameters;
	}

}
