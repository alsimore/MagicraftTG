package magicrafttg.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityMCTGZombie extends EntityMCTGBase {

	public EntityMCTGZombie(World world, EntityPlayer owner, int power, int toughness) {
		super(world, owner, power, toughness);

		this.setCustomNameTag("Zombie");
	}

	// Required or the instantiation will fail and crash the client
	public EntityMCTGZombie(World worldIn)
	{
		super(worldIn);
	}
}
