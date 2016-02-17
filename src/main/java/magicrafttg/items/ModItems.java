package magicrafttg.items;

import magicrafttg.entity.EntityMCTGDireWolf;
import magicrafttg.entity.EntityMCTGSkeleton;
import magicrafttg.entity.EntityMCTGTroll;
import magicrafttg.entity.EntityMCTGZombie;
import magicrafttg.entity.ZZEntityMCTGZombie;
import magicrafttg.mana.ManaColour;
import net.minecraft.entity.passive.EntityVillager;
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

	public static void createItems() {
		GameRegistry.registerItem(summonItem = 
				new MCTGCreatureSummon("summon_item", new ManaColour[] {ManaColour.WHITE}, 
						new int[] {1}, 1, 1, 0, EntityVillager.class), "summon_item");
		
		GameRegistry.registerItem(summonZombie = 
				new MCTGCreatureSummon("mctg_zombie", new ManaColour[] {ManaColour.BLACK},
						new int[] {1}, 1, 1, 0, EntityMCTGZombie.class), "mctg_zombie");
		
		GameRegistry.registerItem(summonVillager = 
				new MCTGCreatureSummon("mctg_villager", new ManaColour[] {ManaColour.WHITE}, 
						new int[] {1}, 1, 1, 0, EntityVillager.class), "mctg_villager");
		
		GameRegistry.registerItem(summonSkeleton = 
				new MCTGCreatureSummon("mctg_skeleton", new ManaColour[] {ManaColour.BLACK, ManaColour.COLOURLESS},
						new int[] {1,1}, 1, 2, 0, EntityMCTGSkeleton.class), "mctg_skeleton");
		
		GameRegistry.registerItem(summonSkeletonArmrd = 
				new MCTGCreatureSummon("mctg_skeleton_armrd", new ManaColour[] {ManaColour.BLACK, ManaColour.COLOURLESS},
						new int[] {1,2}, 2, 2, 0, EntityMCTGSkeleton.class), "mctg_skeleton_armrd");
		
		GameRegistry.registerItem(summonDireWolf = 
				new MCTGCreatureSummon("mctg_dire_wolf", new ManaColour[] {ManaColour.BLACK},
						new int[] {2}, 2, 1, 0, EntityMCTGDireWolf.class), "mctg_dire_wolf");
		
		GameRegistry.registerItem(summonTroll = 
				new MCTGCreatureSummon("mctg_troll", new ManaColour[] {ManaColour.RED,ManaColour.COLOURLESS},
						new int[] {2,2}, 3, 3, 0, EntityMCTGTroll.class), "mctg_troll");
		
		GameRegistry.registerItem(fireball = 
				new MCTGSorceryInstant("mctg_fireball", new ManaColour[] {ManaColour.RED},
						new int[] {1}), "mctg_fireball");
		
		/*GameRegistry.registerItem(summonZombie2 = 
				new MCTGCreatureSummon("mctg_zombie2", new ManaColour[] {ManaColour.BLACK},
						new int[] {1}, 1, 1, 0, EntityMCTGZombie.class), "mctg_zombie2");*/
    }
}
