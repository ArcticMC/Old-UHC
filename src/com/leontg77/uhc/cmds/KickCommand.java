package com.leontg77.uhc.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.PlayersUtil;

public class KickCommand implements CommandExecutor {	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("kick")) {
			if (sender.hasPermission("uhc.kick")) {
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Usage: /kick <player> <reason>");
					return true;
				}
		    	
		    	Player target = Bukkit.getServer().getPlayer(args[0]);
							
				StringBuilder reason = new StringBuilder("");
					
				for (int i = 1; i < args.length; i++) {
					reason.append(args[i]).append(" ");
				}
						
				String msg = reason.toString().trim().trim();

				if (args[0].equals("*")) {
					for (Player online : PlayersUtil.getPlayers()) {
						if (!online.hasPermission("uhc.prelist")) {
					    	online.kickPlayer(msg);
						}
						if (online.hasPermission("uhc.admin")) {
				    		online.sendMessage(Main.prefix() + "�7All players has been kicked for �6" + msg);
			    		}
					}
					return true;
				}
				
		    	if (target == null) {
		    		sender.sendMessage(ChatColor.RED + "That player is not online.");
		            return true;
				}
		    	
		    	for (Player online : PlayersUtil.getPlayers()) {
		    		if (online.hasPermission("uhc.admin")) {
			    		online.sendMessage(Main.prefix() + "�6" + args[0] + " �7has been kicked for �6" + msg);
		    		}
		    	}
		    	target.kickPlayer(msg);
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}