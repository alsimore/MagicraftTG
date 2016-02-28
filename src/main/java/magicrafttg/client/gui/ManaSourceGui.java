package magicrafttg.client.gui;

import java.io.IOException;
import java.util.UUID;

import magicrafttg.entity.MCTGPlayerProperties;
import magicrafttg.mana.ManaColor;
import magicrafttg.network.ManaPacket;
import magicrafttg.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ManaSourceGui extends GuiScreen {

	private GuiButton buttonBlack;
	private GuiButton buttonWhite;
	private GuiButton buttonRed;
	private GuiButton buttonBlue;
	private GuiButton buttonGreen;
	private GuiButton buttonDone;
	
	private GuiButton blackPlus;
	private GuiButton blackMinus;
	private GuiButton whitePlus;
	private GuiButton whiteMinus;
	private GuiButton redPlus;
	private GuiButton redMinus;
	private GuiButton greenPlus;
	private GuiButton greenMinus;
	private GuiButton bluePlus;
	private GuiButton blueMinus;
	
	private int chosen[] = new int[5];
	private int totalChosen = 0;
	
	@Override
	public void initGui()
	{
		buttonBlack = new GuiButton(0, this.width / 2 - 35, this.height / 2 - 72, 70, 20, "Black 0");
		this.buttonList.add(buttonBlack);
		blackPlus = new GuiButton(6, buttonBlack.xPosition - 20, buttonBlack.yPosition, 20, 20, "+");
		this.buttonList.add(blackPlus);
		blackMinus = new GuiButton(7, buttonBlack.xPosition + buttonBlack.width, buttonBlack.yPosition, 20, 20, "-");
		this.buttonList.add(blackMinus);
		
		buttonWhite = new GuiButton(1, this.width / 2 - 35, this.height / 2 - 48, 70, 20, "White 0");
		this.buttonList.add(buttonWhite);
		whitePlus = new GuiButton(8, buttonWhite.xPosition - 20, buttonWhite.yPosition, 20, 20, "+");
		this.buttonList.add(whitePlus);
		whiteMinus = new GuiButton(9, buttonWhite.xPosition + buttonWhite.width, buttonWhite.yPosition, 20, 20, "-");
		this.buttonList.add(whiteMinus);
		
		buttonRed = new GuiButton(2, this.width / 2 - 35, this.height / 2 - 24, 70, 20, "Red 0");
		this.buttonList.add(buttonRed);
		redPlus = new GuiButton(10, buttonRed.xPosition - 20, buttonRed.yPosition, 20, 20, "+");
		this.buttonList.add(redPlus);
		redMinus = new GuiButton(11, buttonRed.xPosition + buttonRed.width, buttonRed.yPosition, 20, 20, "-");
		this.buttonList.add(redMinus);
		
		buttonBlue = new GuiButton(3, this.width / 2 - 35, this.height / 2, 70, 20, "Blue 0");
		this.buttonList.add(buttonBlue);
		bluePlus = new GuiButton(12, buttonBlue.xPosition - 20, buttonBlue.yPosition, 20, 20, "+");
		this.buttonList.add(bluePlus);
		blueMinus = new GuiButton(13, buttonBlue.xPosition + buttonBlue.width, buttonBlue.yPosition, 20, 20, "-");
		this.buttonList.add(blueMinus);
		
		buttonGreen = new GuiButton(4, this.width / 2 - 35, this.height / 2 + 24, 70, 20, "Green 0");
		this.buttonList.add(buttonGreen);
		greenPlus = new GuiButton(14, buttonGreen.xPosition - 20, buttonGreen.yPosition, 20, 20, "+");
		this.buttonList.add(greenPlus);
		greenMinus = new GuiButton(15, buttonGreen.xPosition + buttonGreen.width, buttonGreen.yPosition, 20, 20, "-");
		this.buttonList.add(greenMinus);
		
		buttonDone = new GuiButton(5, this.width / 2 - 35, this.height / 2 + 48, 70, 20, I18n.format("gui.done", new Object[0]));
		this.buttonList.add(buttonDone);
		//System.out.println("ManaSourceGui created");
		
		displayAmounts();
	}
	
	private void displayAmounts()
	{
		
		MCTGPlayerProperties mctg = MCTGPlayerProperties.get(Minecraft.getMinecraft().thePlayer);
		//mctg.updateManaFromServer();
		int[] sources = mctg.getGlobalSourceNumbers();
		
		//System.out.println("Mana amounts: " + sources.length);
		for(int i = 0; i < 5; ++i)
		{
			chosen[i] = sources[i];
			totalChosen += chosen[i]; 
			System.out.println(sources[i]);
		}
		
		buttonWhite.displayString = "White " + chosen[0];
		buttonBlue.displayString = "Blue " + chosen[1];
		buttonBlack.displayString = "Black " + chosen[2];
		buttonRed.displayString = "Red " + chosen[3];
		buttonGreen.displayString = "Green " + chosen[4];
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
	    this.drawDefaultBackground();
	    super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() 
	{
	    return false;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException 
	{
		if (button == this.buttonDone)
	    {
			System.out.println("Sending chosen mana to server");
			for(int m : chosen)
			{
				System.out.println(m);
			}
			
			IMessage msg = new ManaPacket.MCTGManaMessage(PacketHandler.MANA_GLOBAL_SOURCE_SET, 
					// The order is white, blue, black, red, green, colourless.
					this.chosen[0], this.chosen[1], this.chosen[2], this.chosen[3], this.chosen[4], 0);
	        PacketHandler.net.sendToServer(msg);
	        
	        MCTGPlayerProperties mctg = MCTGPlayerProperties.get(Minecraft.getMinecraft().thePlayer);
	        mctg.setGlobalManaSources(this.chosen[0], this.chosen[1], this.chosen[2], this.chosen[3], this.chosen[4]);
	        
	        this.mc.displayGuiScreen(null);
	        if (this.mc.currentScreen == null)
	            this.mc.setIngameFocus();
	    }
		else 
		{
			if (button == this.blackPlus) 
			{
				int ind = ManaColor.BLACK.ordinal();
				if(this.totalChosen < 5)
				{
			        this.chosen[ind] += 1;
			        buttonBlack.displayString = "Black " + this.chosen[ind];
			        ++this.totalChosen;
				}
		    }
			else if (button == this.blackMinus) 
			{
				int ind = ManaColor.BLACK.ordinal();
		        this.chosen[ind] -= 1;
		        if(this.chosen[ind] < 0)
		        {
		        	this.chosen[ind] = 0;
		        }
		        else
		        	--this.totalChosen;
		        buttonBlack.displayString = "Black " + this.chosen[ind];
		    }
			else if (button == this.whitePlus) 
			{
				int ind = ManaColor.WHITE.ordinal();
				if(this.totalChosen < 5)
				{
			        this.chosen[ind] += 1;
			        buttonWhite.displayString = "White " + this.chosen[ind];
			        ++this.totalChosen;
				}
		    }
			else if (button == this.whiteMinus) 
			{
				int ind = ManaColor.WHITE.ordinal();
		        this.chosen[ind] -= 1;
		        if(this.chosen[ind] < 0)
		        {
		        	this.chosen[ind] = 0;
		        }
		        else
		        	--this.totalChosen;
		        buttonWhite.displayString = "White " + this.chosen[ind];
		    }
			else if (button == this.redPlus) 
			{
				int ind = ManaColor.RED.ordinal();
				if(this.totalChosen < 5)
				{
			        this.chosen[ind] += 1;
			        buttonRed.displayString = "Red " + this.chosen[ind];
			        ++this.totalChosen;
				}
		    }
			else if (button == this.redMinus) 
			{
				int ind = ManaColor.RED.ordinal();
		        this.chosen[ind] -= 1;
		        if(this.chosen[ind] < 0)
		        {
		        	this.chosen[ind] = 0;
		        }
		        else
		        	--this.totalChosen;
		        buttonRed.displayString = "Red " + this.chosen[ind];
		    }
			else if (button == this.bluePlus) 
			{
				int ind = ManaColor.BLUE.ordinal();
				if(this.totalChosen < 5)
				{
			        this.chosen[ind] += 1;
			        buttonBlue.displayString = "Blue " + this.chosen[ind];
			        ++this.totalChosen;
				}
		    }
			else if (button == this.blueMinus) 
			{
				int ind = ManaColor.BLUE.ordinal();
		        this.chosen[ind] -= 1;
		        if(this.chosen[ind] < 0)
		        {
		        	this.chosen[ind] = 0;
		        }
		        else
		        	--this.totalChosen;
		        buttonBlue.displayString = "Blue " + this.chosen[ind];
		    }
			else if (button == this.greenPlus) 
			{
				int ind = ManaColor.GREEN.ordinal();
				if(this.totalChosen < 5)
				{
			        this.chosen[ind] += 1;
			        buttonGreen.displayString = "Green " + this.chosen[ind];
			        ++this.totalChosen;
				}
		    }
			else if (button == this.greenMinus) 
			{
				int ind = ManaColor.GREEN.ordinal();
		        this.chosen[ind] -= 1;
		        if(this.chosen[ind] < 0)
		        {
		        	this.chosen[ind] = 0;
		        }
		        else
		        	--this.totalChosen;
		        buttonGreen.displayString = "Green " + this.chosen[ind];
		    }
			
		}
		//System.out.println("Total mana chosen " + this.totalChosen);
	}
}
