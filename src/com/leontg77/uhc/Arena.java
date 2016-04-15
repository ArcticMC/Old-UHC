package com.leontg77.uhc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * PvP Arena class.
 * @author LeonTG77
 */
public class Arena {
	private Settings settings = Settings.getInstance();
	private static Arena instance;
	private boolean enabled = false;	
	private ArrayList<Player> players;
	private ArrayList<BlockState> blocks;
	private ArrayList<LivingEntity> dead;
	
	public static Arena getManager() {
		return (instance == null ? new Arena() : instance);
	}
	
	/**
	 * Setup the arena class.
	 */
	public void setup() {
		players = new ArrayList<Player>();
		blocks = new ArrayList<BlockState>();
		dead = new ArrayList<LivingEntity>();
		enabled = false;
		instance = this;
	}
	
	/**
	 * Check if the arena contains a player.
	 * @param player the player.
	 * @return True if the player is in the arena, false otherwise.
	 */
	public boolean hasPlayer(Player player) {
		return players.contains(player);
	}
	
	/**
	 * Check if the arena is enabled.
	 * @return True if the arena is enabled, false otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Get all the entities that were killed in the arena.
	 * @return List of dead entities.
	 */
	public List<LivingEntity> getDeadEntities() {
		return dead;
	}
	
	/**
	 * Get all the broken blocks at the arena.
	 * @return Map of the block location and type.
	 */
	public ArrayList<BlockState> getBrokenBlocks() {
		return blocks;
	}
	
	/**
	 * Enable or disable the arena
	 * @param enabled enable?
	 */
	public void setEnabled(boolean enabled) {
		if (!enabled) {
			for (Player p : Bukkit.getWorld("arena").getPlayers()) {
				this.removePlayer(p);
			}
		}
		
		this.enabled = enabled;
	}
	
	/**
	 * Get a list of players in the arena.
	 * @return list of players in the arena.
	 */
	public List<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Adds a player to the arena.
	 * @param player the player.
	 */
	public void addPlayer(Player player) {
		players.add(player);
		double areaRadius = 200;
		double minRadius = 5; 
		double t = Math.random() * Math.PI;
		double radius = Math.random()*(areaRadius - minRadius) + minRadius;
		double x = Math.cos(t) * radius;
		double z = Math.sin(t) * radius;
		Location loc = new Location (Bukkit.getWorld("arena"), x, 200, z);
		loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 2);
		player.teleport(loc);
		giveKit(player);
	}
	
	/**
	 * Removes a player from the arena.
	 * @param player the player.
	 */
	public void removePlayer(Player player) {
		players.remove(player);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setItemOnCursor(new ItemStack (Material.AIR));
		
		World w = Bukkit.getServer().getWorld(settings.getData().getString("spawn.world"));
		double x = settings.getData().getDouble("spawn.x");
		double y = settings.getData().getDouble("spawn.y");
		double z = settings.getData().getDouble("spawn.z");
		float yaw = (float) settings.getData().getDouble("spawn.yaw");
		float pitch = (float) settings.getData().getDouble("spawn.pitch");
		
		Location loc = new Location(w, x, y, z, yaw, pitch);
		player.teleport(loc);
	}
	
	/**
	 * Gives the arena kit to a player.
	 * @param player the player.
	 */
	private void giveKit(Player player) {
		ItemStack Sword = new ItemStack(Material.IRON_SWORD);
		ItemStack Bow = new ItemStack(Material.BOW);
		ItemStack Cobble = new ItemStack(Material.COBBLESTONE, 64);
		ItemStack Bucket = new ItemStack(Material.WATER_BUCKET);
		ItemStack Pick = new ItemStack(Material.IRON_PICKAXE);
		Pick.addEnchantment(Enchantment.DIG_SPEED, 2);
		ItemStack Axe = new ItemStack(Material.IRON_AXE);
		Axe.addEnchantment(Enchantment.DIG_SPEED, 2);
		ItemStack Shovel = new ItemStack(Material.IRON_SPADE);
		Shovel.addEnchantment(Enchantment.DIG_SPEED, 2);
		ItemStack Steel = new ItemStack(Material.GOLDEN_APPLE);
		ItemStack Food = new ItemStack(Material.COOKED_BEEF, 32);
		
		ItemStack Helmet = new ItemStack(Material.IRON_HELMET);
		
		ItemStack Chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		
		ItemStack Leggings = new ItemStack(Material.IRON_LEGGINGS);
		
		ItemStack Boots = new ItemStack(Material.IRON_BOOTS);
		
		player.getInventory().setItem(0, Sword);
		player.getInventory().setItem(1, Bow);
		player.getInventory().setItem(2, Cobble);
		player.getInventory().setItem(3, Bucket);
		player.getInventory().setItem(4, Pick);
		player.getInventory().setItem(5, Axe);
		player.getInventory().setItem(6, Shovel);
		player.getInventory().setItem(7, Steel);
		player.getInventory().setItem(8, Food);
		player.getInventory().setItem(27, new ItemStack (Material.ARROW, 64));
		player.getInventory().addItem(new ItemStack (Material.WORKBENCH, 16));
		player.getInventory().addItem(new ItemStack (Material.ENCHANTMENT_TABLE, 4));
		player.getInventory().setHelmet(Helmet);
		player.getInventory().setChestplate(Chestplate);
		player.getInventory().setLeggings(Leggings);
		player.getInventory().setBoots(Boots);
	}

	/**
	 * Adds a broken block to the map.
	 * @param state the block's state.
	 */
	public void addBlockState(BlockState state) {
		if (!blocks.contains(state.getLocation())) {
			blocks.add(state);
		}
	}

	/**
	 * Adds an dead entity to the list.
	 * @param entity the entity
	 */
	public void addDeadEntity(LivingEntity entity) {
		dead.add(entity);
	}
}