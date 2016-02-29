package magicrafttg.event;

import java.util.List;

import magicrafttg.MagicraftTG;
import magicrafttg.client.gui.ManaSourceGui;
import magicrafttg.network.GuiHandler;
import magicrafttg.player.MCTGPlayerProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * For handling events that occur on FMLCommonHandler.instance().bus().
 * @author Adam
 *
 */
public class FMLCommonEventHandler {
	
	boolean manaTimerRunning = false;
	int manaTickCountdown = MagicraftTG.MANA_TICK_DELAY_INITIAL; // Should be ~20 ticks/sec
	int playerCount = 0;
	
	/**
	 * When a player logs in, start the manTimer if it is not already running.
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event)
	{
		//System.out.println("Player has logged in.");
		/*if(MagicraftTG.manaTimer.isRunning() == false)
		{
			//System.out.println("Starting manaTimer");
			MagicraftTG.manaTimer.setInitialDelay(10000);
			MagicraftTG.manaTimer.start();
		}*/
		if(this.manaTimerRunning == false)
		{
			this.manaTickCountdown = MagicraftTG.MANA_TICK_DELAY_INITIAL;
			this.manaTimerRunning = true;
			//System.out.println("[MCTG] Mana tick countdown started");
		}
		
		++playerCount;
	}
	
	
	
	/**
	 * When the last player logs out, stop the manaTimer.
	 * @param event
	 */
	/*
	 * No longer need this as it is called while the player is logging out not
	 * after. So the player is still seen as being on the server.
	 * Will do the check in the server tick function.
	 * */
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent event)
	{
		EntityPlayer player = event.player;
		System.out.println("Player has logged out. There are " + --playerCount + " players on the server");
		if(playerCount == 0)
		{
			System.out.println("Stoping manaTimer");
			//MagicraftTG.manaTimer.stop();
			this.manaTimerRunning = false;
		}
		
		// Handle the player's controlled creatures
		MCTGPlayerProperties mctg = MCTGPlayerProperties.get(player);
		if(mctg != null) // Probably an unnecessary check
		{
			//mctg.removeAllControlled();
		}
	}
	
	/*@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event) 
	{
		EntityPlayer player = (EntityPlayer) event.player;
		MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
		if(player.worldObj.isRemote == false && mctg.manaGuiCountdown > 0) 
		{
			World world = null;
			
			--(mctg.manaGuiCountdown);
			
			if(mctg.manaGuiCountdown == 0)
			{
				System.out.println("Mana gui countdown zero for " + player.getDisplayNameString());
				System.out.println(player.getUniqueID().toString());
				// See if the remote world is loaded yet
				try
				{
					world = player.worldObj; //MinecraftServer.getServer().getEntityWorld();// MinecraftServer.getServer().worldServerForDimension(0);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					System.out.println("Server world not loaded");
					mctg.manaGuiCountdown = MCTGGuiHandler.MANA_GUI_COUNTDOWN;
				}
				
				if (world == null)
				{
					System.out.println("World is null");
					return;
				}
				
				System.out.println(player.getDisplayNameString() + " player: " + player);
				System.out.println(player.getDisplayNameString() + " mctg: " + mctg);
				player.openGui(MagicraftTG.instance, 
						MCTGGuiHandler.MANA_SOURCE_GUI, 
						world, 
						(int) player.posX, 
						(int) player.posY, 
						(int) player.posZ);
			}
		}
	}*/
	
	/**
	 * If enough ticks have passed, notify clients of their updated mana from ManaSources.
	 * @param event
	 */
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		if(this.manaTimerRunning)
		{
			if(--this.manaTickCountdown <= 0)
			{
				//System.out.println("[MCTG] Mana tick countdown has runout");
				this.manaTickCountdown = MagicraftTG.MANA_TICK_DELAY_REGULAR;
				ManaEventHandler.manaTick();
			}
		}
	}
	
	
}
