package mmm.core;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.google.common.collect.Maps;

import mmm.Mmm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class CGui
		implements IGuiHandler
{
	private static final HashMap< Class< ? extends TileEntity > , Constructor< ? > > SERVER_SIDE //
			= Maps.newHashMap( );
	private static final HashMap< Class< ? extends TileEntity > , Constructor< ? > > CLIENT_SIDE //
			= Maps.newHashMap( );

	static {
		new CGui( );
	}


	public static void registerTileEntityGUI( final Class< ? extends TileEntity > teClass ,
			final String containerClassName , final String guiClassName )
	{
		if ( CGui.SERVER_SIDE.containsKey( teClass ) ) {
			throw new IllegalArgumentException( "duplicate GUI registration for tile entity class " + teClass );
		}

		CGui.addTileEntityConstructor( CGui.SERVER_SIDE , teClass , Container.class , containerClassName ,
				"container" );
		if ( FMLLaunchHandler.side( ) == Side.CLIENT ) {
			String clsName = "net.minecraft.client.gui.inventory.GuiContainer";
			if ( FMLDeobfuscatingRemapper.INSTANCE.isRemappedClass( clsName ) ) {
				clsName = FMLDeobfuscatingRemapper.INSTANCE.map( clsName );
			}
			Class< ? > guiClass;
			try {
				guiClass = Class.forName( clsName );
			} catch ( final ClassNotFoundException e ) {
				throw new IllegalArgumentException( "couldn't find GUI class" , e );
			}
			CGui.addTileEntityConstructor( CGui.CLIENT_SIDE , teClass , guiClass , guiClassName , "GUI" );
		}
	}


	public static void openTileEntityGUI( final EntityPlayer player , final World world , final BlockPos pos )
	{
		player.openGui( Mmm.get( ) , 0 , world , pos.getX( ) , pos.getY( ) , pos.getZ( ) );
	}


	private static void addTileEntityConstructor(
			final HashMap< Class< ? extends TileEntity > , Constructor< ? > > output ,
			final Class< ? extends TileEntity > teClass , final Class< ? > parent , final String className ,
			final String designation )
	{
		Class< ? > klass;
		try {
			klass = Class.forName( className );
		} catch ( final ClassNotFoundException e ) {
			throw new IllegalArgumentException( "bad " + designation + " class name" , e );
		}
		if ( !parent.isAssignableFrom( klass ) ) {
			throw new IllegalArgumentException( "class " + className + " is not a " + designation + " sub-class" );
		}
		output.put( teClass , CGui.findConstructor( teClass , klass ) );
	}


	private static Constructor< ? > findConstructor( final Class< ? > teClass , final Class< ? > klass )
	{
		final Constructor< ? >[] allCons = klass.getConstructors( );
		for ( final Constructor< ? > cons : allCons ) {
			final Class< ? >[] types = cons.getParameterTypes( );
			if ( ! ( types.length == 2 && types[ 0 ].isAssignableFrom( InventoryPlayer.class )
					&& types[ 1 ].isAssignableFrom( teClass ) ) ) {
				continue;
			}
			return cons;
		}
		throw new IllegalArgumentException( "class " + klass.getName( ) + " does not have a supported constructor" );
	}


	private CGui( )
	{
		NetworkRegistry.INSTANCE.registerGuiHandler( Mmm.get( ) , this );
	}


	@Override
	public Object getServerGuiElement( final int ID , final EntityPlayer player , final World world , final int x ,
			final int y , final int z )
	{
		if ( ID == 0 ) {
			return this.getObjectForTE( player , world , x , y , z , CGui.SERVER_SIDE , "container" );
		}
		return null;
	}


	@Override
	@SideOnly( Side.CLIENT )
	public Object getClientGuiElement( final int ID , final EntityPlayer player , final World world , final int x ,
			final int y , final int z )
	{
		if ( ID == 0 ) {
			return this.getObjectForTE( player , world , x , y , z , CGui.CLIENT_SIDE , "GUI" );
		}
		return null;
	}


	private Object getObjectForTE( final EntityPlayer player , final World world , final int x , final int y ,
			final int z , final HashMap< Class< ? extends TileEntity > , Constructor< ? > > constructors ,
			final String designation )
	{
		final BlockPos pos = new BlockPos( x , y , z );
		final TileEntity tileEntity = world.getTileEntity( pos );
		final Constructor< ? > cns = constructors.get( tileEntity.getClass( ) );
		if ( cns == null ) {
			return null;
		}
		try {
			return cns.newInstance( player.inventory , tileEntity );
		} catch ( InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e ) {
			throw new RuntimeException( "couldn't create tile entity " + designation //
					+ " for " + tileEntity.getClass( ) );
		}
	}

}
