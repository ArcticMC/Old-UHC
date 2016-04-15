package com.leontg77.uhc.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.GameState;
import com.leontg77.uhc.Main;

@SuppressWarnings("deprecation")
public class StartCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd,	String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("start")) {
			if (sender.hasPermission("uhc.start")) {
				if (!GameState.isState(GameState.INGAME)) {
					sender.sendMessage(Main.prefix() + "You have started the countdown to UHC.");
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (online.hasPermission("tgn.uhcstaff") && online != sender) {
							online.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + sender.getName() + ": has started the countdown.]");
						}
					}
					Main.startCountdown();
				} else {
					sender.sendMessage(ChatColor.RED + "You cannot start the game when it's already started");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}