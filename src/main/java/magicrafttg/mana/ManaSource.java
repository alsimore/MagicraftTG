package magicrafttg.mana;

public class ManaSource {

	/**
	 * Colour of the mana that this ManaSource provides.
	 */
	private ManaColor colour;
	
	public ManaSource(ManaColor colour)
	{
		this.colour = colour;
	}
	/**
	 * Get the colour of mana provided by the source.
	 * @return the mana colour provided
	 */
	public ManaColor getColour() {
		return colour;
	}

	public void setColour(ManaColor colour) {
		this.colour = colour;
	}
}
