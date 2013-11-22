package clashsoft.modoptionsapi.client.gui;

import java.util.HashMap;
import java.util.Map;

import clashsoft.modoptionsapi.ModOptionsAPI;
import clashsoft.modoptionsapi.api.option.OptionCategory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiModOptions extends GuiScreen
{
	protected GuiScreen					parent;
	
	public int							mouseX;
	public int							mouseY;
	
	public GuiButton					back;
	public GuiModOptionsSlot			optionsSlot;
	
	public Map<String, GuiButton>		buttons			= new HashMap();
	public Map<GuiButton, String>		buttonIDs		= new HashMap();
	
	public String						subscreenTitle	= "";
	public String						screenTitle		= "Mod Options";
	
	public OptionCategory				currentCategory	= ModOptionsAPI.rootCategory;
	
	public GuiModOptions(GuiScreen parent, String subscreenTitle)
	{
		this.parent = parent;
		this.subscreenTitle = subscreenTitle;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		this.mouseX = par1;
		this.mouseY = par2;
		
		this.drawDefaultBackground();
		this.optionsSlot.drawScreen(par1, par2, par3);
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 15, 16777215);
		
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	public void initGui()
	{	
		this.screenTitle = getScreenTitle();
		
		this.optionsSlot = new GuiModOptionsSlot(this);
		
		this.back = new GuiButton(1, 20, 10, 20, 20, "<");
		this.back.enabled = false;
		
		this.buttonList.add(back);
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 40, I18n.getString("gui.done")));
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			if (par1GuiButton.id == 0)
			{
				this.mc.displayGuiScreen(parent);
			}
			else if (par1GuiButton.id == 1)
			{
				if (currentCategory != null)
					setCategory(currentCategory.superCategory);
			}
			else
			{
				this.optionsSlot.buttonPressed(buttonIDs.get(par1GuiButton), par1GuiButton);
			}
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
		this.optionsSlot.keyTyped(par1, par2);
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		
		if (par3 == 0)
		{
			for (GuiButton button : buttons.values())
			{
				if (button.mousePressed(this.mc, par1, par2))
				{
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.actionPerformed(button);
				}
			}
		}
	}
	
	public void setCategory(OptionCategory category)
	{
		currentCategory = category;
		
		if (currentCategory == ModOptionsAPI.rootCategory)
		{
			this.subscreenTitle = "";
			back.enabled = false;
		}
		else
		{
			this.subscreenTitle = category.name;
			back.enabled = true;
		}
		
		this.screenTitle = getScreenTitle();
		this.optionsSlot.setCategory(category);
	}
	
	public String getScreenTitle()
	{
		return I18n.getString("options.modoptions") + (isSubscreen() ? (" - " + this.subscreenTitle) : "");
	}
	
	public boolean isSubscreen()
	{
		return currentCategory != ModOptionsAPI.rootCategory;
	}
	
	public void addButton(String id, GuiButton button)
	{
		this.buttons.put(id, button);
		this.buttonIDs.put(button, id);
	}
}
