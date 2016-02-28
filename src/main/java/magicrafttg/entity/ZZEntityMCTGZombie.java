package magicrafttg.entity;

import java.util.UUID;

import magicrafttg.entity.ai.EntityMCTGAIAttackEnemy;
import magicrafttg.entity.ai.EntityMCTGAIFollowController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ZZEntityMCTGZombie extends EntityZombie implements IMCTGEntity {

	// TODO: Decide between setting isImmuneToFire = true, or in onLivingUpdate() 
	// calling this.extinguish(). Still want the zombie to be flammable, just not from sunlight.
	private UUID owner;
	private UUID controller;
	private int power;
	private int toughness;
	
	public ZZEntityMCTGZombie(World worldIn, EntityPlayer owner, int power, int toughness) {
		super(worldIn);
		if(owner != null)
		{
			this.owner = owner.getUniqueID();
			this.controller = this.owner;
		}
		else
		{
			this.owner = this.controller = null;
		}
		this.power = power;
		this.toughness = toughness;
		
		this.setCustomNameTag("Zombie");
		
		// Remove the EntityAINearestAttackableTarget for villagers
		EntityAITaskEntry task1, task2, task3, task4;
		task1 = (EntityAITaskEntry) this.targetTasks.taskEntries.get(2);
		this.targetTasks.removeTask(task1.action);
		// Remove the EntityAIMoveTowardsRestriction and EntityAIWander tasks
		task1 = (EntityAITaskEntry) this.tasks.taskEntries.get(3);
		task2 = (EntityAITaskEntry) this.tasks.taskEntries.get(4);
		task3 = (EntityAITaskEntry) this.tasks.taskEntries.get(9);
		task4 = (EntityAITaskEntry) this.tasks.taskEntries.get(7);
		this.tasks.removeTask(task1.action);
		this.tasks.removeTask(task2.action);
		// Remove the EntityAIMoveThroughVillage task
		this.tasks.removeTask(task3.action);
		// Attack villager on collide
		//this.tasks.removeTask(task4.action);
		
		// It seems if there's no AttackOnCollide then it won't go after the Entity at all
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityLivingBase.class, 1.0D, true));
		// Add EntityMCTGAIFollowController task
		this.tasks.addTask(5, new EntityMCTGAIFollowController(this, 2.0, 2.0F, 30.0F));
		// Add EntityMCTGAIAttackEnemy task
		this.targetTasks.addTask(2, new EntityMCTGAIAttackEnemy(this, EntityLivingBase.class, 1, false, true, null));
	}

	public ZZEntityMCTGZombie(World worldIn) {
		this(worldIn, null, 1, 1);
	}
	
	
	/**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     * 
     * For MCTGZombie, it doesn't burn in sunlight.
     */
	@Override
    public void onLivingUpdate()
    {
        /*if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild())
        {
            float f = this.getBrightness(1.0F);
            BlockPos blockpos = new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canSeeSky(blockpos))
            {
                boolean flag = true;
                ItemStack itemstack = this.getEquipmentInSlot(4);

                if (itemstack != null)
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
                        {
                            this.renderBrokenItemStack(itemstack);
                            this.setCurrentItemOrArmor(4, (ItemStack)null);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    this.setFire(8);
                }
            }
        }

        if (this.isRiding() && this.getAttackTarget() != null && this.ridingEntity instanceof EntityChicken)
        {
            ((EntityLiving)this.ridingEntity).getNavigator().setPath(this.getNavigator().getPath(), 1.5D);
        }*/

        super.onLivingUpdate();
        //System.out.println(this.isBurning());
        this.extinguish();
    }
	
	
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
		boolean ret = super.attackEntityFrom(source, amount);
		/*System.out.println("source " + source);
		Entity src = source.getSourceOfDamage();
		System.out.println("src " + src);
		System.out.println("Suffered " + amount + " from " + src.toString());*/
		return ret;
    }
	
	
	@Override
	public void onDeath(DamageSource cause)
    {
		super.onDeath(cause);
		
		// Remove the creature from the cotroller's controlledCreatures list.
		Entity controllingPlayer = MinecraftServer.getServer().getEntityFromUuid(this.controller);
		if(controllingPlayer instanceof EntityPlayer)
		{
			MCTGPlayerProperties mctg = MCTGPlayerProperties.get((EntityPlayer)controllingPlayer);
			System.out.println("[MCTG] Creature died: " + this.toString());
			System.out.println("[MCTG] Suffered " + cause.toString());
			mctg.removeControlledCreature(this);
		}
		else if (controllingPlayer != null)
		{
			String str = this.toString();
			String plyr = controllingPlayer.toString();
			System.out.println("[MCTG] ERROR: Entity " + str + " not controlled by a player ("
					+ plyr + ")");
		}
    }
	
	
	@Override
    public boolean getCanSpawnHere() {
    	return false;
    }
	
	

	@Override
	public EntityLivingBase getOwnerEntity() {
		EntityLivingBase owner = (EntityLivingBase) MinecraftServer.getServer().getEntityFromUuid(this.owner);
		return owner;
	}

	@Override
	public void setOwnerEntity(EntityLivingBase owner) {
		this.owner = owner.getUniqueID();
	}

	@Override
	public EntityLivingBase getControllerEntity() {
		EntityLivingBase controller = (EntityLivingBase) MinecraftServer.getServer().getEntityFromUuid(this.controller);
		return controller;
	}

	@Override
	public void setControllerEntity(EntityLivingBase controller) {
		this.controller = controller.getUniqueID();
	}

	@Override
	public UUID getOwnerUUID() {
		return this.owner;
	}

	@Override
	public void setOwnerUUID(UUID owner) {
		this.owner = owner;
	}

	@Override
	public UUID getControllerUUID() {
		return this.controller;
	}

	@Override
	public void setControllerUUID(UUID controller) {
		this.controller = controller;
	}
	
	
}
