package com.leontg77.uhc.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R4.MinecraftServer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

@SuppressWarnings("deprecation")
public class PlayersUtil {

	/**
	 * Get the inv size of # players online.
	 * @return the inv size.
	 */
	public static int playerInvSize() {
		int length = Bukkit.getOnlinePlayers().length;
		length = (length - ArrayUtil.spectating.size());
		
		if (length <= 9) {
			return 9;
		} else if (length > 9 && length <= 18) {
			return 18;
		} else if (length > 18 && length <= 27) {
			return 27;
		} else if (length > 27 && length <= 36) {
			return 36;
		} else if (length > 36 && length <= 45) {
			return 45;
		} else if (length > 45 && length <= 54) {
			return 54;
		}
		return 54;
	}
	
	/**
	 * Gets the servers tps.
	 * @return The servers tps.
	 */
	public static double getTps() {
		return MinecraftServer.getServer().recentTps[0];
	}
	
	/**
	 * Gets the servers recent tps.
	 * @return The servers recent tps.
	 */
	public static double[] getRecentTps() {
		return MinecraftServer.getServer().recentTps;
	}
	
	/**
	 * Get a players ping.
	 * @param player the player
	 * @return the players ping
	 */
	public static int getPing(Player player) {
		return (((CraftPlayer) player).getHandle().ping > 20 ? ((CraftPlayer) player).getHandle().ping - 20 : ((CraftPlayer) player).getHandle().ping);
	}
	
	/**
	 * Broadcasts a message to everyone online with a specific permission.
	 * @param message the message.
	 * @param permission the permission.
	 */
	public static void broadcast(String message, String permission) {
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			if (online.hasPermission(permission)) {
				online.sendMessage(message);
			}
		}
		Bukkit.getLogger().info(message);
	}
	
	/**
	 * Broadcasts a message to everyone online.
	 * @param message the message.
	 */
	public static void broadcast(String message) {
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			online.sendMessage(message);
		}
		Bukkit.getLogger().info(message);
	}
	
	public static List<Player> getPlayers() {
		ArrayList<Player> list = new ArrayList<Player>();
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			list.add(online);
		}
		return list;
	}

	public static LivingEntity getShooter(Projectile e) {
		return e.getShooter();
	}
}