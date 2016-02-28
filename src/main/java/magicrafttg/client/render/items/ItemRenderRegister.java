package magicrafttg.client.render.items;

import magicrafttg.MagicraftTG;
import magicrafttg.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class ItemRenderRegister {

	public static String modid = MagicraftTG.MODID;

	public static void reg(Item item) {
		String unlocName = item.getUnlocalizedName();
		String sub = unlocName.substring(5);
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	    .register(item, 0, new ModelResourceLocation(modid + ":" + sub, "inventory"));
	}
	
	public static void registerItemRenderer() {
		//reg(ModItems.summonItem);
		reg(ModItems.manaPicker);
		reg(ModItems.summonZombie);
		reg(ModItems.summonTroll);
		reg(ModItems.summonDireWolf);
		reg(ModItems.fireball);
		reg(ModItems.mindControl);
		reg(ModItems.swamp);
    }
}
