package mmm.world.biome;


import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager.BiomeType;



public class WBiomes
{

	// ****************************************************************************
	// * MOUNTAINS
	// ****************************************************************************

	public static class Mountain
	{

		// ****************************************************************************
		// * MOUNTAINS > LIMESTONE
		// ****************************************************************************

		public static class Limestone
		{
			public final Biome NORMAL;
			public final Biome TREES;
			public final Biome CHAOTIC;
			public final Biome TREES_CHAOTIC;


			private Limestone( )
			{
				final WBBuilder helper = new WBBuilder( WBLimestoneMountains::new );
				this.NORMAL = helper //
						.setNames( "Limestone Mountains" , "limestone/mountains" ) //
						.setElevation( 1f , .5f ) //
						.setWeather( .3f , .2f ) //
						.setWaterColor( 0xe0ff7f ) //
						.setType( BiomeType.COOL , 5 ) //
						.setType( BiomeType.WARM , 2 ) //
						.register( );
				helper.startMutation( );
				this.TREES = helper //
						.setNames( "Limestone Mountains (T)" , "limestone/mountains/t" ) //
						.setExtraProperty( "Trees" ) //
						.register( );
				this.TREES_CHAOTIC = helper //
						.setNames( "Limestone Mountains (TC)" , "limestone/mountains/tc" ) //
						.setExtraProperty( "Chaos" ) //
						.register( );
				this.CHAOTIC = helper //
						.setNames( "Limestone Mountains (C)" , "limestone/mountains/c" ) //
						.removeExtraProperty( "Trees" ) //
						.register( );
			}
		}

		public final Limestone LIMESTONE = new Limestone( );


		private Mountain( )
		{
			// EMPTY
		}
	}

	public final Mountain MOUNTAIN = new Mountain( );

	// ****************************************************************************
	// * PLATEAUS
	// ****************************************************************************

	public static class Plateau
	{

		// ****************************************************************************
		// * PLATEAUS > LIMESTONE
		// ****************************************************************************

		public static class Limestone
		{
			public final Biome NORMAL;
			public final Biome CHAOTIC;


			private Limestone( )
			{
				final WBBuilder helper = new WBBuilder( WBLimestonePlateau::new );
				this.NORMAL = helper //
						.setNames( "Limestone Plateau" , "limestone/plateau" ) //
						.setElevation( .5f , .02f ) //
						.setWeather( .6f , .5f ) //
						.setWaterColor( 0xe0ff7f ) //
						.setType( BiomeType.COOL , 5 ) //
						.setType( BiomeType.WARM , 5 ) //
						.register( );
				helper.startMutation( );
				this.CHAOTIC = helper //
						.setNames( "Chaotic Limestone Plateau" , "limestone/plateau/chaotic" )//
						.setWeather( .8f , .5f ) //
						.setElevation( .6f , .07f ) //
						.setExtraProperty( "ChaosChance" , 4 ) //
						.register( );
			}
		}

		public final Limestone LIMESTONE = new Limestone( );


		private Plateau( )
		{
			// EMPTY
		}
	}

	public final Plateau PLATEAU = new Plateau( );

	// ****************************************************************************
	// * FOREST
	// ****************************************************************************

	public static class Forest
	{

		// ****************************************************************************
		// * FOREST > LIMESTONE
		// ****************************************************************************

		public static class Limestone
		{

			public final Biome NORMAL;
			public final Biome CHAOTIC;


			private Limestone( )
			{
				final WBBuilder helper = new WBBuilder( WBLimestonePlateau::new );
				this.NORMAL = helper //
						.setNames( "Limestone Forest" , "limestone/forest" ) //
						.setElevation( .5f , .02f ) //
						.setWeather( .7f , .5f ) //
						.setExtraProperty( "Trees" )//
						.setWaterColor( 0xe0ff7f ) //
						.setType( BiomeType.COOL , 5 ) //
						.setType( BiomeType.WARM , 5 ) //
						.register( );

				helper.startMutation( );
				this.CHAOTIC = helper //
						.setNames( "Chaotic Limestone Forest" , "limestone/forest/chaotic" ) //
						.setExtraProperty( "ChaosChance" , 4 ) //
						.register( );
			}

		}

		public final Limestone LIMESTONE = new Limestone( );

		// ****************************************************************************
		// * FOREST > TROPICAL
		// ****************************************************************************

		public static class Tropical
		{

			public final Biome NORMAL;
			public final Biome HILLS;


			private Tropical( )
			{
				final WBBuilder helper = new WBBuilder( WBTropicalForest::new );
				this.NORMAL = helper //
						.setNames( "Tropical Forest" , "forest/tropical" ) //
						.setElevation( .1f , .1f ) //
						.setWeather( .9f , .95f ) //
						.setType( BiomeType.WARM , 2 ) //
						.register( );
				this.HILLS = helper //
						.setNames( "Tropical Forest Hills" , "forest/tropical/hills" ) //
						.setElevation( .45f , .3f ) //
						.setType( BiomeType.WARM , 2 ) //
						.register( );
			}

		}

		public final Tropical TROPICAL = new Tropical( );

		// ****************************************************************************
		// * FOREST > BAMBOO
		// ****************************************************************************

		public static class Bamboo
		{

			public final Biome NORMAL;
			public final Biome DENSE;
			public final Biome HILLS;


			private Bamboo( )
			{
				final WBBuilder helper = new WBBuilder( WBBambooForest::new );
				this.NORMAL = helper.setNames( "Bamboo Forest" , "forest/bamboo" ) //
						.setElevation( .1f , .1f ) //
						.setWeather( .9f , .95f ) //
						.setType( BiomeType.WARM , 3 ) //
						.register( );
				this.DENSE = helper.setNames( "Dense Bamboo Forest" , "forest/bamboo/dense" ) //
						.setType( BiomeType.WARM , 1 ) //
						.setExtraProperty( "Dense" ).register( );
				this.HILLS = helper.setNames( "Bamboo Forest Hills" , "forest/bamboo/hills" ) //
						.setElevation( .45f , .3f ) //
						.removeExtraProperty( "Dense" ) //
						.setType( BiomeType.WARM , 2 ) //
						.register( );
			}

		}

		public final Bamboo BAMBOO = new Bamboo( );


		private Forest( )
		{
			// EMPTY
		}

	}

	public final Forest FOREST = new Forest( );

	// ****************************************************************************
	// * SWAMP
	// ****************************************************************************

	public static class Swamp
	{
		public final Biome TROPICAL;


		private Swamp( )
		{
			WBBuilder helper;
			helper = new WBBuilder( WBTropicalSwamp::new );
			this.TROPICAL = helper //
					.setNames( "Tropical Swamp" , "swamp/tropical" ) //
					.setElevation( -.2f , .1f ) //
					.setWeather( .95f , .95f ) //
					.setWaterColor( 0xe0ffae ) // Same as vanilla swamps
					.setType( BiomeType.WARM , 2 ) //
					.register( );

		}

	}

	public final Swamp SWAMP = new Swamp( );
}
