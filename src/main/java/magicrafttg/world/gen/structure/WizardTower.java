package magicrafttg.world.gen.structure;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class WizardTower extends StructureVillagePieces.Village
{
	private static final int HEIGHT = 10;

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox box) 
	{
		this.coordBaseMode = EnumFacing.NORTH;
		int avgGroundLevel = -1;
		this.boundingBox = box;
		// TODO Auto-generated method stub
		/*if (avgGroundLevel < 0){
			avgGroundLevel = getAverageGroundLevel(world, box);
            if (avgGroundLevel < 0){
            	System.out.println("returning true " + avgGroundLevel);
                return true;
            }
            System.out.println("Calc avgground " + avgGroundLevel);
            boundingBox.offset(0, avgGroundLevel - this.boundingBox.maxY + HEIGHT - 1, 0);
        }*/
		
		System.out.println("Clearing foundation");
		// Clear out for building and make foundation
        for (int xx = 0; xx < 4; xx++){
            for (int zz = 0; zz < 4; zz++){
                clearCurrentPositionBlocksUpwards(world, xx,0,zz, box);
                this.func_175811_a(world, Blocks.stonebrick.getDefaultState(), xx, 0, zz, box);
            }
        }
        
		return false;
	}

	public int getHeight()
	{
		return HEIGHT;
	}
}
