package magicrafttg.spell.spells;

import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.spell.ISpellEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
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
				System.out.println("Before cast " + castTarget.getControllerUUID());
				castTarget.setControllerEntity(caster);
				System.out.println("After cast " + castTarget.getControllerUUID());
				castTarget.updateDataWatcher();
			}
			
		}
		catch(ClassCastException e)
		{
			caster.addChatComponentMessage(new ChatComponentText("Not a valid spell target"));
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
	
}
