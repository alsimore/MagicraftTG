package magicrafttg.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import magicrafttg.entity.MagicraftTGPlayer;
import magicrafttg.network.MCTGCreaturePacket.MCTGCreatureMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//The params of the IMessageHandler are <REQ, REPLY>
//This means that the first param is the packet you are receiving, and the second is the packet you are returning.
//The returned packet can be used as a "response" from a sent packet.
public class MCTGCreaturePacket implements IMessageHandler<MCTGCreatureMessage, IMessage> {
	
	@Override
	public IMessage onMessage(final MCTGCreatureMessage msg, MessageContext ctx) {
		// This is the player the packet was sent to the server from
	    //EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
	    
	    if (ctx.side.isClient()) 
	    {
	    	//System.out.println("Client message received");
	    	Minecraft.getMinecraft().addScheduledTask(new Runnable()
		    {
	    		public void run() {
	    			clientReceivedMessage(msg);
		      }
		    });
	    }
	    else if (ctx.side.isServer())
	    {
	    	//System.out.println("Server message received");
	    	EntityPlayerMP player = ctx.getServerHandler().playerEntity;
	    	final MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
	    	player.getServerForPlayer().addScheduledTask(new Runnable()
	    	{
	    	  public void run() {
	    	    serverReceivedMessage(msg, mctg);
	    	  }
	    	});
	    }
		
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	private void clientReceivedMessage(MCTGCreatureMessage msg)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(msg.type == MCTGPacketHandler.ADD_CREATURE)
		{
			// update player data
			
			MagicraftTGPlayer player = MagicraftTGPlayer.get(mc.thePlayer);
			player.addCreatureByUUID(msg.id);
		}
		else if(msg.type == MCTGPacketHandler.REMOVE_CREATURE)
		{
			//System.out.println("Remove creature received: " + msg.id);
			//System.out.println("or " + msg.index);
			MagicraftTGPlayer player = MagicraftTGPlayer.get(mc.thePlayer);
			player.removeCreatureByUUID(msg.id);
		}
		else if(msg.type == MCTGPacketHandler.ADD_CREATURE_INT)
		{
			// update player data
			//System.out.println("Client add " + msg.index);
			MagicraftTGPlayer player = MagicraftTGPlayer.get(mc.thePlayer);
			player.addCreatureById(msg.index);
		}
		else if(msg.type == MCTGPacketHandler.REMOVE_CREATURE_INT)
		{
			//System.out.println("Remove creature received: " + msg.id);
			//System.out.println("or " + msg.index);
			MagicraftTGPlayer player = MagicraftTGPlayer.get(mc.thePlayer);
			player.removeCreatureById(msg.index);
		}
	}
	
	//@SideOnly(Side.SERVER)
	private void serverReceivedMessage(MCTGCreatureMessage msg, MagicraftTGPlayer mctg)
	{
		if(msg.type == MCTGPacketHandler.SELECT_CREATURE)
		{
			mctg.setSelectedCreature(msg.index);
		}
	}
	
	
	//////////////////////////////////////////////////////////////
	public static class MCTGCreatureMessage implements IMessage
	{
		private char type;
		private String name;
		private UUID id;
		private int index;
		
		// this constructor is required otherwise you'll get errors (used
		// somewhere in fml through reflection)
		public MCTGCreatureMessage() {
		}
		
		public MCTGCreatureMessage(char type, UUID creatureId)
		{
			this.type = type;
			this.id = creatureId;
			this.index = 0;
		}
		
		public MCTGCreatureMessage(char type, int index)
		{
			this.type = type;
			this.index = index;
			this.id = null;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			this.type = buf.readChar();
			//System.out.println("[MCTG] Packet type " + this.type);
			if(this.type == MCTGPacketHandler.SELECT_CREATURE)
			{
				this.index = buf.readInt();
			}
			else
			{
				long least = buf.readLong();
				long most = buf.readLong();
				id = new UUID(most, least);
				index = buf.readInt();
				//System.out.println("[MCTG] UUID received " + id.toString());
			}
			
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeChar(type);
			if(type == MCTGPacketHandler.SELECT_CREATURE)
			{
				buf.writeInt(index);
			}
			else
			{
				if(id != null)
				{
					buf.writeLong(id.getLeastSignificantBits());
					buf.writeLong(id.getMostSignificantBits());
				}
				else
				{
					buf.writeLong(0L);
					buf.writeLong(0L);
				}
				buf.writeInt(index);
				//System.out.println("[MCTG] UUID sent " + id.toString());
			}
			
			//
		}
		
	}
}
