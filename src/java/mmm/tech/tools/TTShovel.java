package mmm.tech.tools;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class TTShovel
		extends ItemSpade
		implements I_RecipeRegistrar
{
	public TTShovel( final ToolMaterial material )
	{
		super( material );
		CRegistry.setIdentifiers( this , "tech" , "tools" , material.toString( ).toLowerCase( ) , "shovel" );
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addShapedRecipe( new ItemStack( this ) , //
				"I" , //
				"S" , //
				"S" , //
				'I' , this.getToolMaterial( ).getRepairItemStack( ) , //
				'S' , new ItemStack( Items.STICK ) );
	}

}
