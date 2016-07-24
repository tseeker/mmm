package mmm.tech.tools;


import mmm.MmmMaterials;



public class TTTools
{
	public final TTToolSet COPPER;
	public final TTToolSet BRONZE;
	public final TTToolSet STEEL;


	public TTTools( )
	{
		this.COPPER = new TTToolSet( "copper" , MmmMaterials.METAL.COPPER.INGOT , //
				2 , 192 , 5.0f , 1.5f , 16 , 7 , -3 );
		this.BRONZE = new TTToolSet( "bronze" , MmmMaterials.ALLOY.BRONZE.INGOT , //
				2 , 212 , 5.5f , 1.75f , 20 , 7.5f , -3.1f );
		this.STEEL = new TTToolSet( "steel" , MmmMaterials.ALLOY.STEEL.INGOT , //
				3 , 800 , 7f , 2.5f , 12 , 8.0f , -3f );
	}
}
