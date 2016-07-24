package mmm.materials;


import net.minecraft.block.material.MapColor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;



public class MMetals
{
	public final MMetal GOLD;
	public final MMetal IRON;
	public final MMetal COPPER;
	public final MMetal TIN;
	public final MMetal ZINC;


	public MMetals( )
	{
		this.GOLD = new MMetal( Blocks.GOLD_BLOCK , Items.GOLD_INGOT , Items.GOLD_NUGGET );
		this.IRON = new MMetal( Blocks.IRON_BLOCK , Items.IRON_INGOT , //
				new MItem( E_MItemType.NUGGET , "iron" ) );

		this.COPPER = new MMetal( "copper" , 0.4f , 4f , 1 , MapColor.DIRT );
		this.TIN = new MMetal( "tin" , 0.6f , 1f , 0 , MapColor.GRAY );
		this.ZINC = new MMetal( "zinc" , 0.4f , 4f , 1 , MapColor.GRAY );

	}
}
