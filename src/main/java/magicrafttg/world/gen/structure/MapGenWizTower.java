package magicrafttg.world.gen.structure;

import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenWizTower extends MapGenStructure 
{

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
