package magicrafttg.spell.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpellEventHandler 
{
	
	@SubscribeEvent
	public void onSpell(SpellEvent event)
	{
		System.out.println("Spell cast");
		event.effect.onAdd(event.world, event.target, event.caster);
	}
}
