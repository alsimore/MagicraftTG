package magicrafttg.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityMCTGSkeletonArmoured extends EntityMCTGSkeleton {

	public EntityMCTGSkeletonArmoured(World world, EntityPlayer owner, int power, int toughness) {
		super(world, owner, power, toughness);
	}

	public EntityMCTGSkeletonArmoured(World worldIn) {
		super(worldIn);
	}
	
	
    public void setCombatTaskMCTG()
    {
    	this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
    	this.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
    	this.setCurrentItemOrArmor(3, new ItemStack(Items.leather_chestplate));
    	this.setCurrentItemOrArmor(2, new ItemStack(Items.leather_leggings));
    	this.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
    	
        ItemStack itemstack = this.getHeldItem();

        if (itemstack != null && itemstack.getItem() == Items.bow)
        {
            //this.tasks.addTask(4, this.aiArrowAttack2);
        }
        else
        {
           // this.tasks.addTask(4, this.aiAttackOnCollide2);
        }
    }
}
