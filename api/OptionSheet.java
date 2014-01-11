package clashsoft.modoptionsapi.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import clashsoft.cslib.util.CSLog;
import clashsoft.modoptionsapi.ModOptionsAPI;
import clashsoft.modoptionsapi.api.option.*;

public class OptionSheet extends OptionCategory implements IOptionChangeListener
{
	protected File			file;
	protected List<String>	lines;
	
	public OptionSheet(String modName, File file)
	{
		super(modName);
		this.file = file;
		this.load();
		
		ModOptionsAPI.addCategory(this);
	}
	
	public void addOption(Option option)
	{
		if (option != null)
			option.listener = this;
		this.getOption(option);
	}
	
	public Option getOption(Option option)
	{
		Option loadedOption = this.options.get(option.name);
		if (loadedOption == null)
		{
			option.listener = this;
			super.addOption(option);
			return option;
		}
		else
		{
			loadedOption.defaultValue = option.defaultValue;
			
			if (option instanceof OptionString && loadedOption instanceof OptionString)
				((OptionString)loadedOption).maxLength = ((OptionString)option).maxLength;
			else if (option instanceof OptionNumber && loadedOption instanceof OptionNumber)
			{
				((OptionNumber)loadedOption).minValue = ((OptionNumber)option).minValue;
				((OptionNumber)loadedOption).maxValue = ((OptionNumber)option).maxValue;
			}
			
			return loadedOption;
		}
	}
	
	public Option addOptionByType(String type, String name, String value)
	{
		Option theOption = null;
		
		if ("s".equals(type))
			theOption = new OptionString(name, value, null, 0).setListener(this);
		else if ("b".equals(type))
			theOption = new OptionBoolean(name, Boolean.parseBoolean(value), true).setListener(this);
		else if ("d".equals(type))
			theOption = new OptionNumber(name, Double.parseDouble(value), 0D).setListener(this);
		
		this.addOption(theOption);
		return theOption;
	}
	
	public String getTypeFromOption(Option option)
	{
		if (option instanceof OptionString)
			return "s";
		else if (option instanceof OptionBoolean)
			return "b";
		else if (option instanceof OptionNumber)
			return "d";
		return "";
	}
	
	public boolean getBoolean(String name, boolean _default)
	{
		return ((OptionBoolean)this.getOption(new OptionBoolean(name, _default, _default).setListener(this))).value;
	}
	
	public String getString(String name, String _default)
	{
		return getString(name, _default, 0);
	}
	
	public String getString(String name, String _default, int maxLength)
	{
		return ((OptionString)this.getOption(new OptionString(name, _default, _default, maxLength).setListener(this))).value;
	}
	
	public int getInt(String name, int _default)
	{
		return (int)getDouble(name, _default);
	}
	
	public int getInt(String name, int _default, int minValue, int maxValue)
	{
		return (int)getDouble(name, _default, minValue, maxValue);
	}
	
	public double getDouble(String name, double _default)
	{
		return getDouble(name, _default, Double.MIN_VALUE, Double.MAX_VALUE);
	}
	
	public double getDouble(String name, double _default, double minValue, double maxValue)
	{
		return ((OptionNumber)this.getOption(new OptionNumber(name, _default, _default, minValue, maxValue).setListener(this))).value;
	}
	
	public void load()
	{
		try
		{
			lines = FileUtils.readLines(file);
		}
		catch (IOException ex)
		{
			CSLog.error(ex);
			lines = new ArrayList();
		}
		
		for (String line : lines)
		{
			if (line.startsWith("#"))
				continue;
			
			int pos1 = line.indexOf(':');
			int pos2 = line.indexOf('=');
			
			if (pos2 == -1)
				continue;
			
			String type;
			if (pos1 == -1)
				type = "s";
			else
				type = line.substring(0, pos1).toLowerCase();
			
			String name = line.substring(pos1 + 1, pos2);
			String value = line.substring(pos2 + 1, line.length());
			
			this.addOptionByType(type, name, value);
		}
	}
	
	public void save()
	{
		if (lines != null)
			lines.clear();
		else
			lines = new ArrayList<String>(options.size());
		
		for (Option option : options.values())
		{
			String string = getTypeFromOption(option) + ":" + option.name + "=" + option.value;
			lines.add(string);
		}
		
		try
		{
			if (!file.exists())
				file.createNewFile();
			
			FileUtils.writeLines(file, lines);
		}
		catch (IOException ex)
		{
			CSLog.error(ex);
		}
	}
	
	@Override
	public void addCategory(OptionCategory category)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean optionChanged(String name, Object oldValue, Object newValue)
	{
		Option option = this.options.get(name);
		
		if (option != null)
		{
			option.value = newValue;
			save();
			return true;
		}
		else
			return false;
	}	
}
