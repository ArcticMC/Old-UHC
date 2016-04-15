package com.leontg77.uhc.scenario.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.scenario.Scenario;

@SuppressWarnings("deprecation")
public class BestPvE extends Scenario implements Listener {
	private ArrayList<String> list = new ArrayList<String>();
	private BukkitRunnable task;
	private boolean enabled = false;

	public BestPvE() {
		super("BestPvE", "Everyone starts on a list called bestpve list, if you take damage you are removed from the list. The only way to get back on the list is getting a kill, All players on the bestpve list gets 1 extra heart each 10 minutes.");
	}
	
	public void setEnabled(boolean enable) {
		enabled = enable;
		
		if (enable) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				list.add(online.getName());
			}
			
			this.task = new BukkitRunnable() {
				public void run() {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (list.contains(online.getName())) {
							Damageable dmg = online;
							online.setMaxHealth(dmg.getMaxHealth() + 2);
							online.setHealth(dmg.getHealth() + 2);
							online.sendMessage(ChatColor.GREEN + "You were rewarded for your PvE skills!");
						}
					}
				}
			};
			
			task.runTaskTimer(Main.plugin, 10800, 12000);
		} else {
			list.clear();
			task.cancel();
		}
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public List<String> getList() {
		return list;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		if (event.getEntity().getKiller() == null)
			return;

		final Player player = event.getEntity().getKiller();

		if (!list.contains(player.getName())) {
			Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " got a kill! He is back on the Best PvE List!");
			Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
				public void run() {
					list.add(player.getName());
				}
			}, 20);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		if (!(event.getEntity() instanceof Player))
			return;
		
		if (event.isCancelled()) {
			return;
		}
		
		if (event.getCause() == DamageCause.SUICIDE) {
			return;
		}

		Player player = (Player) event.getEntity();

		if (list.contains(player.getName())) {
			Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " took damage!");
			list.remove(player.getName());
		}
	}
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().startsWith("/blist")) {
			StringBuilder pvelist = new StringBuilder("");
			
			for (int i = 0; i < list.size(); i++) {
				if (pvelist.length() > 0 && i == list.size() - 1) {
					pvelist.append(" §7and §6");
				}
				else if (pvelist.length() > 0 && pvelist.length() != list.size()) {
					pvelist.append("§7, §6");
				}
				
				pvelist.append(ChatColor.GOLD + list.get(i));
			}
			
			event.getPlayer().sendMessage(Main.prefix() + "People still on the best pve list: §6" + (pvelist.length() > 0 ? pvelist.toString().trim() : "None") + "§7.");
			event.setCancelled(true);
		}
	}
}