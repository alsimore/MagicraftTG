package magicrafttg.event;

import java.util.List;

import magicrafttg.MagicraftTG;
import magicrafttg.client.gui.ManaSourceGui;
import magicrafttg.entity.MagicraftTGPlayer;
import magicrafttg.network.MCTGGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
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
			System.out.println("[MCTG] Mana tick countdown started");
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
		System.out.println("Player has logged out. There are " + --playerCount + " players on the server");
		if(playerCount == 0)
		{
			System.out.println("Stoping manaTimer");
			//MagicraftTG.manaTimer.stop();
			this.manaTimerRunning = false;
		}
	}
	
	
	
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
