package magicrafttg.world.gen;

import java.util.Random;

import magicrafttg.entity.EntityWizard;
import magicrafttg.world.gen.structure.MapGenWizTower;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class MyWorldGenerator implements IWorldGenerator {

	int count = 0;
	MapGenWizTower towerGen = new MapGenWizTower();
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) 
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

	private void generateSurface(World world, Random random, int chunkX, int chunkZ)
	{
		int x = chunkX * 16 + random.nextInt(16); // get chords of chunck
		int z = chunkZ * 16 + random.nextInt(16); // get chords of chunck
		int y = getTopBlock(world, new BlockPos(x, 0, z));
		
		BiomeGenBase biome = world.getBiomeGenForCoords(new BlockPos(x, 0, z));
		/*x=x/16;//get exact chords
		z=z/16;//get exact chords
		int y = 200;
		
		//int y = world.getHeightValue(x, z); // get y chord
		world.setBlockState(new BlockPos(x,y,z), Block.getStateById(152), 152); //set a block at that location
		*/
		//System.out.println("Gnerate: " + chunkX + " " + chunkZ);
		
		if (biome.minHeight > 0.4f && random.nextInt(5) == 0)
		{
			System.out.println("Try spawn in " + chunkX + " " + chunkZ);
			System.out.println("Exact " + chunkX * 16 + " " + chunkZ * 16);
			System.out.println(biome.biomeName);
			this.count++;
			Entity newEntity = new EntityWizard(world);
			newEntity.setPosition(x, y, z);
			world.spawnEntityInWorld(newEntity);
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
	
}
