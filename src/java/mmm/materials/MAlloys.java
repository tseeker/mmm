package mmm.materials;


import mmm.MmmMaterials;
import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.block.material.MapColor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;



public class MAlloys
		implements I_RecipeRegistrar
{
	public final MMetal BRONZE;
	public final MMetal BRASS;
	public final MMetal STEEL;
	// public static final MMetal RED_COPPER;


	public MAlloys( )
	{
		CRegistry.addRecipeRegistrar( this );

		this.BRONZE = new MMetal( "bronze" , 0f , 5f , 1 , MapColor.BROWN );
		this.BRASS = new MMetal( "brass" , 0f , 4f , 1 , MapColor.GOLD );
		this.STEEL = new MMetal( "steel" , 0f , 7f , 2 , MapColor.LIGHT_BLUE ) //
				.setBlockResistance( 12f );
		// RED_COPPER = new MMetal( "red_copper" , 0f , 2f , 1 , MapColor.RED );
	}


	@Override
	public void registerRecipes( )
	{
		// Bronze
		MAlloyRecipe.build( ).setName( "materials/bronze" ).setBurnTime( 200 ) //
				.addInput( MmmMaterials.METAL.COPPER.INGOT ) //
				.addInput( MmmMaterials.METAL.TIN.NUGGET ) //
				.setOutput( this.BRONZE.INGOT ).setSlag( 1 ) //
				.register( );

		// Brass
		MAlloyRecipe.build( ).setName( "materials/brass" ).setBurnTime( 200 ) //
				.addInput( MmmMaterials.METAL.COPPER.INGOT , 2 ) //
				.addInput( MmmMaterials.METAL.ZINC.INGOT ) //
				.setOutput( this.BRASS.INGOT , 2 ).setSlag( 1 ) //
				.register( );

		// Pig iron
		MAlloyRecipe.build( ).setName( "materials/pig_iron/from_ingot" ).setBurnTime( 1600 ) //
				.addInput( MmmMaterials.METAL.IRON.INGOT ) //
				.addInput( MmmMaterials.ROCK.LIMESTONE ) //
				.addInput( MmmMaterials.ITEM.COKE ) //
				.setOutput( MmmMaterials.ITEM.PIG_IRON_INGOT , 2 ).setSlag( 3 ) //
				.register( );
		MAlloyRecipe.build( ).setName( "materials/pig_iron/from_ore" ).setBurnTime( 1600 ) //
				.addInput( Blocks.IRON_ORE ) //
				.addInput( MmmMaterials.ROCK.LIMESTONE ) //
				.addInput( MmmMaterials.ITEM.COKE ) //
				.setOutput( MmmMaterials.ITEM.PIG_IRON_INGOT ).setSlag( 5 ) //
				.register( );

		// Steel
		MAlloyRecipe.build( ).setName( "materials/steel/from_ingot" ).setBurnTime( 3200 ) //
				.addInput( MmmMaterials.METAL.IRON.INGOT ) //
				.addInput( MmmMaterials.ROCK.LIMESTONE ) //
				.addInput( MmmMaterials.ITEM.PIG_IRON_INGOT ) //
				.setOutput( this.STEEL.INGOT , 2 ).setSlag( 3 ) //
				.register( );
		MAlloyRecipe.build( ).setName( "materials/steel/from_ore" ).setBurnTime( 3200 ) //
				.addInput( Blocks.IRON_ORE ) //
				.addInput( MmmMaterials.ROCK.LIMESTONE ) //
				.addInput( MmmMaterials.ITEM.PIG_IRON_INGOT ) //
				.setOutput( this.STEEL.INGOT ).setSlag( 5 ) //
				.register( );

		// MAlloyRecipe.build( ).setName( "materials/red_copper" ).setBurnTime( 800 )
		// .addInput( Materials.COPPER.INGOT , 1 ).addInput( Items.REDSTONE , 2 )
		// .setOutput( Materials.RED_COPPER.INGOT ).setSlag( 1 ).register( );

		// XXX coke is not an alloy
		MAlloyRecipe.build( ).setName( "materials/coke" ).setBurnTime( 3200 ).addInput( Items.COAL , 2 )
				.setOutput( MmmMaterials.ITEM.COKE ).setSlag( 1 ).register( );
	}
}
