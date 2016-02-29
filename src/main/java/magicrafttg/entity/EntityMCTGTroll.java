package magicrafttg.entity;

import java.util.UUID;

import magicrafttg.entity.ai.EntityMCTGAIAttackEnemy;
import magicrafttg.entity.ai.EntityMCTGAIFollowController;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMCTGTroll extends EntityMCTGBase implements IMCTGEntity {

	// Sp it attacks like an IronGolem
	private int attackTimer;
	
	public EntityMCTGTroll(World worldIn, EntityPlayer owner, int power, int toughness) {
		super(worldIn, owner, power, toughness);
		
		this.setCustomNameTag("Troll");
	}
	
	public EntityMCTGTroll(World worldIn) {
		super(worldIn);
	}
	
	@Override
	public void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
		
		this.setCustomNameTag("Troll");
		this.setSize(1.5F, 2.5F);
	}
	
	
	@Override
	public boolean attackEntityAsMob(Entity theTarget)
    {
		boolean res;
		this.attackTimer = 10;
		this.worldObj.setEntityState(this, (byte)4);
		//System.out.println("Before aeam: " + this.attackTimer);
		res = super.attackEntityAsMob(theTarget);
		//System.out.println("After aeam: " + this.attackTimer);
		return res;
    }
	
	@SideOnly(Side.CLIENT)
    public int getAttackTimer()
    {
        return this.attackTimer;
    }
	
	/**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
	@Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.attackTimer > 0)
        {
            --this.attackTimer;
            //System.out.println("onLivingUpdate: " + this.attackTimer);
        }

        /*if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0)
        {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
            int k = MathHelper.floor_double(this.posZ);
            IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
            Block block = iblockstate.getBlock();

            if (block.getMaterial() != Material.air)
            {
                this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D, new int[] {Block.getStateId(iblockstate)});
            }
        }*/
    }
	
	// Notify the client so that the attack arm animation works properly
	// From EntityIronGolem
	@SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 4)
        {
            this.attackTimer = 10;
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }
}
