package com.leontg77.uhc.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.util.ArrayUtil;

public class MsgCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can message players.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("msg")) {
	    	if (args.length < 2) {
	    		sender.sendMessage(ChatColor.RED + "Usage: /msg <player> <message>");
	        	return true;
	        }
	    	   
	    	Player target = Bukkit.getServer().getPlayer(args[0]);
	               
	        if (target == null) {
	        	player.sendMessage(ChatColor.RED + "That player is not online.");
	        	return true;
	        }
	               
	        StringBuilder message = new StringBuilder();
	               
	        for (int i = 1; i < args.length; i++) {
	        	message.append(args[i]).append(" ");
	        }
	        
	        if (ArrayUtil.spectating.contains(player.getName()) && !player.hasPermission("uhc.seemsg") && !ArrayUtil.spectating.contains(target.getName())) {
	        	if (!target.hasPermission("uhc.seemsg")) {
	        		player.sendMessage(ChatColor.RED + "You cannot message players that's not spectating.");
		        	return true;
	        	}
	        }
	        
	        String msg = message.toString().trim();
	               
	        if (target == player) {
	        	player.sendMessage(ChatColor.RED + "You can't send a private message to yourself.");
	        } else {
		    	player.sendMessage("§6[me -> " + target.getName() + "§6] §f" + msg);
		    	target.sendMessage("§6[" + player.getName() + "§6 -> me] §f" + msg);
				ArrayUtil.msg.put(target, player);
				ArrayUtil.msg.put(player, target);
	        }
	    }
		return true;
	}
}