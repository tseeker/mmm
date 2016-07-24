package mmm.food;


import javax.annotation.Nullable;

import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class FFoodInContainer
		extends ItemFood
		implements I_RecipeRegistrar
{

	private Object[] recipe;


	public FFoodInContainer( final String section , final String name , final int amount , final float saturation ,
			final Item container , final int maxStackSize , final Object... recipe )
	{
		super( amount , saturation , false );
		this.setMaxStackSize( maxStackSize );
		this.setContainerItem( container );

		if ( recipe.length == 0 ) {
			this.recipe = null;
		} else if ( recipe[ 0 ] instanceof String ) {
			this.recipe = new Object[ recipe.length + 2 ];
			System.arraycopy( recipe , 0 , this.recipe , 0 , recipe.length );
			this.recipe[ recipe.length ] = 'B';
			this.recipe[ recipe.length + 1 ] = container;
		} else {
			this.recipe = new Object[ recipe.length + 1 ];
			System.arraycopy( recipe , 0 , this.recipe , 0 , recipe.length );
			this.recipe[ recipe.length ] = container;
		}

		CRegistry.setIdentifiers( this , "food" , section , name );
		CRegistry.addItem( this );
	}


	@Override
	@Nullable
	public ItemStack onItemUseFinish( final ItemStack stack , final World worldIn ,
			final EntityLivingBase entityLiving )
	{
		super.onItemUseFinish( stack , worldIn , entityLiving );
		return new ItemStack( this.getContainerItem( ) );
	}


	@Override
	public void registerRecipes( )
	{
		if ( this.recipe == null ) {
			return;
		}
		if ( this.recipe[ 0 ] instanceof String ) {
			GameRegistry.addShapedRecipe( new ItemStack( this ) , this.recipe );
		} else {
			GameRegistry.addShapelessRecipe( new ItemStack( this ) , this.recipe );
		}
		this.recipe = null;
	}

}
