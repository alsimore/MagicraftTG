package magicrafttg.items;

import java.util.List;
import java.util.function.BiPredicate;

import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.mana.ManaColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MCTGTargetedSpell extends MCTGSpellItem {

	protected Entity target;
	protected BiPredicate<Entity, EntityPlayer> effect;
	
	public MCTGTargetedSpell(String unlocalizedName, ManaColor[] colour, int[] amt, BiPredicate<Entity, EntityPlayer> effect) {
		super(unlocalizedName, colour, amt);
		this.effect = effect;
	}

	@Override
	protected boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		// TODO: Need to ray trace or something as this is run server side and Minecraft isn't defined
		
		//this.target = Minecraft.getMinecraft().objectMouseOver.entityHit;
		this.target = getMouseOverExtended(worldIn, playerIn, 6.0f).entityHit;
		System.out.println("Target " + this.target);
		
		// Make sure the target is of the correct class
		if(this.target instanceof EntityMCTGBase)
		{
			return effect.test(this.target, playerIn);
		}
		else
		{
			System.out.println("Not an MCTG entity");
			return false;
		}
	}
	

	// Copied from Jabelar's Minecraft Forge Tutorials
	// http://jabelarminecraft.blogspot.com.au/p/minecraft-modding-extending-reach-of.html
	public static MovingObjectPosition getMouseOverExtended(World worldIn, EntityPlayer playerIn, float dist)
	{
	    //Minecraft mc = FMLClientHandler.instance().getClient();
	    //Entity theRenderViewEntity = mc.getRenderViewEntity();
		Entity theRenderViewEntity = playerIn;
	    AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(
	            theRenderViewEntity.posX-0.5D,
	            theRenderViewEntity.posY-0.0D,
	            theRenderViewEntity.posZ-0.5D,
	            theRenderViewEntity.posX+0.5D,
	            theRenderViewEntity.posY+1.5D,
	            theRenderViewEntity.posZ+0.5D
	            );
	    MovingObjectPosition returnMOP = null;
	    //if (mc.theWorld != null)
	    if (worldIn != null)
	    {
	        double var2 = dist;
	        returnMOP = rayTraceServer(worldIn, theRenderViewEntity, var2, 0);
	        // returnMOP = theRenderViewEntity.rayTrace(var2, 0);
	        double calcdist = var2;
	        //Vec3 pos = theRenderViewEntity.getPositionEyes(0);
	        Vec3 pos = getPositionEyesServer(theRenderViewEntity, 0);
	        var2 = calcdist;
	        if (returnMOP != null)
	        {
	            calcdist = returnMOP.hitVec.distanceTo(pos);
	        }
	         
	        Vec3 lookvec = theRenderViewEntity.getLook(0);
	        Vec3 var8 = pos.addVector(lookvec.xCoord * var2, 
	              lookvec.yCoord * var2, 
	              lookvec.zCoord * var2);
	        Entity pointedEntity = null;
	        float var9 = 1.0F;
	        @SuppressWarnings("unchecked")
	        //List<Entity> list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
	        List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(
	              theRenderViewEntity, 
	              theViewBoundingBox.addCoord(
	                    lookvec.xCoord * var2, 
	                    lookvec.yCoord * var2, 
	                    lookvec.zCoord * var2).expand(var9, var9, var9));
	        double d = calcdist;
	        //System.out.println("List size " + list.size());
	            
	        for (Entity entity : list)
	        {
	            if (entity.canBeCollidedWith())
	            {
	                float bordersize = entity.getCollisionBorderSize();
	                AxisAlignedBB aabb = new AxisAlignedBB(
	                      entity.posX-entity.width/2, 
	                      entity.posY, 
	                      entity.posZ-entity.width/2, 
	                      entity.posX+entity.width/2, 
	                      entity.posY+entity.height, 
	                      entity.posZ+entity.width/2);
	                aabb.expand(bordersize, bordersize, bordersize);
	                MovingObjectPosition mop0 = aabb.calculateIntercept(pos, var8);
	                    
	                if (aabb.isVecInside(pos))
	                {
	                    if (0.0D < d || d == 0.0D)
	                    {
	                        pointedEntity = entity;
	                        d = 0.0D;
	                    }
	                } else if (mop0 != null)
	                {
	                    double d1 = pos.distanceTo(mop0.hitVec);
	                        
	                    if (d1 < d || d == 0.0D)
	                    {
	                        pointedEntity = entity;
	                        d = d1;
	                    }
	                }
	            }
	        }
	           
	        if (pointedEntity != null && (d < calcdist || returnMOP == null))
	        {
	             returnMOP = new MovingObjectPosition(pointedEntity);
	        }
	    }
	    return returnMOP;
	}
	
	// Copied from Entity.rayTrace, which is client only
	private static MovingObjectPosition rayTraceServer(World worldIn, Entity entity, double dist, float p_174822_3_)
    {
        Vec3 vec3 = getPositionEyesServer(entity, p_174822_3_);
        Vec3 vec31 = entity.getLook(p_174822_3_);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist);
        return worldIn.rayTraceBlocks(vec3, vec32, false, false, true);
    }
	
	// Copied from Entity.getPositionEyes, which is client only
	private static Vec3 getPositionEyesServer(Entity entity, float p_174824_1_)
    {
        if (p_174824_1_ == 1.0F)
        {
            return new Vec3(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
        }
        else
        {
            double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)p_174824_1_;
            double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)p_174824_1_ + (double)entity.getEyeHeight();
            double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)p_174824_1_;
            return new Vec3(d0, d1, d2);
        }
    }
}
