package mmm.world.biome;


import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class WBBuilder
{

	private static final String BIOME_REG_BASE = "mmm:biome/";

	private final Function< WBBuilder , ? extends Biome > constructor;

	private String name;
	private String regPath;
	private String mutationBase;
	private float baseHeight;
	private float heightVariation;
	private float rainfall;
	private float temperature;
	private int waterColor = 0xffffff;
	private final EnumMap< BiomeType , Integer > biomeTypes = new EnumMap<>( BiomeType.class );
	private EnumMap< BiomeDictionary.Type , BiomeDictionary.Type > bdTags;
	private final HashMap< String , Object > extra = new HashMap<>( );

	private Biome.BiomeProperties cachedProperties;


	public WBBuilder( final Function< WBBuilder , ? extends Biome > constructor )
	{
		this.constructor = constructor;
	}


	public WBBuilder startMutation( )
	{
		this.mutationBase = WBBuilder.BIOME_REG_BASE + this.regPath;
		this.cachedProperties = null;
		return this;
	}


	public WBBuilder clearMutation( )
	{
		this.mutationBase = null;
		this.cachedProperties = null;
		return this;
	}


	public WBBuilder setNames( final String name , final String regPath )
	{
		this.name = name;
		this.regPath = regPath;
		this.cachedProperties = null;
		return this;
	}


	public WBBuilder setElevation( final float baseHeight , final float variation )
	{
		this.baseHeight = baseHeight;
		this.heightVariation = variation;
		this.cachedProperties = null;
		return this;
	}


	public WBBuilder setWeather( final float rainfall , final float temperature )
	{
		this.rainfall = rainfall;
		this.temperature = temperature;
		this.cachedProperties = null;
		return this;
	}


	public WBBuilder setWaterColor( final int filter )
	{
		this.waterColor = filter;
		this.cachedProperties = null;
		return this;
	}


	public WBBuilder setType( final BiomeType biomeType , final int weight )
	{
		if ( weight > 0 ) {
			this.biomeTypes.put( biomeType , weight );
		} else {
			this.biomeTypes.remove( biomeType );
		}
		return this;
	}


	public WBBuilder clearTags( )
	{
		this.bdTags = null;
		return this;
	}


	public WBBuilder setTags( final BiomeDictionary.Type... tags )
	{
		if ( this.bdTags == null ) {
			this.bdTags = new EnumMap<>( BiomeDictionary.Type.class );
		}
		for ( final BiomeDictionary.Type tag : tags ) {
			this.bdTags.put( tag , tag );
		}
		return this;
	}


	public WBBuilder removeTags( final BiomeDictionary.Type... tags )
	{
		if ( this.bdTags == null ) {
			return this;
		}
		for ( final BiomeDictionary.Type tag : tags ) {
			this.bdTags.remove( tag );
		}
		if ( this.bdTags.isEmpty( ) ) {
			this.bdTags = null;
		}
		return this;
	}


	public WBBuilder setExtraProperty( final String name )
	{
		this.extra.put( name , name );
		return this;
	}


	public WBBuilder setExtraProperty( final String name , final Object value )
	{
		this.extra.put( name , value );
		return this;
	}


	public WBBuilder removeExtraProperty( final String name )
	{
		this.extra.remove( name );
		return this;
	}


	public boolean hasExtraProperty( final String name )
	{
		return this.extra.containsKey( name );
	}


	public < T > T getExtraProperty( final String name , final Class< T > cls )
	{
		return this.getExtraProperty( name , cls , null );
	}


	public < T > T getExtraProperty( final String name , final Class< T > cls , final T defaultValue )
	{
		final Object value = this.extra.get( name );
		if ( value == null ) {
			return defaultValue;
		}
		return cls.cast( value );
	}


	public Biome.BiomeProperties getProperties( )
	{
		if ( this.cachedProperties != null ) {
			return this.cachedProperties;
		}

		final Biome.BiomeProperties rv = new Biome.BiomeProperties( this.name ) //
				.setBaseHeight( this.baseHeight ) //
				.setHeightVariation( this.heightVariation ) //
				.setRainfall( this.rainfall ) //
				.setTemperature( this.temperature ) //
				.setWaterColor( this.waterColor ) //
		;

		if ( this.mutationBase != null ) {
			rv.setBaseBiome( this.mutationBase );
		}
		if ( this.temperature <= .1f ) {
			rv.setSnowEnabled( );
		}
		if ( this.rainfall == 0 ) {
			rv.setRainDisabled( );
		}
		this.cachedProperties = rv;
		return rv;
	}


	public Biome register( )
	{
		final Biome biome = this.constructor.apply( this );
		biome.setRegistryName( WBBuilder.BIOME_REG_BASE + this.regPath );
		this.cachedProperties = null;
		GameRegistry.register( biome );
		for ( final Map.Entry< BiomeType , Integer > entry : this.biomeTypes.entrySet( ) ) {
			BiomeManager.addBiome( entry.getKey( ) , new BiomeManager.BiomeEntry( biome , entry.getValue( ) ) );
		}
		if ( this.bdTags == null ) {
			BiomeDictionary.makeBestGuess( biome );
		} else {
			for ( final BiomeDictionary.Type tag : this.bdTags.keySet( ) ) {
				BiomeDictionary.registerBiomeType( biome , tag );
			}
		}
		return biome;
	}

}
