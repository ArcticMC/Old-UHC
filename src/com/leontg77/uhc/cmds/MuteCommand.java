package com.leontg77.uhc.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.ArrayUtil;
import com.leontg77.uhc.util.PlayersUtil;

public class MuteCommand implements CommandExecutor {

	public boolean onCommand(CommandSender player, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("mute")) {
			if (player.hasPermission("uhc.mute")) {
				if (args.length == 0) {
					if (ArrayUtil.mute.contains("a")) {
						ArrayUtil.mute.remove("a");
						PlayersUtil.broadcast(Main.prefix() + "Global chat enabled.");
						return true;
					} 
					PlayersUtil.broadcast(Main.prefix() + "Global chat disabled.");
					ArrayUtil.mute.add("a");
					return true;
				}
				
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if (target == null) {
					player.sendMessage(ChatColor.RED + "That player is not online.");
					return true;
				}
	
				if (ArrayUtil.mute.contains(target.getName())) {
					ArrayUtil.mute.remove(target.getName());
					player.sendMessage(Main.prefix() + target.getName() + " unmuted.");
					return true;
				} 
				player.sendMessage(Main.prefix() + target.getName() + " muted");
				ArrayUtil.mute.add(target.getName());
			} else {
				player.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}