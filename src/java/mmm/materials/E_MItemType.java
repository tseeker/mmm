package mmm.materials;


public enum E_MItemType {

	ORE( "stone" , MOre.class ) ,
	FUEL( "stone" ) ,
	INGOT( "ingot" , MMetal.class ) ,
	NUGGET( "nugget" , MMetal.class ) ,
	MISC( "stone" ),
	//
	;

	public final String path;
	public final Class< ? > extraInfoType;


	private E_MItemType( final String path )
	{
		this( path , Object.class );
	}


	private E_MItemType( final String path , final Class< ? > extraInfoType )
	{
		this.path = path;
		this.extraInfoType = extraInfoType;
	}

}
