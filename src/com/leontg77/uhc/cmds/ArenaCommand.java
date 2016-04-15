package com.leontg77.uhc.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.leontg77.uhc.Arena;
import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.PlayersUtil;

public class ArenaCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("arena")) {
			final Arena a = Arena.getManager();
			if (args.length == 0) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (a.isEnabled()) {
						if (a.hasPlayer(player)) {
							player.sendMessage(Main.prefix() + "You are already in the arena.");
							return true;
						}
						a.addPlayer(player);
						player.sendMessage(Main.prefix() + "You joined the arena.");
					} else {
						player.sendMessage(ChatColor.RED + "The arena is currently disabled.");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Only players can join arenas.");
				}
				return true;
			}
			
			if (sender.hasPermission("uhc.arenaadmin")) {
				if (args[0].equalsIgnoreCase("enable")) {
					a.setEnabled(true);
					PlayersUtil.broadcast(Main.prefix() + "The arena has been enabled.");
				} else if (args[0].equalsIgnoreCase("disable")) {
					a.setEnabled(false);
					PlayersUtil.broadcast(Main.prefix() + "The arena has been disabled.");
				} else if (args[0].equalsIgnoreCase("reset")) {
					new BukkitRunnable() {
			            int i = -1;
			            public void run() {
			                if (i != a.getBrokenBlocks().size() - 1) {
			                    i++;
			                    BlockState bs = a.getBrokenBlocks().get(i);
			                    bs.update(true, false);
			                    bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND, bs.getBlock().getType());
			                } else {
			                	a.getBrokenBlocks().clear();
			                    this.cancel();
			                }
			            }
			        }.runTaskTimer(Main.plugin, 1, 1);
			        
					for (LivingEntity b : a.getDeadEntities()) {
						LivingEntity e = (LivingEntity) b.getWorld().spawnEntity(b.getLocation(), b.getType());
						Damageable d = e;
						e.setHealth(d.getHealth());
						e.setMaxHealth(d.getMaxHealth());
						e.setCustomName(b.getCustomName());
						e.setFireTicks(0);
						e.setCustomNameVisible(b.isCustomNameVisible());
						e.setCanPickupItems(false);
					}
					a.getBrokenBlocks().clear();
					a.getDeadEntities().clear();
				} else {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if (a.isEnabled()) {
							if (a.hasPlayer(player)) {
								player.sendMessage(Main.prefix() + "You are already in the arena.");
								return true;
							}
							a.addPlayer(player);
							player.sendMessage(Main.prefix() + "You joined the arena.");
						} else {
							player.sendMessage(ChatColor.RED + "The arena is currently disabled.");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Only players can join arenas.");
					}
					return true;
				}
			} else {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (a.isEnabled()) {
						if (a.hasPlayer(player)) {
							player.sendMessage(Main.prefix() + "You are already in the arena.");
							return true;
						}
						a.addPlayer(player);
						player.sendMessage(Main.prefix() + "You joined the arena.");
					} else {
						player.sendMessage(ChatColor.RED + "The arena is currently disabled.");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Only players can join arenas.");
				}
				return true;
			}
		}
		return true;
	}
}