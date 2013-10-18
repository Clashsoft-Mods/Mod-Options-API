package clashsoft.modoptionsapi.api;

public interface IModOptionListEntry
{
	public String getName();
	
	public void resetPressed();
	
	public boolean isValid();
	
	public String getError();
}
