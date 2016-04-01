package magicrafttg.world.gen.structure;

import java.util.Random;

import magicrafttg.entity.EntityWizard;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class WizardTower extends StructureVillagePieces.Village
{
	private static final int HEIGHT = 12;

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox box) 
	{
		this.coordBaseMode = EnumFacing.NORTH;
		int avgGroundLevel = -1;
		this.boundingBox = box;
		
		BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(box.minX, box.minY, box.minZ));
		IBlockState wallBlock = biome.biomeName.toLowerCase().contains("desert") ?
				Blocks.sandstone.getDefaultState() : Blocks.stonebrick.getDefaultState();
		IBlockState dirt = Blocks.dirt.getDefaultState();
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
		
		fillWithAir(world, box, 0,0,0, box.getXSize()-1,box.getYSize()-1,box.getZSize()-1);
        
        int xstart = box.getXSize() / 2 - 3;
        int xend = xstart + 6;
        int zstart = box.getZSize() / 2 - 3;
        int zend = zstart + 6;
        int centrex = xstart + 3;
        int centrez = zstart + 3;
        
        // func_175804_a is fillWithBlocks
        // Create base
        func_175804_a(world, box, xstart,0,zstart, xend,0,zend, dirt, dirt, false);
        func_175804_a(world, box, xstart+1,0,zstart+1, xend-1,0,zend-1, wallBlock, wallBlock, false);
        
        // Create walls
        func_175804_a(world, box, xstart+1,1,zstart+1, xstart+1,HEIGHT-4,zend-1, wallBlock, wallBlock, false);
        func_175804_a(world, box, xstart+1,1,zstart+1, xend-1,HEIGHT-4,zstart+1, wallBlock, wallBlock, false);
        func_175804_a(world, box, xstart+1,1,zend-1, xend-1,HEIGHT-4,zend-1, wallBlock, wallBlock, false);
        func_175804_a(world, box, xend-1,1,zstart+1, xend-1,HEIGHT-4,zend-1, wallBlock, wallBlock, false);
        
        // Create top
        func_175804_a(world, box, xstart,HEIGHT-3,zstart, xend,HEIGHT-3,zend, wallBlock, wallBlock, false);
        fillWithAir(world, box, centrex,HEIGHT-3,centrez, centrex,HEIGHT-3,centrez); // Clear hole in centre
        // Parapet
        func_175804_a(world, box, xstart,HEIGHT-2,zstart, xend,HEIGHT-2,zstart, wallBlock, wallBlock, false);
        func_175804_a(world, box, xstart,HEIGHT-2,zstart, xstart,HEIGHT-2,zend, wallBlock, wallBlock, false);
        func_175804_a(world, box, xstart,HEIGHT-2,zend, xend,HEIGHT-2,zend, wallBlock, wallBlock, false);
        func_175804_a(world, box, xend,HEIGHT-2,zstart, xend,HEIGHT-2,zend, wallBlock, wallBlock, false);
        
        // Torches
        /// parapet corners
        func_175811_a(world, Blocks.torch.getStateFromMeta(5), xstart,HEIGHT-1,zstart, box); // up
        func_175811_a(world, Blocks.torch.getStateFromMeta(5), xstart,HEIGHT-1,zend, box);
        func_175811_a(world, Blocks.torch.getStateFromMeta(5), xend,HEIGHT-1,zstart, box);
        func_175811_a(world, Blocks.torch.getStateFromMeta(5), xend,HEIGHT-1,zend, box);
        /// beside door
        func_175811_a(world, Blocks.torch.getStateFromMeta(1), xend,2,centrez-1, box); // east
        func_175811_a(world, Blocks.torch.getStateFromMeta(1), xend,2,centrez+1, box);
        /// inside
        func_175811_a(world, Blocks.torch.getStateFromMeta(4), centrex,3,centrez-1, box); // north
        func_175811_a(world, Blocks.torch.getStateFromMeta(3), centrex,3,centrez+1, box); // south
        func_175811_a(world, Blocks.torch.getStateFromMeta(4), centrex,7,centrez-1, box);
        func_175811_a(world, Blocks.torch.getStateFromMeta(3), centrex,7,centrez+1, box);
        
        // Ladder
        func_175804_a(world, box, centrex,1,centrez, centrex,HEIGHT-3,centrez, 
        		Blocks.ladder.getStateFromMeta(5), Blocks.ladder.getStateFromMeta(5), false);
        
        // func_175811_a may be placeBlockAtCurrentPosition
        // Empty space for door
        func_175811_a(world, Blocks.air.getDefaultState(), xend-1,1,centrez, box);
        func_175811_a(world, Blocks.air.getDefaultState(), xend-1,2,centrez, box);
        // Wooden door
        func_175811_a(world, Blocks.oak_door.getStateFromMeta(2), xend-1,1,centrez, box); // lower door, facing east
        func_175811_a(world, Blocks.oak_door.getStateFromMeta(8), xend-1,2,centrez, box); // upper door
        
        spawnWizard(world, box.minX + centrex, box.minY + HEIGHT-1, box.minZ + centrez);
        
		return false;
	}
	
	private void spawnWizard(World world, int xPos, int yPos, int zPos)
	{
		EntityWizard wizard = new EntityWizard(world);
		wizard.setLocationAndAngles(xPos, yPos, zPos, 0.0f, 0.0f);
		world.spawnEntityInWorld(wizard);
	}

	public static int getHeight()
	{
		return HEIGHT;
	}
	
	/**
     * arguments: (World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, int placeBlock, int replaceBlock, boolean alwaysreplace)
     */
    /*protected void fillWithBlocks(World world, StructureBoundingBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Block placeBlock, Block replaceBlock, boolean alwaysReplace)
    {
        for (int k1 = minY; k1 <= maxY; ++k1)
        {
            for (int l1 = minX; l1 <= maxX; ++l1)
            {
                for (int i2 = minZ; i2 <= maxZ; ++i2)
                {
                    if (!alwaysReplace || this.getBlockAtCurrentPosition(world, l1, k1, i2, box).getMaterial() != Material.air)
                    {
                        if (k1 != minY && k1 != maxY && l1 != minX && l1 != maxX && i2 != minZ && i2 != maxZ)
                        {
                            this.placeBlockAtCurrentPosition(world, replaceBlock, 0, l1, k1, i2, box);
                        }
                        else
                        {
                            this.placeBlockAtCurrentPosition(world, placeBlock, 0, l1, k1, i2, box);
                        }
                    }
                }
            }
        }
    }*/
}
