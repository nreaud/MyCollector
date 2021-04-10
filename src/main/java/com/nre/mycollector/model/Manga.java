package com.nre.mycollector.model;

/**
 * List of the mangas i'm interessted in
 * 
 * @author nicol
 *
 */
public enum Manga {
  // Ordered by name
	AJIN(new String[] { "Ajin" }), ATTACK_ON_TITAN(new String[] { "Attack on Titan", "Shingeki no Kyojin" }),
	BLACK_CLOVER(new String[] { "Black Clover" }), BORUTO(new String[] { "Boruto" }),
	CHAINSAW_MAN(new String[] { "Chainsaw man" }), ENIGMA(new String[] { "Enigma" }),
	FAIRY_TAIL_100_Y(new String[] { "Fairy tail 100 years quest" }), JUJUTSU_KAISEN(new String[] { "Jujutsu Kaisen" }),
	MHA(new String[] { "My hero academia", "Boku No Hero Academia" }), ONE_PIECE(new String[] { "One Piece" }),
	ONE_PUNCH_MAN(new String[] { "One Punch Man" }), ONE_PUNCH_MAN_ONE(new String[] { "One Punch Man - One" }),
	PLATINIUM_END(new String[] { "Platinium End" }), SOLO_LEVELING(new String[] { "Solo leveling" }),
	THE_PROMISED_NEVERLAND(new String[] { "The Promised Neverland" }),
	TOKYO_REVENGERS(new String[] { "Tokyo Revengers" }), UNKNOWN(new String[] { "UNKNOWN" }),
	WORLD_TRIGGER(new String[] { "World Trigger" });

	private String[] names;

	private Manga(String[] names) {
		this.names = names;
	}

	public String[] getNames() {
		return this.names;
	}

	public String getMainName() {
		return this.names[0];
	}

}
