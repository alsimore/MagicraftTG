package magicrafttg.event;

import java.util.List;

import magicrafttg.MagicraftTG;
import magicrafttg.client.gui.ManaSourceGui;
import magicrafttg.entity.MagicraftTGPlayer;
import magicrafttg.mana.ManaColour;
import magicrafttg.network.MCTGGuiHandler;
import magicrafttg.network.MCTGManaPacket;
import magicrafttg.network.MCTGPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * For handling events that occur on MinecraftForge.EVENT_BUS.
 * @author Adam
 *
 */
public class ForgeEventHandler {

	/**
	 * When a new player entity is created, add the appropriate MCTG properties.
	 * @param event
	 */
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer && MagicraftTGPlayer.get((EntityPlayer) event.entity) == null) {
			// This is how extended properties are registered using our convenient method from earlier
			MagicraftTGPlayer.register((EntityPlayer) event.entity);
			System.out.println(String.format("[MCTG] Player: %s",event.entity.getUniqueID().toString()));
			//System.out.println("[MCTG] ### " + ((EntityPlayer)event.entity).getGameProfile().getId().toString() + "\n");
			//System.out.println(String.format("[MCTG] Player: %s",((EntityPlayer)event.entity).getGameProfile().getName()));
			// That will call the constructor as well as cause the init() method
			// to be called automatically
			
		} 
	}
	
	/**
	 * When a player entity joins the world create the MagicraftTGPlayer extended properties.
	 * @param event
	 */
	@SubscribeEvent
	public void OnEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!(event.entity instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) event.entity;
		
		System.out.println(String.format("onEntityJoinWorld for %s", player.getDisplayNameString()));
		if(player.getGameProfile() != null) 
		{
			// Notify client of loaded ManaSources
			if(event.world.isRemote == false)
			{
				// Server side
				MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
				//System.out.println("Notify client of mana sources");
				int[] sources = mctg.getGlobalSourceNumbers();
				for(int src : sources)
				{
					//System.out.println(src);
				}
				IMessage msg = new MCTGManaPacket.MCTGManaMessage(MCTGPacketHandler.MANA_GLOBAL_SOURCE_SET, 
						sources[ManaColour.WHITE.ordinal()], 
						sources[ManaColour.BLUE.ordinal()], 
						sources[ManaColour.BLACK.ordinal()], 
						sources[ManaColour.RED.ordinal()], 
						sources[ManaColour.GREEN.ordinal()], 
						0);
				MCTGPacketHandler.net.sendTo(msg, (EntityPlayerMP) player);
			}
			else // Otherwise set the client's mana gui countdown
			{
				System.out.println("Setting mana gui countdown");
				FMLCommonClientHandler.resetManaGuiCountdown();
			}
		}	
	}
	
	
	/**
	 * When the player respawns make sure that the mana gui is shown.
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		EntityPlayer player = event.player;
		
		System.out.println(String.format("player respawn for %s", player.getDisplayNameString()));
		
		if(player.worldObj.isRemote)
		{
			FMLCommonClientHandler.resetManaGuiCountdown(); 
		}
	}
	
	/**
	 * When a player dies, kill all of their owned creatures.
	 * @param event
	 */
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event)
	{
		//System.out.println("Entity has died " + event.entity);
		if(event.entity instanceof EntityPlayer)
		{
			System.out.println("Player has died " + event.entity);
			MagicraftTGPlayer mctg = MagicraftTGPlayer.get((EntityPlayer)event.entity);
			mctg.removeAllControlled();
		}
	}
	
	
	
	
	/**
	 * When a player logs in, open the mana source screen.
	 * @param event
	 */
	/*@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if(event.entity.worldObj.isRemote == true && event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entity;
			System.out.println("Player has joined world.");
			//player.openGui(
			//			MagicraftTG.instance, 
			//			MCTGGuiHandler.MANA_SOURCE_GUI, 
			//			Minecraft.getMinecraft().theWorld, 
			//			(int) player.posX, (int) player.posY, (int) player.posZ);
			Minecraft.getMinecraft().displayGuiScreen(new ManaSourceGui());
		}
	}*/
	
	
	/*@SubscribeEvent
	public void onMouseEvent(MouseEvent event)
	{
		if(event.button == 1 && event.buttonstate == false)
		{
			System.out.println("MouseEvent button " + event.button + ": " + event.buttonstate);
			Minecraft.getMinecraft().displayGuiScreen(new ManaSourceGui());
		}
		
	}*/
}
