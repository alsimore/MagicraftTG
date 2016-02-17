package magicrafttg;

import javax.swing.Timer;

import magicrafttg.client.gui.ManaSourceGui;
import magicrafttg.entity.EntityMCTGDireWolf;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = MagicraftTG.MODID, name = MagicraftTG.MODNAME, version = MagicraftTG.VERSION)
public class MagicraftTG {

    public static final String MODID = "magicrafttg";
    public static final String MODNAME = "Magicraft: the Gathering";
    public static final String VERSION = "0.1";
    
    public static final float DAMAGE_PER_POWER = 2.0f;
    public static final float HEALTH_PER_TOUGHNESS = 10.0f;
    
    public static final int MANA_TICK_DELAY_INITIAL = 400;
    public static final int MANA_TICK_DELAY_REGULAR = 300;
    
    
    @SidedProxy(clientSide="magicrafttg.ClientProxy", 
    		serverSide="magicrafttg.ServerProxy")
    public static CommonProxy proxy;
    
    @Instance
    public static MagicraftTG instance = new MagicraftTG();
    

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	this.proxy.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
    	this.proxy.init(e);
    	//EntityRegistry.registerModEntity(EntityMCTGDireWolf.class, "Dire Wolf", 124, this, 80, 3, true);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	this.proxy.postInit(e);
    }
}