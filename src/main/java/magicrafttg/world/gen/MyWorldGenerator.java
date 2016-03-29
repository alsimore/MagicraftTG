package magicrafttg.world.gen;

import java.util.Arrays;
import java.util.Random;

import org.apache.logging.log4j.Level;

import magicrafttg.MagicraftTG;
import magicrafttg.entity.EntityWizard;
import magicrafttg.world.gen.structure.MapGenWizTower;
import magicrafttg.world.gen.structure.WizardTower;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.common.ForgeChunkManager;

public class MyWorldGenerator implements IWorldGenerator {

	int count = 0;
	MapGenWizTower towerGen;
	
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) 
	{
		if (!world.isRemote)
		{
			switch(world.provider.getDimensionId())
			{
			case -1: 
				break;
			case 0: generateSurface(world, random, chunkX, chunkZ);
			case 1: 
				break;
			}
		}
	}

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		if (towerGen == null)
			towerGen = new MapGenWizTower(world);
		int x = chunkX * 16 + random.nextInt(16); // get chords of chunck
		int z = chunkZ * 16 + random.nextInt(16); // get chords of chunck
		int y = getTopBlock(world, new BlockPos(x, 0, z));
		
		BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(x, 0, z));

		//FMLLog.log(MagicraftTG.MODID, Level.INFO, "Checking chunk %d %d, starting %d %d", chunkX, chunkZ, chunkX*16, chunkZ*16);
		
		if (towerGen.canSpawnAt(chunkX, chunkZ))// && maxHeight - minHeight < 4)
		{
			System.out.println("Try spawn in " + chunkX + " " + chunkZ);
			Entity newEntity = new EntityWizard(world, x, y, z, 0, 0);
			//newEntity.setPosition(x, y, z);
			boolean res = world.spawnEntityInWorld(newEntity);
			System.out.println(res + " - Exact " + newEntity.posX + " " + newEntity.posY + " " + newEntity.posZ);
			
			
			System.out.println(biome.biomeName);
			
			this.count++;
			/*int avgHeight = chunkAverageHeight(world, chunkX, chunkZ);
			WizardTower tower = new WizardTower();
			
			StructureBoundingBox bb = new StructureBoundingBox(chunkX*16, avgHeight, chunkZ*16,
					chunkX*16+15, avgHeight+15, chunkZ*16+15);
			System.out.println(bb);
			tower.addComponentParts(world, random, bb);
			*/
			
			//world.spawnEntityInWorld(newEntity);
		}
			
	}
	
	
	protected int getTopBlock(World world, BlockPos pos) {
		
		/*while(world.getBlockState(pos).getBlock().isBlockSolid(world, pos, EnumFacing.DOWN))
		{
			pos.add(0, 1, 0);
		}
		
		return pos.getY();*/
		int y = -1, j;
		
		// Ensure block starts from the top of the world
		pos = pos.add(0, world.getActualHeight(), 0);

		for (j = world.getActualHeight(); j >= 10; j--) {
			if (world.getBlockState(pos).getBlock().isBlockSolid(world, pos, EnumFacing.DOWN)) {
				return j + 1;
			}
			
			pos = pos.add(0, -1, 0);
		}
		return y;
	}
	
	
	
	private int chunkAverageHeight(World world, int x, int z)
	{
		int[] heightMap = world.getChunkFromChunkCoords(x, z).getHeightMap();
		int avgHeight = 0;
		for(int i = 0; i < heightMap.length; ++i)
		{
			avgHeight += heightMap[i];
		}
		avgHeight /= heightMap.length;
		
		return avgHeight;
	}
	
	private int chunkMinHeight(World world, int x, int z)
	{
		int[] heightMap = world.getChunkFromChunkCoords(x, z).getHeightMap();
		Arrays.sort(heightMap);
		return heightMap[0];
	}
	
	private int chunkMaxHeight(World world, int x, int z)
	{
		int[] heightMap = world.getChunkFromChunkCoords(x, z).getHeightMap();
		Arrays.sort(heightMap);
		return heightMap[heightMap.length - 1];
	}
}
