package com.leontg77.uhc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Scoreboard class for uhc.
 * @author LeonTG77
 */
public class Teams {
	private Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
	private ArrayList<Team> teams = new ArrayList<Team>();
	
	private Teams() {}
	private static Teams manager = new Teams();
	public static Teams getManager() {
		return manager;
	}
	
	/**
	 * Gets a list of all teams.
	 * @return the list of teams.
	 */
	public List<Team> getTeams() {
		return teams;
	}
	
	/**
	 * Leaves the current team of the player.
	 * @param player the player leaving.
	 */
	public void leaveTeam(Player player) {	
		if (this.getTeam(player) != null) {
			this.getTeam(player).removePlayer(player);
		}
	}

	/**
	 * Joins a team.
	 * @param teamName the team joining.
	 * @param player the player joining.
	 */
	public void joinTeam(String teamName, Player player) {	
		Team team = sb.getTeam(teamName);
		team.addPlayer(player);
	}

	/**
	 * Gets the team of a player.
	 * @param player the player wanting.
	 * @return The team.
	 */
	public Team getTeam(Player player) {
		return player.getScoreboard().getPlayerTeam(player);
	}
	
	/**
	 * Sets up all the teams.
	 */
	public void setupTeams() {
		ArrayList<String> list = new ArrayList<String>();
		
		for (ChatColor color : ChatColor.values()) {
			if (color == ChatColor.RESET || color == ChatColor.MAGIC || color == ChatColor.BOLD || color == ChatColor.ITALIC || color == ChatColor.UNDERLINE || color == ChatColor.STRIKETHROUGH) {
				continue;
			}
			
			list.add(color.toString());
		}
		
		ArrayList<String> list2 = new ArrayList<String>();
		
		for (String li : list) {
			list2.add(li + ChatColor.BOLD);
			list2.add(li + ChatColor.ITALIC);
			list2.add(li + ChatColor.UNDERLINE);
			list2.add(li + ChatColor.STRIKETHROUGH);
			list2.add(li + ChatColor.BOLD + ChatColor.ITALIC);
			list2.add(li + ChatColor.BOLD + ChatColor.ITALIC + ChatColor.UNDERLINE);
			list2.add(li + ChatColor.BOLD + ChatColor.ITALIC + ChatColor.STRIKETHROUGH);
			list2.add(li + ChatColor.BOLD + ChatColor.ITALIC + ChatColor.STRIKETHROUGH + ChatColor.UNDERLINE);
			list2.add(li + ChatColor.BOLD + ChatColor.STRIKETHROUGH + ChatColor.UNDERLINE);
			list2.add(li + ChatColor.BOLD + ChatColor.UNDERLINE);
			list2.add(li + ChatColor.BOLD + ChatColor.STRIKETHROUGH);
			list2.add(li + ChatColor.ITALIC + ChatColor.UNDERLINE);
			list2.add(li + ChatColor.ITALIC + ChatColor.STRIKETHROUGH);
			list2.add(li + ChatColor.ITALIC + ChatColor.STRIKETHROUGH + ChatColor.UNDERLINE);
			list2.add(li + ChatColor.UNDERLINE + ChatColor.STRIKETHROUGH);
		}
		
		list.remove(ChatColor.WHITE.toString());
		list.remove(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString());

		list.addAll(list2);
		
		Team spec = (sb.getTeam("spec") == null ? sb.registerNewTeam("spec") : sb.getTeam("spec"));
		
		spec.setDisplayName("spec");
		spec.setPrefix("§7§o");
		spec.setSuffix("§r");
		spec.setAllowFriendlyFire(false);
		spec.setCanSeeFriendlyInvisibles(true);	
		
		for (int i = 0; i < list.size(); i++) {
			Team team = (sb.getTeam("UHC" + (i + 1)) == null ? sb.registerNewTeam("UHC" + (i + 1)) : sb.getTeam("UHC" + (i + 1)));
			
			team.setDisplayName("UHC" + (i + 1));
			team.setPrefix(list.get(i));
			team.setSuffix("§r");
			team.setAllowFriendlyFire(true);
			team.setCanSeeFriendlyInvisibles(true);
			teams.add(team);
		}
	}
}