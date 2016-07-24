package mmm.world;


import java.util.Random;

import mmm.core.api.world.I_DefaultPopulateHandler;
import mmm.core.api.world.I_TrappedBiome;
import mmm.world.gen.WGBasalt;
import mmm.world.gen.WGFlora;
import mmm.world.gen.WGTrapBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeSwamp;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class WDefaultGenWatcher
{
	private final WGFlora floraGenerator = new WGFlora( );


	@SubscribeEvent
	public void onPopulate( final PopulateChunkEvent.Populate event )
	{
		final BlockPos bp = new BlockPos( event.getChunkX( ) * 16 , 0 , event.getChunkZ( ) * 16 );
		final Biome biome = event.getWorld( ).getBiomeGenForCoords( bp );
		if ( biome instanceof I_DefaultPopulateHandler
				&& ! ( (I_DefaultPopulateHandler) biome ).onDefaultPopulate( event ) ) {
			event.setResult( Result.DENY );
		}
	}


	@SubscribeEvent
	public void afterPopulate( final PopulateChunkEvent.Post event )
	{
		if ( event.isHasVillageGenerated( ) ) {
			return;
		}

		final BlockPos bp = new BlockPos( event.getChunkX( ) * 16 , 0 , event.getChunkZ( ) * 16 );
		final net.minecraft.world.World world = event.getWorld( );
		final Random rand = event.getRand( );

		new WGBasalt( false ).generate( world , rand , bp );

		final Biome biome = world.getBiomeGenForCoords( bp );
		// FIXME hardcoding is bad
		final WGTrapBlocks traps;
		if ( biome instanceof BiomeDesert ) {
			traps = new WGTrapBlocks( false , .05f );
		} else if ( biome instanceof BiomeSwamp ) {
			traps = new WGTrapBlocks( false , .1f );
		} else if ( biome instanceof I_TrappedBiome ) {
			traps = new WGTrapBlocks( false , ( (I_TrappedBiome) biome ).getTrapBlockChance( ) );
		} else {
			return;
		}

		traps.generate( world , rand , bp );
	}


	@SubscribeEvent
	public void onBiomeDecorate( final DecorateBiomeEvent.Post event )
	{
		this.floraGenerator.generate( event.getWorld( ) , event.getRand( ) , event.getPos( ) );
	}
}
