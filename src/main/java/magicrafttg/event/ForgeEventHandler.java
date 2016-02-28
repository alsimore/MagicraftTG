package magicrafttg.event;

import java.util.List;

import org.lwjgl.opengl.GL11;

import magicrafttg.MagicraftTG;
import magicrafttg.client.gui.ManaSourceGui;
import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.entity.MCTGPlayerProperties;
import magicrafttg.items.ModItems;
import magicrafttg.mana.ManaColor;
import magicrafttg.network.MCTGGuiHandler;
import magicrafttg.network.MCTGManaPacket;
import magicrafttg.network.MCTGPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
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
import net.minecraftforge.client.event.MouseEvent;

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
		if (event.entity instanceof EntityPlayer && MCTGPlayerProperties.get((EntityPlayer) event.entity) == null) {
			// This is how extended properties are registered using our convenient method from earlier
			MCTGPlayerProperties.register((EntityPlayer) event.entity);
			
			//System.out.println("Create MCTG " + event.entity + "\n" + event.entity.getUniqueID());
			
			// Add datawatcher objects, mana sources
			//DataWatcher dw = event.entity.getDataWatcher();
			// Each object represents the number of sources of a particular colour
			//dw.addObject(20, 0); // White
			//dw.addObject(21, 0); // Blue
			//dw.addObject(22, 0); // Black
			//dw.addObject(23, 0); // Red
			//dw.addObject(24, 0); // Green
		} 
		
		if (event.entity instanceof EntityMCTGBase)
		{
			// Add datawatcher objects, owner and controller
			DataWatcher dw = event.entity.getDataWatcher();
			dw.addObject(20, ""); // owner UUID as string
			dw.addObject(21, ""); // controller UUID as string
			dw.addObject(22, -1); // owner entityId as int
			dw.addObject(23, -1); // controller entityId as int
		}
	}
	
	/**
	 * When a player entity joins the world create the MagicraftTGPlayer extended properties.
	 * @param event
	 */
	@SubscribeEvent
	public void OnEntityJoinWorld(EntityJoinWorldEvent event) 
	{
		if (!(event.entity instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) event.entity;
		player.replaceItemInInventory(0, new ItemStack(ModItems.manaPicker));
		
		//System.out.println("Player joined world " + player.getUniqueID());
		// Request mana sources from server
		if(event.world.isRemote == true)
		{
			MCTGPlayerProperties.get(player).updateManaFromServer();
		}
		/*if (!(event.entity instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer player = (EntityPlayer) event.entity;
		MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
		
		System.out.println(String.format("onEntityJoinWorld for %s", player.getDisplayNameString()));
		System.out.println(event.world.toString());
		if(player.getGameProfile() != null) 
		{
			// Notify client of loaded ManaSources
			if(event.world.isRemote == false)
			{
				// Server side
				
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
				System.out.println(player.getDisplayNameString() + " has joined world, resetting manaGuiCountdown");
				mctg.manaGuiCountdown = MCTGGuiHandler.MANA_GUI_COUNTDOWN;
			}
		}	*/
	}
	
	
	/*@SubscribeEvent
	public void onRenderWorldPre(RenderWorldEvent.Post event)
	{
		System.out.println("onREnderWorld");
		Minecraft mc = Minecraft.getMinecraft();
		MovingObjectPosition objectMouseOver = mc.objectMouseOver;
		// makes a variable for where you look
		System.out.println(objectMouseOver);
		if(mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
		    // checks if you hit an Entity
		    Entity Target = objectMouseOver.entityHit;
		    // make a variable: Target(just so I can use it easier) AND makes it "select the Entity"
		    if(Target instanceof EntityMCTGBase) // or whatever you want recognised
		        System.out.println("Mouse over base");
		    }
		}
	}*/
	
	/*@SubscribeEvent
	public void onRenderLiving(RenderLivingEvent.Post e)
	{
		Minecraft mc = Minecraft.getMinecraft();
		MovingObjectPosition objectMouseOver = mc.objectMouseOver;
		// makes a variable for where you look
		//System.out.println(objectMouseOver);
		if(mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
		    // checks if you hit an Entity
			//System.out.println("mouseover");
		    Entity Target = objectMouseOver.entityHit;
		    // make a variable: Target(just so I can use it easier) AND makes it "select the Entity"
		    if(Target instanceof EntityMCTGBase) { // or whatever you want recognised
		        System.out.println("Mouse over base");
		    }
		}
	}*/
	
	
	/**
	 * When the player respawns make sure that the mana gui is shown.
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		/*EntityPlayer player = event.player;
		MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
		
		System.out.println(String.format("player respawn for %s", player.getDisplayNameString()));
		
		if(player.worldObj.isRemote)
		{
			System.out.println(player.getDisplayNameString() + " has respawned, resetting manaGuiCountdown");
			mctg.manaGuiCountdown = MCTGGuiHandler.MANA_GUI_COUNTDOWN;
		}*/
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
			MCTGPlayerProperties mctg = MCTGPlayerProperties.get((EntityPlayer)event.entity);
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
	@SideOnly(Side.CLIENT)
	public void onMouseEvent(MouseEvent event)
	{
		if(event.button == 1 && event.buttonstate == false)
		{
			System.out.println("MouseEvent button " + event.button + ": " + event.buttonstate);
			//Minecraft.getMinecraft().displayGuiScreen(new ManaSourceGui());
		}
		
	}*/
}
