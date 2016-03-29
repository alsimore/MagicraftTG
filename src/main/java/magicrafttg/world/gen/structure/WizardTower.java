package magicrafttg.world.gen.structure;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class WizardTower extends StructureVillagePieces.Village
{
	private static final int HEIGHT = 10;

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox box) 
	{
		int avgGroundLevel = -1;
		this.boundingBox = box;
		// TODO Auto-generated method stub
		if (avgGroundLevel < 0){
			avgGroundLevel = getAverageGroundLevel(world, box);
            if (avgGroundLevel < 0){
                return true;
            }
            boundingBox.offset(0, avgGroundLevel - this.boundingBox.maxY + HEIGHT - 1, 0);
        }
		
		System.out.println("Clearing foundation");
		// Clear out for building and make foundation
        for (int xx = 0; xx < 14; xx++){
            for (int zz = 0; zz < 8; zz++){
                clearCurrentPositionBlocksUpwards(world, xx,0,zz, box);
                this.func_175811_a(world, Blocks.cobblestone.getDefaultState(), xx, -1, zz, box);
            }
        }
        
		return false;
	}

}
