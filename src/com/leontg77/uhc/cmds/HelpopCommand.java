package com.leontg77.uhc.cmds;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.PlayersUtil;

public class HelpopCommand implements CommandExecutor {
	public static ArrayList<CommandSender> cooldown = new ArrayList<CommandSender>();

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("helpop")) {
			if (args.length == 0) {
	           	sender.sendMessage(ChatColor.RED + "Usage: /helpop <message>");
	           	return true;
			}
			
			if (cooldown.contains(sender)) {
				sender.sendMessage(Main.prefix().replaceAll("UHC", "HelpOp") + "�7Do not spam helpops.");
				return true;
			}
			
			StringBuilder sb = new StringBuilder("");
			
			for (int i = 0; i < args.length; i++) {
				sb.append(args[i]).append(" ");
			}
			
			String msg = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
			
			for (Player online : PlayersUtil.getPlayers()) {
				if (online.hasPermission("uhc.staff")) {
					online.sendMessage(Main.prefix().replaceAll("UHC", "HelpOp") + "�7" + sender.getName() + "�7: �6" + msg);
					online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 1);
				}
			}
			cooldown.add(sender);
			sender.sendMessage(Main.prefix().replaceAll("UHC", "HelpOp") + "�7Your message has been sent, don't spam this command.");
			Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
				public void run() {
					cooldown.remove(sender);
				}
			}, 150);
		}
		return true;
	}
}