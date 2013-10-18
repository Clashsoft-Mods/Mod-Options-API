package clashsoft.modoptionsapi.api.option;

public class OptionNumber extends Option<Double>
{
	public double	maxValue;
	public double	minValue;
	
	public boolean	fine;
	
	public OptionNumber(String name, double value, double defaultValue)
	{
		this(name, value, defaultValue, Double.MIN_VALUE, Double.MAX_VALUE);
	}
	
	public OptionNumber(String name, double value, double defaultValue, double minValue, double maxValue)
	{
		super(name, value, defaultValue);
		this.maxValue = maxValue;
		this.minValue = minValue;
	}
	
	public boolean increase(double value)
	{
		if (fine)
			value *= 0.1D;
		
		double newValue = this.value + value;
		if ((newValue <= maxValue) && (newValue >= minValue))
		{
			setValue(newValue);
			return true;
		}
		return false;
	}
	
	public void setFineAdjust(boolean flag)
	{
		this.fine = flag;
	}
	
	@Override
	public boolean isValid()
	{
		return (value <= maxValue) && (value >= minValue);
	}
	
	@Override
	public String getError()
	{
		return "The double " + value + " is not in the range " + minValue + " - " + maxValue;
	}
}
