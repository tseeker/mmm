package mmm.recipes;


import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;



class ROreRecipeHelper
{

	static RRequirements extractRequirements( final Object[] recipe )
	{
		final List< ItemStack > combinedInputStacks = new ArrayList<>( );
		final IdentityHashMap< List< ItemStack > , Integer > combinedInputOreLists = new IdentityHashMap<>( );

		ROreRecipeHelper.extractInputs( recipe , combinedInputStacks , combinedInputOreLists );

		final RRequirements reqs = new RRequirements( combinedInputStacks.size( ) + combinedInputOreLists.size( ) );
		int i;
		for ( i = 0 ; i < combinedInputStacks.size( ) ; i++ ) {
			final ItemStack stack = combinedInputStacks.get( i );
			reqs.put( i , stack , stack.stackSize );
		}
		for ( final Map.Entry< List< ItemStack > , Integer > entry : combinedInputOreLists.entrySet( ) ) {
			reqs.put( i++ , entry.getKey( ) , entry.getValue( ) );
		}
		return reqs;
	}


	private static void extractInputs( final Object[] recipe , final List< ItemStack > combinedInputStacks ,
			final IdentityHashMap< List< ItemStack > , Integer > combinedInputOreLists )
	{
		OUTER: for ( final Object input : recipe ) {
			if ( input instanceof ItemStack ) {
				final ItemStack invStack = (ItemStack) input;
				for ( final ItemStack ciStack : combinedInputStacks ) {
					if ( OreDictionary.itemMatches( ciStack , invStack , true ) ) {
						ciStack.stackSize++;
						continue OUTER;
					}
				}
				final ItemStack ciStack = invStack.copy( );
				ciStack.stackSize = 1;
				combinedInputStacks.add( ciStack );

			} else if ( input instanceof List ) {
				@SuppressWarnings( "unchecked" )
				final List< ItemStack > oreList = (List< ItemStack >) input;
				if ( combinedInputOreLists.containsKey( oreList ) ) {
					combinedInputOreLists.put( oreList , combinedInputOreLists.get( oreList ) + 1 );
				} else {
					combinedInputOreLists.put( oreList , 1 );
				}
			}
		}
	}

}
