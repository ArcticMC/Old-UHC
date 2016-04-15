package com.leontg77.uhc.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.ArrayUtil;

@SuppressWarnings("deprecation")
public class VoteCommand implements CommandExecutor {
	public static boolean vote = false;
	public static int yes = 0;
	public static int no = 0;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("vote")) {
			if (sender.hasPermission("uhc.vote")) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Usage: /vote <message>");
					return true;
				}
				
				if (args[0].equalsIgnoreCase("end")) {
					vote = false;
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			        	online.sendMessage(Main.prefix() + "The vote has ended, �a" + yes + " yes �7and �c" + no + " no�7.");
			        }
					yes = 0;
					no = 0;
			        ArrayUtil.voted.clear();
					return true;
				}
				
				StringBuilder message = new StringBuilder();
	               
		        for (int i = 0; i < args.length; i++) {
		        	message.append(args[i]).append(" ");
		        }
		        
		        String msg = message.toString().trim();
		        
		        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
		        	online.sendMessage(Main.prefix() + "A vote has started for " + msg + ".");
		        	online.sendMessage(Main.prefix() + "Say �a'y'�7 or �c'n'�7 in chat to vote.");
		        }
		        vote = true;
		        ArrayUtil.voted.clear();
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}