package mmm.core.api.world;


import mmm.world.gen.WGOreParameters;
import net.minecraft.world.World;



public interface I_BiomeWithOres
{

	public WGOreParameters[] getBiomeOres( final World world );

}
