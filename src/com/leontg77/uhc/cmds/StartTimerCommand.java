package com.leontg77.uhc.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.Scoreboards;

@SuppressWarnings("deprecation")
public class StartTimerCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd,	String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("starttimer")) {
			if (sender.hasPermission("uhc.starttimer")) {
				Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
					public void run() {
						if (Scoreboards.getManager().getScoreType("§b§lFinal heal") != null) {
							if ((Scoreboards.getManager().getScore("§b§lFinal heal") + 1) == 0) {
								Scoreboards.getManager().resetScore("§b§lFinal heal");
								for (Player online : Bukkit.getServer().getOnlinePlayers()) {
									online.sendMessage(Main.prefix() + "§6§lFinal heal!");
									online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 0);
									online.setHealth(20.0);
									online.setFireTicks(0);
									online.setFoodLevel(20);
									online.setSaturation(20);
								}
							} else {
								if ((Scoreboards.getManager().getScore("§b§lFinal heal") + 1) < 0) {
									Scoreboards.getManager().setScore("§b§lFinal heal", Scoreboards.getManager().getScore("§b§lFinal heal") + 1);
								}
							}
						}
						
						if (Scoreboards.getManager().getScoreType("§b§lPvP") != null) {
							if ((Scoreboards.getManager().getScore("§b§lPvP") + 1) == 0) {
								Scoreboards.getManager().resetScore("§b§lPvP");
								for (Player online : Bukkit.getServer().getOnlinePlayers()) {
									online.sendMessage(Main.prefix() + "PvP is now enabled, Good luck everyone.");
									online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 0);
								}
								for (World world : Bukkit.getWorlds()) {
									world.setPVP(true);
									world.setPVP(true);
									world.setPVP(true);
									world.setPVP(true);
									world.setPVP(true);
									world.setPVP(true);
									world.setPVP(true);
									world.setPVP(true);
								}
							} else {
								if ((Scoreboards.getManager().getScore("§b§lPvP") + 1) < 0) {
									Scoreboards.getManager().setScore("§b§lPvP", Scoreboards.getManager().getScore("§b§lPvP") + 1);
								}
								if (Scoreboards.getManager().getScore("§b§lPvP") == -5) {
									Bukkit.broadcastMessage(Main.prefix() + "5 minutes to pvp, do §6/stalk §7if you think you're being stalked.");
									for (Player online : Bukkit.getServer().getOnlinePlayers()) {
										online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 0);
									}
								}
								if (Scoreboards.getManager().getScore("§b§lPvP") == -1) {
									Bukkit.broadcastMessage(Main.prefix() + "1 minute to pvp, do §6/stalk §7if you think you're being stalked.");
									for (Player online : Bukkit.getServer().getOnlinePlayers()) {
										online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 0);
									}
								}
							}
						}

						if (Scoreboards.getManager().getScoreType("§b§lMeetup") != null) {
							if ((Scoreboards.getManager().getScore("§b§lMeetup") + 1) == 0) {
								Scoreboards.getManager().resetScore("§b§lMeetup");
								for (Player online : Bukkit.getServer().getOnlinePlayers()) {
									online.sendMessage(ChatColor.DARK_GRAY + "==============================================");													  
									online.sendMessage(ChatColor.GREEN + " Meetup has started, start headding to 0,0.");											  
									online.sendMessage(ChatColor.GREEN + " Only stop for a fight, nothing else.");
									online.sendMessage(ChatColor.DARK_GRAY + "==============================================");
									online.playSound(online.getLocation(), Sound.WITHER_DEATH, 1, 0);
								}
							} else {
								if ((Scoreboards.getManager().getScore("§b§lMeetup") + 1) < 0) {
									Scoreboards.getManager().setScore("§b§lMeetup", Scoreboards.getManager().getScore("§b§lMeetup") + 1);
								}
								if (Scoreboards.getManager().getScore("§b§lMeetup") == -1) {
									Bukkit.broadcastMessage(Main.prefix() + "1 minute to meetup, Pack your stuff and get ready to head to 0,0.");
									for (Player online : Bukkit.getServer().getOnlinePlayers()) {
										online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 0);
									}
								}
							}
						}
					}
				}, 1200, 1200);
				sender.sendMessage(Main.prefix() + "You restarted the timer runnables.");
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}