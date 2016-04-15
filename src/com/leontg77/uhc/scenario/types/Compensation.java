package com.leontg77.uhc.scenario.types;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import com.leontg77.uhc.scenario.Scenario;

public class Compensation extends Scenario implements Listener {
	private static boolean enabled = false;
	
	public Compensation() {
		super("Compensation", "When a player on a team dies, the player's max health is divided up and added to the max health of the player's teammates. The extra health received will regenerate in 30 seconds.");
	}

	public void setEnabled(boolean enable) {
		enabled = enable;
	}

	public boolean isEnabled() {
		return enabled;
	}
	@EventHandler
    public void onDeath(PlayerDeathEvent event) {
		if (!isEnabled()) {
			return;
		}
		
		Player victim = event.getEntity();
    	Damageable dmg = victim;

        double victimMaxHealth = dmg.getMaxHealth();

        Team victimTeam = victim.getScoreboard().getPlayerTeam(victim);

        if (victimTeam != null) {
            victimTeam.removePlayer(victim);

            double healthPerPerson = victimMaxHealth / victimTeam.getPlayers().size();
            int healthPerPersonRounded = (int) healthPerPerson;

            double excessHealth = healthPerPerson - healthPerPersonRounded;

            int ticksRegen = healthPerPersonRounded * 50;

            for (OfflinePlayer p : victimTeam.getPlayers()) {
            	if (p.getPlayer() == null) {
            		continue;
            	}
            	
            	Damageable dmgt = p.getPlayer();
                p.getPlayer().setMaxHealth(dmgt.getMaxHealth() + healthPerPerson);
                p.getPlayer().setHealth(dmgt.getHealth() + excessHealth);
                p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, ticksRegen, 0));
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
    	if (!isEnabled()) {
    		return;
    	}
    	
    	Player player = event.getPlayer();
    	Damageable dmg = player;

        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            player.getActivePotionEffects().remove(PotionEffectType.REGENERATION);

            double regenTicks = (dmg.getMaxHealth() / 5) * 25;
            int regenTicksRounded = (int) regenTicks;

            double excessHealth = regenTicks - regenTicksRounded;

            player.setHealth(dmg.getHealth() + excessHealth);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, regenTicksRounded, 1));

        }
    }
}