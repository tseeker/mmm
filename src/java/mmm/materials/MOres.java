package mmm.materials;


import java.util.List;

import mmm.MmmMaterials;
import mmm.core.CRegistry;
import mmm.core.api.I_OreGenerationRegistrar;
import mmm.world.WLocation;
import mmm.world.gen.WGOreCondition;
import mmm.world.gen.WGOreParameters;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;



public class MOres
		implements I_OreGenerationRegistrar
{
	public final MOre COPPER;
	public final MOre MALACHITE;
	public final MOre CUPRITE;
	public final MOre CASSITERITE;
	public final MOre SPHALERITE;
	public final MOre ROCK_SALT;
	public final MOre BAUXITE;
	public final MOre GALENA;
	public final MOre CINNABAR;
	public final MOre SULPHUR;
	public final MOre SILVER;
	public final MOre SALTPETER;


	public MOres( )
	{
		CRegistry.addOreGenerationRegistrar( this );

		this.COPPER = new MOre( "copper" , 1 ) //
				.setMetal( MmmMaterials.METAL.COPPER );
		this.MALACHITE = new MOre( "malachite" , 1 )//
				.setMetal( MmmMaterials.METAL.COPPER ) //
				.setDrops( MmmMaterials.ITEM.MALACHITE ) //
				.setExperience( 1 , 3 );
		this.CUPRITE = new MOre( "cuprite" , 1 ) //
				.setMetal( MmmMaterials.METAL.COPPER , 2 ) //
				.setDrops( MmmMaterials.ITEM.CUPRITE ) //
				.setExperience( 2 , 5 );
		this.CASSITERITE = new MOre( "cassiterite" , 0 )//
				.setMetal( MmmMaterials.METAL.TIN , 1 , E_MItemType.NUGGET ) //
				.setDrops( MmmMaterials.ITEM.CASSITERITE , 2 , 5 ) //
				.setExperience( 2 , 5 );
		this.SPHALERITE = new MOre( "sphalerite" , 1 ) //
				.setMetal( MmmMaterials.METAL.ZINC ) //
				.setDrops( MmmMaterials.ITEM.SPHALERITE ) //
				.setExperience( 1 , 3 );
		this.ROCK_SALT = new MOre( "rock_salt" , 0 ) //
				.setDrops( MmmMaterials.ITEM.ROCK_SALT , 2 , 5 ) //
				.setExperience( 0 , 1 );
		this.BAUXITE = new MOre( "bauxite" , 1 );
		this.GALENA = new MOre( "galena" , 1 );
		this.CINNABAR = new MOre( "cinnabar" , 1 );
		this.SULPHUR = new MOre( "sulphur" , 0 ) //
				.setResistance( 2.0f ) //
				.setHardness( 1.0f ) //
				.setDrops( MmmMaterials.ITEM.SULPHUR_POWDER , 3 , 6 ) //
				.setExperience( 1 , 2 );
		this.SILVER = new MOre( "silver" , 2 );
		this.SALTPETER = new MOre( "saltpeter" , 0 ) //
				.setDrops( MmmMaterials.ITEM.SALTPETER , 4 , 8 ) //
				.setExperience( 0 , 1 );
	}


	@Override
	public void addConditions( final List< WGOreCondition > conditions )
	{
		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.COPPER , 20 , 9 , 0 , 128 ) ) );
		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.COPPER , 10 , 17 , 40 , 60 ) ) );
		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.MALACHITE , 5 , 9 , 80 , 255 ) ) );
		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.CUPRITE , 10 , 9 , 0 , 60 ) ) );

		conditions.add( new WGOreCondition( WLocation.inOverworld( ) ,
				new WGOreParameters( this.CASSITERITE , 5 , 9 , 45 , 80 ) ) );

		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.SPHALERITE , 15 , 15 ) ) );

		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.ROCK_SALT , 1 , 30 , 45 , 255 ) ) );

		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.GALENA , 10 , 9 ) ) );

		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.CINNABAR , 1 , 20 , 0 , 50 ) ) );

		conditions.add( new WGOreCondition( WLocation.inTheNether( ) , //
				new WGOreParameters( this.SULPHUR , 5 , 25 , //
						BlockMatcher.forBlock( Blocks.NETHERRACK ) ) ) );

		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.SILVER , 3 , 9 , 0 , 40 ) ) );

		conditions.add( new WGOreCondition( WLocation.inOverworld( ) , //
				new WGOreParameters( this.SALTPETER , 10 , 9 , 0 , 80 ) ) );
	}
}
