package magicrafttg.spell;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Describes a spell effect that will apply to some entity. May be an instant effect (like damage)
 * or an ongoing effect (e.g. through an enchantment).
 * @author Adam
 *
 */
public interface ISpellEffect 
{
	/**
	 * Called when the spell effect is first added / the spell is cast.
	 * @param world
	 * @param target
	 * @param caster
	 */
	public void onAdd(World world, Entity target, EntityPlayer caster);
	
	/**
	 * Called when the spell effect is removed (e.g. when an enchantment is removed).
	 * Should reverse the effects of onAdd.
	 * @param world
	 * @param target
	 * @param caster
	 */
	public void onRemove(World world, Entity target, EntityPlayer caster);
	
	/**
	 * Called when the spell effect is triggered / used. For example if some enchantment adds +1/+1
	 * every time some condition is met (e.g. a white creature is cast)
	 * @param world
	 * @param target
	 * @param caster
	 */
	public void onTrigger(World world, Entity target, EntityPlayer caster);
}
