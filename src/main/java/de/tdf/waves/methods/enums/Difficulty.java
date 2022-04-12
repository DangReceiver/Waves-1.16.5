package de.tdf.waves.methods.enums;

public enum Difficulty {
	EZ, EASY, PRE_NORMAL, NORMAL,
	PAST_NORMAL, HARDER, HARD,
	VERY_HARD, DIFFICULT, INSANE;

	public static Difficulty getFromId(int i) {
		Difficulty d = null;
		if (i >= Difficulty.values().length)
			return null;
		switch (i) {
			case 0 -> d = EZ;
			case 1 -> d = EASY;
			case 2 -> d = PRE_NORMAL;
			case 3 -> d = NORMAL;
			case 4 -> d = PAST_NORMAL;
			case 5 -> d = HARDER;
			case 6 -> d = HARD;
			case 7 -> d = VERY_HARD;
			case 8 -> d = DIFFICULT;
			case 9 -> d = INSANE;
			default -> {
				return null;
			}
		}
		return d;
	}
}
