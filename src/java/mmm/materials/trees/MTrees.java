package mmm.materials.trees;


import mmm.utils.UMaths;
import mmm.world.trees.WTBamboo;
import mmm.world.trees.WTHevea;
import mmm.world.trees.WTHeveaBig;
import mmm.world.trees.WTHeveaMega;
import net.minecraft.block.material.MapColor;



public class MTrees
{

	public final MTree HEVEA;
	public final MTree BAMBOO;


	public MTrees( )
	{
		this.HEVEA = new MTree( "hevea" ) //
				.setBarkColor( MapColor.GRAY ) //
				.setBaseFireInfo( 5 , 8 ) //
				.setTreeGenerator( WTHevea.createSaplingGen( ) ) //
				.setBigTreeGenerator( WTHeveaBig.createSaplingGen( ) , .05f ) //
				.setMegaTreeGenerator( WTHeveaMega.createSaplingGen( ) ) //
				.register( );

		this.BAMBOO = new MTree( "bamboo" ) //
				.setBarkColor( MapColor.FOLIAGE ) //
				.setLogBoundingBox( UMaths.makeBlockAABB( 4 , 0 , 4 , 12 , 16 , 12 ) ) //
				.setGrowthStages( 8 ) //
				.setGrowthChance( .8f ) //
				.setSaplingDropChance( 10 ) //
				.offsetSapling( ) //
				.setTreeGenerator( WTBamboo.createSaplingGen( false ) ) //
				.setBigTreeGenerator( WTBamboo.createSaplingGen( true ) , .15f ) //
				.register( );
	}
}
