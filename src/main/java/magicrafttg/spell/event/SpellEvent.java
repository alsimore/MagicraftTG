package magicrafttg.spell.event;

import magicrafttg.spell.ISpellEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SpellEvent extends Event
{
	public ISpellEffect effect;
	public World world;
	public Entity target;
	public EntityPlayer caster;
	
	public SpellEvent(ISpellEffect effect, World world, Entity target, EntityPlayer caster)
	{
		super();
		this.world = world;
		this.target = target;
		this.caster = caster;
		this.effect = effect;
	}
}
