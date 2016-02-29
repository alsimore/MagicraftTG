package magicrafttg.client.render.entity;

import java.util.List;
import java.util.UUID;

import magicrafttg.MagicraftTG;
import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.items.MCTGTargetedSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RenderMCTGBase extends RenderLiving {

	private static final String modid = MagicraftTG.MODID;
	private static final ResourceLocation textures = null;
	private static final RenderManager manager = Minecraft.getMinecraft().getRenderManager();
	
	public RenderMCTGBase(ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return textures;
	}
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
        AxisAlignedBB aabb = entity.getEntityBoundingBox();
        
        //Entity target = Minecraft.getMinecraft().objectMouseOver.entityHit;
        Minecraft mc = Minecraft.getMinecraft();
        World worldIn = mc.theWorld;
        EntityPlayer playerIn = mc.thePlayer;
        //Entity target = MCTGTargetedSpell.getMouseOverExtended(worldIn, playerIn, 6.0f).entityHit;
        Entity target = Minecraft.getMinecraft().objectMouseOver.entityHit;
        //Entity target = this.getMouseOverExtended(worldIn, playerIn, 6.0f).entityHit;
        //System.out.println("mouseover " + target);
        //System.out.println("render entity " + entity);
        if(entity instanceof EntityMCTGBase && entity.equals(target))
        {
        	int colour = 0;
        	//System.out.println("entity " + entity);
        	//System.out.println("target " + target);
        	//EntityMCTGBase mctgEntity = (EntityMCTGBase) MinecraftServer.getServer().getEntityFromUuid(target.getUniqueID());
        	EntityMCTGBase mctgEntity = (EntityMCTGBase) entity;
        	//System.out.println("mctg " + mctgEntity);
        	//try
        	//{
        		UUID owner = null;
        		try
        		{
        			owner = UUID.fromString(mctgEntity.getDataWatcher().getWatchableObjectString(20)); //mctgEntity.getOwnerUUID();
        		}
        		catch (IllegalArgumentException e)
        		{
        			
        		}
        		
        		UUID controller = null;
        		try
        		{
        			controller = UUID.fromString(mctgEntity.getDataWatcher().getWatchableObjectString(21)); //mctgEntity.getControllerUUID();
        		}
        		catch (IllegalArgumentException e)
        		{
        			
        		}
        		
        		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        		//System.out.println("Owner " + owner + "; Cont " + controller); 
        		//System.out.println("Player " + player.getUniqueID());
            	//if(owner != null && controller != null && owner.equals(controller))
        		if(controller != null && player.getUniqueID().equals(controller))
            	{
            		colour = 65280; // 0000 0000 1111 1111 0000 0000, green
            	}
            	else
            	{
            		colour = 16711680; // 1111 1111 0000 0000 0000 0000, red
            	}
            	
            	
        	//}
        	//catch (IllegalArgumentException e)
        	//{
        		// failed to get owner or controller UUID, go with red (not controlled)
        	//	System.out.println("Error: " + e);
        	//	colour = 16711680; // 1111 1111 0000 0000 0000 0000, red
        	//}
        	
        	renderControlBox(entity, x, y, z, p_76986_8_, partialTicks, colour);
        }
    }
	
	/**
     * Renders the bounding box around an entity when F3+B is pressed
     */
    private void renderControlBox(Entity entity, double x, double y, double z, float p_85094_8_, float partialTicks, int colour)
    {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        float f2 = entity.width / 2.0F;
        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX - entity.posX + x, axisalignedbb.minY - entity.posY + y, axisalignedbb.minZ - entity.posZ + z, axisalignedbb.maxX - entity.posX + x, axisalignedbb.maxY - entity.posY + y, axisalignedbb.maxZ - entity.posZ + z);
        //RenderGlobal.drawOutlinedBoundingBox(axisalignedbb1, 16777215);
        RenderGlobal.drawOutlinedBoundingBox(axisalignedbb1, colour);

        /*if (p_85094_1_ instanceof EntityLivingBase)
        {
            float f3 = 0.01F;
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(p_85094_2_ - (double)f2, p_85094_4_ + (double)p_85094_1_.getEyeHeight() - 0.009999999776482582D, p_85094_6_ - (double)f2, p_85094_2_ + (double)f2, p_85094_4_ + (double)p_85094_1_.getEyeHeight() + 0.009999999776482582D, p_85094_6_ + (double)f2), 16711680);
        }*/

        /*Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        Vec3 vec3 = p_85094_1_.getLook(p_85094_9_);
        worldrenderer.startDrawing(3);
        worldrenderer.setColorOpaque_I(255);
        worldrenderer.addVertex(p_85094_2_, p_85094_4_ + (double)p_85094_1_.getEyeHeight(), p_85094_6_);
        worldrenderer.addVertex(p_85094_2_ + vec3.xCoord * 2.0D, p_85094_4_ + (double)p_85094_1_.getEyeHeight() + vec3.yCoord * 2.0D, p_85094_6_ + vec3.zCoord * 2.0D);
        tessellator.draw();*/
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
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
 	        returnMOP = playerIn.rayTrace(var2, 0);
 	        // returnMOP = theRenderViewEntity.rayTrace(var2, 0);
 	        double calcdist = var2;
 	        //Vec3 pos = theRenderViewEntity.getPositionEyes(0);
 	        Vec3 pos = playerIn.getPositionEyes(0);
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
 	
}
