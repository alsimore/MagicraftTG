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


public class EntityEnergyBolt extends EntitySnowball
{
   
	public EntityEnergyBolt(World worldIn)
    {
        super(worldIn);WorldGenMinable m;
        //this.setSize(1.0F, 1.0F);
    }

    protected void entityInit() {}
    
    public EntityEnergyBolt(World worldIn, EntityLivingBase throwerIn)
    {
    	super(worldIn, throwerIn);
    }
    
    public EntityEnergyBolt(World worldIn, double x, double y, double z)
    {
    	super(worldIn, x, y, z);
    }

    /*public EntityLightningBolt(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setPosition(x, y, z);
        double d6 = (double)MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d6 * 0.1D;
        this.accelerationY = accelY / d6 * 0.1D;
        this.accelerationZ = accelZ / d6 * 0.1D;
    }*/

    /*public EntityLightningBolt(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn);
        this.shootingEntity = shooter;
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = this.motionY = this.motionZ = 0.0D;
        accelX += this.rand.nextGaussian() * 0.4D;
        accelY += this.rand.nextGaussian() * 0.4D;
        accelZ += this.rand.nextGaussian() * 0.4D;
        double d3 = (double)MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d3 * 0.1D;
        this.accelerationY = accelY / d3 * 0.1D;
        this.accelerationZ = accelZ / d3 * 0.1D;
        
        //this.setHeading(accelerationX, accelerationY, accelerationZ, 1.0f, 0.0f);
    }*/
    
    

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        //if (this.worldObj.isRemote)
        //	System.out.println("Pitch: " + this.rotationPitch + ", Yaw: " + this.rotationYaw);
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
                //movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
                //this.func_174815_a(this.shootingEntity, movingObject.entityHit);
            }

            boolean flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
            //this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
            this.setDead();
        }
    }
}