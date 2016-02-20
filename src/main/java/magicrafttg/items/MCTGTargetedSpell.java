package magicrafttg.items;

import magicrafttg.mana.ManaColour;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MCTGTargetedSpell extends MCTGSpellItem {

	protected Entity target;
	
	public MCTGTargetedSpell(String unlocalizedName, ManaColour[] colour, int[] amt) {
		super(unlocalizedName, colour, amt);
	}

	@Override
	protected boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		
		this.target = Minecraft.getMinecraft().objectMouseOver.entityHit;
		
		return true;
	}

	
}
