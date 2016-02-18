package magicrafttg.items;

import magicrafttg.entity.MagicraftTGPlayer;
import magicrafttg.mana.ManaColour;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MCTGSorceryInstant extends MCTGSpellItem {

	public MCTGSorceryInstant(String unlocalizedName, ManaColour[] colour, int[] amt) {
		super(unlocalizedName, colour, amt);
	}

	@Override
	protected boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!worldIn.isRemote) {
			// Test, cast fireball
			double d1 = 1.0D;
	        Vec3 vec3 = playerIn.getLook(1.0F);
	        //System.out.println(String.format("[MCTG] Look: %f %f %f", 
	        //		vec3.xCoord, vec3.yCoord, vec3.zCoord));
			EntityLargeFireball fireball = new EntityLargeFireball(worldIn, 
					playerIn, vec3.xCoord, vec3.yCoord, vec3.zCoord);
	        fireball.explosionPower = 1;
	        fireball.posX = playerIn.posX + vec3.xCoord * d1;
	        fireball.posY = playerIn.posY + (double)(playerIn.height / 2.0F) + 0.5D;
	        fireball.posZ = playerIn.posZ + vec3.zCoord * d1;
	        fireball.accelerationX = vec3.xCoord;
	        fireball.accelerationY = vec3.yCoord;
	        fireball.accelerationZ = vec3.zCoord;
	        
	        MagicraftTGPlayer mctg = MagicraftTGPlayer.get(playerIn);
			if(mctg.consumeMana(this.costColour, this.costAmt)) {
				worldIn.spawnEntityInWorld(fireball);
				mctg.updateManaToClient(playerIn);
				return true;
			}
		}
		return false;
	}

}
