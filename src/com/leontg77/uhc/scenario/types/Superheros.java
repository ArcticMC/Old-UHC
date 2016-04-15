package com.leontg77.uhc.scenario.types;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.scenario.Scenario;
import com.leontg77.uhc.util.PlayersUtil;

public class Superheros extends Scenario implements Listener {
	private ArrayList<HeroType> types = new ArrayList<HeroType>();
	private static boolean enabled = false;
	
	private ArrayList<String> speed = new ArrayList<String>();
	private ArrayList<String> strength = new ArrayList<String>();
	private ArrayList<String> health = new ArrayList<String>();
	private ArrayList<String> jump = new ArrayList<String>();
	private ArrayList<String> invis = new ArrayList<String>();
	private ArrayList<String> resistance = new ArrayList<String>();
	
	public Superheros() {
		super("Superheros", "Each player on the team receives a special \"super\" power such as jump boost, health boost, strength, speed, invis, or resistance.");
		types.add(HeroType.SPEED);
		types.add(HeroType.STRENGTH);
		types.add(HeroType.HEALTH);
		types.add(HeroType.JUMP);
		types.add(HeroType.INVIS);
		types.add(HeroType.RESISTANCE);
	}

	public void setEnabled(boolean enable) {
		enabled = enable;
		
		if (enable) {
			if (Main.ffa) {
				
			} else {
				
			}
		} else {
			for (Player online : PlayersUtil.getPlayers()) {
				online.setMaxHealth(20.0);
				for (PotionEffect effect : online.getActivePotionEffects()) {
					online.removePotionEffect(effect.getType());
				}
			}
			speed.clear();
			strength.clear();
			health.clear();
			jump.clear();
			invis.clear();
			resistance.clear();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		if (event.getItem().getType() == Material.MILK_BUCKET) {
			event.getPlayer().sendMessage(Main.prefix() + "You cannot drink milk in superheros.");
			event.setCancelled(true);
			event.setItem(new ItemStack (Material.AIR));
		}
	}
	
	public enum HeroType {
		SPEED, STRENGTH, HEALTH, JUMP, INVIS, RESISTANCE;
	}
}