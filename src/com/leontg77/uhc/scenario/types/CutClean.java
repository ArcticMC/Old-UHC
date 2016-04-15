package com.leontg77.uhc.scenario.types;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.leontg77.uhc.scenario.Scenario;
import com.leontg77.uhc.util.BlockUtil;

public class CutClean extends Scenario implements Listener {
	private static boolean enabled = false;
	
	public CutClean() {
		super("CutClean", "Everything you mine/kill drops their loot smelted.");
	}
	
	public void setEnabled(boolean enable) {
		enabled = enable;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		for (ItemStack drops : event.getDrops()) {
			if (drops.getType() == Material.PORK) {
				drops.setType(Material.GRILLED_PORK);
			}
			if (drops.getType() == Material.RAW_BEEF) {
				drops.setType(Material.COOKED_BEEF);
			}
			if (drops.getType() == Material.RAW_CHICKEN) {
				drops.setType(Material.COOKED_CHICKEN);
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getType() == Material.IRON_ORE) {
			event.setCancelled(true);
			BlockUtil.blockCrack(event.getPlayer(), block.getLocation(), 15);
			block.setType(Material.AIR);
			block.getState().update();
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.IRON_INGOT));
			ExperienceOrb exp = (ExperienceOrb) event.getBlock().getWorld().spawn(event.getBlock().getLocation().add(0, 1, 0), ExperienceOrb.class);
			exp.setExperience(3);
		}
		
		if (block.getType() == Material.GOLD_ORE) {
			event.setCancelled(true);
			BlockUtil.blockCrack(event.getPlayer(), block.getLocation(), 14);
			block.setType(Material.AIR);
			block.getState().update();
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.GOLD_INGOT));
			ExperienceOrb exp = (ExperienceOrb) event.getBlock().getWorld().spawn(event.getBlock().getLocation().add(0, 1, 0), ExperienceOrb.class);
			exp.setExperience(5);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		Player player = event.getPlayer();
		Block block = event.getBlockPlaced();
		
		if (block.getType() == Material.TNT) {
			block.setType(Material.AIR);
			Location loc = new Location(block.getWorld(), block.getLocation().getBlockX() + 0.5, block.getLocation().getBlockY() + 0.2, block.getLocation().getBlockZ() + 0.5);
        	TNTPrimed tnt = player.getWorld().spawn(loc, TNTPrimed.class);
        	tnt.setFuseTicks(80);
		}
	}
}