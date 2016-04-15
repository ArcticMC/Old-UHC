package com.leontg77.uhc.scenario.types;

import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.leontg77.uhc.scenario.Scenario;

public class VengefulSpirits extends Scenario implements Listener {
	private static boolean enabled = false;
	
	public VengefulSpirits() {
		super("VengefulSpirits", "When a player dies, above y 60 a ghast spawns and bellow y 60 a blaze spawns, you can only get their head by killing that mob.");
	}

	public void setEnabled(boolean enable) {
		enabled = enable;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	@EventHandler
	public void onPlayerDeath(EntityDeathEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		if (event.getEntity() instanceof Blaze || event.getEntity() instanceof Ghast) {
			if (event.getEntity().getCustomName() == null) {
				return;
			}
			
			ItemStack skull = new ItemStack (Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
			skullMeta.setOwner(event.getEntity().getCustomName());
			skull.setItemMeta(skullMeta);
			event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), skull);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		if (event.getEntity().getLocation().getBlockY() <= 60) {
			Blaze blaze = event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Blaze.class);
			blaze.setCustomName(event.getEntity().getName());
		} else {
			Ghast ghast = event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Ghast.class);
			ghast.setCustomName(event.getEntity().getName());
		}
	}
}