package com.leontg77.uhc.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.ArrayUtil;

public class TpCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can teleport to others.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("tp")) {
			if (player.hasPermission("uhc.tp") || ArrayUtil.spectating.contains(player.getName())) {
				if (args.length == 0) {
					player.sendMessage(ChatColor.RED + "Usage: /tp <player>");
					return true;
				}
				
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if (args.length == 1) {
					if (target == null) {
						player.sendMessage(ChatColor.RED + "That player is not online.");
					} else {
						player.sendMessage(Main.prefix() + "You teleported to §6" + target.getName() + "§7.");
						player.teleport(target);
					}
					return true;
				}
				
				Player target2 = Bukkit.getServer().getPlayer(args[1]);
				
				if (target == null || target2 == null) {
					player.sendMessage(ChatColor.RED + "That player is not online.");
				} else {
					player.sendMessage(Main.prefix(ChatColor.GOLD) + target.getName() + "§7 was teleported to §6" + target2.getName() + "§7.");
					target.teleport(target2);
				}
			} else {
				player.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}