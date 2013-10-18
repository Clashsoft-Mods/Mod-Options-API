package clashsoft.modoptionsapi.api.option;

import java.util.HashMap;
import java.util.Map;

import clashsoft.modoptionsapi.api.IModOptionListEntry;

public class OptionCategory implements IModOptionListEntry
{
	public OptionCategory					superCategory	= null;
	
	public String							name;
	
	public Map<String, IModOptionListEntry>	entrys			= new HashMap();
	public Map<String, Option>				options			= new HashMap();
	public Map<String, OptionCategory>		subCategorys	= new HashMap();
	
	public OptionCategory(String name)
	{
		this.name = name;
	}
	
	public void addOption(Option option)
	{
		this.entrys.put(option.name, option);
		this.options.put(option.name, option);
	}
	
	public void addCategory(OptionCategory category)
	{
		category.superCategory = this;
		this.entrys.put(category.name, category);
		this.subCategorys.put(category.name, category);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public void resetPressed()
	{
		for (IModOptionListEntry entry : entrys.values())
			entry.resetPressed();
	}
	
	@Override
	public boolean isValid()
	{
		for (IModOptionListEntry entry : entrys.values())
			if (!entry.isValid())
				return false;
		return true;
	}
	
	@Override
	public String getError()
	{
		int errors = getErrors();
		return errors > 0 ? (errors + " error" + (errors != 1 ? "s" : "")) : "";
	}
	
	public int getErrors()
	{
		int result = 0;
		for (Option o : options.values())
			if (!o.isValid())
				result++;
		for (OptionCategory category : subCategorys.values())
			result += category.getErrors();
		return result;
	}
}
