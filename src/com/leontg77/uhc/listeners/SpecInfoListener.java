package com.leontg77.uhc.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.ArrayUtil;
import com.leontg77.uhc.util.DamageUtil;
import com.leontg77.uhc.util.TimerRunnable;

@SuppressWarnings("deprecation")
public class SpecInfoListener implements Listener {
	private ArrayList<Location> locs = new ArrayList<Location>();

	@EventHandler
	public void onMineDiamond(BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.DIAMOND_ORE) return;
		if (locs.contains(event.getBlock().getLocation())) return;
		
		Player player = event.getPlayer();
		int amount = 0;
		Location loc = event.getBlock().getLocation();
		
		for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
			for (int y = loc.getBlockY() - 1; y <= loc.getBlockY() + 1; y++) {
				for (int z = loc.getBlockZ() - 1; z <= loc.getBlockZ() + 1; z++) {
					if (loc.getWorld().getBlockAt(x, y, z).getType() == Material.DIAMOND_ORE) {
						amount++;
						locs.add(loc.getWorld().getBlockAt(x, y, z).getLocation());
					}
				}
			}
		}
		
		if (ArrayUtil.totalD.containsKey(player.getName())) {
			ArrayUtil.totalD.put(player.getName(), ArrayUtil.totalD.get(player.getName()) + amount);
		} else {
			ArrayUtil.totalD.put(player.getName(), amount);
		}
		
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (ArrayUtil.spectating.contains(online.getName())) {
				online.sendMessage("[�9S�f] �7" + player.getName() + "�f:�3DIAMOND �f[V:�3" + amount + "�f] [T:�3" + ArrayUtil.totalD.get(player.getName()) + "�f]");
			}
		}
		amount = 0;
	}

	@EventHandler
	public void onMineGold(BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.GOLD_ORE) return;
		if (locs.contains(event.getBlock().getLocation())) return;
		
		Player player = event.getPlayer();
		int amount = 0;
		Location loc = event.getBlock().getLocation();
		
		for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
			for (int y = loc.getBlockY() - 1; y <= loc.getBlockY() + 1; y++) {
				for (int z = loc.getBlockZ() - 1; z <= loc.getBlockZ() + 1; z++) {
					if (loc.getWorld().getBlockAt(x, y, z).getType() == Material.GOLD_ORE) {
						amount++;
						locs.add(loc.getWorld().getBlockAt(x, y, z).getLocation());
					}
				}
			}
		}
		
		if (ArrayUtil.totalG.containsKey(player.getName())) {
			ArrayUtil.totalG.put(player.getName(), ArrayUtil.totalG.get(player.getName()) + amount);
		} else {
			ArrayUtil.totalG.put(player.getName(), amount);
		}
		
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (ArrayUtil.spectating.contains(online.getName())) {
				online.sendMessage("[�9S�f] �7" + player.getName() + "�f:�6GOLD �f[V:�6" + amount + "�f] [T:�6" + ArrayUtil.totalG.get(player.getName()) + "�f]");
			}
		}
		amount = 0;
	}

	@EventHandler
	public void onPearl(PlayerTeleportEvent event) {
		if (event.getCause() != TeleportCause.ENDER_PEARL) {
			return;
		}
		
		Player player = event.getPlayer();

		if (ArrayUtil.spectating.contains(player.getName())) return;
		
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (ArrayUtil.spectating.contains(online.getName())) {
				online.sendMessage("[�9S�f] �5Pearl:�6" + player.getName() + "�f->�d" + DamageUtil.convertHealth(event.getFrom().distance(event.getTo())) + " blocks.");
			}
		}
	}

	@EventHandler
	public void onPortal(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();

		if (ArrayUtil.spectating.contains(player.getName())) {
			return;
		}

		if (!event.getFrom().getName().equals("lobby") && !player.getWorld().getName().equals("lobby")) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �dPortal:�6" + player.getName() + "�f from �a" + event.getFrom().getName() + "�f to �c" + player.getWorld().getName());
				}
			}
		}
	}

	@EventHandler
	public void onPot(PlayerItemConsumeEvent event) {
		if (event.getItem().getType() != Material.POTION) return;
		
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		Potion pot;

		if (item.getDurability() == 8261) {
			pot = new Potion(PotionType.INSTANT_HEAL, 1);
		} else if (item.getDurability() == 16453) {
			pot = new Potion(PotionType.INSTANT_HEAL, 1);
		} else {
			try {
				pot = Potion.fromItemStack(item);
			} catch (Exception e) {
				return;
			}
		}
		
		if (ArrayUtil.spectating.contains(player.getName())) return;
		
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (ArrayUtil.spectating.contains(online.getName())) {
				for (PotionEffect e : pot.getEffects()) {
					online.sendMessage("[�9S�f] �5Potion: �a" + player.getName() + "�f<->�d" + pot.getType().name().toUpperCase().substring(0, 1) + pot.getType().name().toLowerCase().replaceAll("_", " ").substring(1) + " �ftier:�d" + pot.getLevel() + ((e.getDuration() / 20) > 0 ? " �flength:�d" + TimerRunnable.ticksToString(e.getDuration() / 20) : ""));
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onPotionSplash(PotionSplashEvent event) {
		if (!(event.getPotion().getShooter() instanceof Player)) return;
		
		Player player = (Player) event.getPotion().getShooter();
		ItemStack item = event.getPotion().getItem();
		Potion pot;

		try {
			pot = Potion.fromItemStack(item);
		} catch (Exception e) {
			return;
		}
		
		if (ArrayUtil.spectating.contains(player.getName())) return;
		
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (ArrayUtil.spectating.contains(online.getName())) {
				for (PotionEffect e : pot.getEffects()) {
					online.sendMessage("[�9S�f] �5Potion: �a" + player.getName() + "�f<->�d" + pot.getType().name().toUpperCase().substring(0, 1) + pot.getType().name().toLowerCase().replaceAll("_", " ").substring(1) + " �ftier:�d" + pot.getLevel() + ((e.getDuration() / 20) > 0 ? " �flength:�d" + TimerRunnable.ticksToString(e.getDuration() / 20) : ""));
					break;
				}
			}
		}
	}

	@EventHandler
	public void onHeal(PlayerItemConsumeEvent event) {
		if (event.getItem().getType() != Material.GOLDEN_APPLE) return;
		
		Player player = event.getPlayer();
		ItemStack item = event.getItem();

		if (ArrayUtil.spectating.contains(player.getName())) return;
		
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (ArrayUtil.spectating.contains(online.getName())) {
				online.sendMessage("[�9S�f] �aHeal: �6" + player.getName() + "�f<->�6" + (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("�6Golden Head") ? "�5Golden Head" : "Golden Apple"));
			}
		}
	}

	@EventHandler
	public void onCraft(CraftItemEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getRecipe().getResult();

		if (ArrayUtil.spectating.contains(player.getName())) return;
		
		if (item.getType() == Material.GOLDEN_APPLE) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�6" + (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("�6Golden Head") ? "�5Golden Head" : "Golden Apple"));
				}
			}
		}
		
		if (item.getType() == Material.DIAMOND_HELMET) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�bDia. Helmet");
				}
			}
		}
		
		if (item.getType() == Material.DIAMOND_CHESTPLATE) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�bDia. Chest");
				}
			}
		}
		
		if (item.getType() == Material.DIAMOND_LEGGINGS) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�bDia. Leggings");
				}
			}
		}
		
		if (item.getType() == Material.DIAMOND_BOOTS) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�bDia. Boots");
				}
			}
		}
		
		if (item.getType() == Material.BOW) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�bBow");
				}
			}
		}
		
		if (item.getType() == Material.DIAMOND_SWORD) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�bDia. Sword");
				}
			}
		}
		
		if (item.getType() == Material.ANVIL) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�dAnvil");
				}
			}
		}
		
		if (item.getType() == Material.ENCHANTMENT_TABLE) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�dEnchant. Table");
				}
			}
		}
		
		if (item.getType() == Material.BREWING_STAND_ITEM) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				if (ArrayUtil.spectating.contains(online.getName())) {
					online.sendMessage("[�9S�f] �2Craft�f: �a" + player.getName() + "�f<->�dBrewing Stand");
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onDamage(final EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;

		final Player player = (Player) event.getEntity();
		
		if (ArrayUtil.spectating.contains(player.getName())) return;
		
		if (event instanceof EntityDamageByEntityEvent) {
			onDamageByOther(player, (EntityDamageByEntityEvent) event);
			return;
		}
		
		final DamageCause cause = event.getCause();
		final Damageable dmg = player;
		final double olddamage = dmg.getHealth();
		
		Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
			public void run() {
				double damage = olddamage - dmg.getHealth();
				
				if (cause == DamageCause.LAVA) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName()) && online.hasPermission("uhc.admin")) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dLava �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dFire �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				}  else if (cause == DamageCause.CONTACT) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dCactus �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.DROWNING) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dDrowning �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.FALL) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dFall �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.LIGHTNING) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dLightning �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.MAGIC) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dMagic �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.POISON) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dPoison �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.STARVATION) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dStarving �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.SUFFOCATION) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dSuffocation �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.VOID) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dVoid �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.WITHER) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dWither �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (cause == DamageCause.BLOCK_EXPLOSION) {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�dTNT �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�d??? �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				}
			}
		}, 1);
	}

	private void onDamageByOther(final Player player, final EntityDamageByEntityEvent event) {
		final Damageable dmg = player;
		
		if (ArrayUtil.spectating.contains(player.getName())) return;
		
		final double olddamage = dmg.getHealth();
		
		Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
			public void run() {
				double damage = olddamage - dmg.getHealth();
				
				if (event.getDamager() instanceof Player) {
					Player killer = (Player) event.getDamager();
					if (ArrayUtil.spectating.contains(killer.getName())) return;
					Damageable dmgr = killer;
					
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName()) && online.hasPermission("uhc.admin")) {
							online.sendMessage("[�9S�f] �4PvP�f:�a" + killer.getName() + "�f-M>�c" + player.getName() + " �f[�a" + DamageUtil.convertHealth((dmgr.getHealth() / 2)) + "�f:�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				} else if (event.getDamager() instanceof Projectile) {
					Projectile p = (Projectile) event.getDamager();
					
					if (p.getShooter() instanceof Player) {
						Player shooter = (Player) p.getShooter();
						Damageable dmgr = shooter;
						
						if (p instanceof Arrow) {
							for (Player online : Bukkit.getServer().getOnlinePlayers()) {
								if (ArrayUtil.spectating.contains(online.getName())) {
									online.sendMessage("[�9S�f] �4PvP�f:�a" + shooter.getName() + "�f-B>�c" + player.getName() + " �f[�a" + DamageUtil.convertHealth((dmgr.getHealth() / 2)) + "�f:�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
								}
							}
						} else if (p instanceof Snowball) {
							for (Player online : Bukkit.getServer().getOnlinePlayers()) {
								if (ArrayUtil.spectating.contains(online.getName())) {
									online.sendMessage("[�9S�f] �4PvP�f:�a" + shooter.getName() + "�f-S>�c" + player.getName() + " �f[�a" + DamageUtil.convertHealth((dmgr.getHealth() / 2)) + "�f:�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
								}
							}
						} else if (p instanceof Egg) {
							for (Player online : Bukkit.getServer().getOnlinePlayers()) {
								if (ArrayUtil.spectating.contains(online.getName())) {
									online.sendMessage("[�9S�f] �4PvP�f:�a" + shooter.getName() + "�f-E>�c" + player.getName() + " �f[�a" + DamageUtil.convertHealth((dmgr.getHealth() / 2)) + "�f:�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
								}
							}
						} else if (p instanceof FishHook) {
							for (Player online : Bukkit.getServer().getOnlinePlayers()) {
								if (ArrayUtil.spectating.contains(online.getName())) {
									online.sendMessage("[�9S�f] �4PvP�f:�a" + shooter.getName() + "�f-F>�c" + player.getName() + " �f[�a" + DamageUtil.convertHealth((dmgr.getHealth() / 2)) + "�f:�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
								}
							}
						} else {
							for (Player online : Bukkit.getServer().getOnlinePlayers()) {
								if (ArrayUtil.spectating.contains(online.getName())) {
									online.sendMessage("[�9S�f] �4PvP�f:�a" + shooter.getName() + "�f-?P>�c" + player.getName() + " �f[�a" + DamageUtil.convertHealth((dmgr.getHealth() / 2)) + "�f:�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
								}
							}
						}
					} else {
						if (!(p.getShooter() instanceof Entity)) {
							return;
						}
						
						for (Player online : Bukkit.getServer().getOnlinePlayers()) {
							if (ArrayUtil.spectating.contains(online.getName())) {
								online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�d" + ((Entity) p.getShooter()).getType().name().substring(0, 1).toUpperCase() + ((Entity) p.getShooter()).getType().name().substring(1).toLowerCase().replaceAll("_", "") + " �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
							}
						}
					}
				} else if (event.getDamager() instanceof LivingEntity) {
					Entity e = event.getDamager();
					
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {
						if (ArrayUtil.spectating.contains(online.getName())) {
							online.sendMessage("[�9S�f] �5PvE�f:�c" + player.getName() + "�f<-�d" + e.getType().name().substring(0, 1).toUpperCase() + e.getType().name().substring(1).toLowerCase().replaceAll("_", "") + " �f[�c" + DamageUtil.convertHealth((dmg.getHealth() / 2)) + "�f] [�6" + DamageUtil.convertHealth((damage / 2)) + "�f]");
						}
					}
				}
			}
		}, 1);
	}
}