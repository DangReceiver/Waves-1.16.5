package de.tdf.waves.methods.enums;

public enum WaveTypes {
	DEFAULT, RUSH, SINGLE, INSANE,
	NO_AI, POTION, JUMP, SOUROUND;

	public static WaveTypes getFromId(int i) {
		WaveTypes wt = null;
		if (i >= Difficulty.values().length)
			return null;
		switch (i) {
			case 0 -> wt = DEFAULT;
			case 1 -> wt = RUSH;
			case 2 -> wt = SINGLE;
			case 3 -> wt = INSANE;
			case 4 -> wt = NO_AI;
			case 5 -> wt = POTION;
			case 6 -> wt = JUMP;
			case 7 -> wt = SOUROUND;
			default -> {
				return null;
			}
		}
		return wt;
	}
}
