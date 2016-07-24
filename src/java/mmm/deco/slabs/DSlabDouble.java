package mmm.deco.slabs;


import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;



public class DSlabDouble
		extends A_DSlabBlock
{

	public final DSlabHalf singleSlab;


	public DSlabDouble( final DSlabHalf single , final String name )
	{
		super( single.modelState , name + "_double" );
		this.singleSlab = single;
	}


	@Override
	@Nullable
	public Item getItemDropped( final IBlockState state , final Random rand , final int fortune )
	{
		return Item.getItemFromBlock( this.singleSlab );
	}


	@Override
	public ItemStack getItem( final World worldIn , final BlockPos pos , final IBlockState state )
	{
		return new ItemStack( this.singleSlab );
	}


	@Override
	public boolean isDouble( )
	{
		return true;
	}

}