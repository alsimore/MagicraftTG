package magicrafttg.entity;

import com.google.common.base.Predicate;

import magicrafttg.entity.ai.EntityMCTGAIAttackEnemy;
import magicrafttg.entity.ai.EntityMCTGAIFollowController;
import magicrafttg.entity.ai.EntityMCTGAIWizardAttack;
import magicrafttg.entity.projectile.EntityEnergyBolt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityWizard extends EntityCreature implements IRangedAttackMob
{
	private EntityMCTGAIWizardAttack aiArrowAttack = new EntityMCTGAIWizardAttack(this, 1.0D, 20, 60, 15.0F);
	
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
		
		
	public EntityWizard(World worldIn)
	{
		super(worldIn);
		
		//((PathNavigateGround)this.getNavigator()).func_179688_b(true); // From EntityZombie, hopefully helps
		// Add custom AI tasks
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, this.avoidCreeper); // Avoid creeper
		// It seems if there's no AttackOnCollide then it won't go after the Entity at all
		//this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityLivingBase.class, 1.0D, true));
		this.tasks.addTask(4, this.aiArrowAttack);
		// Add EntityMCTGAIAttackEnemy task
		//this.targetTasks.addTask(2, new EntityMCTGAIAttackEnemy(this, EntityLivingBase.class, 1, false, true, null));
		// Watch closest and look tasks 
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityMCTGAIAttackEnemy(this, EntityLivingBase.class, 1, false, true, null));
	}
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
        
    }


	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) 
	{
		double d1 = 1.0D;
        Vec3 look = this.getLook(1.0F);
        
        EntityEnergyBolt fireball = new EntityEnergyBolt(this.worldObj, 
				this);
        //fireball.explosionPower = 2;
        /*fireball.posX = this.posX + look.xCoord * d1;
        fireball.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
        fireball.posZ = this.posZ + look.zCoord * d1;
        fireball.accelerationX = look.xCoord;
        fireball.accelerationY = look.yCoord;
        fireball.accelerationZ = look.zCoord;*/
        
        this.worldObj.spawnEntityInWorld(fireball);
	}
}
