package mmm.core.api;


import java.util.List;

import mmm.world.gen.WGOreCondition;



public interface I_OreGenerationRegistrar
{

	public void addConditions( List< WGOreCondition > conditions );

}
