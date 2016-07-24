package mmm.tech.tools;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class TTAxe
		extends ItemAxe
		implements I_RecipeRegistrar
{

	public TTAxe( final ToolMaterial material , final float damage , final float attackSpeed )
	{
		super( material , damage , attackSpeed );
		CRegistry.setIdentifiers( this , "tech" , "tools" , material.toString( ).toLowerCase( ) , "axe" );
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addShapedRecipe( new ItemStack( this ) , //
				"II" , //
				"IS" , //
				" S" , //
				'I' , this.getToolMaterial( ).getRepairItemStack( ) , //
				'S' , new ItemStack( Items.STICK ) );
	}

}
