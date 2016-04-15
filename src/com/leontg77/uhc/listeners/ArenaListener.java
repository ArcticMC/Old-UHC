package com.leontg77.uhc.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.leontg77.uhc.Arena;
import com.leontg77.uhc.Main;

@SuppressWarnings("deprecation")
public class ArenaListener implements Listener {

	@EventHandler
	public void onBlockFade(BlockFadeEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}

		Block block = event.getBlock();

		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block.getState());
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}

		Block block = event.getBlock();

		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block.getState());
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}

		BlockState block = event.getBlockReplacedState();

		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block);
		}
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Location loc = event.getBlock().getLocation();

		for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
			for (int y = loc.getBlockY() - 1; y <= loc.getBlockY() + 1; y++) {
				for (int z = loc.getBlockZ() - 1; z <= loc.getBlockZ() + 1; z++) {
					if (loc.getWorld().getBlockAt(x, y, z).getType() == Material.FIRE) {
						loc.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
						loc.getWorld().getBlockAt(x, y, z).getState().update();
					}
				}
			}
		}

		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getWorld().getName().equals("arena")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}

		Block block = event.getBlock();
		Block block2 = event.getToBlock();

		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block.getState());
			Arena.getManager().addBlockState(block2.getState());
		}
	}

	@EventHandler
	public void onBlockGrow(BlockGrowEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block.getState());
		}
	}

	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block.getState());
		}
	}

	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getWorld().getName().equals("arena")) {
			event.setNewCurrent(0);
		}
	}

	@EventHandler
	public void onBlockMultiPlace(BlockMultiPlaceEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		for (BlockState b : event.getReplacedBlockStates()) {
			if (b.getWorld().getName().equals("arena")) {
				Arena.getManager().addBlockState(b);
			}
		}
		
	}

	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block.getState());
		}
	}

	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block.getState());
		}
	}

	@EventHandler
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getWorld().getName().equals("arena")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event){
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getWorld().getName().equals("arena")) {
			event.setCancelled(true);
		}
    }

	@EventHandler
	public void onBlockForm(EntityBlockFormEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}

		Block block = event.getBlock();

		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block.getState());
		}
	}

	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}
		
		Block block = event.getBlock();
		
		if (block.getWorld().getName().equals("arena")) {
			Arena.getManager().addBlockState(block.getState());
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}

		Entity entity = event.getEntity();

		if (entity.getWorld().getName().equals("arena")) {
			if (entity instanceof Player) {
				return;
			}

			if (entity instanceof LivingEntity) {
				Arena.getManager().addDeadEntity((LivingEntity) entity);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}

		final Player player = event.getEntity();

		StringBuilder s = new StringBuilder("");
    	
    	for (String d : event.getDeathMessage().split(" ")) {
    		s.append(ChatColor.GRAY + d).append(" ");
    	}
    	
    	String result = s.toString().trim();
    	
    	for (Player online : Bukkit.getServer().getOnlinePlayers()) {
    		if (result.contains(online.getName())) {
    			result = result.replaceAll(online.getName(), "§6" + online.getName());
    		}
    	}
    	
    	for (Player p : Arena.getManager().getPlayers()) {
			p.sendMessage(Main.prefix() + result);
		}
		event.setDeathMessage(null);
		event.getDrops().clear();
		event.getDrops().add(new ItemStack(Material.DIAMOND, 1));
		event.getDrops().add(new ItemStack(Material.ARROW, 32));
		ItemStack skull = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta skullMeta = skull.getItemMeta();
		skullMeta.setDisplayName("§6Golden Head");
		skullMeta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Some say consuming the head of a", ChatColor.DARK_PURPLE + "fallen foe strengthens the blood.", ChatColor.AQUA + "Made from the head of: " + player.getName()));
		skull.setItemMeta(skullMeta);
		event.getDrops().add(skull);
		Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, new Runnable() {
			public void run() {
				Arena.getManager().removePlayer(player);
			}
		}, 20);
	}

	@EventHandler
	public void onPlayerKill(PlayerDeathEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}

		Player player = event.getEntity();

		if (Arena.getManager().isEnabled()) {
			Arena.getManager().removePlayer(player);

			if (player.getKiller() == null) {
				player.sendMessage(Main.prefix() + "You were killed by pve.");
				return;
			}
			
			player.getKiller().setLevel(player.getKiller().getLevel() + 1);
			player.sendMessage(Main.prefix() + "You were killed by " + player.getKiller().getName() + ".");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (!Arena.getManager().isEnabled()) {
			return;
		}

		Player player = event.getPlayer();

		if (Arena.getManager().isEnabled() && Arena.getManager().hasPlayer(player)) {
			Arena.getManager().removePlayer(player);
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			for (Player p : Arena.getManager().getPlayers()) {
				p.sendMessage(Main.prefix() + player.getName() + " has left the arena.");
			}
		}
	}
}