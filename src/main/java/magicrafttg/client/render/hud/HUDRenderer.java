package magicrafttg.client.render.hud;

import magicrafttg.MagicraftTG;
import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.mana.ManaColor;
import magicrafttg.player.MCTGPlayerProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
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
    		MCTGPlayerProperties player = MCTGPlayerProperties.get(mc.thePlayer);
    		int[] currentMana = player.getCurrentMana();
    		int[] sources = player.getGlobalSourceNumbers();
    		mc.fontRendererObj.drawStringWithShadow(getBiomeString(), 1, 200, 0xffffff);
    		String str = String.format(
    					"White: %d(%d)  Blue: %d(%d)  Black: %d(%d)  Red: %d(%d)  Green: %d(%d)  Colourless: %d(%d)",
    					currentMana[ManaColor.WHITE.ordinal()], sources[ManaColor.WHITE.ordinal()], 
    					currentMana[ManaColor.BLUE.ordinal()], sources[ManaColor.BLUE.ordinal()], 
    					currentMana[ManaColor.BLACK.ordinal()], sources[ManaColor.BLACK.ordinal()], 
    					currentMana[ManaColor.RED.ordinal()], sources[ManaColor.RED.ordinal()], 
    					currentMana[ManaColor.GREEN.ordinal()], sources[ManaColor.GREEN.ordinal()], 
    					currentMana[ManaColor.COLORLESS.ordinal()], sources[ManaColor.COLORLESS.ordinal()]
    				);
    		
    		mc.fontRendererObj.drawStringWithShadow(str, 1, 208, 0xffffff);
    		
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
    
    private void renderControlledCreatures(MCTGPlayerProperties player)
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
    		if (creature instanceof EntityMCTGBase)
    		{
    			int power = ((EntityMCTGBase) creature).getPower();
    			int toughness = ((EntityMCTGBase) creature).getToughness();
    			
    			//System.out.println(((EntityMCTGBase)creature).getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
    			String str = (i+1) + ": " + creature.getName() + "  " + 
    					//((EntityMCTGBase)creature).getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue() +
    					power + "/" + toughness + " (" + ((EntityLivingBase)creature).getHealth() + ")";
    			
    			if(creature.equals(selected))
        			mc.fontRendererObj.drawStringWithShadow(str, 1, startY, 0xff0000);
        		else
        			mc.fontRendererObj.drawStringWithShadow(str, 1, startY, 0xffffff); 
    		}
    		
    	}
    }
}
