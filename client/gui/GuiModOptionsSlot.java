package clashsoft.modoptionsapi.client.gui;

import java.util.ArrayList;
import java.util.List;

import clashsoft.modoptionsapi.api.IModOptionListEntry;
import clashsoft.modoptionsapi.api.option.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

@SideOnly(Side.CLIENT)
public class GuiModOptionsSlot extends GuiSlot
{
	public GuiModOptions				modOptions;
	
	public int							selectedIndex	= 0;
	
	public OptionCategory				category		= null;
	public List<IModOptionListEntry>	entrys			= new ArrayList();
	
	public List<GuiTextField>			textFields		= new ArrayList();
	
	public GuiModOptionsSlot(GuiModOptions modOptions)
	{
		super(Minecraft.getMinecraft(), modOptions.width, modOptions.height, 32, modOptions.height - 64, 22);
		this.modOptions = modOptions;
		this.setCategory(this.modOptions.currentCategory);
	}
	
	/**
	 * Gets the size of the current slot list.
	 */
	@Override
	protected int getSize()
	{
		return entrys.size();
	}
	
	/**
	 * the element in the slot that was clicked, boolean for wether it was
	 * double clicked or not
	 */
	@Override
	protected void elementClicked(int par1, boolean par2)
	{
		try
		{
			selectedIndex = par1;
			if (entrys.get(par1) instanceof OptionCategory)
				modOptions.setCategory((OptionCategory) entrys.get(par1));
			if (par2 && entrys.get(par1) instanceof OptionString)
				Minecraft.getMinecraft().displayGuiScreen(new GuiEditString(this.modOptions, (OptionString) entrys.get(par1)));
		}
		catch (Exception ex)
		{
		}
	}
	
	/**
	 * returns true if the element passed in is currently selected
	 */
	@Override
	protected boolean isSelected(int par1)
	{
		return selectedIndex == par1;
	}
	
	/**
	 * return the height of the content being scrolled
	 */
	@Override
	protected int getContentHeight()
	{
		return getSize() * 22;
	}
	
	@Override
	protected void drawBackground()
	{
		this.modOptions.drawDefaultBackground();
	}
	
	@Override
	protected void drawSlot(int id, int x, int y, int l, Tessellator tessellator)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (id >= 0 && id < getSize())
		{
			IModOptionListEntry entry = entrys.get(id);
			
			mc.fontRenderer.drawString(entry.getName(), x + 2, y + 1, 0xFFFFFF, true);
			if (!entry.isValid())
			{
				mc.fontRenderer.drawString(entry.getError(), x + 2, y + 11, 0xFF0000, true);
			}
			
			if (entry instanceof OptionCategory)
			{
				mc.fontRenderer.drawString(">", x + 200, y + 6, 0xFFFFFF, true);
			}
			else
			{
				GuiButton resetButton = this.modOptions.buttons.get("reset." + entry.getName());
				if (resetButton != null)
				{
					resetButton.xPosition = x + 180;
					resetButton.yPosition = y - 1;
					resetButton.drawButton(mc, modOptions.mouseX, modOptions.mouseY);
				}
				
				if (entry instanceof OptionBoolean)
				{
					GuiButton button = this.modOptions.buttons.get(entry.getName());
					button.displayString = ((OptionBoolean) entry).value + "";
					button.xPosition = x + 144;
					button.yPosition = y - 1;
					button.drawButton(mc, modOptions.mouseX, modOptions.mouseY);
				}
				else if (entry instanceof OptionString)
				{
					mc.fontRenderer.drawString(EnumChatFormatting.ITALIC + ((OptionString) entry).value, x + 176 - mc.fontRenderer.getStringWidth(((OptionString) entry).value), y + 11, 0xFFFFFF);
				}
				else if (entry instanceof OptionNumber)
				{
					String valueString = ((OptionNumber) entry).value.toString();
					int valueStringWidth = mc.fontRenderer.getStringWidth(valueString);
					mc.fontRenderer.drawString(valueString, x + 138 - valueStringWidth / 2, y + 6, 0xFFFFFF);
					
					GuiButton plusButton = this.modOptions.buttons.get("+." + entry.getName());
					plusButton.xPosition = x + 166;
					plusButton.yPosition = y - 1;
					plusButton.drawButton(mc, modOptions.mouseX, modOptions.mouseY);
					
					GuiButton minusButton = this.modOptions.buttons.get("-." + entry.getName());
					minusButton.xPosition = x + 100;
					minusButton.yPosition = y - 1;
					minusButton.drawButton(mc, modOptions.mouseX, modOptions.mouseY);
				}
			}
		}
	}
	
	public void buttonPressed(String id, GuiButton button)
	{
		String id1 = id.substring(id.indexOf('.') + 1);
		
		// Booleans
		if (button.id >= 1000 && button.id < 1100)
		{
			OptionBoolean option = (OptionBoolean) category.options.get(id);
			option.setValue(!option.value);
		}
		else if (button.id >= 2000 && button.id < 3000)
		{
			OptionNumber option = (OptionNumber) category.options.get(id1);
			option.increase(1D);
		}
		else if (button.id >= 3000 && button.id < 4000)
		{
			OptionNumber option = (OptionNumber) category.options.get(id1);
			option.increase(-1D);
		}
		if (button.id >= 10000)
		{
			Option option = category.options.get(id1);
			option.resetPressed();
		}
	}
	
	public void keyTyped(char par1, int par2)
	{
		
	}
	
	public void setCategory(OptionCategory category)
	{
		this.category = category;
		
		entrys.clear();
		entrys.addAll(category.subCategorys.values());
		
		modOptions.buttons.clear();
		
		for (Option o : category.options.values())
		{
			entrys.add(o);
			
			GuiButton reset = new GuiButton(10000 + entrys.size(), 0, 0, 32, 20, I18n.getString("options.reset"));
			reset.enabled = o.defaultValue != null;
			modOptions.addButton("reset." + o.getName(), reset);
			
			if (o instanceof OptionBoolean)
			{
				int buttonID = 1000 + entrys.size();
				modOptions.addButton(o.getName(), new GuiButton(buttonID, 0, 0, 32, 20, ""));
			}
			else if (o instanceof OptionNumber)
			{
				int buttonID = 2000 + entrys.size();
				int button2ID = 3000 + entrys.size();
				modOptions.addButton("+." + o.getName(), new GuiButton(buttonID, 0, 0, 10, 20, "+"));
				modOptions.addButton("-." + o.getName(), new GuiButton(button2ID, 0, 0, 10, 20, "-"));
			}
		}
	}
}
