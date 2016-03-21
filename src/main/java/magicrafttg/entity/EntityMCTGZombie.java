package magicrafttg.entity;

import magicrafttg.player.MCTGPlayerProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityMCTGZombie extends EntityMCTGBase {

	public EntityMCTGZombie(World world, EntityPlayer owner, int power, int toughness) 
	{
		super(world, owner, power, toughness);
	}

	// Required or the instantiation will fail and crash the client
	public EntityMCTGZombie(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		
		this.setCustomNameTag("Zombie");
		this.setSize(1.0F, 2.0F);
	}
}
