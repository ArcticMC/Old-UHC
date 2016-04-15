package com.leontg77.uhc.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;

import com.leontg77.uhc.GameState;
import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.ArrayUtil;
import com.leontg77.uhc.util.BlockUtil;

public class BlockListener implements Listener {
	
	@EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
    	
    	if (GameState.isState(GameState.WAITING)) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	if (GameState.isState(GameState.LOBBY) && !player.hasPermission("uhc.build") && !event.getBlock().getWorld().getName().equals("arena")) {
    		event.setCancelled(true);
    		return;
    	} 
    	
    	if (event.getBlock().getWorld().getName().equals("lobby") && !player.hasPermission("uhc.build")) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	if (ArrayUtil.spectating.contains(player.getName())) {
    		event.setCancelled(true);
    	}
    }
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onLeavesDecayEvent(LeavesDecayEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		if (event.getBlock().getType() == Material.LEAVES || event.getBlock().getType() == Material.LEAVES_2) {
			MaterialData data = event.getBlock().getState().getData();
			try {
				Tree t = (Tree) data;
				if (t.getSpecies() == TreeSpecies.GENERIC || t.getSpecies() == TreeSpecies.DARK_OAK) {
					Random r = new Random();
					
					if ((r.nextInt(99) + 1) <= Main.applerate) {
						event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
					}
					if ((r.nextInt(99) + 1) <= 5) {
						event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.SAPLING, 1, BlockUtil.saplingDur(t.getSpecies())));
					}
				}
			} catch (ClassCastException localClassCastException) {
				Bukkit.getLogger().warning(ChatColor.RED + "Could not drop apple/sapling @ " + event.getBlock().getLocation().toString());
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
    	Player player = event.getPlayer();
		Block block = event.getBlock();
    	
    	if (GameState.isState(GameState.WAITING)) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	if (GameState.isState(GameState.LOBBY) && !player.hasPermission("uhc.build") && !event.getBlock().getWorld().getName().equals("arena")) {
    		event.setCancelled(true);
    		return;
    	} 
    	
    	if (event.getBlock().getWorld().getName().equals("lobby") && !player.hasPermission("uhc.build")) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	if (ArrayUtil.spectating.contains(player.getName())) {
    		event.setCancelled(true);
    		return;
    	}

		if (event.getBlock().getWorld().getName().equals("arena")) {
			return;
		}
		
		if (block.getType() == Material.GRAVEL) {
			if ((new Random().nextInt(99) + 1) <= Main.flintrate) {
				event.setCancelled(true);
				BlockUtil.blockCrack(player, block.getLocation(), 13);
				block.setType(Material.AIR);
				block.getState().update();
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.FLINT));
			} else {
				event.setCancelled(true);
				BlockUtil.blockCrack(player, block.getLocation(), 13);
				block.setType(Material.AIR);
				block.getState().update();
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack (Material.GRAVEL));
			}
		}
		
		if (event.getBlock().getType() == Material.LEAVES || event.getBlock().getType() == Material.LEAVES_2) {
			MaterialData data = event.getBlock().getState().getData();
			try {
				Tree t = (Tree) data;
				if (t.getSpecies() == TreeSpecies.GENERIC || t.getSpecies() == TreeSpecies.DARK_OAK) {
					Random r = new Random();
					
					if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.SHEARS) {
						if ((r.nextInt(99) + 1) <= Main.shearrate) {
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
						}
						if ((r.nextInt(99) + 1) <= 5) {
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.SAPLING, 1, BlockUtil.saplingDur(t.getSpecies())));
						}
					} else {
						if ((r.nextInt(99) + 1) <= Main.applerate) {
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
						}
						if ((r.nextInt(99) + 1) <= 5) {
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.SAPLING, 1, BlockUtil.saplingDur(t.getSpecies())));
						}
					}
				}
			} catch (ClassCastException localClassCastException) {
				Bukkit.getLogger().warning(ChatColor.RED + "Could not drop apple/sapling @ " + event.getBlock().getLocation().toString());
			}
		}
    }
	
	
	@EventHandler
    public void onBlockCanBuild(BlockCanBuildEvent event) {
		Block block = event.getBlock();
		
		if (!event.isBuildable()) {
			if (BlockUtil.isSpecBlock(block)) {
				event.setBuildable(true);
			}
		}
    }
}