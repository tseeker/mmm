package mmm.food;


import java.util.function.Predicate;

import mmm.core.CRegistry;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;



public class FMilkType
{
	public final String name;
	public final boolean isVanilla;
	public final Class< ? extends EntityAnimal > animal;
	public final Item bucket;
	private int period = 12000;
	private Predicate< EntityAnimal > extraCheck;


	public FMilkType( )
	{
		this( "cow" , true , EntityCow.class , Items.MILK_BUCKET );
	}


	public FMilkType( final String name , final Class< ? extends EntityAnimal > animal )
	{
		this( name , false , animal , null );
	}


	private FMilkType( final String name , final boolean isVanilla , final Class< ? extends EntityAnimal > animal ,
			Item bucket )
	{
		this.name = name;
		this.isVanilla = isVanilla;
		this.animal = animal;
		if ( bucket == null ) {
			bucket = new FMilkBucket( this );
			CRegistry.addItem( bucket );
		}
		this.bucket = bucket;

	}


	public FMilkType setExtraCheck( final Predicate< EntityAnimal > extraCheck )
	{
		this.extraCheck = extraCheck;
		return this;
	}


	public int getPeriod( )
	{
		return this.period;
	}


	public FMilkType setPeriod( final int period )
	{
		this.period = period;
		return this;
	}


	public boolean check( final EntityAnimal animal )
	{
		return this.extraCheck == null || this.extraCheck.test( animal );
	}

}
