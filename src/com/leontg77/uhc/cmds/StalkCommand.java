package com.leontg77.uhc.cmds;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.ArrayUtil;

@SuppressWarnings("deprecation")
public class StalkCommand implements CommandExecutor {
	public static ArrayList<CommandSender> cooldown = new ArrayList<CommandSender>();

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can be stalked.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("stalk")) {
			if (cooldown.contains(sender)) {
				sender.sendMessage(Main.prefix().replaceAll("UHC", "Stalk") + "§7Do not spam stalk reports.");
				return true;
			}
			
			StringBuilder nearby = new StringBuilder("");
			
			for (Entity near : player.getNearbyEntities(200, 200, 200)) {
				if (near instanceof Player) {
					if (Main.ffa) {
						if (!player.canSee((Player) near) || ArrayUtil.spectating.contains(((Player) near).getName())) {
							continue;
						}
					} else {
						if (!player.canSee((Player) near) || (player.getScoreboard().getPlayerTeam(player) != null && player.getScoreboard().getPlayerTeam(player).getPlayers().contains((Player) near)) || ArrayUtil.spectating.contains(((Player) near).getName())) {
							continue;
						}
					}
					
					if (nearby.length() > 0) {
						nearby.append("§8, ");
					}

					Player nearb = (Player) near;
					if (nearb != player) {
						nearby.append("§7" + nearb.getName() + "§f(§c" + ((int) player.getLocation().distance(nearb.getLocation())) + "m§f)§8");
					}
				}
			}
			
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (online.hasPermission("uhc.staff")) {
					online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 1);
					online.sendMessage(Main.prefix().replaceAll("UHC", "Stalk") + "§6" + sender.getName() + " §7thinks he's being stalked!");
					online.sendMessage("§aPossible stalkers: §f" + (nearby.length() > 0 ? nearby.toString().trim() : "None"));
				}
			}
			sender.sendMessage(Main.prefix().replaceAll("UHC", "Stalk") + "§7Stalk report sent, please don't spam this.");
			cooldown.add(sender);
			Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
				public void run() {
					cooldown.remove(sender);
				}
			}, 150);
		}
		return true;
	}
}