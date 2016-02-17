package magicrafttg.client.render.entity;

import magicrafttg.MagicraftTG;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RenderMCTGDireWolf extends RenderLiving {
	
	private static final String modid = MagicraftTG.MODID;
	private static final ResourceLocation direWolfTextures = new ResourceLocation(modid + ":textures/entity/dire-wolf.png");
	private static final RenderManager manager = Minecraft.getMinecraft().getRenderManager();

	public RenderMCTGDireWolf(ModelBase model, float shadowise) {
		super(manager, model, shadowise);
		// TODO Auto-generated constructor stub
	}
	
	/*@Override
	public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
		System.out.println("Render EntityLiving called");
        //super.doRender((EntityLivingBase)entity, x, y, z, p_76986_8_, partialTicks);
    }
	
	@Override
	public void doRender(EntityLivingBase entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
		System.out.println("Render EntityLivingBase called");
        //this.doRender((EntityLiving)entity, x, y, z, p_76986_8_, partialTicks);
    }
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
		System.out.println("Render Entity called");
        super.doRender((EntityLiving)entity, x, y, z, p_76986_8_, partialTicks);
    }*/
	

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.direWolfTextures;
    	//return null;
    }
}
