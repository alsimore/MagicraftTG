package magicrafttg.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityMCTGSkeleton extends EntityMCTGBase {

	public EntityMCTGSkeleton(World world, EntityPlayer owner, int power, int toughness) {
		super(world, owner, power, toughness);

		this.setCustomNameTag("Skeleton");
		
		this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
	}

	// Required or the instantiation will fail and crash the client
	public EntityMCTGSkeleton(World worldIn)
	{
		super(worldIn);
	}
}
