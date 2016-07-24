package mmm.deco.slabs;


import mmm.core.CAccessors;
import mmm.core.CRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;



public abstract class A_DSlabBlock
		extends BlockSlab
{

	public static enum E_Variant
			implements IStringSerializable {
		DEFAULT;

		@Override
		public String getName( )
		{
			return "default";
		}
	}

	public static final PropertyEnum< A_DSlabBlock.E_Variant > VARIANT //
			= PropertyEnum.< A_DSlabBlock.E_Variant > create( "variant" , A_DSlabBlock.E_Variant.class );

	public final IBlockState modelState;
	public final Block modelBlock;


	public A_DSlabBlock( final IBlockState modelState , final String name )
	{
		super( modelState.getMaterial( ) );
		this.modelState = modelState;
		this.modelBlock = modelState.getBlock( );

		this.setSoundType( this.modelBlock.getSoundType( ) );
		try {
			this.setHardness( CAccessors.getBlockHardness( this.modelBlock ) );
			this.setResistance( CAccessors.getBlockResistance( this.modelBlock ) + 0.5f );
		} catch ( final Throwable e ) {
			if ( e instanceof RuntimeException ) {
				throw (RuntimeException) e;
			}
			throw new RuntimeException( e );
		}
		this.setCreativeTab( CreativeTabs.BUILDING_BLOCKS );

		IBlockState state = this.blockState.getBaseState( );
		if ( !this.isDouble( ) ) {
			state = state.withProperty( BlockSlab.HALF , BlockSlab.EnumBlockHalf.BOTTOM );
		}
		this.setDefaultState( state.withProperty( A_DSlabBlock.VARIANT , E_Variant.DEFAULT ) );

		CRegistry.setIdentifiers( this , "deco" , "slabs" , name );
	}


	@Override
	public IBlockState getStateFromMeta( final int meta )
	{
		IBlockState iblockstate = this.getDefaultState( ).withProperty( A_DSlabBlock.VARIANT , E_Variant.DEFAULT );
		if ( !this.isDouble( ) ) {
			iblockstate = iblockstate.withProperty( BlockSlab.HALF ,
					( meta & 1 ) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP );
		}
		return iblockstate;
	}


	@Override
	public int getMetaFromState( final IBlockState state )
	{
		int i = 0;
		if ( !this.isDouble( ) && state.getValue( BlockSlab.HALF ) == BlockSlab.EnumBlockHalf.TOP ) {
			i |= 1;
		}
		return i;
	}


	@Override
	protected BlockStateContainer createBlockState( )
	{
		return this.isDouble( ) //
				? new BlockStateContainer( this , new IProperty[] {
						A_DSlabBlock.VARIANT
				} ) //
				: new BlockStateContainer( this , new IProperty[] {
						BlockSlab.HALF , A_DSlabBlock.VARIANT
				} );
	}


	@Override
	public String getUnlocalizedName( final int meta )
	{
		return super.getUnlocalizedName( );
	}


	@Override
	public IProperty< ? > getVariantProperty( )
	{
		return A_DSlabBlock.VARIANT;
	}


	@Override
	public Comparable< ? > getTypeForItem( final ItemStack stack )
	{
		return E_Variant.DEFAULT;
	}

}
