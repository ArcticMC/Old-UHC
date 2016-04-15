package com.leontg77.uhc.cmds;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.PlayersUtil;

public class BanCommand implements CommandExecutor {	

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("ban")) {
			if (sender.hasPermission("uhc.ban")) {
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Usage: /ban <player> <reason>");
					return true;
				}
		    	
		    	final Player target = Bukkit.getServer().getPlayer(args[0]);
							
				StringBuilder reason = new StringBuilder("");
					
				for (int i = 1; i < args.length; i++) {
					reason.append(args[i]).append(" ");
				}
						
				final String msg = reason.toString().trim().trim();

		    	if (target == null) {
		    		PlayersUtil.broadcast(Main.prefix() + args[0] + " has been banned for " + msg);
		    		Bukkit.getServer().getBanList(Type.NAME).addBan(args[0], msg, null, sender.getName());
		            return true;
				}
		        
		    	for (Player online : PlayersUtil.getPlayers()) {
		    		online.sendMessage(Main.prefix() + "Incoming DQ in §63§7.");
		    		online.playSound(online.getLocation(), Sound.ANVIL_LAND, 1, 1);
		    	}
	    		
				Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
					public void run() {
				    	for (Player online : PlayersUtil.getPlayers()) {
				    		online.sendMessage(Main.prefix() + "Incoming DQ in §62§7.");
				    		online.playSound(online.getLocation(), Sound.ANVIL_LAND, 1, 1);
				    	}
					}
				}, 20);
				
				Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
					public void run() {
				    	for (Player online : PlayersUtil.getPlayers()) {
				    		online.sendMessage(Main.prefix() + "Incoming DQ in §61§7.");
				    		online.playSound(online.getLocation(), Sound.ANVIL_LAND, 1, 1);
				    	}
					}
				}, 40);
				
				Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
					public void run() {
				    	for (Player online : PlayersUtil.getPlayers()) {
				    		online.sendMessage(Main.prefix() + "§6" + args[0] + " §7has been banned for §6" + msg);
				    		online.playSound(online.getLocation(), Sound.EXPLODE, 1, 1);
				    	}
			    		Bukkit.getServer().getBanList(Type.NAME).addBan(target.getName(), msg, null, sender.getName());
				    	target.kickPlayer(msg);
				    	target.setWhitelisted(false);
					}
				}, 60);
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}