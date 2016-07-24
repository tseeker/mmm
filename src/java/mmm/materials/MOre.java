package mmm.materials;


import java.util.Random;

import javax.annotation.Nullable;

import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class MOre
		extends Block
		implements I_RecipeRegistrar
{
	private int dropMin , dropMax;
	private Item dropItems;
	private int dropMeta;

	private int expMin , expMax;

	private MMetal metal;
	private int genQuantity;
	private E_MItemType smeltingOutput;


	public MOre( final String name , final int harvestLevel )
	{
		this( name , harvestLevel , Material.ROCK.getMaterialMapColor( ) );
	}


	public MOre( final String name , final int harvestLevel , final MapColor color )
	{
		super( Material.ROCK , color );
		this.setCreativeTab( CreativeTabs.BUILDING_BLOCKS );
		this.setSoundType( SoundType.STONE );
		this.setHardness( 3.0f );
		this.setResistance( 5.0f );
		this.setHarvestLevel( "pickaxe" , harvestLevel );
		CRegistry.setIdentifiers( this , "materials" , "ore" , name );
		this.dropMin = this.dropMax = 1;
		this.expMin = this.expMax = 0;
		this.metal = null;
		this.genQuantity = 0;
		CRegistry.addBlock( this );
	}


	public MOre setDrops( final Item item )
	{
		return this.setDrops( item , 0 , 1 , 1 );
	}


	public MOre setDrops( final Item item , final int meta )
	{
		return this.setDrops( item , meta , 1 , 1 );
	}


	public MOre setDrops( final Item item , final int dropMin , final int dropMax )
	{
		return this.setDrops( item , 0 , dropMin , dropMax );
	}


	public MOre setDrops( final Item item , final int meta , final int dropMin , final int dropMax )
	{
		assert dropMin <= dropMax;
		assert dropMin > 0;
		this.dropMin = dropMin;
		this.dropMax = dropMax;
		this.dropItems = item;
		this.dropMeta = meta;
		if ( item instanceof MItem ) {
			final MItem oreItem = (MItem) item;
			if ( oreItem.itemType != E_MItemType.ORE ) {
				throw new IllegalArgumentException( "ore item expected" );
			}
			oreItem.setExtraInfo( this );
		}
		return this;
	}


	public MOre setExperience( final int value )
	{
		return this.setExperience( value , value );
	}


	public MOre setExperience( final int min , final int max )
	{
		assert min <= max;
		assert min >= 0;
		this.expMin = min;
		this.expMax = max;
		return this;
	}


	public MOre setMetal( final MMetal metal )
	{
		return this.setMetal( metal , 1 , E_MItemType.INGOT );
	}


	public MOre setMetal( final MMetal metal , final int count )
	{
		return this.setMetal( metal , count , E_MItemType.INGOT );
	}


	public MOre setMetal( final MMetal metal , final int quantity , final E_MItemType type )
	{
		assert metal != null && quantity > 0;
		this.metal = metal;
		this.genQuantity = quantity;
		this.smeltingOutput = type;
		return this;
	}


	@Override
	public MOre setResistance( final float resistance )
	{
		super.setResistance( resistance );
		return this;
	}


	@Override
	public MOre setHardness( final float hardness )
	{
		super.setHardness( hardness );
		return this;
	}


	@Override
	@Nullable
	public Item getItemDropped( final IBlockState state , final Random rand , final int fortune )
	{
		return this.dropItems == null ? Item.getItemFromBlock( this ) : this.dropItems;
	}


	@Override
	public int damageDropped( final IBlockState state )
	{
		return this.dropItems == null ? 0 : this.dropMeta;
	}


	@Override
	public int quantityDropped( final Random random )
	{
		if ( this.dropMax == this.dropMin ) {
			return this.dropMin;
		}
		return this.dropMin + random.nextInt( 1 + this.dropMax - this.dropMin );
	}


	@Override
	public int quantityDroppedWithBonus( final int fortune , final Random random )
	{
		int dropped = this.quantityDropped( random );
		if ( fortune > 0 && this.dropItems != null ) {
			dropped += Math.max( 0 , random.nextInt( fortune + 2 ) - 1 );
		}
		return dropped;
	}


	@Override
	public int getExpDrop( final IBlockState state , final IBlockAccess world , final BlockPos pos , final int fortune )
	{
		final Random rand = world instanceof World ? ( (World) world ).rand : Block.RANDOM;
		if ( this.dropItems != null ) {
			if ( this.expMin == this.expMax ) {
				return this.expMin;
			}
			return MathHelper.getRandomIntegerInRange( rand , this.expMin , this.expMax );
		}
		return 0;
	}


	@Override
	public ItemStack getItem( final World worldIn , final BlockPos pos , final IBlockState state )
	{
		return new ItemStack( this );
	}


	@Override
	public void registerRecipes( )
	{
		if ( this.metal != null ) {
			final ItemStack output;
			final float xpMul;
			switch ( this.smeltingOutput ) {
				case INGOT:
					output = new ItemStack( this.metal.INGOT , this.genQuantity );
					xpMul = 1;
					break;
				case NUGGET:
					output = new ItemStack( this.metal.NUGGET , this.genQuantity );
					xpMul = 1 / 9f;
					break;
				default:
					throw new IllegalStateException( this.smeltingOutput.toString( ) );
			}

			final float xp = this.metal.SMELTING_XP * this.genQuantity * xpMul;
			if ( this.dropItems == null ) {
				GameRegistry.addSmelting( this , output , xp );
			} else {
				GameRegistry.addSmelting( this.dropItems , output , xp );
			}
		}
	}

}
