package magicrafttg.spell;

import magicrafttg.mana.ManaColor;
import magicrafttg.player.MCTGPlayerProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BasicLand extends Card
{
	/**
	 * The SpellEffect is not used and doesn't need to be defined.
	 * @param color
	 * @param effect
	 */
	public BasicLand(String unlocalizedName, ManaColor color, ISpellEffect effect) 
	{
		super(unlocalizedName, color, effect);
	}

	@Override
	protected boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) 
	{
		System.out.println("BasicLand.cast");
		// Haven't used the spell effect as basic lands are simple.
		MCTGPlayerProperties prop = MCTGPlayerProperties.get(playerIn);
		prop.addSource(this.color);
		return true;
	}

}
