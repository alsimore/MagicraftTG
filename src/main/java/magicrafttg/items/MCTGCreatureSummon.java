package magicrafttg.items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import magicrafttg.MagicraftTG;
import magicrafttg.entity.MagicraftTGPlayer;
import magicrafttg.mana.ManaColour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MCTGCreatureSummon extends MCTGSpellItem {

	private int power;
	private int toughness;
	private Class summoned;
	private int armour;
	
	
	public MCTGCreatureSummon(String unlocalizedName, ManaColour[] costColour, int[] costAmt,
			int power, int toughness, int armour, Class summoned) {
		super(unlocalizedName, costColour, costAmt);
		this.power = power;
		this.toughness = toughness;
		this.armour = armour;
		this.summoned = summoned;
	}

	@Override
	/**
	 * Add the summoned entity into the world. Only done on the server side.
	 */
	protected boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if(!worldIn.isRemote) { // If we are on the server side
			
			// Get the summoned class's constructor and create a new instance
			Constructor<?> construct = null;
			Entity newEntity = null;
			System.out.println("Name " + this.summoned.getName());
			try {
				if (this.summoned.getName().contains("MCTG")) {
					construct = this.summoned.getConstructor(World.class, EntityPlayer.class, int.class, int.class);
				//} else if (this.summoned.getName().contains("MCTG")) {
				//	construct = this.summoned.getConstructor(World.class, EntityPlayer.class);
				} else {
					//construct = this.summoned.getConstructor(World.class);
					System.out.println("Not a Magicraft entity: " + this.summoned.getName());
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				if(construct != null) {
					if (this.summoned.getName().contains("MCTG")) {
						newEntity = (Entity) construct.newInstance(worldIn, playerIn, this.power, this.toughness);
					//} else if (this.summoned.getName().contains("MCTG")) {
					//	newEntity = (Entity) construct.newInstance(worldIn, playerIn);
					} else {
						newEntity = (Entity) construct.newInstance(worldIn);
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
				return false;
			}
			
			// South is 0 degrees (+Z direction), West is 90 degrees (-X direction), 
			// East is -90 degrees (+X direction). Therefore 45 degrees is +Z and -X displacement
			float angle = MathHelper.wrapAngleTo180_float(playerIn.rotationYaw);
			
			double xDisplace = -(3.0 * Math.sin(angle*Math.PI/180.0));
			double zDisplace = 3.0 * Math.cos(angle*Math.PI/180.0);
			double newX, newZ, newY;
			
			newX = playerIn.posX + xDisplace;
			newZ = playerIn.posZ + zDisplace;
			newY = playerIn.posY;
			
			// Check the  terrain height at that position
			newY = getTopBlock(worldIn, new BlockPos(newX, newY, newZ));
			if(newY > 0) {
				MagicraftTGPlayer mctg = MagicraftTGPlayer.get(playerIn);
				
				if(mctg.consumeMana(this.costColour, this.costAmt)) {
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
					System.out.println("[MCTG] Summoned by: " + playerIn.getUniqueID().toString() + "\n");
					
					if(worldIn.spawnEntityInWorld(newEntity))
					{
						mctg.updateManaToClient(playerIn);
						mctg.addControlledCreature(newEntity);
						return true;
					}
					else
					{
						System.out.println("[MCTG] Failed to spawn entity in world");
					}
				}
			}
			else {
				System.out.println("[MCTG] Could not spawn entity at height " + newY);
			}
		}
		return false;
	}
	
	
}
/*
protected void applyEntityAttributes()
{
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);

    if (this.isTamed())
    {
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
    }
    else
    {
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
    }

    this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
}
*/