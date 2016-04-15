package com.leontg77.uhc.cmds;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.leontg77.uhc.Main;

public class UnbanCommand implements CommandExecutor {	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("unban")) {
			if (sender.hasPermission("uhc.unban")) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Usage: /unban <player>");
					return true;
				}
		    	
		    	Bukkit.getServer().broadcastMessage(Main.prefix() + args[0] + " has been unbanned.");
		   		Bukkit.getServer().getBanList(Type.NAME).pardon(args[0]);
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}