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
@SideOnly(Side.CLIENT)
public class FMLCommonClientHandler {
	
	// Runs on the client side so making it static shouuldn't mess with
	// other clients.
	
	//private int guiTickCountdown = MANA_GUI_COUNTDOWN;
	
	
	/**
	 * Display a ManaSourceGui after a certain number of ticks.
	 */
	/*@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTick(TickEvent.PlayerTickEvent event) 
	{
		EntityPlayer player = (EntityPlayer) event.player;
		MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
		if(player.worldObj.isRemote == true && mctg.manaGuiCountdown > 0) 
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
						0,
						0,
						0);
				//Minecraft.getMinecraft().displayGuiScreen(new ManaSourceGui());
			}
		}
	}*/
	
	/*public void resetManaGuiCountdown()
	{
		guiTickCountdown = MANA_GUI_COUNTDOWN;
	}*/
	
	
}
