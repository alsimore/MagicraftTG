package magicrafttg.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import magicrafttg.entity.MagicraftTGPlayer;
import magicrafttg.network.MCTGManaPacket.MCTGManaMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//The params of the IMessageHandler are <REQ, REPLY>
//This means that the first param is the packet you are receiving, and the second is the packet you are returning.
//The returned packet can be used as a "response" from a sent packet.
public class MCTGManaPacket implements IMessageHandler<MCTGManaMessage, IMessage> {
	
	@Override
	public IMessage onMessage(final MCTGManaMessage msg, MessageContext ctx) {
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
	private void clientReceivedMessage(MCTGManaMessage msg)
	{
		if(msg.type == MCTGPacketHandler.MANA_UPDATE)
		{
			// update player data
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
			//System.out.println("[MCTG] Client message received by " + player.getUniqueID().toString()
			//		 + "(" + System.identityHashCode(player) + ")");
			//System.out.println("[MCTG] ### " + player.getGameProfile().getId().toString() + "\n");
			mctg.updateMana(msg.w, msg.u, msg.b, msg.r, msg.g, msg.c);
		}
		else if(msg.type == MCTGPacketHandler.MANA_GLOBAL_SOURCE_SET)
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			MagicraftTGPlayer mctg = MagicraftTGPlayer.get(player);
			mctg.setGlobalManaSources(msg.w, msg.u, msg.b, msg.r, msg.g);
		}
	}
	
	private void serverReceivedMessage(MCTGManaMessage msg, MagicraftTGPlayer mctg)
	{
		if(msg.type == MCTGPacketHandler.MANA_GLOBAL_SOURCE_SET)
		{
			mctg.setGlobalManaSources(msg.w, msg.u, msg.b, msg.r, msg.g);
		}
	}

	//////////////////////////////////////////////////////////////
	public static class MCTGManaMessage implements IMessage {
		//private long playerUUIDLeast, playerUUIDMost;
		private char type;
		private int w, u, b, r, g, c;
		

		// this constructor is required otherwise you'll get errors (used
		// somewhere in fml through reflection)
		public MCTGManaMessage() {
		}

		public MCTGManaMessage(char type) {
			this.type = type;
			
		}
		
		public MCTGManaMessage(char type, int w, int u, int b, int r, int g, int c) {
			this.type = type;
			this.w = w;
			this.u = u;
			this.b = b;
			this.r = r;
			this.g = g;
			this.c = c;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			// the order is important
			//this.playerUUIDLeast = buf.readLong();
			//this.playerUUIDMost = buf.readLong();
			this.type = buf.readChar();
			//System.out.println("[MCTG] " + this.type);
			this.w = buf.readInt();
			this.u = buf.readInt();
			this.b = buf.readInt();
			this.r = buf.readInt();
			this.g = buf.readInt();
			this.c = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf) {
			//buf.writeLong(playerUUIDLeast);
			//buf.writeLong(playerUUIDMost);
			buf.writeChar(type);
			buf.writeInt(w);
			buf.writeInt(u);
			buf.writeInt(b);
			buf.writeInt(r);
			buf.writeInt(g);
			buf.writeInt(c);
		}
	}
}
