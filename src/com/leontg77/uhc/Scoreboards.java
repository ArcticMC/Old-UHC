package com.leontg77.uhc;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * The class for managing scoreboards.
 * @author LeonTG77
 */
public class Scoreboards {
	public Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
	public Objective kills = sb.getObjective("PK");
	public Objective heal = sb.getObjective("HP");
	public Objective heal2 = sb.getObjective("HP2");

	private Scoreboards() {}
	private static Scoreboards manager = new Scoreboards();
	public static Scoreboards getManager() {
		return manager;
	}

	/**
	 * Gets a score for a player.
	 * @param string the player getting for
	 * @return the score
	 */
	public int getScore(String string) {
		return kills.getScore(string).getScore();
	}
	
	/**
	 * Gets a scoretype for a player.
	 * @param string the player getting for
	 * @return the scoretype
	 */
	public Score getScoreType(String string) {
		return kills.getScore(string);
	}
	
	/**
	 * Sets the score of a player.
	 * @param offlinePlayer the player setting for.
	 * @param score the new score.
	 */
	public void setScore(String offlinePlayer, int score) {
		Score scores = kills.getScore(offlinePlayer);
		scores.setScore(score);
	}

	/**
	 * Reset the score of a player.
	 * @param player the player.
	 */
	public void resetScore(String player) {
		sb.resetScores(player);
	}
	
	/**
	 * Sets up the scoreboards.
	 */
	public void setup() {
		if (sb.getObjective("PK") == null) {
			kills = sb.registerNewObjective("PK", "dummy");
		}
		if (sb.getObjective("HP") == null) {
			heal = sb.registerNewObjective("HP", "dummy");
		}
		if (sb.getObjective("HP2") == null) {
			heal2 = sb.registerNewObjective("HP2", "dummy");
		}
		kills.setDisplayName("§4UHC §8- §7" + Settings.getInstance().getData().getString("game.host"));
		kills.setDisplaySlot(DisplaySlot.SIDEBAR);
		heal.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		heal2.setDisplaySlot(DisplaySlot.BELOW_NAME);
		heal2.setDisplayName("§4♥");
	}
}