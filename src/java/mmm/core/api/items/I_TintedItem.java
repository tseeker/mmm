package mmm.core.api.items;


import mmm.core.api.I_RequiresClientInit;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



/**
 * For items that need to register a tint, or blocks with automatically registered items that need
 * to be tinted.
 */
public interface I_TintedItem
		extends I_RequiresClientInit
{

	@SideOnly( Side.CLIENT )
	public IItemColor getItemTint( );

}
