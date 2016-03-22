package magicrafttg.spell.spells;

import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.player.MCTGPlayerProperties;
import magicrafttg.spell.ISpellEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class MindControl implements ISpellEffect
{

	@Override
	public void onAdd(World world, Entity target, EntityPlayer caster) 
	{
		// Set the creature's controller to the spell's caster
		try
		{
			EntityMCTGBase castTarget = (EntityMCTGBase)target;
			
			if( caster.getUniqueID().equals(castTarget.getControllerUUID()) )
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
				
				MCTGPlayerProperties props = MCTGPlayerProperties.get(caster);
				props.addControlledCreature(target);
			}
			
		}
		catch(ClassCastException e)
		{
			caster.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + 
					target.getName() + " is not a valid spell target"));
		}
	}

	@Override
	public void onRemove(World world, Entity target, EntityPlayer caster) 
	{
		
	}

	@Override
	public void onTrigger(World world, Entity target, EntityPlayer caster) 
	{
		// Do nothing
	}

	@Override
	public boolean isOneTime() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValidTarget(Entity target) 
	{
		return target instanceof EntityMCTGBase;
	}
	
}
