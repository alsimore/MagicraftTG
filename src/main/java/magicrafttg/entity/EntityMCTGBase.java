package magicrafttg.entity;

import java.util.UUID;

import com.google.common.base.Predicate;

import magicrafttg.MagicraftTG;
import magicrafttg.entity.ai.EntityMCTGAIAttackEnemy;
import magicrafttg.entity.ai.EntityMCTGAIFollowController;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;


public class EntityMCTGBase extends EntityCreature implements IMCTGEntity{

	private int power;
	private int toughness;
	private UUID owner;
	private UUID controller;
	
	
	// AI for avoiding Creeper, from EntityMob
	protected final EntityAIBase avoidCreeper = new EntityAIAvoidEntity(this, new Predicate()
    {
        private static final String __OBFID = "CL_00002208";
        public boolean func_179911_a(Entity p_179911_1_)
        {
            return p_179911_1_ instanceof EntityCreeper && ((EntityCreeper)p_179911_1_).getCreeperState() > 0;
        }
        public boolean apply(Object p_apply_1_)
        {
            return this.func_179911_a((Entity)p_apply_1_);
        }
    }, 4.0F, 1.0D, 2.0D);
	
	public EntityMCTGBase (World world, EntityPlayer owner, int power, int toughness) 
	{
		super(world);
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
		this.experienceValue = 0;
		
		//this.applyEntityAttributes();
		
		((PathNavigateGround)this.getNavigator()).func_179688_b(true); // From EntityZombie, hopefully helps
		// Add custom AI tasks
		this.tasks.addTask(0, new EntityAISwimming(this));
		// It seems if there's no AttackOnCollide then it won't go after the Entity at all
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityLivingBase.class, 1.0D, true));
		this.tasks.addTask(2, this.avoidCreeper); // Avoid creeper
		// Add EntityMCTGAIFollowController task
		this.tasks.addTask(5, new EntityMCTGAIFollowController(this, 2.0, 2.0F, 30.0F));
		// Add EntityMCTGAIAttackEnemy task
		this.targetTasks.addTask(2, new EntityMCTGAIAttackEnemy(this, EntityLivingBase.class, 1, false, true, null));
		// Watch closest and look tasks 
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
	}
	
	// All subclasses should include this as well
	public EntityMCTGBase(World worldIn)
    {
        super(worldIn);
    }
	
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MagicraftTG.HEALTH_PER_TOUGHNESS * this.toughness);
		this.setHealth(MagicraftTG.HEALTH_PER_TOUGHNESS * this.toughness);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(MagicraftTG.DAMAGE_PER_POWER * this.power);
		
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
	}
	
	
	@Override
	public void onDeath(DamageSource cause)
    {
		super.onDeath(cause);
		
		// Remove the creature from the cotroller's controlledCreatures list.
		Entity controllingPlayer = MinecraftServer.getServer().getEntityFromUuid(this.controller);
		if(controllingPlayer instanceof EntityPlayer)
		{
			MagicraftTGPlayer mctg = MagicraftTGPlayer.get((EntityPlayer)controllingPlayer);
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
	
	// maxHealth was being difficult, couldn't register it as an attribute because it was already registered
	// but if I tried to set it without registering it, it didn't exist???
	@Override
	public void setHealth(float health)
    {
        this.dataWatcher.updateObject(6, Float.valueOf(health));
    }
	
	// From EntityMob
	public boolean attackEntityAsMob(Entity theTarget)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int i = 0;

        if (theTarget instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)theTarget).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = theTarget.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0)
            {
                theTarget.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                theTarget.setFire(j * 4);
            }

            this.func_174815_a(this, theTarget);
        }

        return flag;
    }
	
	

	//------------------------------------------------------------------------------
	// From EntityCreature
    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere()
    {
        return false; // Don't want the game spawning this
    }


    /**
     * Applies logic related to leashes, for example dragging the entity or breaking the leash.
     */
    @Override
    protected void updateLeashedState()
    {
    	// Cannot be leashed
    }


	///////////////////////////////////////////////////////////////
	// IMCTGEntity methods
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
