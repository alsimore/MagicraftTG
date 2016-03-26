package magicrafttg.client.render.entity;

import org.lwjgl.opengl.GL11;

import magicrafttg.entity.projectile.EntityEnergyBolt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLightningBolt extends Render
{
	private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");
    private float scale;
    private static final String __OBFID = "CL_00000995";

    public RenderLightningBolt(RenderManager p_i46176_1_, float p_i46176_2_)
    {
        super(p_i46176_1_);
        this.scale = p_i46176_2_;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityEnergyBolt entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
    	int red = 140;
    	int green = 226;
    	int blue = 255;
    	int alpha = 255;
    	
    	GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glDisable(GL11.GL_LIGHTING);
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    	
    	
    	//this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float)(0 + b0 * 10) / 32.0F;
        float f5 = (float)(5 + b0 * 10) / 32.0F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float)(5 + b0 * 10) / 32.0F;
        float f9 = (float)(10 + b0 * 10) / 32.0F;
        float f10 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        /*float f11 = (float)entity.arrowShake - partialTicks;

        if (f11 > 0.0F)
        {
            float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
            GlStateManager.rotate(f12, 0.0F, 0.0F, 1.0F);
        }*/

        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(f10, f10, f10);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);
        /*worldrenderer.startDrawingQuads();
        worldrenderer.setColorRGBA(red, green, blue, alpha);
        //worldrenderer.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f6, (double)f8);
        //worldrenderer.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f7, (double)f8);
        //worldrenderer.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f7, (double)f9);
        //worldrenderer.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f6, (double)f9);
        worldrenderer.addVertex(-7.0D, -2.0D, -2.0D);
        worldrenderer.addVertex(-7.0D, -2.0D, 2.0D);
        worldrenderer.addVertex(-7.0D, 2.0D, 2.0D);
        worldrenderer.addVertex(-7.0D, 2.0D, -2.0D);
        tessellator.draw();
        GL11.glNormal3f(-f10, 0.0F, 0.0F);
        worldrenderer.startDrawingQuads();
        worldrenderer.setColorRGBA(red, green, blue, alpha);
        //worldrenderer.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f6, (double)f8);
        //worldrenderer.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f7, (double)f8);
        //worldrenderer.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f7, (double)f9);
        //worldrenderer.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f6, (double)f9);
        worldrenderer.addVertex(-7.0D, 2.0D, -2.0D);
        worldrenderer.addVertex(-7.0D, 2.0D, 2.0D);
        worldrenderer.addVertex(-7.0D, -2.0D, 2.0D);
        worldrenderer.addVertex(-7.0D, -2.0D, -2.0D);
        tessellator.draw();*/

        for (int i = 0; i < 4; ++i)
        {
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            worldrenderer.startDrawingQuads();
            worldrenderer.setColorRGBA(red, green, blue, alpha);
            /*worldrenderer.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double)f2, (double)f4);
            worldrenderer.addVertexWithUV(8.0D, -2.0D, 0.0D, (double)f3, (double)f4);
            worldrenderer.addVertexWithUV(8.0D, 2.0D, 0.0D, (double)f3, (double)f5);
            worldrenderer.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double)f2, (double)f5);*/
            worldrenderer.addVertex(-8.0D, -2.0D, 0.0D);
            worldrenderer.addVertex(8.0D, -2.0D, 0.0D);
            worldrenderer.addVertex(8.0D, 2.0D, 0.0D);
            worldrenderer.addVertex(-8.0D, 2.0D, 0.0D);
            tessellator.draw();
        }

        GlStateManager.disableRescaleNormal();
        
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, y, p_76986_8_, partialTicks);
    }

    protected ResourceLocation func_180556_a(Entity p_180556_1_)
    {
        return TextureMap.locationBlocksTexture;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        //return this.func_180556_a(entity);
    	return this.arrowTextures;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityEnergyBolt)entity, x, y, z, p_76986_8_, partialTicks);
    }
}