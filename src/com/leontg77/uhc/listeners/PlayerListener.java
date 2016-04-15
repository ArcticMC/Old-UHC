package com.leontg77.uhc.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Skull;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.leontg77.uhc.Arena;
import com.leontg77.uhc.GameState;
import com.leontg77.uhc.InvGUI;
import com.leontg77.uhc.Main;
import com.leontg77.uhc.Scoreboards;
import com.leontg77.uhc.Settings;
import com.leontg77.uhc.Spectator;
import com.leontg77.uhc.cmds.TeamCommand;
import com.leontg77.uhc.cmds.VoteCommand;
import com.leontg77.uhc.util.ArrayUtil;
import com.leontg77.uhc.util.BlockUtil;
import com.leontg77.uhc.util.PlayersUtil;
import com.leontg77.uhc.util.RecipeUtil;

@SuppressWarnings("deprecation")
public class PlayerListener implements Listener {
	private Settings settings = Settings.getInstance();
	
	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event) {
		World w = Bukkit.getServer().getWorld(settings.getData().getString("spawn.world"));
		double x = settings.getData().getDouble("spawn.x");
		double y = settings.getData().getDouble("spawn.y");
		double z = settings.getData().getDouble("spawn.z");
		float yaw = (float) settings.getData().getDouble("spawn.yaw");
		float pitch = (float) settings.getData().getDouble("spawn.pitch");
		
		Location loc = new Location(w, x, y, z, yaw, pitch);
		event.setRespawnLocation(loc);
		
		if (!Arena.getManager().isEnabled() && !GameState.isState(GameState.LOBBY) && !event.getPlayer().hasPermission("uhc.prelist")) {
			Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
				public void run() {
					if (event.getPlayer().isOnline() && !ArrayUtil.spectating.contains(event.getPlayer().getName())) {
						event.getPlayer().kickPlayer("§7Thanks for playing!");
					}
				}
			}, 1200);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		final Player player = event.getEntity().getPlayer();
		
		if (!Arena.getManager().isEnabled()) {
			player.setWhitelisted(false);
			
			StringBuilder s = new StringBuilder("");
	    	
	    	for (String d : event.getDeathMessage().split(" ")) {
	    		s.append(ChatColor.GRAY + d).append(" ");
	    	}
	    	
	    	String result = s.toString().trim();
	    	
	    	for (Player online : Bukkit.getServer().getOnlinePlayers()) {
	    		if (result.contains(online.getName())) {
	    			result = result.replaceAll(online.getName(), (online.getScoreboard().getPlayerTeam(online) == null ? "§f" + online.getName() : online.getScoreboard().getPlayerTeam(online).getPrefix() + online.getName()));
	    		}
	    	}
	    	
	    	event.setDeathMessage(Main.prefix() + result);
	    	
		    player.getWorld().strikeLightningEffect(player.getLocation());

		    if (Main.ghead) {
				try {
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
						public void run() {
							player.getLocation().getBlock().setType(Material.NETHER_FENCE);
					        player.getLocation().add(0, 1, 0).getBlock().setType(Material.SKULL);
					        
					        Skull skull = (Skull) player.getLocation().add(0, 1, 0).getBlock().getState();
						    skull.setSkullType(SkullType.PLAYER);
						    skull.setOwner(player.getName());
						    skull.setRotation(BlockUtil.getBlockFaceDirection(player.getLocation()));
						    skull.update();
						    
						    Block b = player.getLocation().add(0, 1, 0).getBlock();
						    b.setData((byte) 0x1, true);
						}
					}, 1L);
				} catch (Exception e) {
					Bukkit.getLogger().warning(ChatColor.RED + "Could not place player skull.");
				}
		    }
		}
	}
	
	@EventHandler
	public void onPlayerKill(PlayerDeathEvent event){
		if (!Arena.getManager().isEnabled()) {
			if (event.getEntity().getKiller() == null) {
		        Scoreboards.getManager().setScore("§a§lPvE", Scoreboards.getManager().getScore("§a§lPvE") + 1);
				Scoreboards.getManager().resetScore(event.getEntity().getName());
				return;
			}
			
			Player killer = event.getEntity().getKiller();

	        Scoreboards.getManager().setScore(killer.getName(), Scoreboards.getManager().getScore(killer.getName()) + 1);
			Scoreboards.getManager().resetScore(event.getEntity().getName());
		}
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (ArrayUtil.spectating.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (Arena.getManager().hasPlayer(event.getPlayer())) {
			return;
		}
		
		if (GameState.isState(GameState.WAITING) || ArrayUtil.spectating.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		String bm = ChatColor.translateAlternateColorCodes('&', settings.getData().getString("motd"));
		
		if (bm == null) {
			event.setMotd(Bukkit.getMotd());
		} else {
			event.setMotd("§8§l======== §4§lLeon & Polar UHC §a§l1.7-1.8 §8§l======== " + bm);
		}

		int max = settings.getData().getInt("maxplayers");
		
		if (max == 0) {
			event.setMaxPlayers(Bukkit.getMaxPlayers());
		} else {
			event.setMaxPlayers(max);
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		Player player = (Player) event.getEntity();
		
		if (ArrayUtil.spectating.contains(player.getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerPearl(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		if (!(event.getDamager() instanceof EnderPearl)) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {	
        if (event.getItem() == null)
        	return;
        
		Player player = event.getPlayer();
        
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getItem().getType().equals(Material.COMPASS) && ArrayUtil.spectating.contains(player.getName())) {
				InvGUI.getManager().openSelector(player);
				event.setCancelled(true);
			}
			if (event.getItem().getType().equals(Material.INK_SACK) && ArrayUtil.spectating.contains(player.getName())) {
				if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
				} else {
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10000000, 0));
				}
				event.setCancelled(true);
			}
			if (event.getItem().getType().equals(Material.FEATHER) && ArrayUtil.spectating.contains(player.getName())) {
				player.teleport(new Location(player.getWorld(), 0, 100, 0));
				event.setCancelled(true);
			}
		} else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (event.getItem().getType().equals(Material.COMPASS) && ArrayUtil.spectating.contains(player.getName())) {
				ArrayList<Player> players = new ArrayList<Player>();
				for (Player online : Bukkit.getServer().getOnlinePlayers()) {
					if (!ArrayUtil.spectating.contains(online.getName())) {
						players.add(online);
					}
				}
				Player target = players.get(new Random().nextInt(players.size()));
				player.teleport(target.getLocation());
				player.sendMessage(Main.prefix() + "You teleported to §6" + target.getName() + "§7.");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
    public void onSpectatorInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (ArrayUtil.spectating.contains(player.getName())) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {		
				if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.FURNACE || event.getClickedBlock().getType() == Material.BREWING_STAND || event.getClickedBlock().getType() == Material.BURNING_FURNACE)
					return;
				
				if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
					Chest chest = (Chest) event.getClickedBlock().getState();
					Inventory inv = Bukkit.createInventory(null, chest.getInventory().getSize(), "Chest");
					inv.setContents(chest.getInventory().getContents());
					player.openInventory(inv);
				}
				event.setCancelled(true);
			}
			event.setCancelled(true);
		} else {
			event.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (online.hasPermission("uhc.commandspy") && (online.getGameMode() == GameMode.CREATIVE || ArrayUtil.spectating.contains(online.getName())) && online != player) {
				online.sendMessage(ChatColor.YELLOW + player.getName() + ": " + ChatColor.GRAY + event.getMessage());
			}
		}
		
		if (event.getMessage().startsWith("/me") && !player.hasPermission("uhc.admin")) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You do not have access to that command.");
		}
		
		if (event.getMessage().startsWith("/bukkit:") && !player.hasPermission("uhc.admin")) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You do not have access to that command.");
		}
		
		if (event.getMessage().startsWith("/pl") && !event.getMessage().startsWith("/playsound")) {
			event.setCancelled(true);
			player.sendMessage("Plugins (5): §aAutoUBL§f, §aLeonUHC§f, §aPermissionsEx§f, §aWorldEdit§f, §aWorldBorder");
		}
		
		if (event.getMessage().startsWith("/plugins")) {
			event.setCancelled(true);
			player.sendMessage("Plugins (5): §aAutoUBL§f, §aLeonUHC§f, §aPermissionsEx§f, §aWorldEdit§f, §aWorldBorder");
		}
		
		if (event.getMessage().startsWith("/minecraft:") && !player.hasPermission("uhc.admin")) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You do not have access to that command.");
		}
		
		if (event.getMessage().startsWith("/perma")) {
			if (player.hasPermission("uhc.perma")) {
				player.chat("/gamerule doDaylightCycle false");
				player.chat("/time set 6000");
				for (Player online : Bukkit.getServer().getOnlinePlayers()) {
					online.sendMessage(Main.prefix() + "Permaday enabled.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
			event.setCancelled(true);
		}
		
		if ((event.getMessage().startsWith("/genpyro") || event.getMessage().startsWith("/pyrophobia:genpyro")) && !player.hasPermission("uhc.pyro")) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You do not have access to that command.");
		}
		
		if (event.getMessage().startsWith("/kill")) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You do not have access to that command.");
		}
		
		if (ArrayUtil.spectating.contains(player.getName()) && 
				!player.hasPermission("uhc.admin") && 
				!event.getMessage().startsWith("/invsee") && 
				!event.getMessage().startsWith("/tp") && 
				!event.getMessage().startsWith("/speed") && 
				!event.getMessage().startsWith("/list") && 
				!event.getMessage().startsWith("/r") && 
				!event.getMessage().startsWith("/pm") && 
				!event.getMessage().startsWith("/spec") && 
				!event.getMessage().startsWith("/msg") && 
				!event.getMessage().startsWith("/m") && 
				!event.getMessage().startsWith("/reply") && 
				!event.getMessage().startsWith("/t") && 
				!event.getMessage().startsWith("/tell") && 
				!event.getMessage().startsWith("/w") && 
				!event.getMessage().startsWith("/whisper") && 
				!event.getMessage().startsWith("/helpop")) {
			
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot do commands as a spectator.");
		}
	}
	
	@EventHandler
    public void onPlayerInteractPlayer(PlayerInteractEntityEvent event) { 	
    	Player player = (Player) event.getPlayer();
		Entity clicked = event.getRightClicked();
				
		if (ArrayUtil.spectating.contains(player.getName())) {
			if (clicked instanceof Player) {
				if (ArrayUtil.spectating.contains(((Player) clicked).getName())) 
					return;
				InvGUI.getManager().openInv(player, ((Player) clicked));
			} else if (clicked.getType() != EntityType.MINECART_CHEST) {
				event.setCancelled(true);
			}
		}
    }
	
	@EventHandler
	public void onPreCraftEvent(PrepareItemCraftEvent event) {
        if (RecipeUtil.areSimilar(event.getRecipe(), Main.res)) {
            ItemStack itemstack = event.getInventory().getResult();
            ItemMeta itemmeta = itemstack.getItemMeta();
            String s = "N/A";
            ItemStack aitemstack[] = event.getInventory().getContents();
            int i = aitemstack.length;
            for(int j = 0; j < i; j++) {
                ItemStack itemstack1 = aitemstack[j];
                if(itemstack1.getType().equals(Material.SKULL_ITEM)) {
                    SkullMeta skullmeta = (SkullMeta)itemstack1.getItemMeta();
                    s = skullmeta.getOwner();
                }
            }

            List<String> list = itemmeta.getLore();
            list.add(ChatColor.AQUA + "Made from the head of: " + s);
            itemmeta.setLore(list);
            itemstack.setItemMeta(itemmeta);
            event.getInventory().setResult(itemstack);
        }
    }

	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		
		if (ArrayUtil.spectating.contains(player.getName())) {
			event.setQuitMessage(null);
		} else {
			event.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName() + ChatColor.GRAY + " left.");
			if (!GameState.isState(GameState.LOBBY) && !player.hasPermission("uhc.prelist")) {
				ArrayUtil.relog.put(player.getName(), new BukkitRunnable() {
					public void run() {
						if (!player.isOnline()) {
							if (((OfflinePlayer) player).isWhitelisted()) {
								((OfflinePlayer) player).setWhitelisted(false);
								Bukkit.broadcastMessage(Main.prefix() + "§6" + player.getName() + " §7took too long to come back.");
								Scoreboards.getManager().resetScore(player.getName());
								ArrayUtil.relog.remove(player.getName());
							}
						}
					}
				});
				
				ArrayUtil.relog.get(player.getName()).runTaskLater(Main.plugin, 12000);
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if (ArrayUtil.relog.containsKey(player.getName())) {
			ArrayUtil.relog.get(player.getName()).cancel();;
			ArrayUtil.relog.remove(player.getName());
		}
		if (!ArrayUtil.totalD.containsKey(player.getName())) {
			ArrayUtil.totalD.put(player.getName(), 0);
		}
		if (!ArrayUtil.totalG.containsKey(player.getName())) {
			ArrayUtil.totalG.put(player.getName(), 0);
		}
		if (!TeamCommand.invites.containsKey(player)) {
			TeamCommand.invites.put(player, new ArrayList<Player>());
		}
		
		if (!GameState.isState(GameState.LOBBY)) {
			if (ArrayUtil.spectating.contains(player.getName())) {
				event.setJoinMessage(null);
			} else {
				event.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName() + ChatColor.GRAY + " joined.");
			}
		} else {
			event.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName() + ChatColor.GRAY + " joined.");
		}
		
		Spectator.getManager().hideAll(player);
		
		if (ArrayUtil.spectating.contains(player.getName())) {
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			
			Spectator.getManager().set(player, true);
		}
		
		if (GameState.isState(GameState.LOBBY)) {
			World w = Bukkit.getServer().getWorld(settings.getData().getString("spawn.world"));
			double x = settings.getData().getDouble("spawn.x");
			double y = settings.getData().getDouble("spawn.y");
			double z = settings.getData().getDouble("spawn.z");
			float yaw = (float) settings.getData().getDouble("spawn.yaw");
			float pitch = (float) settings.getData().getDouble("spawn.pitch");
			final Location loc = new Location(w, x, y, z, yaw, pitch);
			player.teleport(loc);
		}
		
		player.setFlySpeed(0.1f);
		player.setWalkSpeed(0.2f);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLobbyHunger(FoodLevelChangeEvent event) {
		if (event.getEntity().getWorld().getName().equals("lobby")) {
			event.setFoodLevel(20);
			event.setCancelled(true);
		}
	}
    
	@EventHandler
	public void onPlayerEatFood(PlayerItemConsumeEvent event) {
		final Player player = event.getPlayer();
		final float before = player.getSaturation();
		
		new BukkitRunnable() {
			public void run() {
				float change = player.getSaturation() - before;
				player.setSaturation((float)(before + change * 2.5D));
			}
	    }.runTaskLater(Main.plugin, 1L);
	}
	  
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getFoodLevel() < ((Player)event.getEntity()).getFoodLevel()) {
			event.setCancelled(new Random().nextInt(100) < 66);
	    }
	}
	
	@EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if (VoteCommand.vote) {
			if (event.getMessage().equalsIgnoreCase("y")) {
				if (!GameState.isState(GameState.LOBBY) && player.getWorld().getName().equals("lobby")) {
					player.sendMessage(ChatColor.RED + "You cannot vote when you are dead.");
					event.setCancelled(true);
					return;
				}
				
				if (ArrayUtil.spectating.contains(player.getName())) {
					player.sendMessage(ChatColor.RED + "You cannot vote as a spectator.");
					event.setCancelled(true);
					return;
				}
				
				if (ArrayUtil.voted.contains(player.getName())) {
					player.sendMessage(ChatColor.RED + "You have already voted.");
					return;
				}
				player.sendMessage(Main.prefix() + "Vote voted yes.");
				VoteCommand.yes++;
				event.setCancelled(true);
				ArrayUtil.voted.add(player.getName());
				return;
			}
			
			if (event.getMessage().equalsIgnoreCase("n")) {
				if (!GameState.isState(GameState.LOBBY) && player.getWorld().getName().equals("lobby")) {
					player.sendMessage(ChatColor.RED + "You cannot vote when you are dead.");
					event.setCancelled(true);
					return;
				}
				
				if (ArrayUtil.spectating.contains(player.getName())) {
					player.sendMessage(ChatColor.RED + "You cannot vote as a spectator.");
					event.setCancelled(true);
					return;
				}
				
				if (ArrayUtil.voted.contains(player.getName())) {
					player.sendMessage(ChatColor.RED + "You have already voted.");
					return;
				}
				player.sendMessage(Main.prefix() + "You voted no.");
				VoteCommand.no++;
				event.setCancelled(true);
				ArrayUtil.voted.add(player.getName());
				return;
			}
		}
    	
		if (PermissionsEx.getUser(player).inGroup("Host")) {
			Team team = player.getScoreboard().getPlayerTeam(player);
			if (settings.getData().getString("game.host").equals(player.getName())) {
				for (Player online : Bukkit.getServer().getOnlinePlayers()) {
					online.sendMessage("§4§lHost §8| §f" + (team != null ? (team.getName().equals("spec") ? player.getName() : team.getPrefix() + player.getName()) : player.getName()) + " §8» §f" + event.getMessage());
				}
			} else {
				for (Player online : Bukkit.getServer().getOnlinePlayers()) {
					online.sendMessage("§4§lCo-Host §8| §f" + (team != null ? (team.getName().equals("spec") ? player.getName() : team.getPrefix() + player.getName()) : player.getName()) + " §8» §f" + event.getMessage());
				}
			}
		}
		else if (PermissionsEx.getUser(player).inGroup("Staff")) {
			if (ArrayUtil.mute.contains("a")) {
				player.sendMessage(Main.prefix() + "All players are muted.");
				event.setCancelled(true);
				return;
			}
			if (ArrayUtil.mute.contains(player.getName())) {
				player.sendMessage(Main.prefix() + "You have been muted.");
				event.setCancelled(true);
				return;
			}
			
			Team team = player.getScoreboard().getPlayerTeam(player);
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				online.sendMessage("§c§lStaff §8| §f" + (team != null ? (team.getName().equals("spec") ? player.getName() : team.getPrefix() + player.getName()) : player.getName()) + " §8» §f" + event.getMessage());
			}
		}
		else if (PermissionsEx.getUser(player).inGroup("VIP")) {
			if (ArrayUtil.mute.contains("a")) {
				player.sendMessage(Main.prefix() + "All players are muted.");
				event.setCancelled(true);
				return;
			}
			if (ArrayUtil.mute.contains(player.getName())) {
				player.sendMessage(Main.prefix() + "You have been muted.");
				event.setCancelled(true);
				return;
			}
			
			Team team = player.getScoreboard().getPlayerTeam(player);

			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				online.sendMessage("§5§lVIP §8| §f" + (team != null ? (team.getName().equals("spec") ? player.getName() : team.getPrefix() + player.getName()) : player.getName()) + " §8» §f" + event.getMessage());
			}
		} 
		else if (PermissionsEx.getUser(player).inGroup("User")) {
			if (ArrayUtil.mute.contains("a")) {
				player.sendMessage(Main.prefix() + "All players are muted.");
				event.setCancelled(true);
				return;
			}
			if (ArrayUtil.mute.contains(player.getName())) {
				player.sendMessage(Main.prefix() + "You have been muted.");
				event.setCancelled(true);
				return;
			}
			Team team = player.getScoreboard().getPlayerTeam(player);

			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				online.sendMessage((team != null ? team.getPrefix() + player.getName() : player.getName()) + " §8» §f" + event.getMessage());
			}
		} 
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event) {
		Player player = event.getPlayer();
		
		if (ArrayUtil.spectating.contains(player.getName())) {
			event.setCancelled(true);
		}
		
		if (player.getWorld().getName().equals("lobby")) {
			event.setCancelled(true);
		}
		
		if (player.getWorld().getName().equals("arena")) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();

		if (event.getResult() == Result.KICK_BANNED) {
			if (player.hasPermission("uhc.staff")) {
				event.allow();
				return;
			}

			event.setKickMessage("§aBanned: §7" + Bukkit.getBanList(Type.NAME).getBanEntry(player.getName()).getReason());
			return;
		}
		
		if (event.getResult() == Result.KICK_WHITELIST) {
			if (player.hasPermission("uhc.prelist")) {
				event.allow();
				return;
			}

			event.setKickMessage("§7You were too early or too late.");
			return;
		}
		
		if (PlayersUtil.getPlayers().size() >= settings.getData().getInt("maxplayers")) {
			if (player.hasPermission("uhc.prelist")) {
				event.allow();
			} else {
				event.setKickMessage("§7The server is full, sorry.");
			}
		} else {
			event.allow();
		}
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();

		if (ArrayUtil.spectating.contains(player.getName())) {
			event.setCancelled(true);
		}
	}        

	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		Player player = event.getPlayer();

		if (ArrayUtil.spectating.contains(player.getName())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		final Player player = event.getPlayer();
		
		if (!Main.absorbtion) {
			if (event.getItem().getType() == Material.GOLDEN_APPLE) {
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					public void run() {
						player.removePotionEffect(PotionEffectType.ABSORPTION);
						player.removePotionEffect(PotionEffectType.ABSORPTION);
						player.removePotionEffect(PotionEffectType.ABSORPTION);
					}
		        }, 1L);
			}
		}
		
		if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equals("§6Golden Head")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
		}
		
		if (event.getItem().getType() == Material.POTION && event.getItem().getDurability() == 8233) {
			player.sendMessage(ChatColor.RED + "Str 2 disabled, giving str 1");
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 0));
			ItemStack newitem = event.getItem();
			newitem.setDurability((short) 0); 
			event.setItem(newitem); 
			player.updateInventory();
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onStrengthSplash(PotionSplashEvent event) {
		if (!(event.getPotion().getShooter() instanceof Player)) return;
		
		Player player = (Player) event.getPotion().getShooter();
		
		if (event.getPotion().getItem().getType() == Material.POTION && event.getPotion().getItem().getDurability() == 16425) {
			player.sendMessage(ChatColor.RED + "Str 2 disabled, giving str 1");
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 0));
			player.updateInventory();
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerExpChange(PlayerExpChangeEvent event) {
		Player player = event.getPlayer();
		
		if (ArrayUtil.spectating.contains(player.getName())) {
			ExperienceOrb exp = (ExperienceOrb) player.getWorld().spawn(player.getLocation().add(0, 1, 0), ExperienceOrb.class);
			exp.setExperience(event.getAmount());
			event.setAmount(0);
		}
	}
}