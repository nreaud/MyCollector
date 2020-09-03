package com.nre.mycollector.model;

/**
 * List of the mangas i'm interessted in
 * 
 * @author nicol
 *
 */
public enum Manga {
  // Ordered by name
	AJIN("Ajin"), BLACK_CLOVER("Black Clover"), BORUTO("Boruto"), CHAINSAW_MAN("Chainsaw man"),
	FAIRY_TAIL_100_Y("Fairy tail 100 years quest"), JUJUTSU_KAISEN("Jujutsu Kaisen"), MHA("My hero academia"),
	ONE_PIECE("One Piece"), ONE_PUNCH_MAN("One Punch Man"), ONE_PUNCH_MAN_ONE("One Punch Man - One"),
	PLATINIUM_END("Platinium End"), SOLO_LEVELING("Solo leveling"), SHINGEKI("Attack on Titan"),
	THE_PROMISED_NEVERLAND("The Promised Neverland"), TOKYO_REVENGERS("Tokyo Revengers"), UNKNOWN("UNKNOWN"),
	WORLD_TRIGGER("World Trigger");

	private String name;

	private Manga(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
