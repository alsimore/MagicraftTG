package magicrafttg.entity.ai;

import magicrafttg.entity.IMCTGEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityMCTGAIFollowController extends EntityAIBase {

	private EntityLiving theCreature;
    private EntityLivingBase theController;
    World theWorld;
    private double speed;
    private PathNavigate pathfinder;
    private int field_75343_h; // Some sort of tick countdown I think.
    float maxDist;
    float minDist;
    private boolean field_75344_i;
    //private static final String __OBFID = "CL_00001585";

    public EntityMCTGAIFollowController(EntityLiving entity, double speed, float minDist, float maxDist)
    {
        this.theCreature = entity;
        this.theWorld = entity.worldObj;
        this.speed = speed;
        this.pathfinder = entity.getNavigator();
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.setMutexBits(3);

        if (!(entity.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal: EntityMCTGAIFollowController");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = ((IMCTGEntity)this.theCreature).getControllerEntity();

        if (entitylivingbase == null)
        {
            return false;
        }
        /*else if (this.theCreature.isSitting())
        {
            return false;
        }*/
        else if (this.theCreature.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist))
        {
            return false;
        }
        else
        {
            this.theController = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.pathfinder.noPath() && this.theCreature.getDistanceSqToEntity(this.theController) > (double)(this.maxDist * this.maxDist);// && !this.theCreature.isSitting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_75343_h = 0;
        this.field_75344_i = ((PathNavigateGround)this.theCreature.getNavigator()).func_179689_e();
        ((PathNavigateGround)this.theCreature.getNavigator()).func_179690_a(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theController = null;
        this.pathfinder.clearPathEntity();
        ((PathNavigateGround)this.theCreature.getNavigator()).func_179690_a(true);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.theCreature.getLookHelper().setLookPositionWithEntity(this.theController, 10.0F, (float)this.theCreature.getVerticalFaceSpeed());

        //if (!this.theCreature.isSitting())
        //{
            if (--this.field_75343_h <= 0) // Some sort of countdown?
            {
                this.field_75343_h = 10;

                if (!this.pathfinder.tryMoveToEntityLiving(this.theController, this.speed))
                {
                    if (!this.theCreature.getLeashed())
                    {
                        if (this.theCreature.getDistanceSqToEntity(this.theController) >= 144.0D)
                        {
                            int i = MathHelper.floor_double(this.theController.posX) - 2;
                            int j = MathHelper.floor_double(this.theController.posZ) - 2;
                            int k = MathHelper.floor_double(this.theController.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l)
                            {
                                for (int i1 = 0; i1 <= 4; ++i1)
                                {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, new BlockPos(i + l, k - 1, j + i1)) && !this.theWorld.getBlockState(new BlockPos(i + l, k, j + i1)).getBlock().isFullCube() && !this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)).getBlock().isFullCube())
                                    {
                                        this.theCreature.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theCreature.rotationYaw, this.theCreature.rotationPitch);
                                        this.pathfinder.clearPathEntity();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        //}
    }
}
