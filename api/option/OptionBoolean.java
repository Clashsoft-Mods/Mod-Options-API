package clashsoft.modoptionsapi.api.option;

public class OptionBoolean extends Option<Boolean>
{
	public OptionBoolean(String name, boolean value, boolean defaultValue)
	{
		super(name, value, defaultValue);
	}
	
	@Override
	public boolean isValid()
	{
		return true;
	}
	
	@Override
	public String getError()
	{
		return "";
	}
	
}
