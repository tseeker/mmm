package mmm.world.trees;


import java.util.Random;

import mmm.materials.trees.MSapling;
import mmm.materials.trees.MTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;



public abstract class A_WTTreeGenerator
		extends WorldGenAbstractTree
{
	public static enum E_BlockRequirement {
		/** Require / replace empty (air) block */
		EMPTY ,
		/** Require / replace air, leaves, vines, etc. */
		SOFT ,
		/** Require / replace vanilla */
		VANILLA ,
		/** Anything but bedrock can do, unless it has a TE */
		HARD
	}

	public static class RuntimeData
	{
		public final BlockPos corner;
		public final int xSize;
		public final int zSize;
		public final int height;
		protected E_BlockRequirement require[];
		protected E_BlockRequirement replace[];
		protected IBlockState blocks[];


		public RuntimeData( final BlockPos centre , final int radius , final int height )
		{
			this( centre.add( -radius , 0 , -radius ) , radius * 2 + 1 , radius * 2 + 1 , height );
		}


		public RuntimeData( final BlockPos corner , final int xSize , final int zSize , final int height )
		{
			this.corner = corner;
			this.xSize = xSize;
			this.zSize = zSize;
			this.height = height;
		}


		private void allocateData( )
		{
			this.require = new E_BlockRequirement[ this.xSize * this.zSize * this.height ];
			this.replace = new E_BlockRequirement[ this.xSize * this.zSize * this.height ];
			this.blocks = new IBlockState[ this.xSize * this.zSize * this.height ];
		}


		protected int getOffset( final int i , final int j , final int k )
		{
			return k + this.zSize * ( j + this.height * i );
		}


		public E_BlockRequirement requires( final int i , final int j , final int k )
		{
			return this.require[ this.getOffset( i , j , k ) ];
		}


		public E_BlockRequirement replaces( final int i , final int j , final int k )
		{
			return this.replace[ this.getOffset( i , j , k ) ];
		}


		public IBlockState getBlockState( final int i , final int j , final int k )
		{
			return this.blocks[ this.getOffset( i , j , k ) ];
		}


		public void setBlock( final int i , final int j , final int k , final Block block ,
				final E_BlockRequirement replace )
		{
			this.setBlock( i , j , k , block.getDefaultState( ) , replace );
		}


		public void setBlock( final int i , final int j , final int k , final IBlockState block ,
				final E_BlockRequirement replace )
		{
			final int offset = this.getOffset( i , j , k );
			this.blocks[ offset ] = block;
			this.replace[ offset ] = replace;
		}


		public void clearBlock( final int i , final int j , final int k )
		{
			final int offset = this.getOffset( i , j , k );
			this.blocks[ offset ] = null;
			this.require[ offset ] = null;
			this.replace[ offset ] = null;
		}


		public void setRequirement( final int i , final int j , final int k , final E_BlockRequirement requirement )
		{
			this.require[ this.getOffset( i , j , k ) ] = requirement;
		}


		public void removeCornerLeaves( final Random rand , final IBlockState leaves , final int startY ,
				final int endY , final float chance )
		{
			for ( int y = startY ; y <= endY ; y++ ) {
				for ( int x = 0 ; x < this.xSize ; x++ ) {
					for ( int z = 0 ; z < this.zSize ; z++ ) {
						final IBlockState bs = this.getBlockState( x , y , z );
						if ( bs != leaves ) {
							continue;
						}

						final boolean xp = this.isEmpty( x + 1 , y , z );
						final boolean xn = this.isEmpty( x - 1 , y , z );
						final boolean yp = this.isEmpty( x , y + 1 , z );
						final boolean yn = this.isEmpty( x , y - 1 , z );
						final boolean zp = this.isEmpty( x , y , z + 1 );
						final boolean zn = this.isEmpty( x , y , z - 1 );
						if ( xp ^ xn && yp ^ yn && zp ^ zn && rand.nextFloat( ) < chance ) {
							this.clearBlock( x , y , z );
						}
					}
				}
			}
		}


		public boolean isEmpty( final int x , final int y , final int z )
		{
			if ( x < 0 || x >= this.xSize || y < 0 || y >= this.height || z < 0 || z >= this.zSize ) {
				return true;
			}
			final IBlockState bs = this.getBlockState( x , y , z );
			return bs == null || bs.getMaterial( ) == Material.AIR;
		}


		public void fixer( final IBlockState log , final int trunkX , final int trunkZ )
		{
			final boolean fixedArea[] = new boolean[ this.blocks.length ];
			for ( int i = 0 ; i < this.xSize ; i++ ) {
				for ( int j = 0 ; j < this.height ; j++ ) {
					for ( int k = 0 ; k < this.zSize ; k++ ) {
						if ( !fixedArea[ this.getOffset( i , j , k ) ] ) {
							this.fixLeaves( log , trunkX , trunkZ , i , j , k , fixedArea );
						}
					}
				}
			}
		}


		private void fixLeaves( final IBlockState log , final int trunkX , final int trunkZ , final int x ,
				final int y , final int z , final boolean[] fixedArea )
		{
			final IBlockState bs = this.getBlockState( x , y , z );
			if ( bs == null || ! ( bs.getBlock( ) instanceof BlockLeaves )
					|| this.checkSurroundingBlocks( log , x , y , z ) ) {
				return;
			}

			final int xd = trunkX < x ? 1 : -1;
			final int zd = trunkZ < z ? 1 : -1;

			for ( int i = -4 * xd ; i * xd <= 4 ; i += xd ) {
				final int xi = x + i;
				if ( xi < 0 || xi >= this.xSize ) {
					continue;
				}

				final int sx = i * i;
				for ( int j = -4 ; j <= 4 ; j++ ) {
					final int yj = y + j;
					if ( yj < 0 || yj >= this.height ) {
						continue;
					}

					final int sz = sx + j * j;
					for ( int k = -4 * zd ; k * zd <= 4 ; k += zd ) {
						if ( i == 0 && j == 0 && k == 0 || sz + k * k >= 16 ) {
							continue;
						}

						final int zk = z + k;
						if ( zk < 0 || zk >= this.zSize || fixedArea[ this.getOffset( xi , yj , zk ) ] ) {
							continue;
						}

						final IBlockState state = this.getBlockState( xi , yj , zk );
						if ( state == null || ! ( state.getBlock( ) instanceof BlockLeaves ) ) {
							continue;
						}

						if ( this.isEmpty( xi - 1 , yj , zk ) //
								|| this.isEmpty( xi + 1 , yj , zk ) //
								|| this.isEmpty( xi , yj - 1 , zk ) //
								|| this.isEmpty( xi , yj + 1 , zk ) //
								|| this.isEmpty( xi , yj , zk - 1 ) //
								|| this.isEmpty( xi , yj , zk + 1 ) ) {
							continue;
						}

						this.addFixAt( log , xi , yj , zk , fixedArea );
						return;
					}
				}
			}
		}


		private void addFixAt( final IBlockState log , final int x , final int y , final int z ,
				final boolean[] fixedArea )
		{
			this.setBlock( x , y , z , log , E_BlockRequirement.VANILLA );
			this.setRequirement( x , y , z , E_BlockRequirement.VANILLA );
			for ( int i = -2 ; i <= 2 ; i++ ) {
				final int xi = x + i;
				if ( xi < 0 || xi >= this.xSize ) {
					continue;
				}
				for ( int j = -2 ; j <= 2 ; j++ ) {
					final int yj = y + j;
					if ( yj < 0 || yj >= this.height ) {
						continue;
					}
					for ( int k = -2 ; k <= 2 ; k++ ) {
						final int zk = z + k;
						if ( zk < 0 || zk >= this.zSize ) {
							continue;
						}
						fixedArea[ this.getOffset( xi , yj , zk ) ] = true;
					}
				}
			}
		}


		private boolean checkSurroundingBlocks( final IBlockState log , final int x , final int y , final int z )
		{
			for ( int i = -4 ; i <= 4 ; i++ ) {
				final int xi = x + i;
				if ( xi < 0 || xi >= this.xSize ) {
					continue;
				}

				final int sx = i * i;
				for ( int j = -4 ; j <= 4 ; j++ ) {
					final int yj = y + j;
					if ( yj < 0 || yj >= this.height ) {
						continue;
					}

					final int sz = sx + j * j;
					for ( int k = -4 ; k <= 4 ; k++ ) {
						if ( i == 0 && j == 0 && k == 0 || sz + k * k >= 16 ) {
							continue;
						}

						final int zk = z + k;
						if ( zk < 0 || zk >= this.zSize ) {
							continue;
						}

						final IBlockState bs = this.getBlockState( xi , yj , zk );
						if ( bs != null && bs.getBlock( ) == log.getBlock( ) ) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	private MTree materials;


	public A_WTTreeGenerator( final boolean notify , final MTree materials )
	{
		super( notify );
		this.materials = materials;
	}


	public MTree getTreeMaterials( )
	{
		return this.materials;
	}


	public A_WTTreeGenerator setTreeMaterials( final MTree materials )
	{
		this.materials = materials;
		return this;
	}


	@Override
	public boolean generate( final World worldIn , final Random rand , final BlockPos position )
	{
		if ( position.getY( ) < 1 || !this.materials.canSaplingStay( worldIn , position ) ) {
			return false;
		}

		final RuntimeData rtd = this.determineTreeSize( position , rand );
		if ( rtd == null || position.getY( ) + 1 + rtd.height > 256 ) {
			return false;
		}
		rtd.allocateData( );
		this.generateTreeBlocks( rtd , rand );

		final BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos( );
		for ( int i = 0 ; i < rtd.xSize ; i++ ) {
			for ( int j = 0 ; j < rtd.height ; j++ ) {
				for ( int k = 0 ; k < rtd.zSize ; k++ ) {
					final E_BlockRequirement req = rtd.requires( i , j , k );
					if ( req == null ) {
						continue;
					}

					mbp.setPos( rtd.corner.getX( ) + i , rtd.corner.getY( ) + j , rtd.corner.getZ( ) + k );
					if ( !this.checkRequirement( worldIn , mbp , req ) ) {
						return false;
					}
				}
			}
		}

		for ( int i = 0 ; i < rtd.xSize ; i++ ) {
			for ( int j = 0 ; j < rtd.height ; j++ ) {
				for ( int k = 0 ; k < rtd.zSize ; k++ ) {
					final IBlockState state = rtd.getBlockState( i , j , k );
					if ( state != null ) {
						final BlockPos blockPos = rtd.corner.add( i , j , k );
						final E_BlockRequirement rep = rtd.replaces( i , j , k );
						if ( rep == null ) {
							continue;
						}
						if ( this.checkRequirement( worldIn , blockPos , rep ) ) {
							this.setBlockAndNotifyAdequately( worldIn , blockPos , state );
						}
					}
				}
			}
		}

		return true;
	}


	private boolean checkRequirement( final World worldIn , final BlockPos pos , final E_BlockRequirement requirement )
	{
		switch ( requirement ) {
			case EMPTY:
				return worldIn.getBlockState( pos ).getMaterial( ) == Material.AIR;

			case SOFT: {
				final IBlockState state = worldIn.getBlockState( pos );
				final Block block = worldIn.getBlockState( pos ).getBlock( );
				// FIXME: don't hardcode this
				return state.getMaterial( ) == Material.AIR || block instanceof BlockLeaves
						|| block instanceof BlockFlower || block instanceof BlockBush || block instanceof BlockVine
						|| block instanceof BlockSapling || block instanceof MSapling;
			}

			case VANILLA:
				return this.isReplaceable( worldIn , pos );

			case HARD: {
				final IBlockState state = worldIn.getBlockState( pos );
				final Block block = worldIn.getBlockState( pos ).getBlock( );
				return block != Blocks.BEDROCK && block != Blocks.BARRIER && !block.hasTileEntity( state );
			}

		}
		return false;
	}


	protected abstract RuntimeData determineTreeSize( BlockPos position , Random rand );


	protected abstract void generateTreeBlocks( RuntimeData rtd , Random rand );

}
