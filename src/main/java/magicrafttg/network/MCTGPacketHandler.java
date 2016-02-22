package magicrafttg.network;

import magicrafttg.MagicraftTG;
import magicrafttg.network.MCTGCreaturePacket.MCTGCreatureMessage;
import magicrafttg.network.MCTGManaPacket.MCTGManaMessage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MCTGPacketHandler {

	public static final char MANA_GLOBAL_SOURCE_SET = '0';
	public static final char MANA_UPDATE = '1';
	public static final char MANA_SRC_REQUEST = '7';
	
	public static final char ADD_CREATURE = '2';
	public static final char REMOVE_CREATURE = '3';
	public static final char SELECT_CREATURE = '4';
	public static final char ADD_CREATURE_INT = '5';
	public static final char REMOVE_CREATURE_INT = '6';
	
	public static SimpleNetworkWrapper net;
	  
	public static void initPackets()
	{
		net = NetworkRegistry.INSTANCE.newSimpleChannel(MagicraftTG.MODID.toUpperCase());
		//System.out.println("[MCTG] Registering packets");
		registerMessage(MCTGManaPacket.class, MCTGManaMessage.class);
		registerMessage(MCTGCreaturePacket.class, MCTGCreatureMessage.class);
		    
		//System.out.println("[MCTG] MCTGPacketHandler registered");
	}
	  
	private static int nextPacketId = 0;
	  
	private static void registerMessage(Class packet, Class message)
	{
		net.registerMessage(packet, message, nextPacketId++, Side.SERVER);
		net.registerMessage(packet, message, nextPacketId++, Side.CLIENT);
		
	}
}
