package magicrafttg.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import magicrafttg.ClientProxy;
import magicrafttg.entity.MagicraftTGPlayer;

public class KeyHandler
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onKeyInput(KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		KeyBinding[] keyBindings = ClientProxy.keyBindings;
		KeyBinding lShift = ClientProxy.leftShift;
		
		// checking inGameHasFocus prevents your keys from firing when the player is typing a chat message
		// NOTE that the KeyInputEvent will NOT be posted when a gui screen such as the inventory is open
		// so we cannot close an inventory screen from here; that should be done in the GUI itself
		if (mc.inGameHasFocus) {
			//System.out.println("Key event, shift pressed = " + lShift.isKeyDown());
			//if (lShift.isKeyDown()) {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				//System.out.println("LSHIFT down");
				for(int i = 0; i < keyBindings.length; ++i)
				{
					if(keyBindings[i].isPressed())
					{
						MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
						if(mctg.getSelectedIndex() == i)
						{
							mctg.setSelectedCreature(-1);
						}
						else
						{
							mctg.setSelectedCreature(i);
						}
					}
				}
			//}
		}
	}
}