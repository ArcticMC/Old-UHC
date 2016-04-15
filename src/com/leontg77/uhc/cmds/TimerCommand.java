package com.leontg77.uhc.cmds;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.TimerFeature;

public class TimerCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if (cmd.getName().equals("timer")) {
			if (sender.hasPermission("uhc.timer")) {
				if ((args.length == 0 || args.length == 1) && !args[0].equalsIgnoreCase("cancel")) {
					sender.sendMessage(ChatColor.RED + "Usage: /timer <duration> <message>");
					return true;
				}

				TimerFeature timerFeature = new TimerFeature();

				if (args.length >= 1 && args[0].equalsIgnoreCase("cancel")) {
					if (timerFeature.stopTimer()) {
						sender.sendMessage(Main.prefix() + "Timer stopped.");
					} else {
						sender.sendMessage(Main.prefix() + "No timers running.");
					}
					return true;
				}

				int millis;

				try {
					millis = Integer.parseInt(args[0]);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "Invaild number.");
					return true;
				}

				List<String> arg = Arrays.asList(args);
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < arg.size(); i++) {
					sb.append(arg.get(i)).append(" ");
				}
				String message = sb.substring(0, sb.length() - 1);

				if (timerFeature.startTimer(millis, ChatColor.translateAlternateColorCodes('&', message))) {
					sender.sendMessage(Main.prefix() + "Timer started.");
				} else {
					sender.sendMessage(Main.prefix() + "Timer already running, use /timer cancel.");
				}
			}
		}
		return true;
	}
}