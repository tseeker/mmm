package mmm.core;


import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.launchwrapper.Launch;



/** Accessors for various Minecraft fields */
public class CAccessors
{
	private static final boolean isDeobfuscated;


	private static MethodHandle createGetter( final Class< ? > cls , final String fnForge , final String fnSearge )
	{
		final String fn = CAccessors.isDeobfuscated ? fnForge : fnSearge;
		Field f;
		try {
			f = cls.getDeclaredField( fn );
		} catch ( final NoSuchFieldException e ) {
			throw new RuntimeException( "could not find field " + fnForge + " / " + fnSearge //
					+ " in class " + cls.getCanonicalName( ) , e );
		}
		f.setAccessible( true );
		try {
			return MethodHandles.lookup( ).unreflectGetter( f );
		} catch ( final IllegalAccessException e ) {
			throw new RuntimeException( "error while creating getter for " + fnForge + " / " + fnSearge //
					+ " in class " + cls.getCanonicalName( ) , e );
		}
	}

	private static final MethodHandle fg_Block_blockHardness;
	private static final MethodHandle fg_Block_blockResistance;

	static {
		isDeobfuscated = (Boolean) Launch.blackboard.get( "fml.deobfuscatedEnvironment" );
		fg_Block_blockHardness = CAccessors.createGetter( Block.class , "blockHardness" , "field_149782_v" );
		fg_Block_blockResistance = CAccessors.createGetter( Block.class , "blockResistance" , "field_149781_w" );
	}


	public static void preInit( )
	{
		// EMPTY
	}


	public static float getBlockHardness( final Block block )
			throws Throwable
	{
		return (float) CAccessors.fg_Block_blockHardness.invokeExact( block );
	}


	public static float getBlockResistance( final Block block )
			throws Throwable
	{
		return (float) CAccessors.fg_Block_blockResistance.invokeExact( block );
	}
}
