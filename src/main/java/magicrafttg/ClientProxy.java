package magicrafttg;

import org.lwjgl.input.Keyboard;

import magicrafttg.client.KeyHandler;
import magicrafttg.client.gui.ManaSourceGui;
import magicrafttg.client.model.ModelMCTGDireWolf;
import magicrafttg.client.model.ModelMCTGSkeleton;
import magicrafttg.client.model.ModelMCTGTroll;
import magicrafttg.client.model.ModelMCTGZombie;
import magicrafttg.client.model.ModelWizard;
import magicrafttg.client.render.blocks.BlockRenderRegister;
import magicrafttg.client.render.entity.EntityRenderRegister;
import magicrafttg.client.render.entity.RenderMCTGDireWolf;
import magicrafttg.client.render.entity.RenderMCTGSkeleton;
import magicrafttg.client.render.entity.RenderMCTGTroll;
import magicrafttg.client.render.entity.RenderMCTGZombie;
import magicrafttg.client.render.entity.RenderWizard;
import magicrafttg.client.render.hud.HUDRenderer;
import magicrafttg.client.render.items.ItemRenderRegister;
import magicrafttg.entity.EntityMCTGDireWolf;
import magicrafttg.entity.EntityMCTGSkeleton;
import magicrafttg.entity.EntityMCTGTroll;
import magicrafttg.entity.EntityMCTGZombie;
import magicrafttg.entity.EntityWizard;
import magicrafttg.event.FMLCommonClientHandler;
import magicrafttg.event.FMLCommonEventHandler;
import magicrafttg.event.ForgeEventHandler;
import magicrafttg.event.ManaEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	public static KeyBinding[] keyBindings;
	
	/** Key descriptions; use a language file to localize the description later */
	private static final String[] desc = {"key.select1.desc",
			"key.select2.desc", "key.select3.desc", "key.select4.desc", "key.select5.desc",
			"key.select6.desc", "key.select7.desc", "key.select8.desc", "key.select9.desc",
			"key.select0.desc"};

	/** Default key values */
	private static final int[] keyValues = {Keyboard.KEY_1, Keyboard.KEY_2,
			Keyboard.KEY_3, Keyboard.KEY_4, Keyboard.KEY_5, Keyboard.KEY_6, Keyboard.KEY_7,
			Keyboard.KEY_8, Keyboard.KEY_9, Keyboard.KEY_0};

	public static final KeyBinding leftShift = new KeyBinding("key.leftShift", 
			Keyboard.KEY_LSHIFT, StatCollector.translateToLocal("key.magicraftg.select"));
	
	
	@Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ItemRenderRegister.registerItemRenderer();
        BlockRenderRegister.registerBlockRenderer();
        
        //needed for @SubscribeEvent method subscriptions:
        //   MinecraftForge.EVENT_BUS.register()          --> is used for net.minecraftforge events
        MinecraftForge.EVENT_BUS.register(HUDRenderer.instance);
        MinecraftForge.EVENT_BUS.register(ManaEventHandler.instance);
        EntityRenderRegister.registerEntityRenderer();
        RenderingRegistry.registerEntityRenderingHandler(EntityMCTGDireWolf.class, 
      	      new RenderMCTGDireWolf(new ModelMCTGDireWolf(), 0.7F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMCTGTroll.class, 
        	      new RenderMCTGTroll(new ModelMCTGTroll(), 1.0F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMCTGZombie.class, 
      	      new RenderMCTGZombie(new ModelMCTGZombie(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMCTGSkeleton.class, 
        	      new RenderMCTGSkeleton(new ModelMCTGSkeleton(), 0.5F));
        
        RenderingRegistry.registerEntityRenderingHandler(EntityWizard.class, 
      	      new RenderWizard(new ModelWizard(), 0.5F));
        
        
        // Register key bindings
        keyBindings = new KeyBinding[desc.length];
        
        for (int i = 0; i < desc.length; ++i) {
        	keyBindings[i] = new KeyBinding(desc[i], keyValues[i], StatCollector.translateToLocal("key.magicraftg.select"));
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
        ClientRegistry.registerKeyBinding(leftShift);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        FMLCommonHandler.instance().bus().register(new FMLCommonClientHandler());
        FMLCommonHandler.instance().bus().register(new KeyHandler());
    }
    
    /*@Override
    public void openManaSourceGui()
    {
    	Minecraft.getMinecraft().displayGuiScreen(new ManaSourceGui());
    }*/
}
