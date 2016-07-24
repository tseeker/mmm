package mmm.deco;


import com.google.common.base.Throwables;

import mmm.MmmMaterials;
import mmm.core.CAccessors;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.materials.MRock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class DSmoothStone
{

	public static class SmoothStoneBlock
			extends Block
			implements I_RecipeRegistrar
	{

		public final MRock rock;


		public SmoothStoneBlock( final MRock rock )
		{
			super( Material.ROCK );
			this.rock = rock;
			this.setCreativeTab( CreativeTabs.BUILDING_BLOCKS );
			try {
				this.setHardness( CAccessors.getBlockHardness( rock ) );
				this.setResistance( CAccessors.getBlockResistance( rock ) );
			} catch ( final Throwable t ) {
				throw Throwables.propagate( t );
			}
			this.setSoundType( SoundType.STONE );
			this.setHarvestLevel( "pickaxe" , rock.rockHarvestLevel );
			CRegistry.setIdentifiers( this , "deco" , "smoothstone" , rock.name );
		}


		@Override
		public void registerRecipes( )
		{
			GameRegistry.addShapedRecipe( new ItemStack( this , 4 ) , //
					"RR" , //
					"RR" , //
					'R' , this.rock );
		}

	}

	public final SmoothStoneBlock LIMESTONE;
	public final SmoothStoneBlock SLATE;
	public final SmoothStoneBlock BASALT;


	public DSmoothStone( )
	{
		CRegistry.addBlock( this.LIMESTONE = new SmoothStoneBlock( MmmMaterials.ROCK.LIMESTONE ) );
		CRegistry.addBlock( this.SLATE = new SmoothStoneBlock( MmmMaterials.ROCK.SLATE ) );
		CRegistry.addBlock( this.BASALT = new SmoothStoneBlock( MmmMaterials.ROCK.BASALT ) );
	}
}
