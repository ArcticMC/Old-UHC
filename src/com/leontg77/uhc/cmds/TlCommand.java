package com.leontg77.uhc.cmds;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.util.ArrayUtil;

public class TlCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can tell their team their location.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("teamloc")) {
			if (ArrayUtil.spectating.contains(player.getName())) {
				player.sendMessage(ChatColor.RED + "You are not allowed to do that while spectating.");
	        } else if (player.getScoreboard().getPlayerTeam(player) == null) { 
	        	player.sendMessage(ChatColor.RED + "You are not on a team.");
			} else {
				for (OfflinePlayer team : player.getScoreboard().getPlayerTeam(player).getPlayers()) {
					if (team instanceof Player) {
						((Player) team).sendMessage(player.getScoreboard().getPlayerTeam(player).getPrefix() + "[Team]" + " §f" + player.getName() + "'s location: world: " + player.getWorld().getEnvironment().name().replaceAll("_", " ").toLowerCase().replaceAll("normal", "overworld") + ", x: " + player.getLocation().getBlockX() + ", y: " + player.getLocation().getBlockY() + ", z: " + player.getLocation().getBlockZ());
					}
				}
			}
		}
		return true;
	}
}