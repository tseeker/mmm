package mmm.deco;


import java.util.function.BiFunction;

import mmm.core.CRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;



public class DControllableLightSource
{

	public final Block LIT;
	public final Block UNLIT;
	public final ItemBlock ITEM;


	public DControllableLightSource( final String name , final boolean litByDefault ,
			final BiFunction< DControllableLightSource , Boolean , Block > constructor )
	{
		this.LIT = constructor.apply( this , true );
		CRegistry.setIdentifiers( this.LIT , "deco" , "light" , name , "lit" );

		this.UNLIT = constructor.apply( this , false );
		CRegistry.setIdentifiers( this.UNLIT , "deco" , "light" , name , "unlit" );

		this.ITEM = new ItemBlock( litByDefault ? this.LIT : this.UNLIT );
		CRegistry.setIdentifiers( this.ITEM , "deco" , "light" , name );

		CRegistry.addBlock( this.LIT , null );
		CRegistry.addBlock( this.UNLIT , null );
		CRegistry.addItem( this.ITEM );
	}

}
