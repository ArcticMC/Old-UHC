package com.leontg77.uhc.scenario.types;

import org.bukkit.event.Listener;

import com.leontg77.uhc.scenario.Scenario;

public class Astrophobia extends Scenario implements Listener {
	private static boolean enabled = false;

	public Astrophobia() {
		super("Astrophobia", "");
	}

	public void setEnabled(boolean enable) {
		enabled = enable;
	}

	public boolean isEnabled() {
		return enabled;
	}
}