package com.leontg77.uhc.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The arraylist class soring.
 * @author LeonTG77
 */
public class ArrayUtil {
	
	public static ArrayList<String> spectating = new ArrayList<String>();
	public static ArrayList<String> mute = new ArrayList<String>();
	public static ArrayList<String> voted = new ArrayList<String>();
	
	public static HashMap<String, Integer> totalD = new HashMap<String, Integer>();
	public static HashMap<String, Integer> totalG = new HashMap<String, Integer>();
	public static HashMap<Player, Player> msg = new HashMap<Player, Player>();
	public static HashMap<String, BukkitRunnable> relog = new HashMap<String, BukkitRunnable>();
	public static HashMap<Inventory, BukkitRunnable> invsee = new HashMap<Inventory, BukkitRunnable>();
}