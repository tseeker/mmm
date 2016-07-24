package mmm.core.api.tech;


public enum E_ActivationMode {

	ALWAYS_ACTIVE ,
	POWERED ,
	UNPOWERED ,
	DISABLED;

	private static final E_ActivationMode[] VALUES = E_ActivationMode.values( );


	public String getDisplayName( )
	{
		return "gui.mmm.tech.base.am." + this.toString( ).toLowerCase( );
	}


	public E_ActivationMode next( )
	{
		return E_ActivationMode.VALUES[ ( this.ordinal( ) + 1 ) % E_ActivationMode.VALUES.length ];
	}
}
