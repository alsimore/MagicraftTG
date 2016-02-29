package magicrafttg.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;
import javax.swing.event.EventListenerList;

import magicrafttg.MagicraftTG;
import magicrafttg.blocks.ManaBlock;
import magicrafttg.mana.ManaColor;
import magicrafttg.network.ManaPacket;
import magicrafttg.network.PacketHandler;
import magicrafttg.player.MCTGPlayerProperties;
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
	/*@SubscribeEvent
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
				
				MCTGPlayerProperties mctg = MCTGPlayerProperties.get(player); // This will be the server-side data
				mctg.increaseMana(newBlock.getColour(), 1);
				
				mctg.updateManaToClient(player);
				
				// Register the block to listen to the manaTimer
				addBlock(newBlock);
			}
		}
	}*/
	
	/**
	 * If the block broken is a mana block, remove the appropriate mana from the 
	 * player's pool.
	 * @param event
	 */
	/*@SubscribeEvent
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
			
			if(player instanceof EntityPlayerMP) {
				MCTGPlayerProperties mctg = MCTGPlayerProperties.get(player);
				if (mctg == null)
					return;
				mctg.decreaseMana(block.getColour(), 1);
				int[] currentMana = mctg.getCurrentMana();
				IMessage msg = new ManaPacket.MCTGManaMessage(PacketHandler.MANA_UPDATE, 
									currentMana[ManaColor.WHITE.ordinal()], currentMana[ManaColor.BLUE.ordinal()], 
									currentMana[ManaColor.BLACK.ordinal()], currentMana[ManaColor.RED.ordinal()], 
									currentMana[ManaColor.GREEN.ordinal()], currentMana[ManaColor.COLORLESS.ordinal()]
    							);
				PacketHandler.net.sendTo(msg, (EntityPlayerMP)player);
			}
		}
	}*/

	public void addBlock(ManaBlock b)
	{
		blockList.add(b);
	}
	
	public void removeBlock(ManaBlock b)
	{
		blockList.remove(b);
	}
	
	
	/**
	 * Called from serverTick to update all players' current mana periodically.
	 */
	public static void manaTick() 
	{
		System.out.println("ManaEventHandler.manaTick");
		// Increase the players' mana based on their sources.
		List<EntityPlayerMP> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		
		for(EntityPlayerMP player : allPlayers)
		{
			MCTGPlayerProperties mctg = MCTGPlayerProperties.get(player);
			mctg.incrementManaFromSources();
		}

		
		// Increase the players' mana from the blocks placed in the world.
		/*for(ManaBlock block : blockList)
		{
			EntityPlayer player = (EntityPlayer) MinecraftServer.getServer().getEntityFromUuid(block.getPlayerID()); 
			MCTGPlayerProperties mctg = MCTGPlayerProperties.get(player); // This will be the server-side data
			mctg.increaseMana(block.getColour(), 1);
			
			mctg.updateManaToClient(player);
		}*/
		
		
	}
}
