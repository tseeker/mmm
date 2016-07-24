package mmm.tech.tools;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class TTSword
		extends ItemSword
		implements I_RecipeRegistrar
{
	protected final ToolMaterial material;


	public TTSword( final ToolMaterial material )
	{
		super( material );
		this.material = material;
		CRegistry.setIdentifiers( this , "tech" , "tools" , material.toString( ).toLowerCase( ) , "sword" );
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addShapedRecipe( new ItemStack( this ) , //
				"I" , //
				"I" , //
				"S" , //
				'I' , this.material.getRepairItemStack( ) , //
				'S' , new ItemStack( Items.STICK ) );
	}

}
