package clashsoft.modoptionsapi.client.gui;

import cpw.mods.fml.common.ObfuscationReflectionHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.EnumOptions;
import net.minecraft.client.settings.GameSettings;

public class GuiMoreOptions extends GuiOptions
{
	public static final EnumOptions[]	relevantOptions	= new EnumOptions[] { EnumOptions.MUSIC, EnumOptions.SOUND, EnumOptions.INVERT_MOUSE, EnumOptions.SENSITIVITY, EnumOptions.FOV, EnumOptions.DIFFICULTY, EnumOptions.TOUCHSCREEN };
	
	public GameSettings					options;
	
	public GuiMoreOptions(GuiOptions options)
	{
		super((GuiScreen) ObfuscationReflectionHelper.getPrivateValue(GuiOptions.class, options, 1), Minecraft.getMinecraft().gameSettings);
		this.options = Minecraft.getMinecraft().gameSettings;
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		int i = 0;
		this.screenTitle = I18n.getString("options.title");
		EnumOptions[] aenumoptions = relevantOptions;
		int j = aenumoptions.length;
		
		for (int k = 0; k < j; ++k)
		{
			EnumOptions enumoptions = aenumoptions[k];
			
			if (enumoptions.getEnumFloat())
			{
				this.buttonList.add(new GuiSlider(enumoptions.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), enumoptions, this.options.getKeyBinding(enumoptions), this.options.getOptionFloatValue(enumoptions)));
			}
			else
			{
				GuiSmallButton guismallbutton = new GuiSmallButton(enumoptions.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), enumoptions, this.options.getKeyBinding(enumoptions));
				
				if (enumoptions == EnumOptions.DIFFICULTY && this.mc.theWorld != null && this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
				{
					guismallbutton.enabled = false;
					guismallbutton.displayString = I18n.getString("options.difficulty") + ": " + I18n.getString("options.difficulty.hardcore");
				}
				
				this.buttonList.add(guismallbutton);
			}
			
			++i;
		}
		
		this.buttonList.add(new GuiButton(101, this.width / 2 - 152, this.height / 6 + 84, 150, 20, I18n.getString("options.video")));
		this.buttonList.add(new GuiButton(100, this.width / 2 + 2, this.height / 6 + 84, 150, 20, I18n.getString("options.controls")));
		this.buttonList.add(new GuiButton(102, this.width / 2 - 152, this.height / 6 + 108, 150, 20, I18n.getString("options.language")));
		this.buttonList.add(new GuiButton(103, this.width / 2 + 2, this.height / 6 + 108, 150, 20, I18n.getString("options.multiplayer.title")));
		this.buttonList.add(new GuiButton(105, this.width / 2 - 152, this.height / 6 + 132, 150, 20, I18n.getString("options.resourcepack") + "..."));
		this.buttonList.add(new GuiButton(104, this.width / 2 + 2, this.height / 6 + 132, 150, 20, I18n.getString("options.snooper.view")));
		
		this.buttonList.add(new GuiButton(199, this.width / 2 - 152, this.height / 6 + 156, 304, 20, I18n.getString("options.modoptions") + "..."));
		
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 180, I18n.getString("gui.done")));
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled && par1GuiButton.id == 199)
		{
			this.mc.displayGuiScreen(new GuiModOptions(this, ""));
		}
		super.actionPerformed(par1GuiButton);
	}
}
