package magicrafttg.player;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import magicrafttg.entity.EntityMCTGBase;
import magicrafttg.entity.EntityMCTGDireWolf;
import magicrafttg.entity.EntityMCTGZombie;
import magicrafttg.event.FMLCommonClientHandler;
import magicrafttg.mana.ManaColor;
import magicrafttg.mana.ManaSource;
import magicrafttg.network.CreaturePacket;
import magicrafttg.network.GuiHandler;
import magicrafttg.network.MCTGNetworkManager;
import magicrafttg.network.ManaPacket;
import magicrafttg.network.PacketHandler;
import magicrafttg.spell.ISpellEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Extended properties for the MagicraftTGPlayer and functions to deal with them.
 * @author Adam
 *
 */
public class MCTGPlayerProperties implements IExtendedEntityProperties {
	
	public final static String EXT_PROP_NAME = "MCTGPlayerProperties";
	
	private final WeakReference<EntityPlayer> player;
	
	/**
	 * The amount of mana the player currently has:
	 * white, blue, black, red, green, colourless.
	 */
	private int currentMana[];
	
	/**
	 * A list of ManaSources that the player selected when they logged on.
	 * In order: white, blue, black, red, green, colorless
	 */
	//private ArrayList<ManaSource> manaSources;
	private int[] manaSources;
	
	/**
	 * A list of the creatures that this player currently controls.
	 */
	private List<WeakReference<Entity>> controlledCreatures;
	
	/**
	 * The creature that the player has currently selected. Not necessarily 
	 * controlled by the player.
	 */
	private WeakReference<Entity> selectedCreature;
	private int selectedIndex = -1;
	
	/**
	 * A list of SpellEffects currently applying to the player.
	 */
	private List<ISpellEffect> effects;
	public int manaGuiCountdown;
	
	
	
	public MCTGPlayerProperties(EntityPlayer player)
	{
		//System.out.println("Creating MagicraftTGPlayer for " + player.getUniqueID().toString() );
		//System.out.println("Game profile " + player.getGameProfile().getId().toString());
		//System.out.println("ID " + player.getEntityId());
		this.player = new WeakReference<EntityPlayer>(player);
		this.currentMana = new int[6];
		
		
		manaSources = new int[6]; //new ArrayList<ManaSource>();
		//System.out.println(manaSources.length);

		controlledCreatures = new ArrayList<WeakReference<Entity>>();
		
		effects = new ArrayList<ISpellEffect>();
		
		manaGuiCountdown = GuiHandler.MANA_GUI_COUNTDOWN;
	}
	
	/**
	 * Used to register these extended properties for the player during EntityConstructing event.
	 * This method is for convenience only; it will make your code look nicer.
	 */
	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(MCTGPlayerProperties.EXT_PROP_NAME, new MCTGPlayerProperties(player));
	}
	
	/**
	 * Returns MagicraftTGPlayer properties for player.
	 * This method is for convenience only; it will make your code look nicer.
	 */
	public static final MCTGPlayerProperties get(EntityPlayer player)
	{
		return (MCTGPlayerProperties) player.getExtendedProperties(EXT_PROP_NAME);
	}
	
	
	public int[] getCurrentMana()
	{
		return this.currentMana;
	}
	
	
	public void addSource(ManaColor color)
	{
		System.out.println("MCTGPlayerProperties.addSource");
		//manaSources.add(new ManaSource(color));
		manaSources[color.ordinal()]++;
		// Happens on the server side so update client
		MCTGNetworkManager.sendToClientManaSources(this.player.get(), manaSources);
	}
	
	
	/**
	 * Set the player's global sources selected when they enter.
	 * @param w white
	 * @param u blue
	 * @param b black
	 * @param r red
	 * @param g green
	 */
	public void setGlobalManaSources(int w, int u, int b, int r, int g)
	{
		System.out.println("MCTGPlayerProperties.setGlobalManaSources");
		manaSources[0] = w;
		manaSources[1] = u;
		manaSources[2] = b;
		manaSources[3] = r;
		manaSources[4] = g;
		System.out.println(manaSources[0] + " " + manaSources[1] + " " + manaSources[2]
				+ " " + manaSources[3] + " " + manaSources[4]);
	}
	/*public void setGlobalManaSources(int w, int u, int b, int r, int g)
	{
		manaSources.clear();
		for(int i = 0; i < w; ++i)
		{
			manaSources.add(new ManaSource(ManaColor.WHITE));
			//System.out.println("Added WHITE");
		}
		for(int i = 0; i < u; ++i)
		{
			manaSources.add(new ManaSource(ManaColor.BLUE));
			//System.out.println("Added BLUE");
		}
		for(int i = 0; i < b; ++i)
		{
			manaSources.add(new ManaSource(ManaColor.BLACK));
			//System.out.println("Added BLACK");
		}
		for(int i = 0; i < r; ++i)
		{
			manaSources.add(new ManaSource(ManaColor.RED));
			//System.out.println("Added RED");
		}
		for(int i = 0; i < g; ++i)
		{
			manaSources.add(new ManaSource(ManaColor.GREEN));
			//System.out.println("Added GREEN");
		}
		//System.out.println("Client sources set: " + w + " "
		//		+ u + " " + b + " " + r + " " + g + " ");
	}*/
	
	
	
	public int[] getGlobalSourceNumbers()
	{
		/*int sources[] = new int[5];
		
		for (ManaSource src : manaSources)
		{
			sources[src.getColour().ordinal()]++;
		}*/
		
		return manaSources;
	}

	/**
	 * Consumes the amounts and colours of mana specified. Checks whether the player has
	 * sufficient mana available. If any colour is too low then no mana (of any colour) will
	 * be consumed.
	 * @param colors
	 * @param amounts
	 * @return true if the mana was consumed, false otherwise.
	 */
	public boolean consumeMana(ManaColor[] colors, int[] amounts)
	{
		boolean sufficient = true;
		int totalColored = 0;
		int totalColorless = 0;
		int colorless_i = -1;
		
		System.out.println("MCTGPlayerProperties.consumeMana");
		if(colors.length != amounts.length) {
			System.out.println("[MCTG] consumeMana: amounts and colours arrays differ in length");
			return false;
		}
		
		// Attempt to spend the mana and see if any amounts go negative
		int newMana[] = new int[6];
		for (int i = 0; i < 6; ++i)
		{
			newMana[i] = this.currentMana[i];
		}
		
		// Record the amount of coloured and colourless mana required.
		for(int i = 0; i < colors.length; ++i) {
			int colourIndex = colors[i].ordinal();
			
			if(colors[i] == ManaColor.COLORLESS)
			{
				totalColorless += amounts[i];
				colorless_i = i;
			}
			else
			{
				if( (newMana[colourIndex] -= amounts[i]) < 0)
					return false; // Insufficient colour mana, can't be helped.
				totalColored += amounts[i];
			}
		}
		
		// At this point we have not encountered any *color* with insufficient amount.
		// If we have enough colorless then consume that.
		if(newMana[ManaColor.COLORLESS.ordinal()] >= totalColorless)
		{
			for(int i = 0; i < colors.length; ++i) 
			{
				if(!decreaseMana(colors[i], amounts[i])) 
				{
					// Shouldn't fail as we already checked.
					System.out.println("[MCTG] consumeMana: decreaseMana failed: "
							+ colors[i].toString() + " " + amounts[i]);
					return false;
				}
			}
			//return true;
		}
		else
		{
			while (totalColorless > 0)
			{
				// If we don't have enough colorless directly then consume the color of
				// which we have the most. Keep spending until we no longer need any colorless.
				ManaColor mostColour = ManaColor.COLORLESS;
				int mostAmount = -1;
				for (ManaColor col : ManaColor.getColourArray())
				{
					if(newMana[ col.ordinal() ] > mostAmount)
					{
						mostColour = col;
						mostAmount = newMana[ col.ordinal() ];
					}
				}
				
				if(mostAmount < 1)
				{
					System.out.println("[MCTG] consumeMana: insufficient to cover colourless mana (" 
							+ totalColorless + ")");
					return false;
				}
				else if(mostAmount >= totalColorless)
				{
					newMana[ mostColour.ordinal() ] -= totalColorless;
					totalColorless -= totalColorless;
				}
				else if(mostAmount < totalColorless)
				{
					newMana[ mostColour.ordinal() ] -= mostAmount;
					totalColorless -= mostAmount;
				}
			}
		}
		// Set the new mana amounts.
		System.out.println("Got to setCurrentMana");
		setCurrentMana(newMana);
		// Update the client of current mana
		MCTGNetworkManager.sendToClientCurrentMana(this.player.get(), currentMana);
		
		return true;
	}
	
	public void setCurrentMana(int[] amounts)
	{
		System.out.println("MCTGPlayerProperties.setCurrentMana");
		this.currentMana[ManaColor.WHITE.ordinal()] = amounts[ManaColor.WHITE.ordinal()];
		this.currentMana[ManaColor.BLUE.ordinal()] = amounts[ManaColor.BLUE.ordinal()];
		this.currentMana[ManaColor.BLACK.ordinal()] = amounts[ManaColor.BLACK.ordinal()];
		this.currentMana[ManaColor.RED.ordinal()] = amounts[ManaColor.RED.ordinal()];
		this.currentMana[ManaColor.GREEN.ordinal()] = amounts[ManaColor.GREEN.ordinal()];
		this.currentMana[ManaColor.COLORLESS.ordinal()] = amounts[ManaColor.COLORLESS.ordinal()];
	}
	
	
	
	/**
	 * Increase the player's mana based on the sources they have.
	 */
	public void incrementManaFromSources()
	{
		System.out.println("MCTGPlayerProperties.incrementManaFromSources");
		for(int color = 0; color < 6; ++color) // Six colors if you include colorless
		{
			int num = manaSources[color];
			increaseMana(ManaColor.colourFromIndex(color), num);
		}
		
		//updateManaServerToClient();
		MCTGNetworkManager.sendToClientCurrentMana(this.player.get(), currentMana);
	}
	
	/**
	 * Increase the given mana color by the specified amount.
	 * @param color
	 * @param amount
	 */
	public void increaseMana(ManaColor color, int amount)
	{
		if(amount < 1)
			return;
		
		switch(color)
		{
		case WHITE:
			this.currentMana[ManaColor.WHITE.ordinal()] += amount;
			break;
		case BLUE:
			this.currentMana[ManaColor.BLUE.ordinal()] += amount;
			break;
		case BLACK:
			this.currentMana[ManaColor.BLACK.ordinal()] += amount;
			break;
		case RED:
			this.currentMana[ManaColor.RED.ordinal()] += amount;
			break;
		case GREEN:
			this.currentMana[ManaColor.GREEN.ordinal()] += amount;
			break;
		default:
			this.currentMana[ManaColor.COLORLESS.ordinal()] += amount;
		}
	}
	
	/**
	 * Decrease the given mana colour by the specified amount. Checks to ensure that the
	 * player has sufficient mana. If insufficient then NO mana is deducted (all or nothing).
	 * @param color
	 * @param amount
	 * @return
	 */
	public boolean decreaseMana(ManaColor color, int amount)
	{
		if(amount < 1 || color == null)
			return false;
		
		switch(color)
		{
		case WHITE:
			if(amount <= this.currentMana[ManaColor.WHITE.ordinal()]) {
				this.currentMana[ManaColor.WHITE.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		case BLUE:
			if(amount <= this.currentMana[ManaColor.BLUE.ordinal()]) {
				this.currentMana[ManaColor.BLUE.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		case BLACK:
			if(amount <= this.currentMana[ManaColor.BLACK.ordinal()]) {
				this.currentMana[ManaColor.BLACK.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		case RED:
			if(amount <= this.currentMana[ManaColor.RED.ordinal()]) {
				this.currentMana[ManaColor.RED.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		case GREEN:
			if(amount <= this.currentMana[ManaColor.GREEN.ordinal()]) {
				this.currentMana[ManaColor.GREEN.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		default:
			if(amount <= this.currentMana[ManaColor.COLORLESS.ordinal()]) {
				this.currentMana[ManaColor.COLORLESS.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		}
	}
	
	
	
	/*
	public void updateMana(int white, int blue, int black, int red, int green, int colourless)
	{
		this.currentMana[ManaColor.WHITE.ordinal()] = white;
		this.currentMana[ManaColor.BLUE.ordinal()] = blue;
		this.currentMana[ManaColor.BLACK.ordinal()] = black;
		this.currentMana[ManaColor.RED.ordinal()] = red;
		this.currentMana[ManaColor.GREEN.ordinal()] = green;
		this.currentMana[ManaColor.COLORLESS.ordinal()] = colourless;
	}*/
	
	
	private void updateManaServerToClient()
	{
		MCTGNetworkManager.updateManaToClient(this.player.get(), this.currentMana);
	}
	
	
	public void updateManaToClient(EntityPlayer player)
	{
		MCTGNetworkManager.updateManaToClient(player, this.currentMana);
	}
	
	public void updateManaFromServer()
	{
		//System.out.println("Requesting mana update from server");
		IMessage msg = new ManaPacket.MCTGManaMessage(PacketHandler.MANA_SRC_REQUEST,
				0, 0, 0, 0, 0, 0);
		PacketHandler.net.sendToServer(msg);
	}
	
	
	
	
	
	
	
	
	public void addCreatureByUUID(UUID id)
	{
		//Entity creature = MinecraftServer.getServer().getEntityFromUuid(id);
		Entity creature = findByUuidClient(id);
		
		//if(creature != null)
		//{
			//System.out.println("[MCTG] Add creature " + id.toString() + "(" + System.identityHashCode(creature) + ")");
			controlledCreatures.add(new WeakReference<Entity>(creature));
		//}
		//else
		//{
		//	this.player.get().addChatComponentMessage(new ChatComponentText("Couldn't add creature to list"));
		//}
	}
	
	/**
	 * Find an Entity on the client side as MinecraftServer.getEntityFromUuid(UUID) doesn't 
	 * seem to work on the client side.
	 * @param id
	 * @return
	 */
	Entity findByUuidClient(UUID id)
	{
		List entities = Minecraft.getMinecraft().theWorld.getLoadedEntityList();
		int num = Minecraft.getMinecraft().theWorld.getLoadedEntityList().size();
		for (Object e : entities)
		{
			//System.out.println(e);
			UUID entId = ((Entity) e).getUniqueID(); 
			
			if(e instanceof EntityMCTGDireWolf)
			{
				//System.out.println(entId);
			}
			
			if(id.equals(entId))
			{
				return (Entity)e;
			}
		}
		
		return null;
	}
	
	
	
	
	
	
	
	/**
	 * Add a creature to controlledCreatures by global ID. Performed on the client side.
	 * @param id
	 */
	public void addCreatureById(int id)
	{
		System.out.println("MCTGPlayerProperties.addCreatureById");
		Entity creature = findByIdClient(id);
		//System.out.println("addclient " + ((EntityMCTGBase)creature).getOwnerUUID());
		//System.out.println(((EntityMCTGBase)creature).getControllerUUID());
		//System.out.println("add client " + creature.getDataWatcher().getWatchableObjectString(20));
		//System.out.println(creature.getDataWatcher().getWatchableObjectString(21));
		//System.out.println("[MCTG] Add creature " + id + "(" + System.identityHashCode(creature) + ")");
		//System.out.println("Add creature for " + this.player.get() + "\n" + this.player.get().getUniqueID());
		controlledCreatures.add(new WeakReference<Entity>(creature));
	}
	
	/**
	 * Remove a creature from controlledCreatures by global ID. Performed on the client side.
	 * @param id
	 */
	public void removeCreatureById(int id)
	{
		System.out.println("MCTGPlayerProperties.removeCeatureById");
		Entity creature = findByIdClient(id);
		
		for (Iterator<WeakReference<Entity>> iterator = controlledCreatures.iterator();
		         iterator.hasNext(); )
	    {
	        WeakReference<Entity> weakRef = iterator.next();
	        if (weakRef.get() == creature)
	        {
	            iterator.remove();
	            //System.out.println("Removed creature " + id);
	        }
	    }
	}
	
	/**
	 * Find an Entity on the client side through the loadedEntityList.
	 * @param id
	 * @return
	 */
	Entity findByIdClient(int id)
	{
		List entities = Minecraft.getMinecraft().theWorld.getLoadedEntityList();
		int num = Minecraft.getMinecraft().theWorld.getLoadedEntityList().size();
		for (Object e : entities)
		{
			//System.out.println(e);
			int entId = ((Entity) e).getEntityId(); 
			
			//if(e instanceof EntityMCTGDireWolf)
			//if(e instanceof EntityMCTGZombie)
			//{
				//System.out.println(entId);
			//}
			
			if(id == entId)
			{
				return (Entity)e;
			}
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	public void removeCreatureByUUID(UUID id)
	{
		Entity creature = MinecraftServer.getServer().getEntityFromUuid(id);
		
		for (Iterator<WeakReference<Entity>> iterator = controlledCreatures.iterator();
		         iterator.hasNext(); )
	    {
	        WeakReference<Entity> weakRef = iterator.next();
	        if (weakRef.get() == creature)
	        {
	            iterator.remove();
	            //System.out.println("Removed creature " + id.toString());
	        }
	    }
	}
	
	
	
	public void updateCreatureToClient(char type, UUID creatureId)
	{
		IMessage msg = new CreaturePacket.MCTGCreatureMessage(type, creatureId);
		
		//System.out.println("[MCTG] Send creatureUpdate to " + this.player.get().getUniqueID().toString()
		//		 + "(" + System.identityHashCode(this.player.get()) + ")");
		//System.out.println("[MCTG] theCreature: " + creatureId.toString());
		
		PacketHandler.net.sendTo(msg, (EntityPlayerMP)this.player.get());
	}
	
	public void updateCreatureToClient(char type, int creatureId)
	{
		IMessage msg = new CreaturePacket.MCTGCreatureMessage(type, creatureId);
		
		//System.out.println("Send creatureUpdate to " + this.player.get().getUniqueID().toString());
		//System.out.println("[MCTG] theCreature: " + creatureId);
		
		PacketHandler.net.sendTo(msg, (EntityPlayerMP)this.player.get());
	}
	
	
	public void addControlledCreature(Entity creature)
	{
		System.out.println("MCTGPlayerProperties.addControlledCreature");
		// Only called from the server thread
		controlledCreatures.add(new WeakReference<Entity>(creature));
		//System.out.println("[MCTG] Added - Controlled: " + controlledCreatures.size());
		//System.out.println("[MCTG] " + creature.toString());
		//System.out.println("[MCTG] " + creature.getUniqueID() + "(" + System.identityHashCode(creature) + ")\n");
		
		//Entity test = MinecraftServer.getServer().getEntityFromUuid(creature.getUniqueID());
		//System.out.println("Before send client: " + creature.getUniqueID() + " returns");
		//System.out.println(test);
		//System.out.println("add " + ((EntityMCTGBase)creature).getOwnerUUID());
		//System.out.println(((EntityMCTGBase)creature).getControllerUUID());
		//System.out.println("add " + creature.getDataWatcher().getWatchableObjectString(20));
		//System.out.println(creature.getDataWatcher().getWatchableObjectString(21));
		//updateCreatureToClient(PacketHandler.ADD_CREATURE_INT, creature.getEntityId());
		MCTGNetworkManager.sendToClientAddedCreature(this.player.get(), creature);
	}
	
	public void removeControlledCreature(Entity creature)
	{
		// Only called from the server thread
		System.out.println("MCTGPlayerProperties.removeControlledCreature");
		// From Stack Overflow: 
		// http://stackoverflow.com/questions/6296051/how-to-remove-a-weakreference-from-a-list
		for (Iterator<WeakReference<Entity>> iterator = controlledCreatures.iterator();
		         iterator.hasNext(); )
	    {
	        WeakReference<Entity> weakRef = iterator.next();
	        if (weakRef.get() == creature)
	        {
	            iterator.remove();
	            
	            // See if the removed creature is the selected one
	    		Entity selected = getSelectedCreature();
	            if(creature == selected)
	            {
	            	setSelectedCreature(-1);
	            }
	        }
	    }
		
		//System.out.println("[MCTG] Removed - Controlled: " + controlledCreatures.size());
		//updateCreatureToClient(MCTGPacketHandler.REMOVE_CREATURE, creature.getUniqueID());
		//updateCreatureToClient(PacketHandler.REMOVE_CREATURE_INT, creature.getEntityId());
		MCTGNetworkManager.sendToClientRemovedCreature(this.player.get(), creature);
	}
	
	/**
	 * Kill owned creatures and return controlled creatures to their original owners.
	 */
	public void removeAllControlled()
	{
		for(WeakReference<Entity> creatureRef : controlledCreatures)
		{
			Entity creature = creatureRef.get();
			//System.out.println("Creature: " + creature);
			if(creature instanceof EntityMCTGBase)
			{
				UUID controllerId = ((EntityMCTGBase) creature).getControllerUUID();
				UUID ownerId = ((EntityMCTGBase) creature).getOwnerUUID();
				if(controllerId.equals(ownerId))
				{
					// Creature was summoned by the player, kill it
					creature.setDead();
					//System.out.println("Killing");
					
				}
				else
				{
					// Return control to the owner (summoner)
					((EntityMCTGBase) creature).setControllerUUID(ownerId);
					//System.out.println("Revert to owner: " + ownerId);
				}
			}
			
		}
	}
	
	public int numControlledCreatures()
	{
		return controlledCreatures.size();
	}
	
	public Entity getControlledEntity(int index)
	{
		return controlledCreatures.get(index).get();
	}
	
	
	
	public void setSelectedCreature(int index)
	{
		System.out.println("Selected " + index);
		if(index >= controlledCreatures.size())
		{
			System.out.println("Selected creature outside number controlled");
		}
		else if(index == -1)
		{
			// Clear selected creature
			if(selectedCreature != null) // To prevent crashes from accidental presses
				selectedCreature.clear();
			// Update to client
			if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			{
				// Send selection to server
				IMessage msg = new CreaturePacket.MCTGCreatureMessage(PacketHandler.SELECT_CREATURE, index);
				PacketHandler.net.sendTo(msg, (EntityPlayerMP) this.player.get());
			}
		}
		else
		{
			selectedCreature = new WeakReference<Entity>(controlledCreatures.get(index).get());
			
			if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			{
				// Send selection to server
				IMessage msg = new CreaturePacket.MCTGCreatureMessage(PacketHandler.SELECT_CREATURE, index);
				PacketHandler.net.sendToServer(msg);
			}
		}
		if(selectedCreature != null)
			System.out.println("Selected creature " + selectedCreature.get());
		
		selectedIndex = index;
	}
	
	public Entity getSelectedCreature()
	{
		Entity ent = null;
		if(selectedCreature != null && selectedCreature.get() != null)
			ent = selectedCreature.get();
		return ent;
	}
	
	public int getSelectedIndex()
	{
		return selectedIndex;
	}
	
	
	

	
	// IExtendedEntityProperties interface methods
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		System.out.println("Save NBT ");
		
		// We need to create a new tag compound that will save everything for our Extended Properties
		NBTTagCompound properties = new NBTTagCompound();
		int sources[] = manaSources; //new int[5];
		
		/*for(ManaSource src : this.manaSources)
		{
			sources[src.getColour().ordinal()]++;
		}*/
		
		// Save variables to the new tag
		properties.setInteger("SourceWhite", sources[ManaColor.WHITE.ordinal()]);
		properties.setInteger("SourceBlue", sources[ManaColor.BLUE.ordinal()]);
		properties.setInteger("SourceBlack", sources[ManaColor.BLACK.ordinal()]);
		properties.setInteger("SourceRed", sources[ManaColor.RED.ordinal()]);
		properties.setInteger("SourceGreen", sources[ManaColor.GREEN.ordinal()]);
		
		
		// Now add our custom tag to the player's tag with a unique name (our property's name)
		// This will allow you to save multiple types of properties and distinguish between them
		// If you only have one type, it isn't as important, but it will still avoid conflicts between
		// your tag names and vanilla tag names. For instance, if you add some "Items" tag,
		// that will conflict with vanilla. Not good. So just use a unique tag name.
		compound.setTag(EXT_PROP_NAME, properties);
		
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		System.out.println("Load NBT ");
		
		// Here we fetch the unique tag compound we set for this class of Extended Properties
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		if(properties == null)
			return;
		
		int sources[] = new int[5];
		
		// Get our data from the custom tag compound
		manaSources[ManaColor.WHITE.ordinal()] = properties.getInteger("SourceWhite");
		//System.out.println(sources[0]);
		manaSources[ManaColor.BLUE.ordinal()] = properties.getInteger("SourceBlue");
		//System.out.println(sources[1]);
		manaSources[ManaColor.BLACK.ordinal()] = properties.getInteger("SourceBlack");
		//System.out.println(sources[2]);
		manaSources[ManaColor.RED.ordinal()] = properties.getInteger("SourceRed");
		//System.out.println(sources[3]);
		manaSources[ManaColor.GREEN.ordinal()] = properties.getInteger("SourceGreen");
		//System.out.println(sources[4]);
		
		/*this.manaSources.clear();
		
		for(int s = 0; s < sources.length; ++s)
		{
			//System.out.println("Processing " + s + "(" + sources[s] + ")");
			for(int i = 0; i < sources[s]; ++i)
			{
				ManaColor colour = ManaColor.colourFromIndex(s);
				this.manaSources.add(new ManaSource(colour));
				//System.out.println("s = " + s);
				System.out.println("Added " + colour.toString());
				//System.out.println("after, s = " + s);
			}
		}*/
		
		//this.manaSources = sources;
		
		//sources = getGlobalSourceNumbers();
		//System.out.println("Send to player " + this.player.get());
		//DataWatcher dw = this.player.get().getDataWatcher();
		//dw.updateObject(20, sources[ManaColour.WHITE.ordinal()]);
		//dw.updateObject(21, sources[ManaColour.BLUE.ordinal()]);
		//dw.updateObject(22, sources[ManaColour.BLACK.ordinal()]);
		//dw.updateObject(23, sources[ManaColour.RED.ordinal()]);
		//dw.updateObject(24, sources[ManaColour.GREEN.ordinal()]);
		
		/*IMessage msg = new MCTGManaPacket.MCTGManaMessage(MCTGPacketHandler.MANA_GLOBAL_SOURCE_SET, 
				sources[ManaColour.WHITE.ordinal()], 
				sources[ManaColour.BLUE.ordinal()], 
				sources[ManaColour.BLACK.ordinal()], 
				sources[ManaColour.RED.ordinal()], 
				sources[ManaColour.GREEN.ordinal()], 
				0);
		MCTGPacketHandler.net.sendTo(msg, (EntityPlayerMP)this.player.get());*/
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub
		//System.out.println("init ");
	}
}
