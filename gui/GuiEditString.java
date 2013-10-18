package clashsoft.modoptionsapi.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import clashsoft.modoptionsapi.api.option.OptionString;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiEditString extends GuiScreen
{
	public static final ResourceLocation bg = new ResourceLocation("textures/gui/demo_background.png");
	
	public GuiScreen parent;
	public OptionString string;
	
	public GuiTextField text;
	
	public GuiEditString(GuiScreen parent, OptionString string)
	{
		this.parent = parent;
		this.string = string;
	}
	
	@Override
	public void initGui()
	{
		text = new GuiTextField(fontRenderer, width / 2 - 100, height / 2 - 60, 200, 100);
		text.setText(string.value);
		text.setMaxStringLength(string.maxLength < 1 ? Integer.MAX_VALUE : string.maxLength);
		
		this.buttonList.add(new GuiButton(0, width / 2 - 102, height / 2 + 50, 100, 20, I18n.getString("gui.done")));
		this.buttonList.add(new GuiButton(1, width / 2 + 2, height / 2 + 50, 100, 20, I18n.getString("gui.cancel")));
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		text.mouseClicked(par1, par2, par3);
	}
	
	@Override
	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
		text.textboxKeyTyped(par1, par2);
		
		if (par2 == Keyboard.KEY_ESCAPE)
			mc.displayGuiScreen(parent);
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.id == 0)
			string.setValue(text.getText());
		mc.displayGuiScreen(parent);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		parent.drawScreen(-1000, -1000, par3);
		this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bg);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);
        
        super.drawScreen(par1, par2, par3);
		
		text.drawTextBox();
		
		String s = I18n.getString("options.editstring");
		this.fontRenderer.drawString(s, (width - fontRenderer.getStringWidth(s)) / 2, height / 2 - 75, 0xFFFFFF, true);
	}
}
