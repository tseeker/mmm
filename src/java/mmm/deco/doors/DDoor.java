package mmm.deco.doors;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.materials.MWood;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class DDoor
{

	public final DDoorBlock BLOCK;
	public final ItemDoor ITEM;


	public DDoor( final MWood wood )
	{
		this.BLOCK = new DDoorBlock( this , wood );
		this.ITEM = new ItemDoor( this.BLOCK );
		CRegistry.setIdentifiers( this.ITEM , "deco" , "door" , wood.getSuffix( ) );
		CRegistry.addBlock( this.BLOCK , this.ITEM );
		CRegistry.addRecipeRegistrar( (I_RecipeRegistrar) ( ) -> {
			GameRegistry.addShapedRecipe( new ItemStack( DDoor.this.ITEM , 3 ) , //
					"WW" , //
					"WW" , //
					"WW" , //
					'W' , wood.getPlanksBlock( ) );
		} );
	}

}
