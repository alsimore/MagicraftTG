package magicrafttg.world.gen.structure;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenWizTower extends MapGenStructure 
{

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
	
	
	
	
	
	@Override
	public String getStructureName() {
		return "Wizard Tower";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected StructureStart getStructureStart(int x, int z) {
		// TODO Auto-generated method stub
		return null;
	}

}
