package magicrafttg.world.gen.structure;

import java.util.Arrays;

import org.apache.logging.log4j.Level;

import magicrafttg.MagicraftTG;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.fml.common.FMLLog;

public class MapGenWizTower extends MapGenStructure 
{

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
	protected boolean canSpawnStructureAtCoords(int x, int z) {
		// Make sure the chunk is high enough
		float avgHeight = chunkAverageHeight(x, z);
		if (avgHeight > 97.0f)
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
}
