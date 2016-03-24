package magicrafttg;

import magicrafttg.blocks.ModBlocks;
import magicrafttg.client.KeyHandler;
import magicrafttg.entity.EntityMCTGDireWolf;
import magicrafttg.entity.EntityMCTGSkeleton;
import magicrafttg.entity.EntityMCTGSkeletonArmoured;
import magicrafttg.entity.EntityMCTGTroll;
import magicrafttg.entity.EntityMCTGZombie;
import magicrafttg.entity.EntityWizard;
import magicrafttg.entity.ZZEntityMCTGTroll2;
import magicrafttg.entity.ZZEntityMCTGZombie;
import magicrafttg.entity.projectile.EntityLightningBolt;
import magicrafttg.event.FMLCommonEventHandler;
import magicrafttg.event.ForgeEventHandler;
import magicrafttg.items.ModItems;
import magicrafttg.network.GuiHandler;
import magicrafttg.network.PacketHandler;
import magicrafttg.spell.event.SpellEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

public class CommonProxy {

	private int modEntityID = 0;
	
    public void preInit(FMLPreInitializationEvent e) {
    	ModItems.createItems();
    	ModBlocks.createBlocks();
    	//EntityRegistry.registerGlobalEntityID(ZZEntityMCTGZombie.class, "MCTGZombie", 127, 230, 78);
    	//EntityRegistry.registerGlobalEntityID(EntityMCTGSkeleton.class, "MCTGSkeleton", 126, 230, 78);
    	//EntityRegistry.registerGlobalEntityID(EntityMCTGSkeletonArmoured.class, "MCTGSkeletonArmoured", 125, 230, 78);
    	//EntityRegistry.registerGlobalEntityID(EntityMCTGDireWolf.class, "EntityMCTGDireWolf", 124, 230, 78);
    	//EntityRegistry.registerGlobalEntityID(EntityMCTGTroll.class, "EntityMCTGTroll", 123, 230, 78);
    	//EntityRegistry.registerGlobalEntityID(EntityMCTGTroll2.class, "EntityMCTGTroll2", 122, 230, 78);
    	
    	EntityRegistry.registerModEntity(EntityMCTGDireWolf.class, "DireWolf", 
    			modEntityID++, MagicraftTG.instance, 80, 3, false);
    	EntityRegistry.registerModEntity(EntityMCTGTroll.class, "Troll", 
    			modEntityID++, MagicraftTG.instance, 80, 3, false);
    	EntityRegistry.registerModEntity(EntityMCTGZombie.class, "Zombie", 
    			modEntityID++, MagicraftTG.instance, 80, 3, false);
    	EntityRegistry.registerModEntity(EntityMCTGSkeleton.class, "Skeleton", 
    			modEntityID++, MagicraftTG.instance, 80, 3, false);
    	
    	EntityRegistry.registerModEntity(EntityWizard.class, "Wizard", 
    			modEntityID++, MagicraftTG.instance, 80, 3, false);
    	
    	EntityRegistry.registerModEntity(EntityLightningBolt.class, "Lightning Bolt", 
    			modEntityID++, MagicraftTG.instance, 80, 3, false);
    }

    public void init(FMLInitializationEvent e) {
    	PacketHandler.initPackets();
    	NetworkRegistry.INSTANCE.registerGuiHandler(MagicraftTG.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
    	MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
    	FMLCommonHandler.instance().bus().register(new FMLCommonEventHandler());
    	MinecraftForge.EVENT_BUS.register(new SpellEventHandler());
    }
    
    /*public void openManaSourceGui()
    {
    	
    }*/
}