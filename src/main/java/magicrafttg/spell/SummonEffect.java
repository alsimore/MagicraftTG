package magicrafttg.spell;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import magicrafttg.MagicraftTG;
import magicrafttg.mana.ManaColor;
import magicrafttg.player.MCTGPlayerProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SummonEffect implements ISpellEffect
{
	private Class summoned;
	private int power;
	private int toughness;
	
	
	public SummonEffect(int power, int toughness, Class summoned)
	{
		this.power = power;
		this.toughness = toughness;
		this.summoned = summoned;
	}
	
	
	@Override
	public void onAdd(World world, Entity target, EntityPlayer caster) 
	{
		System.out.println("Summoning");
		if(world.isRemote == false) { // If we are on the server side
			
			// Get the summoned class' constructor and create a new instance
			Constructor<?> construct = null;
			Entity newEntity = null;
			
			// Try finding the MCTG constructor
			try {
				construct = this.summoned.getConstructor(World.class, EntityPlayer.class, int.class, int.class);
			} catch (NoSuchMethodException e) {
				System.out.println("Not a Magicraft entity: " + this.summoned.getName());
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			
			
			// Try constructing a new instance
			try {
				if(construct != null) {
					if (this.summoned.getName().contains("MCTG")) {
						newEntity = (Entity) construct.newInstance(world, caster, this.power, this.toughness);
					} else {
						newEntity = (Entity) construct.newInstance(world);
					}
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			if(newEntity == null) {
				System.out.println("[MCTG] newEntity == null");
				return;
			}
			
			// South is 0 degrees (+Z direction), West is 90 degrees (-X direction), 
			// East is -90 degrees (+X direction). Therefore 45 degrees is +Z and -X displacement
			float angle = MathHelper.wrapAngleTo180_float(caster.rotationYaw);
			
			double xDisplace = -(3.0 * Math.sin(angle*Math.PI/180.0));
			double zDisplace = 3.0 * Math.cos(angle*Math.PI/180.0);
			double newX, newZ, newY;
			
			newX = caster.posX + xDisplace;
			newZ = caster.posZ + zDisplace;
			newY = caster.posY;
			
			// Check the  terrain height at that position
			newY = getTopBlock(world, new BlockPos(newX, newY, newZ));
			
			if(newY > 0) {
				MCTGPlayerProperties mctg = MCTGPlayerProperties.get(caster);
				
				
				newEntity.setPosition(newX, newY, newZ);
				((EntityLivingBase)newEntity).setHealth(MagicraftTG.HEALTH_PER_TOUGHNESS * this.toughness);
				
				((EntityLivingBase)newEntity).getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(MagicraftTG.HEALTH_PER_TOUGHNESS * this.toughness);
				((EntityLivingBase)newEntity).setHealth(MagicraftTG.HEALTH_PER_TOUGHNESS * this.toughness);
				//System.out.println("[MCTG] Health: " + ((EntityLivingBase)newEntity).getHealth());
				IAttributeInstance attr = ((EntityLivingBase)newEntity).getEntityAttribute(SharedMonsterAttributes.attackDamage);
				if(attr != null) {
					((EntityLivingBase)newEntity).getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(MagicraftTG.DAMAGE_PER_POWER * this.power);
				}
				else {
					((EntityLivingBase)newEntity).getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
					((EntityLivingBase)newEntity).getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(MagicraftTG.DAMAGE_PER_POWER * this.power);
				}
				
				
				System.out.println("[MCTG] " + newEntity.toString());
				System.out.println("[MCTG] " + newEntity.getUniqueID() + "(" + System.identityHashCode(newEntity) + ")");
				System.out.println("[MCTG] Damage: " + ((EntityLivingBase)newEntity).getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
				System.out.println("[MCTG] Health: " + ((EntityLivingBase)newEntity).getHealth());
				System.out.println("[MCTG] Summoned by: " + caster.getUniqueID().toString() + "\n");
				
				if(world.spawnEntityInWorld(newEntity))
				{
					//mctg.updateManaToClient(caster);
					mctg.addControlledCreature(newEntity);
					return;
				}
				else
				{
					System.out.println("[MCTG] Failed to spawn entity in world");
				}
				
			}
			else {
				System.out.println("[MCTG] Could not spawn entity at height " + newY);
			}
		}
	}

	@Override
	public void onRemove(World world, Entity target, EntityPlayer caster) {
		// Do nothing
	}

	@Override
	public void onTrigger(World world, Entity target, EntityPlayer caster) {
		// Do nothing
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
