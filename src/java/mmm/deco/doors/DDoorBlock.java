package mmm.deco.doors;


import java.util.Random;

import mmm.core.CRegistry;
import mmm.core.api.blocks.I_StateMapperProvider;
import mmm.materials.MWood;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class DDoorBlock
		extends BlockDoor
		implements I_StateMapperProvider
{

	private final DDoor door;
	private final MapColor mapColor;


	public DDoorBlock( final DDoor door , final MWood wood )
	{
		super( Material.WOOD );
		this.mapColor = wood.getMapColor( );
		this.door = door;

		this.setHardness( 3f );
		this.setSoundType( SoundType.WOOD );

		CRegistry.setIdentifiers( this , "deco" , "door" , wood.getSuffix( ) );
	}


	@Override
	public MapColor getMapColor( final IBlockState state )
	{
		return this.mapColor;
	}


	@Override
	public Item getItemDropped( final IBlockState state , final Random rand , final int fortune )
	{
		return state.getValue( BlockDoor.HALF ) == BlockDoor.EnumDoorHalf.UPPER ? null : this.door.ITEM;
	}


	@Override
	public ItemStack getPickBlock( final IBlockState state , final RayTraceResult target , final World world ,
			final BlockPos pos , final EntityPlayer player )
	{
		return new ItemStack( this.door.ITEM );
	}


	@Override
	@SideOnly( Side.CLIENT )
	public IStateMapper getStateMapper( )
	{
		return new StateMap.Builder( ).ignore( BlockDoor.POWERED ).build( );
	}

}
