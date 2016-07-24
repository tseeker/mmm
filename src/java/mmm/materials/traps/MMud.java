package mmm.materials.traps;


import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.core.api.blocks.I_TrapBlock;
import mmm.utils.UMaths;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;



public class MMud
		extends Block
		implements I_TrapBlock , I_RecipeRegistrar
{
	private static final AxisAlignedBB MUD_AABB = UMaths.makeBlockAABB( 0 , 0 , 0 , 16 , 15 , 16 );


	public MMud( )
	{
		super( Material.GROUND );
		this.setHardness( 0.6f );
		this.setHarvestLevel( "shovel" , 0 );
		this.setSoundType( SoundType.GROUND );
		this.setCreativeTab( CreativeTabs.BUILDING_BLOCKS );
		CRegistry.setIdentifiers( this , "materials" , "trap" , "mud" );
	}


	@Override
	public IBlockState[] getReplacedBlocks( )
	{
		return new IBlockState[] {
				Blocks.DIRT.getDefaultState( ) , Blocks.GRASS.getDefaultState( )
		};
	}


	@Override
	public String getTrapType( )
	{
		return "slow";
	}


	@Override
	public AxisAlignedBB getCollisionBoundingBox( final IBlockState blockState , final World worldIn ,
			final BlockPos pos )
	{
		return MMud.MUD_AABB;
	}


	@Override
	public void onEntityCollidedWithBlock( final World worldIn , final BlockPos pos , final IBlockState state ,
			final Entity entityIn )
	{
		entityIn.motionX *= .1;
		entityIn.motionZ *= .1;
	}


	@Override
	public void registerRecipes( )
	{
		GameRegistry.addSmelting( this , new ItemStack( Blocks.DIRT ) , .01f );
	}

}
