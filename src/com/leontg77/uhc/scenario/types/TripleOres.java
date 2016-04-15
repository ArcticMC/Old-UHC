package com.leontg77.uhc.scenario.types;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.leontg77.uhc.scenario.Scenario;
import com.leontg77.uhc.scenario.ScenarioManager;

public class TripleOres extends Scenario implements Listener {
	private static boolean enabled = false;
	
	public TripleOres() {
		super("TripleOres", "Every ore you mine drops 3 times more of the ores drops.");
	}

	public void setEnabled(boolean enable) {
		enabled = enable;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (ScenarioManager.getManager().getScenario("CutClean") != null && ScenarioManager.getManager().getScenario("CutClean").isEnabled()) {
			if (block.getType() == Material.COAL_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.COAL, 2));
			}
			
			if (block.getType() == Material.IRON_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.IRON_INGOT, 2));
			}
			
			if (block.getType() == Material.GOLD_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.GOLD_INGOT, 2));
			}
			
			if (block.getType() == Material.DIAMOND_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.DIAMOND, 2));
			}
			
			if (block.getType() == Material.EMERALD_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.EMERALD, 2));
			}
		} else {
			if (block.getType() == Material.COAL_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.COAL, 2));
			}
			
			if (block.getType() == Material.IRON_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.IRON_ORE, 2));
			}
			
			if (block.getType() == Material.GOLD_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.GOLD_ORE, 2));
			}
			
			if (block.getType() == Material.DIAMOND_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.DIAMOND, 2));
			}
			
			if (block.getType() == Material.EMERALD_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.EMERALD, 2));
			}
		}
	}
}