package com.leontg77.uhc.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.leontg77.uhc.Main;
import com.leontg77.uhc.util.DamageUtil;
import com.leontg77.uhc.util.PlayersUtil;

public class TpsCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("tps")) {
			sender.sendMessage(Main.prefix() + "Current TPS: §6" + DamageUtil.convertHealth(PlayersUtil.getTps()));
		}
		return true;
	}
}