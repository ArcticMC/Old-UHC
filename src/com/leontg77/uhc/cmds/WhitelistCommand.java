package com.leontg77.uhc.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.PlayersUtil;

@SuppressWarnings("deprecation")
public class WhitelistCommand implements CommandExecutor, TabCompleter {

	public boolean onCommand(CommandSender player, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("wl")) {
			if (player.hasPermission("uhc.whitelist")) {
	           	if (args.length == 0) {
	        		player.sendMessage(ChatColor.RED + "Usage: /wl <all|on|off|clear|add|remove> [player]");
	        		return true;
	           	}
				
	           	if (args.length == 1) {
	           		if (args[0].equalsIgnoreCase("all")) {
	           			for (Player online : PlayersUtil.getPlayers()) {
	           				online.setWhitelisted(true);
	           				online.sendMessage(Main.prefix() + "All players whitelisted.");
	           			}
	           		} 
	           		else if (args[0].equalsIgnoreCase("on")) {
	           			Bukkit.setWhitelist(true);
	           			for (Player online : PlayersUtil.getPlayers()) {
	           				online.sendMessage(Main.prefix() + "Whitelist turned on.");
	           			}
	           		} 
	           		else if (args[0].equalsIgnoreCase("off")) {
	           			Bukkit.setWhitelist(false);
	           			for (Player online : PlayersUtil.getPlayers()) {
	           				online.sendMessage(Main.prefix() + "Whitelist turned off.");
	           			}
	           		} 
	           		else if (args[0].equalsIgnoreCase("add")) {
		           		player.sendMessage(ChatColor.RED + "Usage: /wl add <player>");
	           		} 
	           		else if (args[0].equalsIgnoreCase("remove")) {
		           		player.sendMessage(ChatColor.RED + "Usage: /wl remove <player>");
	           		} 
	           		else if (args[0].equalsIgnoreCase("clear")) {
	           			for (OfflinePlayer whitelisted : Bukkit.getWhitelistedPlayers()) {
	           				whitelisted.setWhitelisted(false);
	           			}
	           			for (Player online : PlayersUtil.getPlayers()) {
	           				online.sendMessage(Main.prefix() + "Whitelist cleared.");
	           			}
	           		} 
	           		else {
		           		player.sendMessage(ChatColor.RED + "Usage: /wl <all|on|off|clear|add|remove> [player]");
	           		}
					return true;
	           	}
				
	           	Player target = Bukkit.getServer().getPlayer(args[1]);
	           	OfflinePlayer offline = Bukkit.getServer().getOfflinePlayer(args[1]);
	           	
	           	if (args[0].equalsIgnoreCase("all")) {
           			for (Player online : PlayersUtil.getPlayers()) {
           				online.setWhitelisted(true);
           				online.sendMessage(Main.prefix() + "All players whitelisted.");
           			}
           		} 
           		else if (args[0].equalsIgnoreCase("on")) {
           			Bukkit.setWhitelist(true);
           			for (Player online : PlayersUtil.getPlayers()) {
           				online.sendMessage(Main.prefix() + "Whitelist turned on.");
           			}
           		} 
           		else if (args[0].equalsIgnoreCase("off")) {
           			Bukkit.setWhitelist(false);
           			for (Player online : PlayersUtil.getPlayers()) {
           				online.sendMessage(Main.prefix() + "Whitelist turned off.");
           			}
           		} 
           		else if (args[0].equalsIgnoreCase("add")) {
	           		if (target == null) {
	           			for (Player online : PlayersUtil.getPlayers()) {
	           				online.sendMessage(Main.prefix() + "§6" + offline.getName() + " §7whitelisted.");
	           			}
	           			offline.setWhitelisted(true);
	           			return true;
	           		}
           			for (Player online : PlayersUtil.getPlayers()) {
           				online.sendMessage(Main.prefix() + "§6" + target.getName() + " §7whitelisted.");
           			}
           			target.setWhitelisted(true);
	           	} 
           		else if (args[0].equalsIgnoreCase("remove")) {
	           		if (target == null) {
	           			for (Player online : PlayersUtil.getPlayers()) {
	           				online.sendMessage(Main.prefix() + offline.getName() + " unwhitelisted.");
	           			}
	           			offline.setWhitelisted(false);
	           			return true;
	           		}
           			for (Player online : PlayersUtil.getPlayers()) {
           				online.sendMessage(Main.prefix() + target.getName() + " unwhitelisted.");
           			}
	           		target.setWhitelisted(false);
	           	} 
           		else if (args[0].equalsIgnoreCase("clear")) {
           			for (OfflinePlayer whitelisted : Bukkit.getWhitelistedPlayers()) {
           				whitelisted.setWhitelisted(false);
           			}
           			for (Player online : PlayersUtil.getPlayers()) {
           				online.sendMessage(Main.prefix() + "Whitelist cleared.");
           			}
           		} 
           		else {
	           		player.sendMessage(ChatColor.RED + "Usage: /wl <all|on|off|clear|add|remove> [player]");
           		}
			} else {
				player.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}
		
		if (cmd.getName().equalsIgnoreCase("wl")) {
			if (args.length == 1) {
	        	ArrayList<String> arg = new ArrayList<String>();
	        	ArrayList<String> types = new ArrayList<String>();
	        	types.add("all");
	        	types.add("on");
	        	types.add("off");
	        	types.add("clear");
	        	types.add("add");
	        	types.add("remove");
	        	
	        	if (!args[0].equals("")) {
	        		for (String type : types) {
	        			if (type.startsWith(args[0].toLowerCase())) {
	        				arg.add(type);
	        			}
	        		}
	        	}
	        	
	        	else {
	        		for (String type : types) {
	        			arg.add(type);
	        		}
	        	}
	        	return arg;
	        }
			
			if (args.length == 2) {
	        	ArrayList<String> arg = new ArrayList<String>();
	        	
	        	if (args[1].equalsIgnoreCase("add")) {
		        	if (!args[1].equals("")) {
		        		for (Player online : PlayersUtil.getPlayers()) {
		        			if (online.getName().startsWith(args[0].toLowerCase())) {
		        				if (!online.isWhitelisted()) {
			        				arg.add(online.getName());
		        				}
		        			}
		        		}
		        		for (OfflinePlayer offlines : Bukkit.getServer().getOfflinePlayers()) {
		        			if (offlines.getName().startsWith(args[0].toLowerCase())) {
		        				if (!offlines.isWhitelisted()) {
			        				arg.add(offlines.getName());
		        				}
		        			}
	           			}
		        	}
		        	else {
		        		for (Player online : PlayersUtil.getPlayers()) {
		        			if (!online.isWhitelisted()) {
			        			arg.add(online.getName());
		        			}
		        		}
		        		for (OfflinePlayer offlines : Bukkit.getServer().getOfflinePlayers()) {
		        			if (!offlines.isWhitelisted()) {
			        			arg.add(offlines.getName());
		        			}
	           			}
		        	}
	        	}
	        	else if (args[1].equalsIgnoreCase("remove")) {
		        	if (!args[1].equals("")) {
		        		for (Player online : PlayersUtil.getPlayers()) {
		        			if (online.getName().startsWith(args[0].toLowerCase())) {
		        				if (online.isWhitelisted()) {
			        				arg.add(online.getName());
		        				}
		        			}
		        		}
		        		for (OfflinePlayer offlines : Bukkit.getServer().getOfflinePlayers()) {
		        			if (offlines.getName().startsWith(args[0].toLowerCase())) {
		        				if (offlines.isWhitelisted()) {
			        				arg.add(offlines.getName());
		        				}
		        			}
	           			}
		        	}
		        	else {
		        		for (Player online : PlayersUtil.getPlayers()) {
		        			if (online.isWhitelisted()) {
			        			arg.add(online.getName());
		        			}
		        		}
		        		for (OfflinePlayer offlines : Bukkit.getServer().getOfflinePlayers()) {
		        			if (offlines.isWhitelisted()) {
			        			arg.add(offlines.getName());
		        			}
	           			}
		        	}
	        	}
	        	return arg;
	        }
		}
		return null;
	}
}