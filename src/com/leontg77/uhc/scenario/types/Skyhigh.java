package com.leontg77.uhc.scenario.types;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.scenario.Scenario;

@SuppressWarnings("deprecation")
public class Skyhigh extends Scenario {
	private BukkitRunnable task;
	private static boolean enabled = false;
	
	public Skyhigh() {
		super("Skyhigh", "After 45 minutes, any player below y: 101 will begin to take half a heart of damage every 30 seconds.");
	}

	public void setEnabled(boolean enable) {
		enabled = enable;
		
		if (enable) {
			this.task = new BukkitRunnable() {
				public void run() {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (!online.getWorld().getName().equals("lobby")) {
							if (online.getLocation().getBlockY() < 101) {
								online.damage(1.0);
							}
						}
					}
				}
			};
			
			task.runTaskTimer(Main.plugin, 600, 600);
		} else {
			task.cancel();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}
}