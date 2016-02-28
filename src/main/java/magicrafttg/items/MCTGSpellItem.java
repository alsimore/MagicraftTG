package magicrafttg.items;

import magicrafttg.mana.ManaColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class MCTGSpellItem extends Item {

	protected ManaColor[] costColour;
	protected int[] costAmt;
	
	public MCTGSpellItem(String unlocalizedName, ManaColor[] colour, int[] amt) {
		super();

		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(CreativeTabs.tabMisc);
		
		this.costColour = colour;
		this.costAmt = amt;
	}
	
	
	/**
	 * Implement the spell's effect. Should be overridden by subclasses.
	 * @param itemStackIn
	 * @param worldIn
	 * @param playerIn
	 * @return true if the spell was successfully cast, false otherwise.
	 */
	protected abstract boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn);

	@Override
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. 
     * Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
		if (!worldIn.isRemote) {
			boolean result = cast(itemStackIn, worldIn, playerIn);
			if(!result)
				System.out.println("[MCTG] Cast failed");
		}
        return itemStackIn;
    }

	/**
	 * Find the Y coord of the top block at given X/Z location.
	 * From http://www.minecraftforge.net/forum/index.php?topic=25687.0
	 * @param world
	 * @param pos
	 * @return
	 */
	protected int getTopBlock(World world, BlockPos pos) {
		
		while(world.getBlockState(pos).getBlock().isBlockSolid(world, pos, EnumFacing.DOWN))
		{
			pos.add(0, 1, 0);
		}
		
		return pos.getY();
		/*int y = -1, j;
		
		// Ensure block starts from the top of the world
		pos = pos.add(0, 255, 0);

		for (j = 255; j >= 10; j--) {
			if (world.getBlockState(pos).getBlock().isBlockSolid(world, pos, EnumFacing.DOWN)) {
				return j + 1;
			}
			
			pos = pos.add(0, -1, 0);
		}
		return y;*/
	}
}
