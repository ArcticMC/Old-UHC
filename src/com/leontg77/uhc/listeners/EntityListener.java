package com.leontg77.uhc.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.leontg77.uhc.GameState;
import com.leontg77.uhc.Main;
import com.leontg77.uhc.scenario.ScenarioManager;
import com.leontg77.uhc.util.ArrayUtil;
import com.leontg77.uhc.util.DamageUtil;

@SuppressWarnings("deprecation")
public class EntityListener implements Listener {
	
	@EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		if (!(event.getEntity() instanceof Player)) 
			return;
		
		if (event.getRegainReason() == RegainReason.SATIATED) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
    	Entity entity = event.getEntity();
    	
    	if (entity instanceof Ghast) {
    		event.getDrops().remove(new ItemStack (Material.GHAST_TEAR));
    		event.getDrops().add(new ItemStack (Material.GOLD_INGOT));
    		return;
        }
    	
    	if (entity instanceof Sheep) {
    		event.getDrops().add(new ItemStack (Material.BREAD, new Random().nextInt(2) + 1));
    		return;
        }
    	
    	ItemStack potion = new ItemStack (Material.POTION, 1, (short) 8261);
    	
    	if (entity instanceof Witch) {
    		if (((Witch) entity).getKiller() != null) {
    			if (((Witch) entity).getKiller().hasPotionEffect(PotionEffectType.POISON)) {
    	    		event.getDrops().add(potion);
    			} else {
    				if ((new Random().nextInt(99) + 1) <= 50) {
    		    		event.getDrops().add(potion);
    				}
    			}
    		}
        }
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {		
		if (GameState.isState(GameState.WAITING)) {
			event.setCancelled(true);
			return;
		}
    	
		if (event.getEntity() instanceof Player) {
	    	if (event.getEntity().getWorld() == Bukkit.getWorld("lobby")) {
	    		event.setCancelled(true);
	    	}
		}
	}
	
	@EventHandler
	public void onShot(final EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Projectile) || !(event.getEntity() instanceof Player)) return;
	
		final Player player = (Player) event.getEntity();
		final Projectile damager = (Projectile) event.getDamager();

		if (ArrayUtil.spectating.contains(player.getName())) {
			return;
		}
		
		if (!(damager.getShooter() instanceof Player)) return;
		
		Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
			public void run() {
				Player killer = (Player) damager.getShooter();
				Damageable damage = player;
				double health = Double.parseDouble(DamageUtil.convertHealth(damage.getHealth()));
				double hearts = health / 2;
				double precent = hearts * 10;
				
				if (precent > 0) {
					killer.sendMessage(Main.prefix() + "§6" + player.getName() + " §7is now at §6" + ((int) precent) + "%");
				}
			}
		}, 1);
	}
	
	@EventHandler
	public void onLongShot(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Projectile) || !(event.getEntity() instanceof Player)) return;
		
		if (ScenarioManager.getManager().getScenario("RewardingLongshots") != null && ScenarioManager.getManager().getScenario("RewardingLongshots").isEnabled()) {
			return;
		}
	
		Player player = (Player) event.getEntity();
		Projectile damager = (Projectile) event.getDamager();

		if (ArrayUtil.spectating.contains(player.getName())) {
			return;
		}
		
		if (!(damager.getShooter() instanceof Player)) return;
		
		Player killer = (Player) damager.getShooter();
		
		double distance = killer.getLocation().distance(player.getLocation());
		
		if (distance < 50) return;
		
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			online.sendMessage(Main.prefix() + killer.getName() + " got a longshot of §6" + DamageUtil.convertHealth(distance) + " §7blocks.");
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
	
		Player killer = (Player) event.getDamager();

		if (ArrayUtil.spectating.contains(killer.getName())) {
			event.setCancelled(true);
		}
	}
		
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		if (!(event.getTarget() instanceof Player))
			return;
		
		Player target = (Player) event.getTarget();
		
		if (ArrayUtil.spectating.contains(target.getName())) {
			event.setCancelled(true);
		}
	}
	
	/* No longer in use
	@EventHandler
    public void onPlayerHitByPlayer(EntityDamageByEntityEvent event) {	
		Entity player = event.getEntity();
        Entity killer = event.getDamager();
       
        if (killer instanceof Arrow) {
            Arrow arrow = (Arrow) killer;
            if (player instanceof Player && arrow.getShooter() instanceof Player) {
                Player damaged = (Player) player;
                if (ArrayUtil.spectating.contains(damaged.getName())) {
                	damaged.sendMessage(ChatColor.RED + "Don't try to specblock arrows.");
					Vector velocity = arrow.getVelocity();
                    damaged.teleport(damaged.getLocation().add(0, 7, 0));
                    Arrow nextArrow = ((LivingEntity) arrow.getShooter()).launchProjectile(Arrow.class);
                    nextArrow.setVelocity(velocity);
                    nextArrow.setBounce(false);
                    nextArrow.setShooter(arrow.getShooter());
                    event.setCancelled(true);
                    arrow.remove();
                }
            }
        }
    }*/

	@EventHandler
	public void onVehicleDamage(VehicleDamageEvent event) {
		if (!(event.getAttacker() instanceof Player))
			return;
		
		Player player = (Player) event.getAttacker();

		if (ArrayUtil.spectating.contains(player.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getLocation().getWorld().getName().equals("lobby") || event.getLocation().getWorld().getName().equals("arena")) {
			event.setCancelled(true);
		}
	}
}