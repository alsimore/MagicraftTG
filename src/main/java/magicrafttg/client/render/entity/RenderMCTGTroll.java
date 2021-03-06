package magicrafttg.client.render.entity;

import magicrafttg.MagicraftTG;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderMCTGTroll extends RenderMCTGBase {

	private static final String modid = MagicraftTG.MODID;
	private static final ResourceLocation trollTextures = new ResourceLocation(modid + ":textures/entity/troll.png");
	private static final RenderManager manager = Minecraft.getMinecraft().getRenderManager();
	
	public RenderMCTGTroll(ModelBase model, float shadowSize) {
		super(model, shadowSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return trollTextures;
	}

}
