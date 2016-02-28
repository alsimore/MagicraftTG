package magicrafttg.spell;

import magicrafttg.mana.ManaColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Spell extends Card
{

	public Spell(String unlocalizedName, ManaColor color, ISpellEffect effect) {
		super(unlocalizedName, color, effect);
	}

	@Override
	protected boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		effect.onAdd(worldIn, null, playerIn);
		return true;
	}

}
