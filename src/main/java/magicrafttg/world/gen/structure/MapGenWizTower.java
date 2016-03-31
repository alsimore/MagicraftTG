package magicrafttg.world.gen.structure;

import java.util.Arrays;

import org.apache.logging.log4j.Level;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import magicrafttg.MagicraftTG;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.fml.common.FMLLog;

public class MapGenWizTower extends MapGenStructure 
{

	public static final float MIN_HEIGHT_FOR_TOWER = 90.0f;
	
	public MapGenWizTower(World world)
	{
		this.worldObj = world;
	}
	
	/**
	 * Sets the random seed and runs func_180701_a for 8 chunks in all directions of the specified chunk.
	 * <p>
	 * func_180701_a:<br>
	 * Calls func_143027_a, which loads the structure data for the specific type of structure (e.g. "Village")
	 * and initialises the structureMap of already generated structures.
	 * <p>
	 * If there is no structure in the map at the chunk then it attempts to spawn one, calling canSpawnStructureAtCoords.
	 * If that returns true then it calls getStructureStart, puts it in the structureMap, and calls func_143026_a
	 * with the structurestart (which sets an NBTTagCompound in the MapGenStructureData).
	 */
	public void func_175792_a(IChunkProvider provider, World worldIn, int x, int z, ChunkPrimer chunkprimer)
    {
		super.func_175792_a(provider, worldIn, x, z, chunkprimer);
    }
	
	
	public boolean canSpawnAt(int x, int z)
	{
		return this.canSpawnStructureAtCoords(x, z);
	}
	
	public void structureStart(int x, int z)
	{
		this.getStructureStart(x, z);
	}
	
	
	@Override
	public String getStructureName() {
		return "Wizard Tower";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int x, int z) 
	{
		String biomename = worldObj.getBiomeGenForCoords(new BlockPos(x*16, 64, z*16)).biomeName;
		//if (biomename.toLowerCase().contains("forest") || biomename.toLowerCase().contains("jungle"))
		if (biomename.toLowerCase().contains("roofed"))
			return false;
		
		// Make sure the chunk is high enough
		float avgHeight = chunkAverageHeight(x, z);
		float minHeight = chunkMinHeight(x, z);
		float maxHeight = chunkMaxHeight(x, z);
		if (avgHeight > MIN_HEIGHT_FOR_TOWER)// && maxHeight - minHeight < 4)
		{
			boolean isPeak = false;
			// Make sure that this chunk is a peak (higher than surrounding chunks)
			FMLLog.log(MagicraftTG.MODID,  Level.INFO, "Potential peak at %d %d (%f)", x, z, avgHeight);
			isPeak = chunkIsPeak(x, z);
			FMLLog.log(MagicraftTG.MODID, Level.INFO, "Chunk %d %d isPeak = " + isPeak, x, z);
			return isPeak;
		}
		
		return false;
	}

	@Override
	protected StructureStart getStructureStart(int x, int z) {
		// TODO Do StructureStart properly for future use as auto-generated village type
		
		return null;
	}
	
	private float chunkAverageHeight(int x, int z)
	{
		Chunk chunk = this.worldObj.getChunkFromChunkCoords(x, z);
		
		int[] heightMap = chunk.getHeightMap();
		float avgHeight = 0;
		for(int i = 0; i < heightMap.length; ++i)
		{
			avgHeight += heightMap[i];
		}
		avgHeight /= heightMap.length;
		
		return avgHeight;
	}
	
	private int chunkMinHeight(int x, int z)
	{
		int[] heightMap = this.worldObj.getChunkFromChunkCoords(x, z).getHeightMap();
		Arrays.sort(heightMap);
		return heightMap[0];
	}
	
	private int chunkMaxHeight(int x, int z)
	{
		int[] heightMap = this.worldObj.getChunkFromChunkCoords(x, z).getHeightMap();
		Arrays.sort(heightMap);
		return heightMap[heightMap.length - 1];
	}
	
	private boolean chunkIsPeak(int x, int z)
	{
		float thisAvg = chunkAverageHeight(x, z);
		// Is this chunk's average height higher than all surrounding chunks?
		for (int i = x - 1; i <= x + 1; ++i)
		{
			for (int j = z - 1; j <= z + 1; ++j)
			{
				if (i == x && j == z)
				{
					FMLLog.log(MagicraftTG.MODID,  Level.INFO,  "skip %d %d", x, z);
					continue;
				}
				float avg = chunkAverageHeight(i, j);
				FMLLog.log(MagicraftTG.MODID,  Level.INFO,  "%d %d: %f", i, j, avg);
				if (avg >= thisAvg)
				{	
					FMLLog.log(MagicraftTG.MODID,  Level.INFO,  "%d %d: false", x, z);
					return false;
				}
			}
		}
		FMLLog.log(MagicraftTG.MODID,  Level.INFO,  "%d %d: true", x, z);
		return true;
	}

	public StructureBoundingBox findFlatArea(int chunkX, int chunkZ, int minDim1, int minDim2) 
	{
		int minX = -1, minZ = -1;
		int maxX = -1, maxZ = -1;
		StructureBoundingBox largest = null;
		
		int largestDim1, largestDim2, largestArea;
		
		Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkX, chunkZ);
		System.out.println("findFlatArea in chunk " + chunkX + " " + chunkZ + ": " + chunkX*16 + " " + chunkZ*16);
		printHeightMap(chunk);
		
		for (int h = chunkMaxHeight(chunkX, chunkZ); h > (int)MIN_HEIGHT_FOR_TOWER; --h)
		{
			for (int x = 0; x < 16; ++x)
			{
				for (int z = 0; z < 16; ++z)
				{
					BlockPos bp = new BlockPos(chunkX*16 + x, h, chunkZ*16 + z);
					//if (chunk.getHeight(x, z) >= h)
					if (worldObj.getTopSolidOrLiquidBlock(bp).getY() >= h)
					{
						//Vec3i pos = new Vec3i(chunkX*16 + x, h, chunkZ*16 + z);
						System.out.println("");
						// If this block is already included in a flat area skip it
						// n.b. func_175898_b checks if a vec3i is inside the bounding box 
						// (could be called "containsBlock" or "withinBoundingBox")
						//if (largest != null)
						//{
						//	System.out.println("Check bounds on " + pos);
						//	System.out.println("Against " + largest);
						//	if (largest.func_175898_b(pos))
						//		continue;
						//}
						
						
						System.out.println("Try find box starting " + x + " " + z + " at height " + h);
						StructureBoundingBox box = expandFlatArea(chunk, x, z, h);
						int xDim = box.getXSize();
						int zDim = box.getZSize();
						int area = xDim * zDim;
						int dim1 = xDim > zDim ? xDim : zDim;
						int dim2 = xDim + zDim - dim1;
						System.out.println("Box " + xDim + "x" + zDim + ": " + area + " height " + box.maxY);
						System.out.println("Chunk-relative " + box.minX + "," + box.minZ + " to " + box.maxX + "," + box.maxZ);
						
						box.offset(chunkX*16, 0, chunkZ*16);
						System.out.println("Global " + box.minX + "," + box.minZ + " to " + box.maxX + "," + box.maxZ);
						
						if (largest == null || (sbbArea(box) >= sbbArea(largest) && getSmallDim(box) > getSmallDim(largest)) )
						{
							largest = box;
							System.out.println("Largest now " + largest);
						}
						
						if (dim1 >= minDim1 && dim2 >= minDim2)
							return box;
					}
				}
			}
		}
		return largest;
	}
	
	/**
	 * Returns a bounding box in chunk-relative coords (i.e. all dimensions from 0 to 15).
	 * @param chunk
	 * @param x
	 * @param z
	 * @param height
	 * @return
	 */
	private StructureBoundingBox expandFlatArea(Chunk chunk, int x, int z, int height)
	{
		int minX, minZ, maxX, maxZ;
		minX = maxX = x;
		minZ = maxZ = z;
		// Find largest rectangular area at this or greater height
		// Starting at the high block try to expand in one direction then the other,
		// once both directions fail we have finished.
		boolean finished = false;
		int xSize = 1;
		int zSize = 1;
		boolean expandAlongX = true;
		boolean canExpandX = true;
		boolean canExpandZ = true;
		while (!finished)
		{
			if (expandAlongX)
			{
				for (int zz = minZ; zz <= maxZ; ++zz)
				{
					//if (minX + xSize >= 16 || chunk.getHeight(minX + xSize, zz) < height)
					BlockPos pos = new BlockPos((chunk.xPosition*16) + minX + xSize, 64, (chunk.zPosition*16) + zz);
					if (minX + xSize >= 16 || worldObj.getTopSolidOrLiquidBlock(pos).getY() < height)
					{
						canExpandX = false;
						break;
					}
				}
				if (canExpandX)
				{
					maxX++;
					xSize++;
				}
				expandAlongX = !expandAlongX;
			}
			else
			{
				for (int xx = minX; xx <= maxX; ++xx)
				{
					//if (minZ + zSize >= 16 || chunk.getHeight(xx, minZ + zSize) < height)
					BlockPos pos = new BlockPos((chunk.xPosition*16) + xx, 64, (chunk.zPosition*16) + minZ + zSize);
					if (minZ + zSize >= 16 || worldObj.getTopSolidOrLiquidBlock(pos).getY() < height)
					{
						canExpandZ = false;
						break;
					}
				}
				if (canExpandZ)
				{
					maxZ++;
					zSize++;
				}
				expandAlongX = !expandAlongX;
			}
			
			if (!canExpandX && !canExpandZ)
			{
				finished = true;
			}
		}
		
		
		return new StructureBoundingBox(minX, height, minZ, maxX, height, maxZ);
	}
	
	private int sbbArea(StructureBoundingBox box)
	{
		return box.getXSize() * box.getZSize();
	}
	
	private int sbbVolume(StructureBoundingBox box)
	{
		return box.getXSize() * box.getZSize() * box.getYSize();
	}
	
	private void printHeightMap(Chunk chunk)
	{
		int xstart = chunk.xPosition * 16;
		int zstart = chunk.zPosition * 16;
		int xend = chunk.xPosition * 16 + 15;
		int zend = chunk.zPosition * 16 + 15;
		System.out.println(String.format("Chunk %d %d", chunk.xPosition, chunk.zPosition));
		System.out.println(String.format("Chunk 0,0 = %d %d", xstart, zstart));
		System.out.println(String.format("Chunk 15,15 = %d %d", xend, zend));
		for (int x = 0; x < 16; ++x)
		{
			System.out.print("\n");
			for (int z = 0; z < 16; ++z)
			{
				//System.out.print(String.format(" %3d", chunk.getHeight(x, z)));
				BlockPos pos = new BlockPos(xstart + x, 64, zstart + z);
				System.out.print(String.format(" %3d", this.worldObj.getTopSolidOrLiquidBlock(pos).getY() ));
			}
		}
		System.out.print("\n");
		BlockPos zero = this.worldObj.getTopSolidOrLiquidBlock(new BlockPos(xstart, 64, zstart));
		BlockPos fifteen = this.worldObj.getTopSolidOrLiquidBlock(new BlockPos(xend, 64, zend));
		System.out.println(String.format("World %d %d = %d", xstart, zstart, zero.getY()));
		System.out.println(String.format("World %d %d = %d", xend, zend, fifteen.getY()));
	}
	
	/**
	 * Returns size of the largest dimension (X or Z).
	 * @param box
	 * @return
	 */
	private int getlargeDim(StructureBoundingBox box)
	{
		return box.getXSize() >= box.getZSize() ? box.getXSize() : box.getZSize();
	}
	
	/**
	 * Returns size of the smallest dimension (X or Z).
	 * @param box
	 * @return
	 */
	private int getSmallDim(StructureBoundingBox box)
	{
		return box.getXSize() < box.getZSize() ? box.getXSize() : box.getZSize();
	}
	
	private int getGroundHeight(Chunk chunk, int x, int z)
	{
		int y = 255;
		for (y = 255; y > 0; --y)
		{
			int id = Block.getIdFromBlock(chunk.getBlock(x, y, z));
			if (id >= 1 && id <= 4 || id == 12 || id == 13)
				return y;
		}
		
		return y;
	}
	
	
	private int startX(int chunkX)
	{
		if (chunkX < 0)
			return 15;
		else
			return 0;
	}
	
	private int nextX(int x, int chunkX)
	{
		if (chunkX < 0)
			return x - 1;
		else
			return x + 1;
	}
	
	private int startZ(int chunkZ)
	{
		if (chunkZ < 0)
			return 15;
		else
			return 0;
	}
	
	private int nextZ(int z, int chunkZ)
	{
		if (chunkZ < 0)
			return z - 1;
		else
			return z + 1;
	}
}
