package magicrafttg.spell;

import magicrafttg.mana.ManaColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

/**
 * Basic parent class for land and spells.
 * @author Adam
 *
 */
public abstract class Card extends Item
{
	protected ManaColor color;
	protected ISpellEffect effect;
	
	public Card(String unlocalizedName, ManaColor color, ISpellEffect effect)
	{
		super();
		
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(CreativeTabs.tabMisc);
		
		this.color = color;
		this.effect = effect;
	}
	
	
	/**
	 * Implement the card's effect. Should be overridden by subclasses.
	 * @param itemStackIn
	 * @param worldIn
	 * @param playerIn
	 * @return true if the spell was successfully cast, false otherwise.
	 */
	protected abstract boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn);

	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. 
     * Args: itemStack, world, entityPlayer
     */
	@Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
		System.out.println("Card.onRightClick");
		if (!worldIn.isRemote) 
		{
			boolean result = cast(itemStackIn, worldIn, playerIn);
			if(!result)
			{
				IChatComponent message = new ChatComponentText("Cast failed");
				playerIn.addChatMessage(message);
			}
		}
		itemStackIn.stackSize -= 1;
        return itemStackIn;
    }
}
