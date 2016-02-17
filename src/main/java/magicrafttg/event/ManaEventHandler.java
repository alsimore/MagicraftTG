package magicrafttg.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;
import javax.swing.event.EventListenerList;

import magicrafttg.MagicraftTG;
import magicrafttg.blocks.ManaBlock;
import magicrafttg.entity.MagicraftTGPlayer;
import magicrafttg.mana.ManaColour;
import magicrafttg.network.MCTGManaPacket;
import magicrafttg.network.MCTGPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ManaEventHandler {
	public static final ManaEventHandler instance = 
			new ManaEventHandler();
	//private static Minecraft mc = Minecraft.getMinecraft();

	protected static ArrayList<ManaBlock> blockList;
	
	private ManaEventHandler()
	{
		blockList = new ArrayList<ManaBlock>();
		
	}

	/**
	 * Check if the block is a mana block. If it is add the appropriate colour 
	 * mana to the player's pool.
	 * @param event
	 */
	@SubscribeEvent
	public void onBlockPlaced(BlockEvent.PlaceEvent event)
	{
		String blockName = event.placedBlock.getBlock().getUnlocalizedName().substring(5);
		EntityPlayer player = event.player;
		if(blockName.equals("mana_block"))
		{
			
			if(player instanceof EntityPlayerMP)
			{
				ManaBlock newBlock = (ManaBlock) event.placedBlock.getBlock();
				newBlock.setPropertiesFromEvent(event);
				
				MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player); // This will be the server-side data
				mctg.increaseMana(newBlock.getColour(), 1);
				
				mctg.updateManaToClient(player);
				
				// Register the block to listen to the manaTimer
				addBlock(newBlock);
			}
		}
	}
	
	/**
	 * If the block broken is a mana block, remove the appropriate mana from the 
	 * player's pool.
	 * @param event
	 */
	@SubscribeEvent
	public void onBlockBroken(BlockEvent.BreakEvent event)
	{
		String blockName = event.state.getBlock().getUnlocalizedName().substring(5);
		
		if(blockName.equals("mana_block"))
		{
			ManaBlock block = (ManaBlock) event.state.getBlock();
			// Find the owner of the block
			EntityPlayer player = (EntityPlayer) MinecraftServer.getServer().getEntityFromUuid(block.getPlayerID()); // event.getPlayer();
			// Remove the block from the list
			removeBlock(block);
			/*if(blockList.isEmpty())
			{
				MagicraftTG.manaTimer.stop();
			}*/
			
			if(player instanceof EntityPlayerMP) {
				MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
				if (mctg == null)
					return;
				mctg.decreaseMana(block.getColour(), 1);
				IMessage msg = new MCTGManaPacket.MCTGManaMessage(MCTGPacketHandler.MANA_UPDATE, 
						mctg.getWhiteMana(), mctg.getBlueMana(), 
						mctg.getBlackMana(), mctg.getRedMana(), mctg.getGreenMana(),
						mctg.getColourlessMana());
				MCTGPacketHandler.net.sendTo(msg, (EntityPlayerMP)player);
			}
		}
	}

	public void addBlock(ManaBlock b)
	{
		blockList.add(b);
	}
	
	public void removeBlock(ManaBlock b)
	{
		blockList.remove(b);
	}
	
	
	
	public static void manaTick() {
		//System.out.println("*********************************************");
		//System.out.println("[MCTG] manaTimer has fired");
		
		

		// Increase the players' mana from the blocks placed in the world.
		for(ManaBlock block : blockList)
		{
			EntityPlayer player = (EntityPlayer) MinecraftServer.getServer().getEntityFromUuid(block.getPlayerID()); 
			MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player); // This will be the server-side data
			mctg.increaseMana(block.getColour(), 1);
			
			mctg.updateManaToClient(player);
		}
		
		// Increase the players' mana based on their globalSources.
		List<EntityPlayerMP> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		//System.out.println("[MCTG] There are " + allPlayers.size() + " players on the server.");
		for(EntityPlayerMP player : allPlayers)
		{
			MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
			mctg.incrementManaFromSources();
			//System.out.println("Player " + player.getUniqueID().toString() + "(" + System.identityHashCode(player) + ")");
			//System.out.println("[MCTG] ### " + player.getGameProfile().getId().toString());
			
			mctg.updateManaToClient(player);
		}
		//System.out.println("*********************************************");
	}
}
