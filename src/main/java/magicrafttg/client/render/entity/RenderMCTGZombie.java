package magicrafttg.client.render.entity;

import magicrafttg.MagicraftTG;
import magicrafttg.entity.EntityMCTGBase;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderMCTGZombie extends RenderMCTGBase {

	private static final String modid = MagicraftTG.MODID;
	private static final ResourceLocation textures = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final RenderManager manager = Minecraft.getMinecraft().getRenderManager();
	
	public RenderMCTGZombie(ModelBase model, float shadowSize) {
		super(model, shadowSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return textures;
	}
	
	/*@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
    }*/
	
}
