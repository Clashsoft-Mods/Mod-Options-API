package clashsoft.modoptionsapi;

import clashsoft.clashsoftapi.ClashsoftAPI;
import clashsoft.clashsoftapi.util.CSUpdate;
import clashsoft.clashsoftapi.util.update.ModUpdate;
import clashsoft.modoptionsapi.api.IOptionChangeListener;
import clashsoft.modoptionsapi.api.OptionSheet;
import clashsoft.modoptionsapi.api.option.Option;
import clashsoft.modoptionsapi.api.option.OptionCategory;
import clashsoft.modoptionsapi.gui.GuiModOptions;
import clashsoft.modoptionsapi.lib.MOAPITickHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
	
	// Mod stuff
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		/* Example code on how to use the new OptionSheet system
		 */ 
		OptionSheet sheet = new OptionSheet("Mod Options API", event.getSuggestedConfigurationFile());
		
		//yourField = sheet.getString("Your Setting Name", "default value");
		//yourField1 = sheet.getString("Your Second Setting Name", "default value", 13 (max length));
		//yourField2 = sheet.getDouble("Your Third Setting Name", 13.5D, 0D, 20D);
		
		sheet.save();
		
		//*/
	}
	
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
			ModUpdate update = CSUpdate.checkForUpdate("Mod Options API", "moapi", ClashsoftAPI.VERSION);
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
		OptionCategory category = GuiModOptions.rootCategory;
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
