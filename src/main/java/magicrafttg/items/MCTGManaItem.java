package magicrafttg.items;

import magicrafttg.MagicraftTG;
import magicrafttg.network.MCTGGuiHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MCTGManaItem extends Item {

	
	public MCTGManaItem(String unlocalizedName)
	{
		super();

		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		System.out.println("Mana item right click");
		playerIn.openGui(MagicraftTG.instance, 
				MCTGGuiHandler.MANA_SOURCE_GUI, 
				worldIn, 
				playerIn.getEntityId(), 
				MathHelper.floor_double(playerIn.posY), 
				MathHelper.floor_double(playerIn.posZ));
		return itemStackIn;
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
    {
		// Cannot drop the mana picker
        return false;
    }
}
