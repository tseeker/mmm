package mmm.tech.tools;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class TTPickaxe
		extends ItemPickaxe
		implements I_RecipeRegistrar
{

	public TTPickaxe( final ToolMaterial material )
	{
		super( material );
		CRegistry.setIdentifiers( this , "tech" , "tools" , material.toString( ).toLowerCase( ) , "pickaxe" );
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addShapedRecipe( new ItemStack( this ) , //
				"III" , //
				" S " , //
				" S " , //
				'I' , this.getToolMaterial( ).getRepairItemStack( ) , //
				'S' , new ItemStack( Items.STICK ) );
	}

}
