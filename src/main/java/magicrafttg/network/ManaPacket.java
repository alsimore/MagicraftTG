package magicrafttg.network;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import magicrafttg.entity.MCTGPlayerProperties;
import magicrafttg.network.ManaPacket.MCTGManaMessage;
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
public class ManaPacket implements IMessageHandler<MCTGManaMessage, IMessage> {
	
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
	    	final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
	    	final MCTGPlayerProperties mctg = MCTGPlayerProperties.get(player);
	    	player.getServerForPlayer().addScheduledTask(new Runnable()
	    	{
	    	  public void run() {
	    	    serverReceivedMessage(msg, mctg, player);
	    	  }
	    	});
	    }
		
		return null;
	}
	
	
	private void clientReceivedMessage(MCTGManaMessage msg)
	{
		if(msg.type == PacketHandler.MANA_UPDATE)
		{
			int[] manaAmounts = {msg.w, msg.u, msg.b, msg.r, msg.g, msg.c};
			MCTGNetworkManager.updateManaServerToClient(Minecraft.getMinecraft().thePlayer, manaAmounts);
			// update player data
			/*EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			MCTGPlayerProperties mctg = MCTGPlayerProperties.get(player);
			//System.out.println("[MCTG] Client message received by " + player.getUniqueID().toString()
			//		 + "(" + System.identityHashCode(player) + ")");
			//System.out.println("[MCTG] ### " + player.getGameProfile().getId().toString() + "\n");
			mctg.updateMana(msg.w, msg.u, msg.b, msg.r, msg.g, msg.c);*/
		}
		else if(msg.type == PacketHandler.MANA_GLOBAL_SOURCE_SET)
		{
			System.out.println("Received mana update");
			System.out.println(msg.w);
			System.out.println(msg.u);
			System.out.println(msg.b);
			System.out.println(msg.r);
			System.out.println(msg.g);
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			MCTGPlayerProperties mctg = MCTGPlayerProperties.get(player);
			mctg.setGlobalManaSources(msg.w, msg.u, msg.b, msg.r, msg.g);
		}
	}
	
	private void serverReceivedMessage(MCTGManaMessage msg, MCTGPlayerProperties mctg, EntityPlayer player)
	{
		if(msg.type == PacketHandler.MANA_GLOBAL_SOURCE_SET)
		{
			mctg.setGlobalManaSources(msg.w, msg.u, msg.b, msg.r, msg.g);
		}
		else if(msg.type == PacketHandler.MANA_SRC_REQUEST)
		{
			MCTGPlayerProperties serverMctg = MCTGPlayerProperties.get(player);
			System.out.println("Server player " + player);
			int[] sources = serverMctg.getGlobalSourceNumbers();
			for(int s : sources)
				System.out.println(s);
			IMessage reply = new ManaPacket.MCTGManaMessage(PacketHandler.MANA_GLOBAL_SOURCE_SET,
					sources[0], sources[1], sources[2], sources[3], sources[4], 0);
			PacketHandler.net.sendTo(reply, (EntityPlayerMP)player);
		}
	}

	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
	public static class MCTGManaMessage implements IMessage {
		
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
