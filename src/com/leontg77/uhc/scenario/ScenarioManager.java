package com.leontg77.uhc.scenario;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;

import com.leontg77.uhc.scenario.types.Barebones;
import com.leontg77.uhc.scenario.types.BestPvE;
import com.leontg77.uhc.scenario.types.BetaZombies;
import com.leontg77.uhc.scenario.types.BloodDiamonds;
import com.leontg77.uhc.scenario.types.Compensation;
import com.leontg77.uhc.scenario.types.Cryophobia;
import com.leontg77.uhc.scenario.types.CutClean;
import com.leontg77.uhc.scenario.types.Depths;
import com.leontg77.uhc.scenario.types.DoubleOres;
import com.leontg77.uhc.scenario.types.EnchantedDeath;
import com.leontg77.uhc.scenario.types.Fallout;
import com.leontg77.uhc.scenario.types.GoldenPearl;
import com.leontg77.uhc.scenario.types.Krenzinator;
import com.leontg77.uhc.scenario.types.Moles;
import com.leontg77.uhc.scenario.types.NightmareMode;
import com.leontg77.uhc.scenario.types.NoFall;
import com.leontg77.uhc.scenario.types.Pyrophobia;
import com.leontg77.uhc.scenario.types.QuadOres;
import com.leontg77.uhc.scenario.types.RewardingLongshots;
import com.leontg77.uhc.scenario.types.SkyClean;
import com.leontg77.uhc.scenario.types.Skyhigh;
import com.leontg77.uhc.scenario.types.TripleOres;
import com.leontg77.uhc.scenario.types.VengefulSpirits;

/**
 * Scenario management class.
 * @author LeonTG77
 */
public class ScenarioManager {
	private ArrayList<Scenario> scen = new ArrayList<Scenario>();
	
	private ScenarioManager() {}
	private static ScenarioManager manager = new ScenarioManager();
	public static ScenarioManager getManager() {
		return manager;
	}
	
	/**
	 * Setup all the scenarios.
	 */
	public void setup() {
		scen.add(new Barebones());
		scen.add(new BestPvE());
		scen.add(new BetaZombies());
		scen.add(new BloodDiamonds());
		scen.add(new Compensation());
		scen.add(new Cryophobia());
		scen.add(new CutClean());
		scen.add(new Depths());
		scen.add(new DoubleOres());
		scen.add(new EnchantedDeath());
		scen.add(new Fallout());
		scen.add(new GoldenPearl());
		scen.add(new Krenzinator());
		scen.add(new Moles());
		scen.add(new NightmareMode());
		scen.add(new NoFall());
		scen.add(new Pyrophobia());
		scen.add(new QuadOres());
		scen.add(new RewardingLongshots());
		scen.add(new SkyClean());
		scen.add(new Skyhigh());
		scen.add(new TripleOres());
		scen.add(new VengefulSpirits());
	}
	
	/**
	 * Get a scenario by a name.
	 * @param name the name.
	 * @return The scenario, null if not found.
	 */
	public Scenario getScenario(String name) {
		for (Scenario s : scen) {
			if (name.equalsIgnoreCase(s.getName())) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Get a list of all scenarios.
	 * @return the list of scenarios.
	 */
	public List<Scenario> getScenarios() {
		return scen;
	}

	/**
	 * Get a list of all enabled scenarios.
	 * @return the list of enabled scenarios.
	 */
	public List<Scenario> getEnabledScenarios() {
		ArrayList<Scenario> l = new ArrayList<Scenario>();
		for (Scenario s : scen) {
			if (s.isEnabled()) {
				l.add(s);
			}
		}
		return l;
	}

	/**
	 * Get a list of all scenarios that implements listener.
	 * @return the list of scenarios that implements listener.
	 */
	public List<Listener> getScenariosWithListeners() {
		ArrayList<Listener> l = new ArrayList<Listener>();
		for (Scenario s : scen) {
			if (s instanceof Listener) {
				l.add((Listener) s);
			}
		}
		return l;
	}
}