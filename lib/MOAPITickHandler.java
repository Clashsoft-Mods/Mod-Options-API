package clashsoft.modoptionsapi.lib;

import java.util.EnumSet;

import clashsoft.modoptionsapi.gui.GuiMoreOptions;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;

@SideOnly(Side.CLIENT)
public class MOAPITickHandler implements ITickHandler
{
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		updateOptions();
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		updateOptions();
	}
	
	public void updateOptions()
	{
		if (Minecraft.getMinecraft().currentScreen instanceof GuiOptions)
		{
			Minecraft.getMinecraft().displayGuiScreen(new GuiMoreOptions((GuiOptions) Minecraft.getMinecraft().currentScreen));
		}
	}
	
	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT);
	}
	
	@Override
	public String getLabel()
	{
		return "ModOptionsAPI";
	}
	
}
