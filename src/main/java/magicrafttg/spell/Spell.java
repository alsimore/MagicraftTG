package magicrafttg.spell;

import java.util.List;

import magicrafttg.MagicraftTG;
import magicrafttg.mana.ManaColor;
import magicrafttg.player.MCTGPlayerProperties;
import magicrafttg.spell.event.SpellEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class Spell extends Card
{
	protected int[] costAmt;
	protected ManaColor[] costColor;
	protected boolean hasTarget;
	/**
	 * Type of the spell, e.g. Creature, Instant etc.
	 */
	protected int type;
	/**
	 * Subtype where required, e.g. creature type (Human, Goblin etc.)
	 */
	protected String subtype;
	
	/**
	 * Constructor for generic Spell.
	 * @param unlocalizedName
	 * @param costColor
	 * @param costAmt
	 * @param effect
	 * @param hasTarget Is the spell targeted at an Entity (e.g. an enchantment) or is it
	 * untargeted (e.g. summoning a creature). Note that damage instants like fireball have no target
	 * as they must be manually targeted (i.e. you have to point in the right direction). The target is determined
	 * at cast time.
	 */
	public Spell(String unlocalizedName, ManaColor[] costColor, int[] costAmt, ISpellEffect effect, boolean hasTarget) 
	{
		super(unlocalizedName, determineColor(costColor), effect);
		this.costColor = costColor;
		this.costAmt = costAmt;
		this.hasTarget = hasTarget;
	}

	@Override
	protected boolean cast(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) 
	{
		System.out.println("Spell.cast");
		MCTGPlayerProperties mctg = MCTGPlayerProperties.get(playerIn);
		
		String unlocName = this.getUnlocalizedName().substring(5);
		String str = "";
		boolean result = false;
		
		// Does the player have the mana to cast the spell?
		if(mctg != null) 
		{
			//effect.onAdd(worldIn, null, playerIn);
			Entity target = determineTarget(worldIn, playerIn);
			System.out.println(target);
			
			if(this.effect.isValidTarget(target))
			{
				if (mctg.consumeMana(this.costColor, this.costAmt))
				{
					System.out.println("Posting SpellEvent");
					MinecraftForge.EVENT_BUS.post( new SpellEvent(this.effect, worldIn, target, playerIn) );
					
					str = String.format(EnumChatFormatting.GREEN + 
							"Successfully cast %s", StatCollector.translateToLocal(unlocName));
										
					result = true;
				}
				else
				{
					str = String.format(EnumChatFormatting.RED + 
							"Failed to cast %s: insufficient mana", StatCollector.translateToLocal(unlocName));
					
					result = false;
				}
			}
			else
			{
				str = String.format(EnumChatFormatting.RED + 
						"Failed to cast %s: invalid target", StatCollector.translateToLocal(unlocName));
				
				result = false;
			}
			
			 
			
		}
		else
		{
			str = String.format(EnumChatFormatting.RED + 
					"Failed to cast %s: unknown error", StatCollector.translateToLocal(unlocName));
			
			result = false;
		}
		
		
		
		IChatComponent msg = new ChatComponentText(str);
		playerIn.addChatMessage(msg);
		
		return result;
	}

	/**
	 * Determine the color of the spell based on the mana required to cast it.
	 * @param costColor
	 * @return
	 */
	private static ManaColor determineColor(ManaColor[] costColor)
	{
		ManaColor color = ManaColor.COLORLESS;
		for(ManaColor c : costColor)
		{
			if(c != ManaColor.COLORLESS)
			{
				// Currently only colorless, or as yet undetermined
				if(color == ManaColor.COLORLESS)
				{
					color = c;
				}
				else if(c != color)
				{
					// Have found more than 1 color
					color = ManaColor.MULTI;
				}
			}
		}
		
		return color;
	}
	
	private Entity determineTarget(World world, EntityPlayer caster)
	{
		MovingObjectPosition mop = getMouseOverExtended(world, caster, 6f);
		if(mop != null)
			return mop.entityHit;
		return null;
	}
	
	// Copied from Jabelar's Minecraft Forge Tutorials
	// http://jabelarminecraft.blogspot.com.au/p/minecraft-modding-extending-reach-of.html
	public static MovingObjectPosition getMouseOverExtended(World worldIn, EntityPlayer playerIn, float dist)
	{
	    //Minecraft mc = FMLClientHandler.instance().getClient();
	    //Entity theRenderViewEntity = mc.getRenderViewEntity();
		Entity theRenderViewEntity = playerIn;
	    AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(
	            theRenderViewEntity.posX-0.5D,
	            theRenderViewEntity.posY-0.0D,
	            theRenderViewEntity.posZ-0.5D,
	            theRenderViewEntity.posX+0.5D,
	            theRenderViewEntity.posY+1.5D,
	            theRenderViewEntity.posZ+0.5D
	            );
	    MovingObjectPosition returnMOP = null;
	    //if (mc.theWorld != null)
	    if (worldIn != null)
	    {
	        double var2 = dist;
	        returnMOP = rayTraceServer(worldIn, theRenderViewEntity, var2, 0);
	        // returnMOP = theRenderViewEntity.rayTrace(var2, 0);
	        double calcdist = var2;
	        //Vec3 pos = theRenderViewEntity.getPositionEyes(0);
	        Vec3 pos = getPositionEyesServer(theRenderViewEntity, 0);
	        var2 = calcdist;
	        if (returnMOP != null)
	        {
	            calcdist = returnMOP.hitVec.distanceTo(pos);
	        }
	         
	        Vec3 lookvec = theRenderViewEntity.getLook(0);
	        Vec3 var8 = pos.addVector(lookvec.xCoord * var2, 
	              lookvec.yCoord * var2, 
	              lookvec.zCoord * var2);
	        Entity pointedEntity = null;
	        float var9 = 1.0F;
	        @SuppressWarnings("unchecked")
	        //List<Entity> list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
	        List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(
	              theRenderViewEntity, 
	              theViewBoundingBox.addCoord(
	                    lookvec.xCoord * var2, 
	                    lookvec.yCoord * var2, 
	                    lookvec.zCoord * var2).expand(var9, var9, var9));
	        double d = calcdist;
	        //System.out.println("List size " + list.size());
	            
	        for (Entity entity : list)
	        {
	            if (entity.canBeCollidedWith())
	            {
	                float bordersize = entity.getCollisionBorderSize();
	                AxisAlignedBB aabb = new AxisAlignedBB(
	                      entity.posX-entity.width/2, 
	                      entity.posY, 
	                      entity.posZ-entity.width/2, 
	                      entity.posX+entity.width/2, 
	                      entity.posY+entity.height, 
	                      entity.posZ+entity.width/2);
	                aabb.expand(bordersize, bordersize, bordersize);
	                MovingObjectPosition mop0 = aabb.calculateIntercept(pos, var8);
	                    
	                if (aabb.isVecInside(pos))
	                {
	                    if (0.0D < d || d == 0.0D)
	                    {
	                        pointedEntity = entity;
	                        d = 0.0D;
	                    }
	                } else if (mop0 != null)
	                {
	                    double d1 = pos.distanceTo(mop0.hitVec);
	                        
	                    if (d1 < d || d == 0.0D)
	                    {
	                        pointedEntity = entity;
	                        d = d1;
	                    }
	                }
	            }
	        }
	           
	        if (pointedEntity != null && (d < calcdist || returnMOP == null))
	        {
	             returnMOP = new MovingObjectPosition(pointedEntity);
	        }
	    }
	    return returnMOP;
	}
	
	// Copied from Entity.rayTrace, which is client only
	private static MovingObjectPosition rayTraceServer(World worldIn, Entity entity, double dist, float p_174822_3_)
    {
        Vec3 vec3 = getPositionEyesServer(entity, p_174822_3_);
        Vec3 vec31 = entity.getLook(p_174822_3_);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist);
        return worldIn.rayTraceBlocks(vec3, vec32, false, false, true);
    }
	
	// Copied from Entity.getPositionEyes, which is client only
	private static Vec3 getPositionEyesServer(Entity entity, float p_174824_1_)
    {
        if (p_174824_1_ == 1.0F)
        {
            return new Vec3(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
        }
        else
        {
            double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)p_174824_1_;
            double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)p_174824_1_ + (double)entity.getEyeHeight();
            double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)p_174824_1_;
            return new Vec3(d0, d1, d2);
        }
    }
}
