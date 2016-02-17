package magicrafttg.mana;

public enum ManaColour {
	WHITE, BLUE, BLACK, RED, GREEN, COLOURLESS;
	
	public static ManaColour[] getColourArray() {
		return new ManaColour[] {WHITE, BLUE, BLACK, RED, GREEN, COLOURLESS};
	}
	
	public static ManaColour colourFromIndex(int index)
	{
		switch(index)
		{
		case 0:
			return WHITE;
		case 1:
			return BLUE;
		case 2:
			return BLACK;
		case 3:
			return RED;
		case 4:
			return GREEN;
		default:
			return COLOURLESS;
		}
	}
}
