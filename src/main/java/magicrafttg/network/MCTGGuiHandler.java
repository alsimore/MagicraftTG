package magicrafttg.network;

import magicrafttg.client.gui.ManaSourceGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class MCTGGuiHandler implements IGuiHandler {

	public static final int MANA_SOURCE_GUI = 0;
	public static final int MANA_GUI_COUNTDOWN = 60;
	
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    	//return new DummyContainer();
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    	//System.out.println("return ManaSourceGui");
    	if (ID == MANA_SOURCE_GUI)
            return new ManaSourceGui();
        return null;
    }
    
    
    public class DummyContainer extends Container
    {

		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			// TODO Auto-generated method stub
			return false;
		}
    	
    }
}