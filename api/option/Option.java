package clashsoft.modoptionsapi.api.option;

import clashsoft.modoptionsapi.api.IModOptionListEntry;
import clashsoft.modoptionsapi.api.IOptionChangeListener;

public abstract class Option<T> implements IModOptionListEntry
{
	public String	name;
	public String	comment;
	public String	category;
	public T		value;
	public T		defaultValue;
	
	public IOptionChangeListener listener;
	
	public Option(String name, T value, T defaultValue)
	{
		this.name = name;
		this.value = value;
		this.defaultValue = defaultValue;
	}
	
	public void setValue(T value)
	{
		if (listener != null)
		{
			if (listener.optionChanged(name, this.value, value))
				this.value = value;
		}
		else
			this.value = value;
	}
	
	public Option setListener(IOptionChangeListener listener)
	{
		this.listener = listener;
		return this;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public void resetPressed()
	{
		if (this.defaultValue != null)
			this.setValue(defaultValue);
	}
	
	public Option setComment(String comment)
	{
		this.comment = comment;
		return this;
	}
	
	public Option setCategory(String category)
	{
		this.category = category;
		return this;
	}
}
