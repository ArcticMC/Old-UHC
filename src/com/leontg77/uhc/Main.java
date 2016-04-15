package com.leontg77.uhc;

import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.leontg77.uhc.cmds.ArenaCommand;
import com.leontg77.uhc.cmds.BanCommand;
import com.leontg77.uhc.cmds.BroadcastCommand;
import com.leontg77.uhc.cmds.ButcherCommand;
import com.leontg77.uhc.cmds.ClearInvCommand;
import com.leontg77.uhc.cmds.ClearXpCommand;
import com.leontg77.uhc.cmds.ConfigCommand;
import com.leontg77.uhc.cmds.EditCommand;
import com.leontg77.uhc.cmds.EndCommand;
import com.leontg77.uhc.cmds.FeedCommand;
import com.leontg77.uhc.cmds.GamemodeCommand;
import com.leontg77.uhc.cmds.GiveallCommand;
import com.leontg77.uhc.cmds.HealCommand;
import com.leontg77.uhc.cmds.HealthCommand;
import com.leontg77.uhc.cmds.HelpopCommand;
import com.leontg77.uhc.cmds.InvseeCommand;
import com.leontg77.uhc.cmds.KickCommand;
import com.leontg77.uhc.cmds.ListCommand;
import com.leontg77.uhc.cmds.MsCommand;
import com.leontg77.uhc.cmds.MsgCommand;
import com.leontg77.uhc.cmds.MuteCommand;
import com.leontg77.uhc.cmds.NearCommand;
import com.leontg77.uhc.cmds.PmCommand;
import com.leontg77.uhc.cmds.PvPCommand;
import com.leontg77.uhc.cmds.RandomCommand;
import com.leontg77.uhc.cmds.ReplyCommand;
import com.leontg77.uhc.cmds.RulesCommand;
import com.leontg77.uhc.cmds.ScenarioCommand;
import com.leontg77.uhc.cmds.SethealthCommand;
import com.leontg77.uhc.cmds.SetmaxhealthCommand;
import com.leontg77.uhc.cmds.SetspawnCommand;
import com.leontg77.uhc.cmds.SkullCommand;
import com.leontg77.uhc.cmds.SpectateCommand;
import com.leontg77.uhc.cmds.SpeedCommand;
import com.leontg77.uhc.cmds.SpreadCommand;
import com.leontg77.uhc.cmds.StaffChatCommand;
import com.leontg77.uhc.cmds.StalkCommand;
import com.leontg77.uhc.cmds.StartCommand;
import com.leontg77.uhc.cmds.StartTimerCommand;
import com.leontg77.uhc.cmds.TeamCommand;
import com.leontg77.uhc.cmds.TimerCommand;
import com.leontg77.uhc.cmds.TlCommand;
import com.leontg77.uhc.cmds.TpCommand;
import com.leontg77.uhc.cmds.TpsCommand;
import com.leontg77.uhc.cmds.UnbanCommand;
import com.leontg77.uhc.cmds.VoteCommand;
import com.leontg77.uhc.cmds.WhitelistCommand;
import com.leontg77.uhc.listeners.ArenaListener;
import com.leontg77.uhc.listeners.BlockListener;
import com.leontg77.uhc.listeners.EntityListener;
import com.leontg77.uhc.listeners.InventoryListener;
import com.leontg77.uhc.listeners.PlayerListener;
import com.leontg77.uhc.listeners.SpecInfoListener;
import com.leontg77.uhc.scenario.ScenarioManager;
import com.leontg77.uhc.util.ArrayUtil;
import com.leontg77.uhc.util.PlayersUtil;

/**
 * Main class of the UHC plugin.
 * @author LeonTG77
 */
public class Main extends JavaPlugin {
	private final Logger logger = Bukkit.getServer().getLogger();
	private Settings settings = Settings.getInstance();
	public static BukkitRunnable countdown;
	public static Recipe res;
	public static Main plugin;
	public static int task;
	
	public static boolean ffa;
	public static int teamSize;
	
	public static boolean absorbtion;
	public static boolean ghead;
	public static boolean pearldmg;
	public static boolean godapple;
	
	public static int flintrate;
	public static int applerate;
	public static int shearrate;
	
	@Override
	public void onDisable() {
		Bukkit.getServer().clearRecipes();
		Bukkit.getServer().getScheduler().cancelAllTasks();
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is now disabled.");
		settings.getData().set("game.currentstate", GameState.getState().name());
		settings.saveData();
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " v" + pdfFile.getVersion() + " is now enabled.");
		plugin = this;
		settings.setup(this);
		Arena.getManager().setup();
		
		ffa = settings.getData().getBoolean("game.ffa");
		teamSize = settings.getData().getInt("game.teamsize");
		
		absorbtion = settings.getData().getBoolean("options.absorb");
		ghead = settings.getData().getBoolean("options.ghead");
		pearldmg = settings.getData().getBoolean("options.pearldmg");
		godapple = settings.getData().getBoolean("options.godapple");
		
		flintrate = settings.getData().getInt("game.flintrate");
		applerate = settings.getData().getInt("game.applerate");
		shearrate = settings.getData().getInt("game.shearrate");
		
		Bukkit.getServer().getPluginManager().registerEvents(new ArenaListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new EntityListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new SpecInfoListener(), this);

		getCommand("arena").setExecutor(new ArenaCommand());
		getCommand("ban").setExecutor(new BanCommand());
		getCommand("broadcast").setExecutor(new BroadcastCommand());
		getCommand("butcher").setExecutor(new ButcherCommand());
		getCommand("clearinv").setExecutor(new ClearInvCommand());
		getCommand("clearxp").setExecutor(new ClearXpCommand());
		getCommand("config").setExecutor(new ConfigCommand());
		getCommand("edit").setExecutor(new EditCommand());
		getCommand("end").setExecutor(new EndCommand());
		getCommand("feed").setExecutor(new FeedCommand());
		getCommand("gamemode").setExecutor(new GamemodeCommand());
		getCommand("giveall").setExecutor(new GiveallCommand());
		getCommand("heal").setExecutor(new HealCommand());
		getCommand("health").setExecutor(new HealthCommand());
		getCommand("helpop").setExecutor(new HelpopCommand());
		getCommand("invsee").setExecutor(new InvseeCommand());
		getCommand("kick").setExecutor(new KickCommand());
		getCommand("list").setExecutor(new ListCommand());
		getCommand("ms").setExecutor(new MsCommand());
		getCommand("msg").setExecutor(new MsgCommand());
		getCommand("mute").setExecutor(new MuteCommand());
		getCommand("near").setExecutor(new NearCommand());
		getCommand("pm").setExecutor(new PmCommand());
		getCommand("pvp").setExecutor(new PvPCommand());
		getCommand("random").setExecutor(new RandomCommand());
		getCommand("reply").setExecutor(new ReplyCommand());
		getCommand("rules").setExecutor(new RulesCommand());
		getCommand("scenario").setExecutor(new ScenarioCommand());
		getCommand("sethealth").setExecutor(new SethealthCommand());
		getCommand("setmaxhealth").setExecutor(new SetmaxhealthCommand());
		getCommand("skull").setExecutor(new SkullCommand());
		getCommand("spectate").setExecutor(new SpectateCommand());
		getCommand("setspawn").setExecutor(new SetspawnCommand());
		getCommand("speed").setExecutor(new SpeedCommand());
		getCommand("spread").setExecutor(new SpreadCommand());
		getCommand("ac").setExecutor(new StaffChatCommand());
		getCommand("stalk").setExecutor(new StalkCommand());
		getCommand("start").setExecutor(new StartCommand());
		getCommand("starttimer").setExecutor(new StartTimerCommand());
		getCommand("team").setExecutor(new TeamCommand());
		getCommand("timer").setExecutor(new TimerCommand());
		getCommand("teamloc").setExecutor(new TlCommand());
		getCommand("tp").setExecutor(new TpCommand());
		getCommand("tps").setExecutor(new TpsCommand());
		getCommand("unban").setExecutor(new UnbanCommand());
		getCommand("vote").setExecutor(new VoteCommand());
		getCommand("wl").setExecutor(new WhitelistCommand());
		
		if (settings.getData().contains("game.currentstate")) {
			GameState.setState(GameState.valueOf(settings.getData().getString("game.currentstate")));
		} else {
			GameState.setState(GameState.LOBBY);
		}
		if (ghead) {
			addGoldenHeads();
		}
		if (!godapple) {
			removeNotchApple();
		}
		Scoreboards.getManager().setup();
		Teams.getManager().setupTeams();
		ScenarioManager.getManager().setup();
		
		for (Listener s : ScenarioManager.getManager().getScenariosWithListeners()) {
			Bukkit.getServer().getPluginManager().registerEvents(s, this);
		}
		
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Player online : PlayersUtil.getPlayers()) {	
					if (ArrayUtil.spectating.contains(online.getName())) {
						online.setFoodLevel(20);
						online.setSaturation(20);
						online.setHealth(20.0D);
						online.setFireTicks(0);
						online.setGameMode(GameMode.SURVIVAL);
						
						if (!online.getAllowFlight()) {
							online.setAllowFlight(true);
						}
						
						for (Entity e : online.getNearbyEntities(2, 2, 2)) {
							if (e instanceof Projectile) {
								if (((Projectile) e).isOnGround()) {
									continue;
								}
								
								online.setVelocity(PlayersUtil.getShooter((Projectile) e).getLocation().getDirection().setY(2));
								online.sendMessage(ChatColor.RED + "You were pushed away from that projectile.");
							}
						}
					}
					
					if (((CraftPlayer) online).getHandle().playerConnection.networkManager.getVersion() < 47) {
						if (online.isFlying()) {
							if (online.isSprinting()) {
								if (online.getFlySpeed() == 0.1f) {
									online.setFlySpeed(0.25f);
								}
							} else {
								if (online.getFlySpeed() == 0.25f) {
									online.setFlySpeed(0.1f);
								}
							}
						}
					}
					
					if (online.isOp()) {
						online.sendMessage(ChatColor.DARK_RED + "You aren't allowed to have operator status.");
						online.setOp(false);
					}
					
					if (ScenarioManager.getManager().getScenario("Pyrophobia").isEnabled()) {
						for (ItemStack item : online.getInventory().getContents()) {
							if (item != null && item.getType() == Material.WATER_BUCKET) {
								item.setType(Material.BUCKET);
								online.sendMessage(ChatColor.DARK_RED + "You aren't allowed to have water buckets in pyrophobia.");
							}
						}
					}
					
					Damageable player = (Damageable) online;
					Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
					Objective obj = sb.getObjective("HP");
					
					if (sb.getObjective("HP") == null)
						return;
					
					Objective obj1 = sb.getObjective("HP2");
					
					if (sb.getObjective("HP2") == null)
						return;
					
					Score score = obj.getScore(online.getName());
					Score score1 = obj1.getScore(online.getName());
					double health = player.getHealth();
					double hearts = health / 2;
					double precent = hearts * 10;
					score.setScore((int) precent);
					score1.setScore((int) precent);
				}
			}
		}, 1, 1);
	}
	
	/**
	 * Start the starting countdown.
	 */
	public static void startCountdown() {
		GameState.setState(GameState.WAITING);
		Runnables.timeToStart = 3;
		countdown = new Runnables();
		countdown.runTaskTimer(plugin, 20, 20);
		host().chat("/gamerule doMobSpawning false");
		host().chat("/config set motd &6&lGame has started.");
	}
	
	/**
	 * Stop the starting countdown.
	 */
	public static void stopCountdown() {
		countdown.cancel();
	}
	
	/**
	 * Get the UHC prefix with an ending color.
	 * @param endcolor the ending color.
	 * @return The UHC prefix.
	 */
	public static String prefix() {
		String prefix = "§4§lUHC §8§l>> §7";
		return prefix;
	}
	
	/**
	 * Get the UHC prefix with an ending color.
	 * @param endcolor the ending color.
	 * @return The UHC prefix.
	 */
	public static String prefix(ChatColor endcolor) {
		String prefix = "§4§lUHC §8§l>> " + endcolor;
		return prefix;
	}
	
	/**
	 * Adds the golden head recipe.
	 */
	@SuppressWarnings("deprecation")
	private void addGoldenHeads() {
        MaterialData mater = new MaterialData (Material.SKULL_ITEM);
        mater.setData((byte) 3);
        ItemStack head = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD  + "Golden Head");
        meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Some say consuming the head of a", ChatColor.DARK_PURPLE + "fallen foe strengthens the blood."));
        head.setItemMeta(meta); 
        
        ShapedRecipe goldenhead = new ShapedRecipe(head).shape("@@@", "@*@", "@@@").setIngredient('@', Material.GOLD_INGOT).setIngredient('*', mater);
        Bukkit.getServer().addRecipe(goldenhead);
        res = goldenhead;
	}

	/**
	 * Disables notch apples to be craftable.
	 */
	private void removeNotchApple() {
		Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
		Recipe recipe;
		
		while (it.hasNext()) {
			recipe = it.next();
			if (recipe != null && recipe.getResult().getType() == Material.GOLDEN_APPLE && recipe.getResult().getDurability() == 1) {
				it.remove();
			}
		}
	}

	/**
	 * Get the host of the game.
	 * @return the host, random player if offline.
	 */
	public static Player host() {
		Player host = Bukkit.getPlayer(Settings.getInstance().getData().getString("game.host"));
		
		if (host == null) {
			return PlayersUtil.getPlayers().get(0);
		}
		return host;
	}
}