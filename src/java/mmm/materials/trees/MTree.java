package mmm.materials.trees;


import java.util.Random;

import mmm.core.CRegistry;
import mmm.core.api.I_RecipeRegistrar;
import mmm.world.trees.A_WTTreeGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;



public class MTree
		implements I_RecipeRegistrar
{
	public static interface I_SaplingChecker
	{
		public boolean canSaplingStay( World worldIn , BlockPos pos );
	}

	public final String NAME;
	public final MLog LOG;
	public final MLeaves LEAVES;
	public final MSapling SAPLING;
	public final MPlanks PLANKS;

	private MapColor barkColor = MapColor.WOOD;
	private MapColor plankColor = MapColor.WOOD;

	private AxisAlignedBB logBoundingBoxX = null;
	private AxisAlignedBB logBoundingBoxY = null;
	private AxisAlignedBB logBoundingBoxZ = null;

	private int baseFireEncouragement = 5;
	private int baseFlammability = 5;

	private int saplingDropChance = 20;
	private int fruitDropChance = 0;
	private Item fruit = null;

	private boolean mustOffsetSapling = false;
	private int saplingGrowthStages = 2;
	private float bonemealChance = .45f;
	private float growthChance = .142f;
	private int growthMinLight = 9;
	private int growthMaxLight = 16;
	private I_SaplingChecker saplingCheck = new I_SaplingChecker( ) {

		@Override
		public boolean canSaplingStay( final World worldIn , final BlockPos pos )
		{
			final BlockPos soilPos = pos.down( );
			final IBlockState soilState = worldIn.getBlockState( soilPos );
			final Block down = soilState.getBlock( );
			return down.canSustainPlant( soilState , worldIn , soilPos , EnumFacing.UP , (IPlantable) Blocks.SAPLING );
		}

	};

	private WorldGenAbstractTree genNormal;
	private WorldGenAbstractTree genBig;
	private float genBigChance;
	private WorldGenAbstractTree genMega;


	public MTree( final String name )
	{
		this.NAME = name;
		this.LOG = new MLog( this );
		this.LEAVES = new MLeaves( this );
		this.SAPLING = new MSapling( this );
		this.PLANKS = new MPlanks( this );
	}


	public MTree setBarkColor( final MapColor barkColor )
	{
		this.barkColor = barkColor;
		return this;
	}


	public MTree setPlankColor( final MapColor plankColor )
	{
		this.plankColor = plankColor;
		return this;
	}


	public MTree setLogBoundingBox( final AxisAlignedBB box )
	{
		this.logBoundingBoxY = box;
		this.logBoundingBoxX = new AxisAlignedBB( box.minY , box.minX , box.minZ , box.maxY , box.maxX , box.maxZ );
		this.logBoundingBoxZ = new AxisAlignedBB( box.minX , box.minZ , box.minY , box.maxX , box.maxZ , box.maxY );
		return this;
	}


	public MTree setSaplingDropChance( final int chance )
	{
		this.saplingDropChance = chance;
		return this;
	}


	public MTree setFruit( final Item fruit , final float dropChance )
	{
		this.fruit = fruit;
		this.fruitDropChance = (int) ( 200 * dropChance );
		return this;
	}


	public MTree setBaseFireInfo( final int encouragement , final int flammability )
	{
		this.baseFireEncouragement = encouragement;
		this.baseFlammability = flammability;
		return this;
	}


	public MTree offsetSapling( )
	{
		this.mustOffsetSapling = true;
		return this;
	}


	public MTree setGrowthStages( final int stages )
	{
		this.saplingGrowthStages = stages;
		return this;
	}


	public MTree setBonemealChance( final float chance )
	{
		this.bonemealChance = chance;
		return this;
	}


	public MTree setGrowthChance( final float chance )
	{
		this.growthChance = chance;
		return this;
	}


	public MTree setGrowthLightConditions( final int min , final int max )
	{
		this.growthMinLight = min;
		this.growthMaxLight = max;
		return this;
	}


	public MTree setSaplingCheck( final I_SaplingChecker check )
	{
		this.saplingCheck = check;
		return this;
	}


	public MTree setTreeGenerator( final A_WTTreeGenerator generator )
	{
		this.genNormal = generator;
		if ( generator != null ) {
			generator.setTreeMaterials( this );
		}
		return this;
	}


	public MTree setBigTreeGenerator( final A_WTTreeGenerator generator )
	{
		return this.setBigTreeGenerator( generator , .1f );
	}


	public MTree setBigTreeGenerator( final A_WTTreeGenerator generator , final float chance )
	{
		this.genBig = generator;
		this.genBigChance = chance;
		if ( generator != null ) {
			generator.setTreeMaterials( this );
		}
		return this;
	}


	public MTree setMegaTreeGenerator( final A_WTTreeGenerator generator )
	{
		this.genMega = generator;
		if ( generator != null ) {
			generator.setTreeMaterials( this );
		}
		return this;
	}


	public MTree register( )
	{
		CRegistry.addBlock( this.LOG );
		CRegistry.addBlock( this.LEAVES );
		CRegistry.addBlock( this.PLANKS );
		final Item sapling = CRegistry.addBlock( this.SAPLING );

		if ( this.baseFlammability != 0 ) {
			Blocks.FIRE.setFireInfo( this.LOG , this.baseFireEncouragement , this.baseFlammability );
			Blocks.FIRE.setFireInfo( this.LEAVES , this.baseFireEncouragement * 6 , this.baseFlammability * 12 );
			Blocks.FIRE.setFireInfo( this.PLANKS , this.baseFireEncouragement , this.baseFlammability * 4 );
			Blocks.FIRE.setFireInfo( this.SAPLING , this.baseFireEncouragement * 2 , this.baseFlammability * 8 );
			CRegistry.setFuel( sapling , this.baseFlammability * 30 );
		}

		OreDictionary.registerOre( "logWood" , this.LOG );
		OreDictionary.registerOre( "plankWood" , this.PLANKS );
		OreDictionary.registerOre( "treeLeaves" , this.LEAVES );
		OreDictionary.registerOre( "treeSapling" , this.SAPLING );

		CRegistry.addRecipeRegistrar( this );
		return this;
	}


	public MapColor getBarkColor( )
	{
		return this.barkColor;
	}


	public MapColor getPlankColor( )
	{
		return this.plankColor;
	}


	public AxisAlignedBB getLogBoundingBox( final BlockLog.EnumAxis axis )
	{
		switch ( axis ) {
			case X:
				return this.logBoundingBoxX;
			case Z:
				return this.logBoundingBoxZ;
			default:
				return this.logBoundingBoxY;
		}
	}


	public boolean hasLogBoundingBox( )
	{
		return this.logBoundingBoxX != null;
	}


	public int getBaseFireEncouragement( )
	{
		return this.baseFireEncouragement;
	}


	public int getBaseFlammability( )
	{
		return this.baseFlammability;
	}


	public int getSaplingDropChance( )
	{
		return this.saplingDropChance;
	}


	public int getFruitDropChance( )
	{
		return this.fruitDropChance;
	}


	public Item getFruit( )
	{
		return this.fruit;
	}


	public boolean mustOffsetSapling( )
	{
		return this.mustOffsetSapling;
	}


	public int getSaplingGrowthStages( )
	{
		return this.saplingGrowthStages;
	}


	public float getBonemealChance( )
	{
		return this.bonemealChance;
	}


	public float getGrowthChance( )
	{
		return this.growthChance;
	}


	public boolean checkGrowthLight( final int lightLevel )
	{
		return lightLevel >= this.growthMinLight && lightLevel <= this.growthMaxLight;
	}


	public boolean canSaplingStay( final World worldIn , final BlockPos pos )
	{
		if ( this.saplingCheck != null ) {
			return this.saplingCheck.canSaplingStay( worldIn , pos );
		}
		return true;
	}


	public boolean canGenerateBigOrSmall( )
	{
		return this.genNormal != null || this.genBig != null;
	}


	public boolean canGenerateMega( )
	{
		return this.genMega != null;
	}


	public boolean generateNormalOrBig( final World worldIn , final BlockPos pos , final Random rand )
	{
		if ( this.genBig != null
				&& ( this.genBigChance > 0 && rand.nextFloat( ) < this.genBigChance || this.genNormal == null ) ) {
			if ( this.genBig.generate( worldIn , rand , pos ) ) {
				return true;
			}
		}

		if ( this.genNormal == null ) {
			return false;
		}
		return this.genNormal.generate( worldIn , rand , pos );
	}


	public boolean generateMega( final World worldIn , final BlockPos pos , final Random rand )
	{
		if ( this.genMega == null ) {
			return false;
		}
		return this.genMega.generate( worldIn , rand , pos );
	}


	@Override
	public void registerRecipes( )
	{
		// Log -> planks
		GameRegistry.addShapelessRecipe( new ItemStack( this.PLANKS , 4 ) , new ItemStack( this.LOG ) );
		// Log -> charcoal
		GameRegistry.addSmelting( this.LOG , new ItemStack( Items.COAL , 1 , 1 ) , 0.15f );

	}

}
