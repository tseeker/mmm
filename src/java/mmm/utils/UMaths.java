package mmm.utils;


import net.minecraft.util.math.AxisAlignedBB;



public class UMaths
{

	public static AxisAlignedBB makeBlockAABB( final int x1 , final int y1 , final int z1 , final int x2 ,
			final int y2 , final int z2 )
	{
		return new AxisAlignedBB( x1 * .0625 , y1 * .0625 , z1 * .0625 , x2 * .0625 , y2 * .0625 , z2 * .0625 );
	}

}
