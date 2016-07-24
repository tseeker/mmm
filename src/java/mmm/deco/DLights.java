package mmm.deco;


public class DLights
{

	public final DControllableLightSource PLATE;
	public final DControllableLightSource BUTTON;


	public DLights( )
	{
		this.PLATE = new DControllableLightSource( "plate" , true , //
				( ls , lit ) -> new DLightPlate( false , ls , lit ) );
		this.BUTTON = new DControllableLightSource( "button" , false , //
				( ls , lit ) -> new DLightPlate( true , ls , lit ) );
	}

}
