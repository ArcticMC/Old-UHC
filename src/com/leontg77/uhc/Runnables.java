package com.leontg77.uhc;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.leontg77.uhc.util.ArrayUtil;
import com.leontg77.uhc.util.PlayersUtil;

/**
 * The runnable class for all the runnables
 * @author LeonTG77
 */
public class Runnables extends BukkitRunnable {
	public static int timeToStart;
	
	/**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     */
	public void run() {
		if (timeToStart > 0 && timeToStart <= 3) {
			for (Player online : PlayersUtil.getPlayers()) {
				online.sendMessage(Main.prefix() + "UHC is starting in §6" + String.valueOf(timeToStart) + "§7.");
				online.playSound(online.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0);
			}
		}
		
		if (timeToStart == 1) {
			Main.stopCountdown();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				public void run() {
					for (Player online : PlayersUtil.getPlayers()) {
						online.playSound(online.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
						if (ArrayUtil.spectating.contains(online.getName())) {
							Spectator.getManager().set(online, false);
						}
						online.setAllowFlight(false);
						online.setFlying(false);
						online.setHealth(20.0);
						online.setFireTicks(0);
						online.setSaturation(20);
						online.setLevel(0);
						online.setExp(0);
						if (!ArrayUtil.spectating.contains(online.getName())) {
							online.showPlayer(online);
						}
						online.awardAchievement(Achievement.OPEN_INVENTORY);
						online.setFoodLevel(20);
						online.getInventory().clear();
						online.getInventory().setArmorContents(null);
						online.setItemOnCursor(new ItemStack (Material.AIR));
						
						if (online.getGameMode() != GameMode.SURVIVAL) {
							online.setGameMode(GameMode.SURVIVAL);
						}
						
						for (PotionEffect effect : online.getActivePotionEffects()) {
							online.removePotionEffect(effect.getType());	
						}
						online.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 250, 100));
						online.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 6000, 100));
						online.sendMessage(Main.prefix() + "UHC has now started, Good luck!");
						online.sendMessage(Main.prefix() + "PvP will be enabled in §6" + Settings.getInstance().getData().getInt("game.pvp") + " §7mins in.");
						online.sendMessage(Main.prefix() + "Meetup at §6" + Settings.getInstance().getData().getInt("game.meetup") + " §7mins in.");
						online.sendMessage(Main.prefix() + "Remember to read the match post: " + Settings.getInstance().getData().getString("match.post"));
						online.sendMessage(Main.prefix() + "/helpop questions.");
					}
					
					GameState.setState(GameState.INGAME);
					Scoreboards.getManager().setScore("§a§lPvE", 1);
					Scoreboards.getManager().setScore("§a§lPvE", 0);
					Scoreboards.getManager().setScore("§b§lMeetup", Integer.parseInt("-" + Settings.getInstance().getData().getInt("game.meetup")));
					Scoreboards.getManager().setScore("§b§lPvP", Integer.parseInt("-" + Settings.getInstance().getData().getInt("game.pvp")));
					Scoreboards.getManager().setScore("§b§lFinal heal", -1);
					
					for (World world : Bukkit.getWorlds()) {
						world.setTime(0);
						world.setDifficulty(Difficulty.HARD);
						world.setPVP(false);
						for (Entity mobs : world.getEntities()) {
							if (mobs.getType() == EntityType.BLAZE ||
									mobs.getType() == EntityType.CAVE_SPIDER ||
									mobs.getType() == EntityType.CREEPER ||
									mobs.getType() == EntityType.ENDERMAN ||
									mobs.getType() == EntityType.ZOMBIE ||
									mobs.getType() == EntityType.WITCH ||
									mobs.getType() == EntityType.WITHER ||
									mobs.getType() == EntityType.DROPPED_ITEM ||
									mobs.getType() == EntityType.GHAST ||
									mobs.getType() == EntityType.GIANT ||
									mobs.getType() == EntityType.MAGMA_CUBE ||
									mobs.getType() == EntityType.DROPPED_ITEM ||
									mobs.getType() == EntityType.SKELETON ||
									mobs.getType() == EntityType.SPIDER ||
									mobs.getType() == EntityType.SLIME ||
									mobs.getType() == EntityType.SILVERFISH ||
									mobs.getType() == EntityType.SKELETON || 
									mobs.getType() == EntityType.EXPERIENCE_ORB) {
								
								mobs.remove();
							}
						}
					}
				}
			}, 20);
			
			Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
				public void run() {
					if (Scoreboards.getManager().getScoreType("§b§lFinal heal") != null) {
						if ((Scoreboards.getManager().getScore("§b§lFinal heal") + 1) == 0) {
							Scoreboards.getManager().resetScore("§b§lFinal heal");
							for (Player online : PlayersUtil.getPlayers()) {
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
							for (Player online : PlayersUtil.getPlayers()) {
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
								for (Player online : PlayersUtil.getPlayers()) {
									online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 0);
								}
							}
							if (Scoreboards.getManager().getScore("§b§lPvP") == -1) {
								Bukkit.broadcastMessage(Main.prefix() + "1 minute to pvp, do §6/stalk §7if you think you're being stalked.");
								for (Player online : PlayersUtil.getPlayers()) {
									online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 0);
								}
							}
						}
					}

					if (Scoreboards.getManager().getScoreType("§b§lMeetup") != null) {
						if ((Scoreboards.getManager().getScore("§b§lMeetup") + 1) == 0) {
							Scoreboards.getManager().resetScore("§b§lMeetup");
							for (Player online : PlayersUtil.getPlayers()) {
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
								for (Player online : PlayersUtil.getPlayers()) {
									online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 0);
								}
							}
						}
					}
				}
			}, 1200, 1200);
			
			Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
				public void run() {
					try {
						Bukkit.getPlayer(Settings.getInstance().getData().getString("game.host")).chat("/gamerule doMobSpawning true");
					} catch (Exception e) {}
				}
			}, 3600);
		}
		timeToStart -=1;
	}
}