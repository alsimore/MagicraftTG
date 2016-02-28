package magicrafttg.blocks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.UUID;

import magicrafttg.MagicraftTG;
import magicrafttg.mana.ManaColor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.BlockEvent;

public class ManaBlock extends BasicBlock {
	/**
	 * UUID of the owning player.
	 */
	private UUID playerId;
	/**
	 * Name of the owning player. Assigned to the player by the server on login.
	 */
	private String playerName;
	/**
	 * Colour of the mana that this ManaBlock provides. Based on the biome in which it was placed.
	 */
	private ManaColor colour;
	
	public ManaBlock(UUID id, String name) {
		super("mana_block");
		this.playerId = id;
		this.playerName = name;
		
	}
	
	public ManaBlock() {
		this(null, "Player???");
	}
	
	
	
	/**
	 * Set the name and UUID of the player placing the block. Also set the colour of
	 * mana provided by the block based on the name of the biome in which it was placed.
	 * @param e The place event
	 */
	public void setPropertiesFromEvent(BlockEvent.PlaceEvent e) {
		setPlayerID(e.player.getUniqueID());
		setPlayerName(e.player.getName());
		
		int xCoord = (int) Math.floor(e.player.getPosition().getX());
        int zCoord = (int) Math.floor(e.player.getPosition().getZ());
        
    	String biome = Minecraft.getMinecraft().theWorld.getBiomeGenForCoords(new BlockPos(xCoord, 64, zCoord)).biomeName;
		setColour(getManaColourFromBiomeName(biome));
	}
	
	/**
	 * Returns the appropriate ManaColour from the biome String provided.
	 * @param biome
	 * @return
	 */
	private ManaColor getManaColourFromBiomeName(String biome) {
		
		// Hill, Extreme, and "M" versions of other biomes provide red mana
    	if(biome.toLowerCase().contains("hills") || biome.toLowerCase().contains("extreme") ||
    			biome.endsWith(" M")) {
    		 return ManaColor.RED;
    	}
    	else if(biome.toLowerCase().contains("plains") || biome.toLowerCase().contains("savanna")) {
			return ManaColor.WHITE;
		}
		else if(biome.toLowerCase().contains("forest") || biome.toLowerCase().contains("jungle")) {
			return ManaColor.GREEN;
		}
		else if(biome.toLowerCase().contains("island") || biome.toLowerCase().contains("beach")) {
			return ManaColor.BLUE;
		}
    	// River probably isn't appropriate for black but I can't think of many other
    	// black sources
		else if(biome.toLowerCase().contains("swamp") || biome.toLowerCase().contains("river")) {
			return ManaColor.BLACK;
		}
		else {
			return ManaColor.COLORLESS;
		}
	}
	
	public void setPlayerID(UUID id) {
		this.playerId = id;
	}
	/**
	 * Get the UUID of the owning player (the player who placed the block).
	 * @return owning player's UUID
	 */
	public UUID getPlayerID() {
		return this.playerId;
	}
	
	public void setPlayerName(String name) {
		this.playerName = name;
	}
	/**
	 * Get the name of the owning player (the player who placed the block).
	 * @return owning player's name
	 */
	public String getPlayerName() {
		return this.playerName;
	}
	/**
	 * Get the colour of mana provided by the block (based on the biome in which it is placed).
	 * @return the mana colour provided
	 */
	public ManaColor getColour() {
		return colour;
	}

	public void setColour(ManaColor colour) {
		this.colour = colour;
	}
}
