package magicrafttg.client.render.entity;

import magicrafttg.MagicraftTG;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderMCTGZombie extends RenderLiving {

	private static final String modid = MagicraftTG.MODID;
	private static final ResourceLocation textures = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final RenderManager manager = Minecraft.getMinecraft().getRenderManager();
	
	public RenderMCTGZombie(ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return textures;
	}
}
