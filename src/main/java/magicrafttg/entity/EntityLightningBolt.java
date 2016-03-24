package magicrafttg.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityLightningBolt extends EntityFireball
{

	public EntityLightningBolt(World worldIn) 
	{
		super(worldIn);
	}
	
	public EntityLightningBolt(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
		super(worldIn, x, y, z, accelX, accelY, accelZ);
    }
	
	public EntityLightningBolt(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
		super(worldIn, shooter, accelX, accelY, accelZ);
    }

	@Override
	protected void onImpact(MovingObjectPosition movingObject) 
	{
		if (!this.worldObj.isRemote)
        {
            if (movingObject.entityHit != null)
            {
                movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
                this.func_174815_a(this.shootingEntity, movingObject.entityHit);
            }

            boolean flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
            //this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
            //this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, 1.0f, flag, flag);
            this.setDead();
        }
	}

}
