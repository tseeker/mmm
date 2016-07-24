package mmm.core.api.world;


import net.minecraftforge.event.terraingen.PopulateChunkEvent;



public interface I_DefaultPopulateHandler
{

	public boolean onDefaultPopulate( PopulateChunkEvent.Populate event );

}
