package clashsoft.modoptionsapi.api;

public interface IOptionChangeListener
{
	public boolean optionChanged(String name, Object oldValue, Object newValue);
}
