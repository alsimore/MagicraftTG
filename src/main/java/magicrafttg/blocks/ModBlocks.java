package magicrafttg.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModBlocks {
	
	public static Block manaBlock;

    public static void createBlocks() {
    	GameRegistry.registerBlock(manaBlock = 
    			new ManaBlock().setLightLevel(0.5f), "mana_block");
    }
}
