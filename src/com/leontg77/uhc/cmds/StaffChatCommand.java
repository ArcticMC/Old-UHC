package com.leontg77.uhc.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.PlayersUtil;

public class StaffChatCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("ac")) {
			if (sender.hasPermission("uhc.staff")) {
				if (args.length == 0) {
		    		sender.sendMessage(ChatColor.RED + "Usage: /ac <message>");
		        	return true;
		        } 
		        
		    	StringBuilder message = new StringBuilder("");
				
				for (int i = 0; i <args.length; i++) {
					message.append(ChatColor.WHITE + args[i]).append(" " + ChatColor.WHITE);
				}
		               
		        String msg = message.toString().trim();
		        
		        for (Player online : PlayersUtil.getPlayers()) {
		        	if (online.hasPermission("uhc.staff")) {
		        		online.sendMessage(Main.prefix().replaceAll("UHC", "StaffChat") + ChatColor.AQUA + sender.getName() + ChatColor.DARK_GRAY + ": §f" + msg);
		        	}
		        }
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}