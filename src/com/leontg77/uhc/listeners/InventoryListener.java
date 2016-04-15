package com.leontg77.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.leontg77.uhc.util.ArrayUtil;

public class InventoryListener implements Listener {
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) { 
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		
    	Player player = (Player) event.getWhoClicked();

		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
    		return;
		
		if (event.getInventory().getTitle().equals("Player Selector") && ArrayUtil.spectating.contains(player.getName())) {
			if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
	    		return;
			
			Player target = Bukkit.getServer().getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().substring(2, event.getCurrentItem().getItemMeta().getDisplayName().length()));
			if (target != null) {
				player.teleport(target);
			}
			event.setCancelled(true);
		}
		
		if (ArrayUtil.spectating.contains(player.getName())) { 
			if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
	    		return;
			
	    	if (player.getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
	    	}
		}
    }
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (ArrayUtil.invsee.containsKey(event.getInventory())) {
			ArrayUtil.invsee.get(event.getInventory()).cancel();
			ArrayUtil.invsee.remove(event.getInventory());
		}
	}
}