package magicrafttg.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityEnergyBolt extends EntityThrowable
{

	private float speed = 2.0f;
	
	public EntityEnergyBolt(World worldIn)
    {
        super(worldIn);
        //this.setSize(1.0F, 1.0F);
    }

    protected void entityInit() {}
    
    public EntityEnergyBolt(World worldIn, EntityLivingBase throwerIn)
    {
    	super(worldIn, throwerIn);
    	
    	// Seems to move in a wrong direction normally, so fix it
    	Vec3 look = throwerIn.getLookVec();
    	this.motionX = look.xCoord * speed;
    	this.motionY = look.yCoord * speed;
    	this.motionZ = look.zCoord * speed;
    }
    
    public EntityEnergyBolt(World worldIn, double x, double y, double z)
    {
    	super(worldIn, x, y, z);
    }

    
    @Override
    protected float getGravityVelocity() 
    {
    	return 0.0f;
    }

    
    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onImpact(MovingObjectPosition movingObject)
    {
        if (!this.worldObj.isRemote)
        {
            if (movingObject.entityHit != null)
            {
                movingObject.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 6.0F);
                this.func_174815_a(this.getThrower(), movingObject.entityHit);
            }

            boolean flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
            //this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
            this.setDead();
        }
    }
}