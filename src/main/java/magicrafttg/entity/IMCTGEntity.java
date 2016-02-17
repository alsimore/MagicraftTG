package magicrafttg.entity;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public interface IMCTGEntity {
	
	/** Get the Entity (usually a player) who created the entity. */
	public EntityLivingBase getOwnerEntity();

	/** Set the Entity (usually a player) who created this entity. */
	public void setOwnerEntity(EntityLivingBase owner);
	
	/** Get the current Entity who controls this one. May differ from the entity
	 * who created it. */ 
	public EntityLivingBase getControllerEntity();
	
	/** Set the current Entity who controls this one. May differ from the entity
	 * who created it. */
	public void setControllerEntity(EntityLivingBase controller);
	
	public UUID getOwnerUUID();

	public void setOwnerUUID(UUID owner);
	
	public UUID getControllerUUID();
	
	public void setControllerUUID(UUID controller);
}
