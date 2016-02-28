package magicrafttg.network;

import magicrafttg.entity.MCTGPlayerProperties;
import magicrafttg.mana.ManaColor;
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
	 * Notify the client of the current mana as recorded on the server.
	 * @param thePlayer
	 * @param manaAmounts 
	 */
	public static void updateManaServerToClient(EntityPlayer player, int[] manaAmounts) 
	{
		MCTGPlayerProperties prop = MCTGPlayerProperties.get(player);
		prop.setCurrentMana(manaAmounts);
	}
	
}
