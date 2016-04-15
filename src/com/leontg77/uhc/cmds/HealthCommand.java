package com.leontg77.uhc.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;

public class HealthCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("health")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Usage: /health <player>");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "That player is not online.");
				return true;
			}
			
			Damageable damage = target;
			double health = damage.getHealth();
			double hearts = health / 2;
			double precent = hearts * 10;
			double maxhealth = damage.getMaxHealth();
			double maxhearts = maxhealth / 2;
			double maxprecent = maxhearts * 10;

			sender.sendMessage(Main.prefix(ChatColor.GOLD) + target.getName() + " �7is at �6" + ((int) precent) + "%" + (maxprecent == 100 ? "." : " �7out of maximum �6" + ((int) maxprecent) + "%"));
		}
		return true;
	}
}