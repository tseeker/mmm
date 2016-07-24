package mmm.materials.traps;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.core.api.blocks.I_TrapBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class MQuicksand
		extends BlockFalling
		implements I_TrapBlock , I_RecipeRegistrar
{
	public final IBlockState forType;


	public MQuicksand( final String name , final IBlockState forType )
	{
		super( Material.SAND );
		this.forType = forType;
		this.setHardness( .6f );
		this.setSoundType( SoundType.SAND );
		this.setCreativeTab( CreativeTabs.BUILDING_BLOCKS );
		this.setHarvestLevel( "shovel" , 0 );
		CRegistry.setIdentifiers( this , "materials" , "trap" , name );
	}


	@Override
	public IBlockState[] getReplacedBlocks( )
	{
		return new IBlockState[] {
				this.forType
		};
	}


	@Override
	public String getTrapType( )
	{
		return "quicksand";
	}


	@Override
	public MapColor getMapColor( final IBlockState state )
	{
		return this.forType.getMapColor( );
	}


	@Override
	public AxisAlignedBB getCollisionBoundingBox( final IBlockState blockState , final World worldIn ,
			final BlockPos pos )
	{
		return Block.NULL_AABB;
	}


	@Override
	public void onEntityCollidedWithBlock( final World worldIn , final BlockPos pos , final IBlockState state ,
			final Entity entityIn )
	{
		entityIn.setInWeb( );
	}


	@Override
	public boolean canSustainPlant( final IBlockState state , final IBlockAccess world , final BlockPos pos ,
			final EnumFacing direction , final IPlantable plantable )
	{
		switch ( plantable.getPlantType( world , pos.offset( direction ) ) ) {

			case Desert:
				return true;

			case Beach:
				return world.getBlockState( pos.east( ) ).getMaterial( ) == Material.WATER
						|| world.getBlockState( pos.west( ) ).getMaterial( ) == Material.WATER
						|| world.getBlockState( pos.north( ) ).getMaterial( ) == Material.WATER
						|| world.getBlockState( pos.south( ) ).getMaterial( ) == Material.WATER;

			default:
				return false;
		}
	}


	@Override
	public void registerRecipes( )
	{
		final Block block = this.forType.getBlock( );
		GameRegistry.addSmelting( this , new ItemStack( block , 1 , block.getMetaFromState( this.forType ) ) , .01f );
	}

}
