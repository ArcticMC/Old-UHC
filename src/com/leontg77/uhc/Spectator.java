package com.leontg77.uhc;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.leontg77.uhc.util.ArrayUtil;
import com.leontg77.uhc.util.PlayersUtil;

/**
 * The spectator class to manage spectating.
 * @author LeonTG77
 */
public class Spectator {
	private Spectator() {}
	private static Spectator manager = new Spectator();
	public static Spectator getManager() {
		return manager;
	}
	
	/**
	 * Hides all spectators from non spectators.
	 * @param player The players not spectating.
	 */
	public void hideAll(Player player) {
		for (Player online : PlayersUtil.getPlayers()) {
			if (ArrayUtil.spectating.contains(online.getName()) && !ArrayUtil.spectating.contains(player.getName())) {
				player.hidePlayer(online);
			}
		}
	}
	
	/**
	 * Manage the players spectator mode.
	 * @param player the player you're setting for.
	 * @param type true or false if what you want for spectating.
	 */
	public void set(Player player, boolean type) {
		if (type == false) {
			player.sendMessage(Main.prefix() + "You are no longer spectating.");
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(false);
			player.setFlying(false);
			player.setFlySpeed((float) 0.1);
			
			if (Teams.getManager().getTeam(player) != null) {
				Teams.getManager().leaveTeam(player);
			}
			
			if (ArrayUtil.spectating.contains(player.getName())) {
				ArrayUtil.spectating.remove(player.getName());
			}
			
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.getInventory().remove(Material.COMPASS);
			player.getInventory().remove(Material.INK_SACK);
			player.getInventory().remove(Material.FEATHER);
			
			for (Player online : PlayersUtil.getPlayers()) {
				if (!ArrayUtil.spectating.contains(online.getName())) {
					player.showPlayer(online);
				} else {
					player.hidePlayer(online);
				}
				online.showPlayer(player);
				hideAll(online);
			}
		}
		
		if (type == true) {
			player.sendMessage(Main.prefix() + "You are now spectating, Don't spoil ANYTHING.");
			player.getInventory().remove(Material.COMPASS);
			player.getInventory().remove(Material.INK_SACK);
			player.getInventory().remove(Material.FEATHER);
			ItemStack[] contents = player.getInventory().getContents();
			for (ItemStack content : contents) {
				if (content != null) {
					player.getWorld().dropItemNaturally(player.getLocation(), content);
					player.getInventory().remove(content);
				}
			}

			ItemStack[] armorContents = player.getInventory().getArmorContents();
			for (ItemStack armorContent : armorContents) {
				if (armorContent.getAmount() != 0) {
					player.getWorld().dropItemNaturally(player.getLocation(), armorContent);
					player.getInventory().setArmorContents(null);
				}
			}
			
			player.setAllowFlight(true);
			player.setFlying(true);
			player.setFlySpeed((float) 0.1);
			if (!ArrayUtil.spectating.contains(player.getName())) {
				ArrayUtil.spectating.add(player.getName());
			}
			Teams.getManager().joinTeam("spec", player);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000000, 0));
			ItemStack compass = new ItemStack (Material.COMPASS);
			ItemMeta compassMeta = compass.getItemMeta();
			compassMeta.setDisplayName(ChatColor.GREEN + "Teleporter");
			compassMeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to teleport to a random player.", ChatColor.GRAY + "Right click to open a player teleporter."));
			compass.setItemMeta(compassMeta);
			ItemStack night = new ItemStack (Material.INK_SACK, 1, (short) 12);
			ItemMeta nightMeta = night.getItemMeta();
			nightMeta.setDisplayName(ChatColor.GREEN + "Toggle Night Vision");
			nightMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right click to toggle the night vision effect."));
			night.setItemMeta(nightMeta);
			ItemStack tp = new ItemStack (Material.FEATHER);
			ItemMeta tpMeta = tp.getItemMeta();
			tpMeta.setDisplayName(ChatColor.GREEN + "Teleport to 0,0");
			tpMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right click to telepor to 0,0."));
			tp.setItemMeta(tpMeta);
			player.getInventory().setItem(1, tp);
			player.getInventory().setItem(4, compass);
			player.getInventory().setItem(7, night);
			
			for (Player online : PlayersUtil.getPlayers()) {
				if (!ArrayUtil.spectating.contains(online.getName())) {
					online.hidePlayer(player);
				}
				player.showPlayer(online);
			}
		}
	}
	
	/**
	 * Toggles the players spectator mode.
	 * @param player the player toggling.
	 */
	public void toggle(Player player) {
		if (ArrayUtil.spectating.contains(player.getName())) {
			this.set(player, false);
		} else {
			this.set(player, true);
		}
	}
}