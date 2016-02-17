package magicrafttg.network;

import magicrafttg.client.gui.ManaSourceGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class MCTGGuiHandler implements IGuiHandler {

	public static final int MANA_SOURCE_GUI = 0;
	
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    	//System.out.println("return ManaSourceGui");
    	if (ID == MANA_SOURCE_GUI)
            return new ManaSourceGui();
        return null;
    }
}