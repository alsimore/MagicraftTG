package magicrafttg.mana;

public enum ManaColor {
	WHITE, BLUE, BLACK, RED, GREEN, COLORLESS, MULTI;
	
	public static ManaColor[] getColourArray() {
		return new ManaColor[] {WHITE, BLUE, BLACK, RED, GREEN, COLORLESS, MULTI};
	}
	
	public static ManaColor colourFromIndex(int index)
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
		case 5:
			return COLORLESS;
		case 6:
			return MULTI;
		default:
			return COLORLESS;
		}
	}
}
