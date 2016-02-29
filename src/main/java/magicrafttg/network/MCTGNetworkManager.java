package magicrafttg.network;

import magicrafttg.mana.ManaColor;
import magicrafttg.player.MCTGPlayerProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Handles the communication between client and server side for the MCTG extended properties.
 * Sits between MCTGPlayerProperties and the PacketHandler and packet types.
 * @author Adam
 *
 */
public class MCTGNetworkManager 
{

	/**
	 * Send the updated mana amounts to the client instance of EntityPlayer.
	 * I thought of making it @SideOnly(Side.SERVER) but this caused a
	 * NoSuchMethodException even though it seems to run on the server-side code.
	 * @param player
	 */
	public static void updateManaToClient(EntityPlayer player, int[] manaAmounts) {
		
		IMessage msg = new ManaPacket.MCTGManaMessage(PacketHandler.MANA_UPDATE, 
						manaAmounts[ManaColor.WHITE.ordinal()], 
						manaAmounts[ManaColor.BLUE.ordinal()], 
						manaAmounts[ManaColor.BLACK.ordinal()], 
						manaAmounts[ManaColor.RED.ordinal()],
						manaAmounts[ManaColor.GREEN.ordinal()], 
						manaAmounts[ManaColor.COLORLESS.ordinal()]
					);
		
		PacketHandler.net.sendTo(msg, (EntityPlayerMP)player);
	}
	
	
	/**
	 * Request current mana from the server.
	 */
	public static void updateManaFromServer()
	{
		//System.out.println("Requesting mana update from server");
		IMessage msg = new ManaPacket.MCTGManaMessage(PacketHandler.MANA_SRC_REQUEST,
				0, 0, 0, 0, 0, 0);
		PacketHandler.net.sendToServer(msg);
	}


	
	/**
	 * Send from the server to the client the current mana source numbers for the specified player.
	 * @param player
	 * @param sources
	 */
	public static void sendToClientManaSources(EntityPlayer player, int[] sources)
	{
		System.out.println("MCTGNetworkManager.sendToClientManaSources");
		IMessage msg = new ManaPacket.MCTGManaMessage(PacketHandler.MANA_GLOBAL_SOURCE_SET, 
				sources[0], sources[1], sources[2], sources[3], sources[4], sources[5]);
		PacketHandler.net.sendTo(msg, (EntityPlayerMP) player);
	}
	
	/**
	 * Update the mana source numbers as received from the server.
	 */
	public static void receiveFromServerManaSources(int w, int u, int b, int r, int g)
	{
		System.out.println("MCTGNetworkManager.receiveFromServerManaSources");
		MCTGPlayerProperties mctg = MCTGPlayerProperties.get(Minecraft.getMinecraft().thePlayer);
		if(mctg != null)
		{
			mctg.setGlobalManaSources(w, u, b, r, g);
		}
	}

	/**
	 * Send the current mana recorded on the server to the client instance of EntityPlayer. 
	 * Called after a spell is cast.
	 * @param entityPlayer
	 * @param currentMana
	 */
	public static void sendToClientCurrentMana(EntityPlayer player, int[] currentMana) 
	{
		System.out.println("MCTGNetworkManager.sendToClientCurrentMana");
		IMessage msg = new ManaPacket.MCTGManaMessage(PacketHandler.MANA_UPDATE, 
				currentMana[ManaColor.WHITE.ordinal()], 
				currentMana[ManaColor.BLUE.ordinal()], 
				currentMana[ManaColor.BLACK.ordinal()], 
				currentMana[ManaColor.RED.ordinal()],
				currentMana[ManaColor.GREEN.ordinal()], 
				currentMana[ManaColor.COLORLESS.ordinal()]
			);

		PacketHandler.net.sendTo(msg, (EntityPlayerMP)player);
	}

	/**
	 * Update the current mana recorded in the client to match what is recorded on the server.
	 * @param thePlayer
	 * @param manaAmounts 
	 */
	public static void receiveFromServerCurrentMana(int[] manaAmounts) 
	{
		System.out.println("MCTGNetworkManager.receiveFromServerCurrentMana");
		MCTGPlayerProperties prop = MCTGPlayerProperties.get(Minecraft.getMinecraft().thePlayer);
		prop.setCurrentMana(manaAmounts);
	}
}
