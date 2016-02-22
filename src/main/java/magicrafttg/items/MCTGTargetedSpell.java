package magicrafttg.items;

import java.util.function.BiPredicate;

import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.mana.ManaColour;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MCTGTargetedSpell extends MCTGSpellItem {

	protected Entity target;
	protected BiPredicate<Entity, EntityPlayer> effect;
	
	public MCTGTargetedSpell(String unlocalizedName, ManaColour[] colour, int[] amt, BiPredicate<Entity, EntityPlayer> effect) {
		super(unlocalizedName, colour, amt);
		this.effect = effect;
	}

	@Override
	protected boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		// TODO: Need to ray trace or something as this is run server side and Minecraft isn't defined
		this.target = Minecraft.getMinecraft().objectMouseOver.entityHit;
		// Make sure the target is of the correct class
		if(this.target instanceof EntityMCTGBase)
		{
			return effect.test(this.target, playerIn);
		}
		else
		{
			return false;
		}
	}

	
}
