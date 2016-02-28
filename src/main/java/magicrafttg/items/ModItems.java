package magicrafttg.items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import magicrafttg.MagicraftTG;
import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.entity.EntityMCTGDireWolf;
import magicrafttg.entity.EntityMCTGSkeleton;
import magicrafttg.entity.EntityMCTGTroll;
import magicrafttg.entity.EntityMCTGZombie;
import magicrafttg.entity.MCTGPlayerProperties;
import magicrafttg.entity.ZZEntityMCTGZombie;
import magicrafttg.mana.ManaColor;
import magicrafttg.spell.BasicLand;
import magicrafttg.spell.ISpellEffect;
import magicrafttg.spell.Spell;
import magicrafttg.spell.SummonEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModItems {
	
	public static Item summonItem;
	public static Item summonZombie;
	public static Item summonSkeleton;
	public static Item summonVillager;
	public static Item summonSkeletonArmrd;
	public static Item summonDireWolf;
	public static Item summonTroll;
	public static Item fireball;
	public static Item summonZombie2;
	public static Item manaPicker;
	public static Item mindControl;
	
	public static Item swamp;
	

	public static void createItems() {
		//-------------------------------------------------------------------------------
		// Spell predicates
		// TODO: Move these effects to separate classes
		final BiPredicate<Entity, EntityPlayer> predMindControl = new BiPredicate<Entity, EntityPlayer>() { 
			@Override
			public boolean test(Entity target, EntityPlayer caster) {
				// Set the controller of the target to the caster
				if( caster.getUniqueID().equals(((EntityMCTGBase)target).getControllerUUID()) )
				{
					// Caster already controls target
					return false;
				}
				else
				{
					// Set target's controller to caster
					System.out.println("Before cast " + ((EntityMCTGBase)target).getControllerUUID());
					((EntityMCTGBase)target).setControllerEntity(caster);
					System.out.println("After cast " + ((EntityMCTGBase)target).getControllerUUID());
					((EntityMCTGBase)target).updateDataWatcher();
					return true;
				}
			}
		};
		//-------------------------------------------------------------------------------	
		final ISpellEffect mindControlEffect = new ISpellEffect() {

			@Override
			public void onAdd(World world, Entity target, EntityPlayer caster) {
				// Set the controller of the target to the caster
				if( caster.getUniqueID().equals(((EntityMCTGBase)target).getControllerUUID()) )
				{
					// Caster already controls target
				}
				else
				{
					// Set target's controller to caster
					System.out.println("Before cast " + ((EntityMCTGBase)target).getControllerUUID());
					((EntityMCTGBase)target).setControllerEntity(caster);
					System.out.println("After cast " + ((EntityMCTGBase)target).getControllerUUID());
					((EntityMCTGBase)target).updateDataWatcher();
				}
			}

			@Override
			public void onRemove(World world, Entity target, EntityPlayer caster) {
				// Change the controller back to the owner / caster
			}

			@Override
			public void onTrigger(World world, Entity target, EntityPlayer caster) {
				// Do nothing
			}
		};
		//-------------------------------------------------------------------------------
		final ISpellEffect fireballEffect = new ISpellEffect() {

			@Override
			public void onAdd(World world, Entity target, EntityPlayer caster) {
				// Test, cast fireball
				double d1 = 1.0D;
		        Vec3 look = caster.getLook(1.0F);
		        
				EntityLargeFireball fireball = new EntityLargeFireball(world, 
						caster, look.xCoord, look.yCoord, look.zCoord);
		        fireball.explosionPower = 2;
		        fireball.posX = caster.posX + look.xCoord * d1;
		        fireball.posY = caster.posY + (double)(caster.height / 2.0F) + 0.5D;
		        fireball.posZ = caster.posZ + look.zCoord * d1;
		        fireball.accelerationX = look.xCoord;
		        fireball.accelerationY = look.yCoord;
		        fireball.accelerationZ = look.zCoord;
		        
		        world.spawnEntityInWorld(fireball);
			}

			@Override
			public void onRemove(World world, Entity target, EntityPlayer caster) {
				// Do nothing
			}

			@Override
			public void onTrigger(World world, Entity target, EntityPlayer caster) {
				// Do nothing
			}
		};
		
		//-------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------
		
		
		
		
		
		
		
		
		GameRegistry.registerItem(summonZombie = 
				new Spell(
						"zombie",
						new ManaColor[] {ManaColor.BLACK},
						new int[] {1}, 
						new SummonEffect(1, 1, 
								EntityMCTGZombie.class),
						false
				), 
				"zombie"
		);
		
		/*GameRegistry.registerItem(summonZombie = 
				new MCTGCreatureSummon("zombie", new ManaColor[] {ManaColor.BLACK},
						new int[] {1}, 1, 1, 0, EntityMCTGZombie.class), "zombie");*/
		
		GameRegistry.registerItem(summonVillager = 
				new MCTGCreatureSummon("villager", new ManaColor[] {ManaColor.WHITE}, 
						new int[] {1}, 1, 1, 0, EntityVillager.class), "villager");
		
		GameRegistry.registerItem(summonSkeleton = 
				new MCTGCreatureSummon("skeleton", new ManaColor[] {ManaColor.BLACK, ManaColor.COLORLESS},
						new int[] {1,1}, 1, 2, 0, EntityMCTGSkeleton.class), "skeleton");
		
		GameRegistry.registerItem(summonSkeletonArmrd = 
				new MCTGCreatureSummon("skeleton_armrd", new ManaColor[] {ManaColor.BLACK, ManaColor.COLORLESS},
						new int[] {1,2}, 2, 2, 0, EntityMCTGSkeleton.class), "skeleton_armrd");
		
		GameRegistry.registerItem(summonDireWolf = 
				new Spell(
						"dire_wolf",
						new ManaColor[] {ManaColor.BLACK},
						new int[] {2}, 
						new SummonEffect(2, 2, 
								EntityMCTGDireWolf.class),
						false
				), 
				"dire_wolf"
		);
		
		/*GameRegistry.registerItem(summonDireWolf = 
				new MCTGCreatureSummon("dire_wolf", new ManaColor[] {ManaColor.BLACK},
						new int[] {2}, 2, 1, 0, EntityMCTGDireWolf.class), "dire_wolf");*/
		
		GameRegistry.registerItem(summonTroll = 
				new Spell(
						"troll",
						new ManaColor[] {ManaColor.RED, ManaColor.COLORLESS},
						new int[] {2, 2},
						new SummonEffect(3, 3, EntityMCTGTroll.class),
						false
				), 
				"troll"
		);
		
		/*GameRegistry.registerItem(summonTroll = 
				new MCTGCreatureSummon("troll", new ManaColor[] {ManaColor.RED,ManaColor.COLORLESS},
						new int[] {2,2}, 3, 3, 0, EntityMCTGTroll.class), "troll");*/
		
		GameRegistry.registerItem(fireball = 
				new MCTGSorceryInstant("fireball", new ManaColor[] {ManaColor.RED},
						new int[] {1}), "fireball");
		
		
		/*GameRegistry.registerItem(mindControl =
				new MCTGTargetedSpell(
						"mind_control", // name
						new ManaColor[] {ManaColor.COLORLESS, ManaColor.BLUE}, // required colours 
						new int[] {3,2}, // cost
						predMindControl), // effect
				"mind_control");*/
		
		GameRegistry.registerItem(mindControl = 
				new Spell(
						"mind_control",
						new ManaColor[] {ManaColor.COLORLESS, ManaColor.BLUE}, 
						new int[] {3,2},
						mindControlEffect,
						true
						),
				"mind_control"
				);
		
		
		GameRegistry.registerItem(manaPicker = new MCTGManaItem("mana_picker"), "mana_picker");
		
		
		GameRegistry.registerItem(swamp = new BasicLand("swamp", ManaColor.BLACK, null), "swamp");
		
    }
	
	
	
}
