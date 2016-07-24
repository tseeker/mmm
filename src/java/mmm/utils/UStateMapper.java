package mmm.utils;


import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class UStateMapper
{

	@SideOnly( Side.CLIENT )
	public static IStateMapper ignoreProperties( final IProperty< ? >... properties )
	{
		return new StateMap.Builder( ).ignore( properties ).build( );
	}

}
