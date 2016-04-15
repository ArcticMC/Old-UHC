package com.leontg77.uhc.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.util.ArrayUtil;

public class ReplyCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can message players.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("reply")) {
			if (args.length == 0) {
	    		sender.sendMessage(ChatColor.RED + "Usage: /reply <message>");
	        	return true;
	        }   
	    	
	    	if (!ArrayUtil.msg.containsKey(player)) {
				player.sendMessage(ChatColor.RED + "You have no one to reply to.");
				return true;
			}
	        
	        Player target = ArrayUtil.msg.get(player);
					
			if (target == null) {
				player.sendMessage(ChatColor.RED + "That player is not online.");
				return true;
			}
			
	        StringBuilder message = new StringBuilder();
	               
	        for (int i = 0; i < args.length; i++) {
	        	message.append(args[i]).append(" ");
	        }
	        
	        if (ArrayUtil.spectating.contains(player.getName()) && !player.hasPermission("uhc.seemsg") && !ArrayUtil.spectating.contains(target.getName())) {
	        	if (!target.hasPermission("uhc.seemsg")) {
	        		player.sendMessage(ChatColor.RED + "You cannot message players that's not spectating.");
		        	return true;
	        	}
	        }
	               
	        String msg = message.toString().trim();			
	   
	    	player.sendMessage("§6[me -> " + target.getName() + "§6] §f" + msg);
	    	target.sendMessage("§6[" + player.getName() + "§6 -> me] §f" + msg);
	    	ArrayUtil.msg.put(target, player);
	    	ArrayUtil.msg.put(player, target);
	    }
		return true;
	}
}