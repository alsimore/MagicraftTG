package magicrafttg.spell.event;

import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.player.MCTGPlayerProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpellEventHandler 
{
	
	@SubscribeEvent
	public void onSpell(SpellEvent event)
	{
		System.out.println("Spell cast");
		if(event.effect.isOneTime())
		{
			System.out.println("One time");
			event.effect.onAdd(event.world, event.target, event.caster);
		}
		else if(event.target instanceof EntityMCTGBase)
		{
			System.out.println("MCTGBase target");
			EntityMCTGBase castedTarget = (EntityMCTGBase)event.target;
			castedTarget.addSpellEffect(event.effect, event.world, event.target, event.caster);
		}
		else if(event.target instanceof EntityPlayer)
		{
			System.out.println("Player target");
			MCTGPlayerProperties prop = MCTGPlayerProperties.get((EntityPlayer) event.target);
			if(prop != null)
			{
				
			}
		}
	}
}
