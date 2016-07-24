package mmm.tech.tools;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class TTHoe
		extends ItemHoe
		implements I_RecipeRegistrar
{
	public TTHoe( final ToolMaterial material )
	{
		super( material );
		CRegistry.setIdentifiers( this , "tech" , "tools" , material.toString( ).toLowerCase( ) , "hoe" );
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addShapedRecipe( new ItemStack( this ) , //
				"II" , //
				" S" , //
				" S" , //
				'I' , this.theToolMaterial.getRepairItemStack( ) , //
				'S' , new ItemStack( Items.STICK ) );
	}

}
