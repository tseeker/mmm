package mmm.materials;


import java.util.List;

import mmm.MmmMaterials;
import mmm.core.CRegistry;
import mmm.core.api.I_OreGenerationRegistrar;
import mmm.world.WLocation;
import mmm.world.WLocationInBiomeClass;
import mmm.world.gen.WGOreCondition;
import mmm.world.gen.WGOreParameters;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.biome.BiomeSwamp;



public class MRocks
		implements I_OreGenerationRegistrar
{

	public final MRock LIMESTONE;
	public final MRock CHALK;
	public final MRock SLATE;
	public final MRock BASALT;


	public MRocks( )
	{
		CRegistry.addOreGenerationRegistrar( this );

		this.LIMESTONE = new MRock( "limestone" , MapColor.SNOW , 0 , 1f , 7.5f );
		this.CHALK = new MRock( "chalk" , MapColor.SNOW , 0 , 1f , 5f );
		this.SLATE = new MRock( "slate" , MapColor.BLACK );
		this.BASALT = new MRock( "basalt" , MapColor.BLACK );
	}


	@Override
	public void addConditions( final List< WGOreCondition > conditions )
	{
		conditions.add( new WGOreCondition( //
				WLocation.inOverworld( ) , //
				new WGOreParameters( MmmMaterials.ROCK.LIMESTONE , 15 , 40 ) ) );

		conditions.add( new WGOreCondition( //
				new WLocationInBiomeClass<>( BiomePlains.class ) , //
				new WGOreParameters( MmmMaterials.ROCK.SLATE , 15 , 40 ) ) );
		conditions.add( new WGOreCondition( //
				new WLocationInBiomeClass<>( BiomeSwamp.class ) , //
				new WGOreParameters( MmmMaterials.ROCK.SLATE , 20 , 60 ) ) );
	}
}
