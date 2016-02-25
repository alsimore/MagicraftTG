package magicrafttg.entity.ai;

import java.util.UUID;

import com.google.common.base.Predicate;

import magicrafttg.entity.IMCTGEntity;
import magicrafttg.entity.MagicraftTGPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;

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

	private int countdown = 25;
	@Override
	/**
     * A method used to see if an entity is a suitable target through a number of checks. Args : entity,
     * canTargetInvinciblePlayer
     * 
     * Is the entity controlled by another player.
     */
    protected boolean isSuitableTarget(EntityLivingBase potentialTarget, boolean canTargetInvinciblePlayer)
    {
		--this.countdown;
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
		else if (this.taskOwner instanceof IMCTGEntity && potentialTarget instanceof EntityPlayer) {
			//System.out.println("Potential player target");
			//System.out.println(potentialTarget);
			IMCTGEntity itself = ((IMCTGEntity)this.taskOwner);
			
			// See if the player is an MagicraftTGPlayer
			try
			{
				MagicraftTGPlayer castedTarget = MagicraftTGPlayer.get((EntityPlayer)potentialTarget);
			}
			catch (Exception e)
			{
				System.out.println("Error in isSuitableTarget: " + e.toString());
				return false;
			}
			
			if (potentialTarget.getUniqueID() == itself.getControllerUUID()) {
				// The potential target is the controller
				return false;
			}
			
			UUID target = potentialTarget.getUniqueID();
			UUID thisController = ((IMCTGEntity)this.taskOwner).getControllerUUID();
			System.out.println("Target: " + target + " Controller: " + thisController);
	        return thisController == null || !thisController.equals(target);
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
