package magicrafttg.mana;

public class ManaSource {

	/**
	 * Colour of the mana that this ManaSource provides.
	 */
	private ManaColour colour;
	
	public ManaSource(ManaColour colour)
	{
		this.colour = colour;
	}
	/**
	 * Get the colour of mana provided by the source.
	 * @return the mana colour provided
	 */
	public ManaColour getColour() {
		return colour;
	}

	public void setColour(ManaColour colour) {
		this.colour = colour;
	}
}
