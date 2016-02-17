package magicrafttg.client.render.entity;

import magicrafttg.MagicraftTG;
import magicrafttg.client.model.ModelMCTGDireWolf;
import magicrafttg.entity.EntityMCTGDireWolf;
import magicrafttg.entity.EntityMCTGTroll;
import magicrafttg.entity.EntityMCTGZombie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class EntityRenderRegister {

	public static String modid = MagicraftTG.MODID;

	public static void reg(Class theClass, Render render, RenderManager manager) {
		manager.entityRenderMap.put(theClass, render);
	}
	
	public static void registerEntityRenderer() {
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		//reg(EntityMCTGDireWolf.class, new RenderMCTGDireWolf(rm, new ModelMCTGDireWolf(), 0.7F), rm);
		//reg(EntityMCTGTroll.class, new RenderMCTGTroll(rm, new ModelIronGolem(), 0.7F), rm);
		//reg(EntityMCTGZombie2.class, new RenderMCTGZombie2(rm), rm);
		System.out.println("[MCTG] Registered entity renderers");
		
    }
}
