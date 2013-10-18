package clashsoft.modoptionsapi.api.option;

public class OptionString extends Option<String>
{
	public int	maxLength;
	
	public OptionString(String name, String value, String defaultValue, int maxLength)
	{
		super(name, value, defaultValue);
		this.maxLength = maxLength;
	}
	
	@Override
	public boolean isValid()
	{
		return maxLength < 1 || value.length() <= maxLength;
	}
	
	@Override
	public String getError()
	{
		return "The String '" + value + "' is too long, it must be at most " + maxLength + " characters.";
	}
}
