package clashsoft.modoptionsapi;

import clashsoft.cslib.minecraft.CSLib;
import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.cslib.minecraft.update.ModUpdate;
import clashsoft.modoptionsapi.api.IOptionChangeListener;
import clashsoft.modoptionsapi.api.option.Option;
import clashsoft.modoptionsapi.api.option.OptionCategory;
import clashsoft.modoptionsapi.handlers.MOAPITickHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

@Mod(modid = "ModOptionsAPI", name = "Mod Options API", version = ModOptionsAPI.VERSION)
public class ModOptionsAPI
{
	public static final int								REVISION				= 1;
	public static final String							VERSION					= CSUpdate.CURRENT_VERSION + "-" + REVISION;
	public static OptionCategory		rootCategory	= new OptionCategory("ROOT");
	
	// Mod stuff
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			TickRegistry.registerTickHandler(new MOAPITickHandler(), Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(this);
		
		LanguageRegistry.instance().addStringLocalization("options.modoptions", "Mod Options");
		LanguageRegistry.instance().addStringLocalization("options.editstring", "Edit String");
		LanguageRegistry.instance().addStringLocalization("options.reset", "Reset");
		
		LanguageRegistry.instance().addStringLocalization("options.modoptions", "de_DE", "Mod Optionen");
		LanguageRegistry.instance().addStringLocalization("options.editstring", "de_DE", "Zeichenkette bearbeiten");
		LanguageRegistry.instance().addStringLocalization("options.reset", "de_DE", "Reset");
	}
	
	@ForgeSubscribe
	public void playerJoined(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			ModUpdate update = CSUpdate.checkForUpdate("Mod Options API", "moapi", CSLib.VERSION);
			CSUpdate.notifyUpdate((EntityPlayer) event.entity, "Mod Options API", update);
		}
	}
	
	// API stuff
	
	public static void addCategory(OptionCategory category, String... path)
	{
		getCategoryFromPath(path).addCategory(category);
	}
	
	public static void addOption(Option option, IOptionChangeListener listener, String... path)
	{
		option.setListener(listener);
		getCategoryFromPath(path).addOption(option);
	}
	
	private static OptionCategory getCategoryFromPath(String... path)
	{
		OptionCategory category = ModOptionsAPI.rootCategory;
		for (String s : path)
		{
			OptionCategory c = category.subCategorys.get(s);
			if (c == null)
			{
				c = new OptionCategory(s);
				category.addCategory(c);
			}
			category = c;
		}
		return category;
	}
}
