package com.leontg77.uhc.cmds;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PmCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can chat with teammates.");
			return true;
		}
		
		final Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("pm")) {
			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "Usage: /pm <message>");
				return true;
			}
			
			if (player.getScoreboard().getPlayerTeam(player) != null) {
				StringBuilder message = new StringBuilder();
	               
		        for (int i = 0; i < args.length; i++) {
		        	message.append(ChatColor.GRAY + args[i]).append(" " + ChatColor.GRAY);
		        }
		               
		        String msg = message.toString().trim();
		        
				for (OfflinePlayer team : player.getScoreboard().getPlayerTeam(player).getPlayers()) {
					if (team instanceof Player) {
						((Player) team).sendMessage((player.getScoreboard().getPlayerTeam(player).getName().equals("spec") ? "§7[SpecChat]" : player.getScoreboard().getPlayerTeam(player).getPrefix() + "[Team]") + " §f" + player.getName() + ": " + msg);
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "You are not on a team.");
			}
		}
		return true;
	}
}