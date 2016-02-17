package magicrafttg.entity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import magicrafttg.mana.ManaColour;
import magicrafttg.mana.ManaSource;
import magicrafttg.network.MCTGCreaturePacket;
import magicrafttg.network.MCTGManaPacket;
import magicrafttg.network.MCTGPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
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
public class MagicraftTGPlayer implements IExtendedEntityProperties {
	
	public final static String EXT_PROP_NAME = "MagicraftTGPlayer";
	
	private final WeakReference<EntityPlayer> player;
	
	/**
	 * The amount of mana the player currently has:
	 * white, blue, black, red, green, colourless.
	 */
	private int mana[] = new int[6];
	
	/**
	 * A list of ManaSources that the player selected when they logged on.
	 * In order: white, blue, black, red, green
	 */
	private ArrayList<ManaSource> globalSources;
	
	/**
	 * A list of the creatures that this player currently controls.
	 */
	private ArrayList<WeakReference<Entity>> controlledCreatures;
	
	/**
	 * The creature that the player has currently selected. Not necessarily 
	 * controlled by the player.
	 */
	private WeakReference<Entity> selectedCreature;
	private int selectedIndex = -1;
	
	public MagicraftTGPlayer(EntityPlayer player)
	{
		//System.out.println("[MCTG] Creating MagicraftTGPlayer for " 
		//		+ player.getUniqueID().toString() + "(" + System.identityHashCode(player) + ")");
		//System.out.println("[MCTG] ### " + player.getGameProfile().getId().toString() + "\n");
		this.player = new WeakReference<EntityPlayer>(player);
		this.setWhiteMana(0);	
		this.setBlueMana(0);
		this.setBlackMana(0);
		this.setRedMana(0);
		this.setGreenMana(0);
		this.setColourlessMana(0);
		
		
		globalSources = new ArrayList<ManaSource>();

		controlledCreatures = new ArrayList<WeakReference<Entity>>();
	}
	
	/**
	 * Used to register these extended properties for the player during EntityConstructing event.
	 * This method is for convenience only; it will make your code look nicer.
	 */
	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(MagicraftTGPlayer.EXT_PROP_NAME, new MagicraftTGPlayer(player));
	}
	
	/**
	 * Returns MagicraftTGPlayer properties for player.
	 * This method is for convenience only; it will make your code look nicer.
	 */
	public static final MagicraftTGPlayer get(EntityPlayer player)
	{
		return (MagicraftTGPlayer) player.getExtendedProperties(EXT_PROP_NAME);
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
		globalSources.clear();
		for(int i = 0; i < w; ++i)
		{
			globalSources.add(new ManaSource(ManaColour.WHITE));
			System.out.println("Added WHITE");
		}
		for(int i = 0; i < u; ++i)
		{
			globalSources.add(new ManaSource(ManaColour.BLUE));
			System.out.println("Added BLUE");
		}
		for(int i = 0; i < b; ++i)
		{
			globalSources.add(new ManaSource(ManaColour.BLACK));
			System.out.println("Added BLACK");
		}
		for(int i = 0; i < r; ++i)
		{
			globalSources.add(new ManaSource(ManaColour.RED));
			System.out.println("Added RED");
		}
		for(int i = 0; i < g; ++i)
		{
			globalSources.add(new ManaSource(ManaColour.GREEN));
			System.out.println("Added GREEN");
		}
		//System.out.println("Client sources set: " + w + " "
		//		+ u + " " + b + " " + r + " " + g + " ");
	}
	
	public int[] getGlobalSourceNumbers()
	{
		int sources[] = new int[5];
		
		for (ManaSource src : globalSources)
		{
			sources[src.getColour().ordinal()]++;
		}
		
		return sources;
	}

	/**
	 * Consumes the amounts and colours of mana specified. Checks whether the player has
	 * sufficient mana available. If any colour is too low then no mana (of any colour) will
	 * be consumed.
	 * @param colours
	 * @param amounts
	 * @return true if the mana was consumed, false otherwise.
	 */
	public boolean consumeMana(ManaColour[] colours, int[] amounts)
	{
		boolean sufficient = true;
		int totalColoured = 0;
		int totalColourless = 0;
		int colourless_i = -1;
		
		if(colours.length != amounts.length) {
			System.out.println("[MCTG] consumeMana: amounts and colours arrays differ in length");
			return false;
		}
		
		// Attempt to spend the mana and see if any amounts go negative
		int newMana[] = new int[6];
		for (int i = 0; i < 6; ++i)
		{
			newMana[i] = this.mana[i];
		}
		
		// Record the amount of coloured and colourless mana required.
		for(int i = 0; i < colours.length; ++i) {
			int colourIndex = colours[i].ordinal();
			
			if(colours[i] == ManaColour.COLOURLESS)
			{
				totalColourless += amounts[i];
				colourless_i = i;
			}
			else
			{
				if( (newMana[colourIndex] -= amounts[i]) < 0)
					return false; // Insufficient colour mana, can't be helped.
				totalColoured += amounts[i];
			}
		}
		// At this point we have not encountered any *colour* with insufficient amount.
		// If we have enough colourless then consume that.
		if(newMana[ManaColour.COLOURLESS.ordinal()] >= totalColourless)
		{
			for(int i = 0; i < colours.length; ++i) 
			{
				if(!decreaseMana(colours[i], amounts[i])) 
				{
					// Shouldn't fail as we already checked.
					System.out.println("[MCTG] consumeMana: decreaseMana failed: "
							+ colours[i].toString() + " " + amounts[i]);
					return false;
				}
			}
			return true;
		}
		else
		{
			while (totalColourless > 0)
			{
				// If we don't have enough colourless directly then consume the colour of
				// which we have the most. Keep spending until we no longer need any colourless.
				ManaColour mostColour = ManaColour.COLOURLESS;
				int mostAmount = -1;
				for (ManaColour col : ManaColour.getColourArray())
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
							+ totalColourless + ")");
					return false;
				}
				else if(mostAmount >= totalColourless)
				{
					newMana[ mostColour.ordinal() ] -= totalColourless;
					totalColourless -= totalColourless;
				}
				else if(mostAmount < totalColourless)
				{
					newMana[ mostColour.ordinal() ] -= mostAmount;
					totalColourless -= mostAmount;
				}
			}
			
			// Set the new mana amounts.
			updateMana(newMana);
		}
		return true;
	}
	
	/**
	 * Determines whether the player has sufficient mana of the specified colours and amounts.
	 * @param colours
	 * @param amounts
	 * @return
	 */
	private boolean hasSufficientMana(ManaColour[] colours, int[] amounts) {
		boolean sufficient = true;
		int totalColoured = 0;
		int totalColourless = 0;
		int colourless_i = -1;
		
		if(colours.length != amounts.length) {
			System.out.println("[MCTG] hasSufficientMana: amounts and colours arrays differ in length");
			return false;
		}
		
		// Record the amount of coloured and colourless mana required.
		for(int i = 0; i < colours.length; ++i) {
			int colourIndex = colours[i].ordinal();
			
			if(colours[i] == ManaColour.COLOURLESS)
			{
				totalColourless += amounts[i];
				colourless_i = i;
			}
			else
			{
				if(this.mana[colourIndex] < amounts[i])
					return false; // Insufficient colour mana, can't be helped.
				totalColoured += amounts[i];
			}
		}
		
		// At this point we have not encountered any *colour* with insufficient amount.
		// If we have enough colourless then consume that.
		if(this.mana[ManaColour.COLOURLESS.ordinal()] >= totalColourless)
		{
			return true;
		}
		else
		{
			// If we don't have enough colourless directly then consume the colour of
			// which we have the most.
			ManaColour mostColour;
			int mostAmount = -1;
			for (ManaColour col : ManaColour.getColourArray())
			{
				
			}
		}
		return sufficient;
	}
	
	/**
	 * Increase the player's mana based on the globalSources they have.
	 */
	public void incrementManaFromSources()
	{
		for (ManaSource source : globalSources)
		{
			increaseMana(source.getColour(), 1);
		}
	}
	
	/**
	 * Increase the given mana colour by the specified amount.
	 * @param colour
	 * @param amount
	 */
	public void increaseMana(ManaColour colour, int amount)
	{
		if(amount < 1)
			return;
		
		switch(colour)
		{
		case WHITE:
			
			this.mana[ManaColour.WHITE.ordinal()] += amount;
			break;
		case BLUE:
			this.mana[ManaColour.BLUE.ordinal()] += amount;
			break;
		case BLACK:
			this.mana[ManaColour.BLACK.ordinal()] += amount;
			break;
		case RED:
			this.mana[ManaColour.RED.ordinal()] += amount;
			break;
		case GREEN:
			this.mana[ManaColour.GREEN.ordinal()] += amount;
			break;
		default:
			this.mana[ManaColour.COLOURLESS.ordinal()] += amount;
		}
	}
	
	/**
	 * Decrease the given mana colour by the specified amount. Checks to ensure that the
	 * player has sufficient mana. If insufficient then NO mana is deducted (all or nothing).
	 * @param colour
	 * @param amount
	 * @return
	 */
	public boolean decreaseMana(ManaColour colour, int amount)
	{
		if(amount < 1 || colour == null)
			return false;
		
		switch(colour)
		{
		case WHITE:
			if(amount <= this.mana[ManaColour.WHITE.ordinal()]) {
				this.mana[ManaColour.WHITE.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		case BLUE:
			if(amount <= this.mana[ManaColour.BLUE.ordinal()]) {
				this.mana[ManaColour.BLUE.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		case BLACK:
			if(amount <= this.mana[ManaColour.BLACK.ordinal()]) {
				this.mana[ManaColour.BLACK.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		case RED:
			if(amount <= this.mana[ManaColour.RED.ordinal()]) {
				this.mana[ManaColour.RED.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		case GREEN:
			if(amount <= this.mana[ManaColour.GREEN.ordinal()]) {
				this.mana[ManaColour.GREEN.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		default:
			if(amount <= this.mana[ManaColour.COLOURLESS.ordinal()]) {
				this.mana[ManaColour.COLOURLESS.ordinal()] -= amount;
				return true;
			}
			else
				return false;
		}
	}
	
	public int[] getManaAmounts() {
		return mana;
	}
	
	public void updateMana(int[] amounts)
	{
		this.mana[ManaColour.WHITE.ordinal()] = amounts[ManaColour.WHITE.ordinal()];
		this.mana[ManaColour.BLUE.ordinal()] = amounts[ManaColour.BLUE.ordinal()];
		this.mana[ManaColour.BLACK.ordinal()] = amounts[ManaColour.BLACK.ordinal()];
		this.mana[ManaColour.RED.ordinal()] = amounts[ManaColour.RED.ordinal()];
		this.mana[ManaColour.GREEN.ordinal()] = amounts[ManaColour.GREEN.ordinal()];
		this.mana[ManaColour.COLOURLESS.ordinal()] = amounts[ManaColour.COLOURLESS.ordinal()];
	}
	
	public void updateMana(int white, int blue, int black, int red, int green, int colourless)
	{
		this.mana[ManaColour.WHITE.ordinal()] = white;
		this.mana[ManaColour.BLUE.ordinal()] = blue;
		this.mana[ManaColour.BLACK.ordinal()] = black;
		this.mana[ManaColour.RED.ordinal()] = red;
		this.mana[ManaColour.GREEN.ordinal()] = green;
		this.mana[ManaColour.COLOURLESS.ordinal()] = colourless;
	}
	
	/**
	 * Send the updated mana amounts to the client instance of EntityPlayer.
	 * I thought of making it @SideOnly(Side.SERVER) but this caused a
	 * NoSuchMethodException even though it seems to run on the server-side code.
	 * @param player
	 */
	public void updateManaToClient(EntityPlayer player) {
		
		IMessage msg = new MCTGManaPacket.MCTGManaMessage(MCTGPacketHandler.MANA_UPDATE, 
				this.getWhiteMana(), this.getBlueMana(), 
				this.getBlackMana(), this.getRedMana(), this.getGreenMana(),
				this.getColourlessMana());
		// See if we can use the stored player
		//System.out.println("[MCTG] Try send to " + this.player.get().getUniqueID().toString()
		//		 + "(" + System.identityHashCode(this.player.get()) + ")");
		
		MCTGPacketHandler.net.sendTo(msg, (EntityPlayerMP)this.player.get());
		
		//System.out.println("[MCTG] Updating mana to " + player.getUniqueID().toString()
		//		 + "(" + System.identityHashCode(player) + ")");
		//System.out.println("[MCTG] ### " + player.getGameProfile().getId().toString());
		
		/*if(player instanceof EntityPlayerMP) {
			IMessage msg = new MCTGPacket.MCTGManaMessage(MCTGPacket.MANA_UPDATE, 
					this.getWhiteMana(), this.getBlueMana(), 
					this.getBlackMana(), this.getRedMana(), this.getGreenMana(),
					this.getColourlessMana());
			MCTGPacketHandler.net.sendTo(msg, (EntityPlayerMP)player);
			System.out.println("[MCTG] Updating mana to " + player.getUniqueID().toString()
					 + "(" + System.identityHashCode(player) + ")");
			System.out.println("[MCTG] ### " + player.getGameProfile().getId().toString());
		}*/
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
	
	public void addCreatureById(int id)
	{
		Entity creature = findByIdClient(id);
		//System.out.println("[MCTG] Add creature " + id + "(" + System.identityHashCode(creature) + ")");
		controlledCreatures.add(new WeakReference<Entity>(creature));
	}
	
	/**
	 * Find an entity on the client side as MinecraftServer.getEntityFromUuid(UUID) doesn't 
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
	
	
	Entity findByIdClient(int id)
	{
		List entities = Minecraft.getMinecraft().theWorld.getLoadedEntityList();
		int num = Minecraft.getMinecraft().theWorld.getLoadedEntityList().size();
		for (Object e : entities)
		{
			//System.out.println(e);
			int entId = ((Entity) e).getEntityId(); 
			
			//if(e instanceof EntityMCTGDireWolf)
			if(e instanceof EntityMCTGZombie)
			{
				//System.out.println(entId);
			}
			
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
	
	public void removeCreatureById(int id)
	{
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
	
	public void updateCreatureToClient(char type, UUID creatureId)
	{
		IMessage msg = new MCTGCreaturePacket.MCTGCreatureMessage(type, creatureId);
		
		//System.out.println("[MCTG] Send creatureUpdate to " + this.player.get().getUniqueID().toString()
		//		 + "(" + System.identityHashCode(this.player.get()) + ")");
		//System.out.println("[MCTG] theCreature: " + creatureId.toString());
		
		MCTGPacketHandler.net.sendTo(msg, (EntityPlayerMP)this.player.get());
	}
	
	public void updateCreatureToClient(char type, int creatureId)
	{
		IMessage msg = new MCTGCreaturePacket.MCTGCreatureMessage(type, creatureId);
		
		//System.out.println("[MCTG] Send creatureUpdate to " + this.player.get().getUniqueID().toString()
		//		 + "(" + System.identityHashCode(this.player.get()) + ")");
		//System.out.println("[MCTG] theCreature: " + creatureId);
		
		MCTGPacketHandler.net.sendTo(msg, (EntityPlayerMP)this.player.get());
	}
	
	
	public void addControlledCreature(Entity creature)
	{
		// Only called from the server thread
		controlledCreatures.add(new WeakReference<Entity>(creature));
		//System.out.println("[MCTG] Added - Controlled: " + controlledCreatures.size());
		//System.out.println("[MCTG] " + creature.toString());
		//System.out.println("[MCTG] " + creature.getUniqueID() + "(" + System.identityHashCode(creature) + ")\n");
		
		Entity test = MinecraftServer.getServer().getEntityFromUuid(creature.getUniqueID());
		System.out.println("Before send client: " + creature.getUniqueID() + " returns");
		System.out.println(test);
		
		updateCreatureToClient(MCTGPacketHandler.ADD_CREATURE_INT, creature.getEntityId());
	}
	
	public void removeControlledCreature(Entity creature)
	{
		// Only called from the server thread
		
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
		updateCreatureToClient(MCTGPacketHandler.REMOVE_CREATURE_INT, creature.getEntityId());
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
				IMessage msg = new MCTGCreaturePacket.MCTGCreatureMessage(MCTGPacketHandler.SELECT_CREATURE, index);
				MCTGPacketHandler.net.sendTo(msg, (EntityPlayerMP) this.player.get());
			}
		}
		else
		{
			selectedCreature = new WeakReference<Entity>(controlledCreatures.get(index).get());
			
			if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			{
				// Send selection to server
				IMessage msg = new MCTGCreaturePacket.MCTGCreatureMessage(MCTGPacketHandler.SELECT_CREATURE, index);
				MCTGPacketHandler.net.sendToServer(msg);
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
	
	
	///////////////////////////////////////////////////////////
	// Getters and setters

	public int getWhiteMana() {
		return this.mana[ManaColour.WHITE.ordinal()];
	}

	public void setWhiteMana(int whiteMana) {
		this.mana[ManaColour.WHITE.ordinal()] = whiteMana;
	}

	public int getBlueMana() {
		return this.mana[ManaColour.BLUE.ordinal()];
	}

	public void setBlueMana(int blueMana) {
		this.mana[ManaColour.BLUE.ordinal()] = blueMana;
	}

	public int getBlackMana() {
		return this.mana[ManaColour.BLACK.ordinal()];
	}

	public void setBlackMana(int blackMana) {
		this.mana[ManaColour.BLACK.ordinal()] = blackMana;
	}

	public int getRedMana() {
		return this.mana[ManaColour.RED.ordinal()];
	}

	public void setRedMana(int redMana) {
		this.mana[ManaColour.RED.ordinal()] = redMana;
	}

	public int getGreenMana() {
		return this.mana[ManaColour.GREEN.ordinal()];
	}

	public void setGreenMana(int greenMana) {
		this.mana[ManaColour.GREEN.ordinal()] = greenMana;
	}

	public int getColourlessMana() {
		return this.mana[ManaColour.COLOURLESS.ordinal()];
	}

	public void setColourlessMana(int colourlessMana) {
		this.mana[ManaColour.COLOURLESS.ordinal()] = colourlessMana;
	}

	
	// IExtendedEntityProperties interface methods
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		System.out.println("Save NBT ");
		
		// We need to create a new tag compound that will save everything for our Extended Properties
		NBTTagCompound properties = new NBTTagCompound();
		int sources[] = new int[5];
		
		for(ManaSource src : this.globalSources)
		{
			sources[src.getColour().ordinal()]++;
		}
		
		// Save variables to the new tag
		properties.setInteger("SourceWhite", sources[ManaColour.WHITE.ordinal()]);
		properties.setInteger("SourceBlue", sources[ManaColour.BLUE.ordinal()]);
		properties.setInteger("SourceBlack", sources[ManaColour.BLACK.ordinal()]);
		properties.setInteger("SourceRed", sources[ManaColour.RED.ordinal()]);
		properties.setInteger("SourceGreen", sources[ManaColour.GREEN.ordinal()]);
		
		
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
		sources[ManaColour.WHITE.ordinal()] = properties.getInteger("SourceWhite");
		//System.out.println(sources[0]);
		sources[ManaColour.BLUE.ordinal()] = properties.getInteger("SourceBlue");
		//System.out.println(sources[1]);
		sources[ManaColour.BLACK.ordinal()] = properties.getInteger("SourceBlack");
		//System.out.println(sources[2]);
		sources[ManaColour.RED.ordinal()] = properties.getInteger("SourceRed");
		//System.out.println(sources[3]);
		sources[ManaColour.GREEN.ordinal()] = properties.getInteger("SourceGreen");
		//System.out.println(sources[4]);
		
		this.globalSources.clear();
		
		for(int s = 0; s < sources.length; ++s)
		{
			//System.out.println("Processing " + s + "(" + sources[s] + ")");
			for(int i = 0; i < sources[s]; ++i)
			{
				ManaColour colour = ManaColour.colourFromIndex(s);
				this.globalSources.add(new ManaSource(colour));
				//System.out.println("s = " + s);
				//System.out.println("Added " + colour.toString());
				//System.out.println("after, s = " + s);
			}
			
		}
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub
		//System.out.println("init ");
	}
}
