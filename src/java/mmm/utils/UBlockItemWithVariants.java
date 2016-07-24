package mmm.utils;


import mmm.Mmm;
import mmm.core.CRegistry;
import mmm.core.api.items.I_ItemWithVariants;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;



public class UBlockItemWithVariants
		extends ItemBlock
		implements I_ItemWithVariants
{
	private final String[] baseName;
	private String[] variants;


	public UBlockItemWithVariants( final Block block , final String... baseName )
	{
		super( block );

		final StringBuilder sbName = new StringBuilder( Mmm.ID );
		this.baseName = new String[ baseName.length ];
		for ( int i = 0 ; i < baseName.length ; i++ ) {
			this.baseName[ i ] = baseName[ i ];
			sbName.append( '.' ).append( baseName[ i ] );
		}
		this.setUnlocalizedName( sbName.toString( ) );

		this.setMaxDamage( 0 );
		this.setHasSubtypes( true );
	}


	public UBlockItemWithVariants setVariants( final String... variantNames )
	{
		this.variants = new String[ variantNames.length ];
		for ( int i = 0 ; i < variantNames.length ; i++ ) {
			this.variants[ i ] = variantNames[ i ];
		}
		return this;
	}


	public UBlockItemWithVariants useColorVariants( )
	{
		this.variants = new String[ 16 ];
		for ( int i = 0 ; i < 16 ; i++ ) {
			this.variants[ i ] = EnumDyeColor.byMetadata( i ).getUnlocalizedName( );
		}
		return this;
	}


	public UBlockItemWithVariants register( )
	{
		final StringBuilder sbRegPath = new StringBuilder( );
		for ( int i = 0 ; i < this.baseName.length ; i++ ) {
			if ( i != 0 ) {
				sbRegPath.append( '/' );
			}
			sbRegPath.append( this.baseName[ i ] );
		}
		this.setRegistryName( Mmm.ID , sbRegPath.toString( ) );
		CRegistry.addItem( this );

		return this;
	}


	@Override
	public int getMetadata( final int damage )
	{
		return damage;
	}


	@Override
	public String getUnlocalizedName( final ItemStack stack )
	{
		return super.getUnlocalizedName( ) + "." + this.variants[ stack.getMetadata( ) ];
	}


	@Override
	public int getVariantsCount( )
	{
		return this.variants.length;
	}


	@Override
	public ModelResourceLocation getModelResourceLocation( final int variant )
	{
		return new ModelResourceLocation( //
				new ResourceLocation( Mmm.ID ,
						this.getRegistryName( ).getResourcePath( ) + "/" + this.variants[ variant ] ) , //
				"inventory" );
	}

}
