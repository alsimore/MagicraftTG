package magicrafttg.items;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.entity.EntityMCTGDireWolf;
import magicrafttg.entity.EntityMCTGSkeleton;
import magicrafttg.entity.EntityMCTGTroll;
import magicrafttg.entity.EntityMCTGZombie;
import magicrafttg.entity.ZZEntityMCTGZombie;
import magicrafttg.mana.ManaColour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
	
	

	public static void createItems() {
		//-------------------------------------------------------------------------------
		// Spell predicates
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
		// Item registry
		//GameRegistry.registerItem(summonItem = 
		//		new MCTGCreatureSummon("summon_item", new ManaColour[] {ManaColour.WHITE}, 
		//				new int[] {1}, 1, 1, 0, EntityVillager.class), "summon_item");
		
		GameRegistry.registerItem(summonZombie = 
				new MCTGCreatureSummon("zombie", new ManaColour[] {ManaColour.BLACK},
						new int[] {1}, 1, 1, 0, EntityMCTGZombie.class), "zombie");
		
		GameRegistry.registerItem(summonVillager = 
				new MCTGCreatureSummon("villager", new ManaColour[] {ManaColour.WHITE}, 
						new int[] {1}, 1, 1, 0, EntityVillager.class), "villager");
		
		GameRegistry.registerItem(summonSkeleton = 
				new MCTGCreatureSummon("skeleton", new ManaColour[] {ManaColour.BLACK, ManaColour.COLOURLESS},
						new int[] {1,1}, 1, 2, 0, EntityMCTGSkeleton.class), "skeleton");
		
		GameRegistry.registerItem(summonSkeletonArmrd = 
				new MCTGCreatureSummon("skeleton_armrd", new ManaColour[] {ManaColour.BLACK, ManaColour.COLOURLESS},
						new int[] {1,2}, 2, 2, 0, EntityMCTGSkeleton.class), "skeleton_armrd");
		
		GameRegistry.registerItem(summonDireWolf = 
				new MCTGCreatureSummon("dire_wolf", new ManaColour[] {ManaColour.BLACK},
						new int[] {2}, 2, 1, 0, EntityMCTGDireWolf.class), "dire_wolf");
		
		GameRegistry.registerItem(summonTroll = 
				new MCTGCreatureSummon("troll", new ManaColour[] {ManaColour.RED,ManaColour.COLOURLESS},
						new int[] {2,2}, 3, 3, 0, EntityMCTGTroll.class), "troll");
		
		GameRegistry.registerItem(fireball = 
				new MCTGSorceryInstant("fireball", new ManaColour[] {ManaColour.RED},
						new int[] {1}), "fireball");
		
		
		GameRegistry.registerItem(mindControl =
				new MCTGTargetedSpell(
						"mind_control", // name
						new ManaColour[] {ManaColour.COLOURLESS, ManaColour.BLUE}, // required colours 
						new int[] {3,2}, // cost
						predMindControl), // effect
				"mind_control");
		
		
		GameRegistry.registerItem(manaPicker = new MCTGManaItem("mana_picker"), "mana_picker");
		
		/*GameRegistry.registerItem(summonZombie2 = 
				new MCTGCreatureSummon("mctg_zombie2", new ManaColour[] {ManaColour.BLACK},
						new int[] {1}, 1, 1, 0, EntityMCTGZombie.class), "mctg_zombie2");*/
		
    }
	
	
	
}
