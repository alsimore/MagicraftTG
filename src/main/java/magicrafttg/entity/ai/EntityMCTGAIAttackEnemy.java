package magicrafttg.entity.ai;

import java.util.UUID;

import com.google.common.base.Predicate;

import magicrafttg.entity.IMCTGEntity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class EntityMCTGAIAttackEnemy extends EntityAINearestAttackableTarget {

	public EntityMCTGAIAttackEnemy(EntityCreature taskOwner, Class targetClass, int targetChance, 
			boolean shouldCheckSight, boolean nearbyOnly, final Predicate predicate) {
		
		super(taskOwner, targetClass, targetChance, shouldCheckSight, nearbyOnly, predicate);
		//super(taskOwner, EntityMCTGZombie.class, targetChance, shouldCheckSight, nearbyOnly, predicate);
		//super(taskOwner, EntityCreeper.class, targetChance, shouldCheckSight, nearbyOnly, predicate);

		// Override the superclass targetEntitySelector
		/*this.targetEntitySelector = new Predicate()
        {
            //private static final String __OBFID = "CL_00001621";
            public boolean func_179878_a(EntityLivingBase potentialTarget)
            {
                if (predicate != null && !predicate.apply(potentialTarget))
                {
                    return false;
                }
                else
                {
                    return EntityMCTGAIAttackEnemy.this.isSuitableTarget(potentialTarget, false);
                }
            }
            public boolean apply(Object p_apply_1_)
            {
                return this.func_179878_a((EntityLivingBase)p_apply_1_);
            }
        };*/
	}
	
//	@Override
//	/**
//     * Returns whether an in-progress EntityAIBase should continue executing
//     */
//    public boolean continueExecuting()
//    {
//        EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();
//
//        if (entitylivingbase == null)
//        {
//        	System.out.println("[MCTG] No target");
//            return false;
//        }
//        else if (!entitylivingbase.isEntityAlive())
//        {
//        	System.out.println("[MCTG] Target not alive");
//            return false;
//        }
//        else
//        {
//            Team team = this.taskOwner.getTeam();
//            Team team1 = entitylivingbase.getTeam();
//
//            if (team != null && team1 == team)
//            {
//            	System.out.println("[MCTG] Teams");
//                return false;
//            }
//            else
//            {
//                double d0 = this.getTargetDistance();
//
//                if (this.taskOwner.getDistanceSqToEntity(entitylivingbase) > d0 * d0)
//                {
//                	System.out.println("[MCTG] Target too far away");
//                    return false;
//                }
//                else
//                {
//                    /*if (this.shouldCheckSight)
//                    {
//                        if (this.taskOwner.getEntitySenses().canSee(entitylivingbase))
//                        {
//                            this.targetUnseenTicks = 0;
//                        }
//                        else if (++this.targetUnseenTicks > 60)
//                        {
//                            return false;
//                        }
//                    }*/
//
//                	boolean retVal = !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage;
//                	System.out.println("[MCTG] continueExecuting returning " + retVal);
//                    return retVal;
//                }
//            }
//        }
//    }

	
	@Override
	/**
     * A method used to see if an entity is a suitable target through a number of checks. Args : entity,
     * canTargetInvinciblePlayer
     * 
     * Is the entity controlled by another player.
     */
    protected boolean isSuitableTarget(EntityLivingBase potentialTarget, boolean canTargetInvinciblePlayer)
    {
		if (this.taskOwner instanceof IMCTGEntity && potentialTarget instanceof IMCTGEntity) {
			IMCTGEntity itself = ((IMCTGEntity)this.taskOwner);
			IMCTGEntity castedTarget = (IMCTGEntity)potentialTarget;
			
			if (castedTarget == itself) {
				return false;
			}
			
			UUID targetsController = ((IMCTGEntity) potentialTarget).getControllerUUID();
			UUID thisController = ((IMCTGEntity)this.taskOwner).getControllerUUID();
	        return targetsController == null || thisController == null ||
	        		!thisController.equals(targetsController);
		}
		return false;
    }
	
	@Override
	/**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
}
