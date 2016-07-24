package mmm.utils;


import mmm.materials.MBlockType;
import mmm.materials.MBlockTypes;
import mmm.materials.MItemType;
import mmm.materials.MItemTypes;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;



public class UMaterials
{

	public static boolean hasType( final String type , final ItemStack stack )
	{
		return UMaterials.hasType( type , stack.getItem( ) , stack.getMetadata( ) );
	}


	@SuppressWarnings( "deprecation" )
	public static boolean hasType( final String type , final Item item , final int metadata )
	{
		if ( item instanceof ItemBlock ) {
			final MBlockType bt = MBlockTypes.get( type );
			if ( bt != null && bt.matches( ( (ItemBlock) item ).block.getStateFromMeta( metadata ) ) ) {
				return true;
			}
		}
		final MItemType it = MItemTypes.get( type );
		return it != null && it.matches( item , metadata );
	}


	public static boolean hasType( final String type , final Block block )
	{
		return UMaterials.hasType( type , block.getDefaultState( ) );
	}


	public static boolean hasType( final String type , final IBlockState state )
	{
		final MBlockType bt = MBlockTypes.get( type );
		if ( bt != null && bt.matches( state ) ) {
			return true;
		}

		final MItemType it = MItemTypes.get( type );
		final Block block = state.getBlock( );
		final Item item = Item.getItemFromBlock( block );
		return it != null && item != null && it.matches( item , block.damageDropped( state ) );
	}


	public static boolean hasType( final MBlockType type , final ItemStack stack )
	{
		return UMaterials.hasType( type , stack.getItem( ) , stack.getMetadata( ) );
	}


	@SuppressWarnings( "deprecation" )
	public static boolean hasType( final MBlockType type , final Item item , final int metadata )
	{
		final MItemType equivalentItemCheck = MItemTypes.get( type.name );
		if ( equivalentItemCheck != null && equivalentItemCheck.matches( item , metadata ) ) {
			return true;
		}
		if ( ! ( item instanceof ItemBlock ) ) {
			return false;
		}
		return UMaterials.hasType( type , ( (ItemBlock) item ).block.getStateFromMeta( metadata ) );
	}


	public static boolean hasType( final MBlockType type , final Block block )
	{
		return UMaterials.hasType( type , block.getDefaultState( ) );
	}


	public static boolean hasType( final MBlockType type , final IBlockState state )
	{
		return type.matches( state );
	}


	public static boolean hasType( final MItemType type , final ItemStack stack )
	{
		return UMaterials.hasType( type , stack.getItem( ) , stack.getMetadata( ) );
	}


	@SuppressWarnings( "deprecation" )
	public static boolean hasType( final MItemType type , final Item item , final int metadata )
	{
		if ( type.matches( item , metadata ) ) {
			return true;
		}
		if ( item instanceof ItemBlock ) {
			final MBlockType bt = MBlockTypes.get( type.name );
			return bt != null && bt.matches( ( (ItemBlock) item ).block.getStateFromMeta( metadata ) );
		}
		return false;
	}


	public static boolean hasType( final MItemType type , final Block block )
	{
		final Item item = Item.getItemFromBlock( block );
		return item != null && type.matches( item , block.damageDropped( block.getDefaultState( ) ) );
	}


	public static boolean hasType( final MItemType type , final IBlockState state )
	{
		final Block block = state.getBlock( );
		final Item item = Item.getItemFromBlock( block );
		return item != null && type.matches( item , block.damageDropped( state ) );
	}
}
