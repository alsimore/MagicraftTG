package magicrafttg.client.render.hud;

import magicrafttg.entity.MagicraftTGPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUDRenderer {

	public static HUDRenderer instance = new HUDRenderer();
	private static Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * Event fired at various points during the GUI rendering process.
	 * We render anything that need to be rendered onto the HUD in this method.
	 * @param event
	 */
    @SubscribeEvent
    public void RenderGameOverlayEvent(RenderGameOverlayEvent event)
    {
    	if(event.type == RenderGameOverlayEvent.ElementType.TEXT)
    	{
    		MagicraftTGPlayer player = MagicraftTGPlayer.get(mc.thePlayer);
    		mc.fontRendererObj.drawStringWithShadow(getBiomeString(), 1, 200, 0xffffff);
    		String currentMana = String.format(
    				"White: %2d    Blue: %2d    Black: %2d    Red: %2d    Green: %2d    Colourless: %2d",
    				player.getWhiteMana(), player.getBlueMana(), 
    				player.getBlackMana(), player.getRedMana(), 
    				player.getGreenMana(), player.getColourlessMana());
    		mc.fontRendererObj.drawStringWithShadow(currentMana, 1, 208, 0xffffff);
    		
    		mc.fontRendererObj.drawStringWithShadow(mc.thePlayer.getName(), 1, 1, 0xffffff);
    		
    		renderControlledCreatures(player);
    	}
    }
    
    private static String getBiomeString()
    {
    	int xCoord = (int) Math.floor(mc.thePlayer.posX);
        int zCoord = (int) Math.floor(mc.thePlayer.posZ);
        
    	return mc.theWorld.getBiomeGenForCoords(new BlockPos(xCoord, 64, zCoord)).biomeName;
    }
    
    private void renderControlledCreatures(MagicraftTGPlayer player)
    {
    	int startY = 12;
    	//System.out.println("Rendering controlled creatures");
    	mc.fontRendererObj.drawStringWithShadow("Controlled: " + player.numControlledCreatures(), 
    			1, startY, 0xffffff);
    	for(int i = 0; i < player.numControlledCreatures(); ++i)
    	{
    		startY += 10;
    		//System.out.println("Creature " + i + ": " + player.getControlledEntity(i));
    		Entity creature = player.getControlledEntity(i);
    		Entity selected = player.getSelectedCreature();
    		if(creature.equals(selected))
    			mc.fontRendererObj.drawStringWithShadow((i+1) + ": " + creature.getName(), 1, startY, 0xff0000);
    		else
    			mc.fontRendererObj.drawStringWithShadow((i+1) + ": " + creature.getName(), 1, startY, 0xffffff); 
    	}
    }
}
