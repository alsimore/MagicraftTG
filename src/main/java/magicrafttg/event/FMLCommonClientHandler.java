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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * For handling events that occur on FMLCommonHandler.instance().bus() on the
 * client side only.
 * @author Adam
 *
 */
public class FMLCommonClientHandler {
	
	// Runs on the client side so making it static shouuldn't mess with
	// other clients.
	private static final int MANA_GUI_COUNTDOWN = 60;
	private static int guiTickCountdown = MANA_GUI_COUNTDOWN;
	
	
	/**
	 * Display a ManaSourceGui after a certain number of ticks.
	 */
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = (EntityPlayer) event.player;
		if(player.worldObj.isRemote && guiTickCountdown > 0) 
		{
			--guiTickCountdown;
			//System.out.println("Countdown " + tickCountdown);
			if(guiTickCountdown == 0)
			{
				Minecraft.getMinecraft().displayGuiScreen(new ManaSourceGui());
			}
		}
	}
	
	public static void resetManaGuiCountdown()
	{
		guiTickCountdown = MANA_GUI_COUNTDOWN;
	}
	
	
}
