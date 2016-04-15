package com.leontg77.uhc.scenario.types;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.leontg77.uhc.scenario.Scenario;
import com.leontg77.uhc.util.BlockUtil;

public class SkyClean extends Scenario implements Listener {
	private static boolean enabled = false;
	
	public SkyClean() {
		super("SkyClean", "Sand drops glass and snow blocks drop snowblocks rather than snowballs.");
	}
	
	public void setEnabled(boolean enable) {
		enabled = enable;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getType() == Material.SAND) {
			event.setCancelled(true);
			BlockUtil.blockCrack(event.getPlayer(), block.getLocation(), 20);
			block.setType(Material.AIR);
			block.getState().update();
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.GLASS));
		}
		
		if (block.getType() == Material.SNOW_BLOCK) {
			event.setCancelled(true);
			BlockUtil.blockCrack(event.getPlayer(), block.getLocation(), 80);
			block.setType(Material.AIR);
			block.getState().update();
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.SNOW_BLOCK));
		}
	}
}