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

public class DoubleOres extends Scenario implements Listener {
	private static boolean enabled = false;
	
	public DoubleOres() {
		super("DoubleOres", "Every ore you mine drops 2 times more of the ores drops.");
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
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.COAL, 1));
			}
			
			if (block.getType() == Material.IRON_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.IRON_INGOT, 1));
			}
			
			if (block.getType() == Material.GOLD_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.GOLD_INGOT, 1));
			}
			
			if (block.getType() == Material.DIAMOND_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.DIAMOND, 1));
			}
			
			if (block.getType() == Material.EMERALD_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.EMERALD, 1));
			}
		} else {
			if (block.getType() == Material.COAL_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.COAL, 1));
			}
			
			if (block.getType() == Material.IRON_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.IRON_ORE, 1));
			}
			
			if (block.getType() == Material.GOLD_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.GOLD_ORE, 1));
			}
			
			if (block.getType() == Material.DIAMOND_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.DIAMOND, 1));
			}
			
			if (block.getType() == Material.EMERALD_ORE) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.EMERALD, 1));
			}
		}
	}
}