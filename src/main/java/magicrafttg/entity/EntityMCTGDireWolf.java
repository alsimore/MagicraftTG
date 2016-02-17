package magicrafttg.entity;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


public class EntityMCTGDireWolf extends EntityMCTGBase {
	
	public EntityMCTGDireWolf(World worldIn, EntityPlayer owner, int power, int toughness) {
		super(worldIn, owner, power, toughness);
		
		this.setCustomNameTag("Dire Wolf");
	}
	
	public EntityMCTGDireWolf(World worldIn) {
		super(worldIn);
	}
	
	@Override
	public void applyEntityAttributes()
	{
		super.applyEntityAttributes();
	}
	
}
