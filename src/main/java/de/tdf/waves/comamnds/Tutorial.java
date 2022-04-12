package de.tdf.waves.comamnds;

import de.tdf.helpy.methods.Other;
import de.tdf.helpy.methods.lang.Eng;
import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.listeners.player.spawn.FallDamage;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tutorial implements CommandExecutor {

	final private static String TUTORIAL_PERM_RESET_SELF = "Waves.Tutorial.reset.self",
			TUTORIAL_PERM_START_FORCE = "Tutorial.start.force",
			TUTORIAL_PERM_RESET_OTHERS = "Waves.Tutorial.reset.others";

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		if (sen instanceof Player p) {
			pConfig pc = pConfig.loadConfig(p, "Waves");
			if (args.length == 0) {
				if (pc.getBoolean("Tutorial.done")) {
					p.sendMessage(En.PRE + En.TUTORIAL_ERROR_STARTED_ALREADY);
					return true;
				}
				if (de.tdf.waves.listeners.tutorial.Tutorial.start(p, false)) {
					p.getInventory().clear();
					p.sendMessage(En.TUTORIAL + En.TUTORIAL_PRE_INV_CLEAR);
				} else {
					p.sendMessage(En.PRE + En.TUTORIAL_CONCLUSIVE_INTERRUPTION);
					return true;
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reset")) {
					if (!p.hasPermission(TUTORIAL_PERM_RESET_SELF)) {
						Eng.permissionShow(p, TUTORIAL_PERM_RESET_SELF);
						return true;
					}
					pc.set("Tutorial", null);
					pc.savePCon();
					Other.actionBar(p, "neutral", En.TUTORIAL_TIP_HOWTO_START, 0.25f, 1.1f);
				} else {
					try {
						boolean b = Boolean.parseBoolean(args[0]);
						if (!p.hasPermission(TUTORIAL_PERM_START_FORCE)) {
							Eng.permissionShow(p, TUTORIAL_PERM_START_FORCE);
							return true;
						}
						if (!pc.getBoolean("Tutorial.done")) {
							de.tdf.waves.listeners.tutorial.Tutorial.start(p, b);
							p.getInventory().clear();
							p.sendMessage(En.TUTORIAL + En.TUTORIAL_PRE_INV_CLEAR);
						} else
							p.sendMessage(En.PRE + En.TUTORIAL_ERROR_PLAYED_ALREADY);
					} catch (Exception e) {
						Eng.argsUsage(sen, "[<reset>] [<player name>]", true);
					}
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("reset")) {
					if (!p.hasPermission(TUTORIAL_PERM_RESET_OTHERS)) {
						Eng.permissionShow(p, TUTORIAL_PERM_RESET_OTHERS);
						return true;
					}
					Player t = Bukkit.getPlayer(args[1]);
					if (t == p) {
						p.sendMessage(En.PRE + En.YOU_PLAYED_YOURSELF);
						return true;
					}
					if (t != null) {
						pConfig tc = pConfig.loadConfig(t, "Waves");
						tc.set("Tutorial", null);
						tc.savePCon();
						Other.actionBar(t, "neutral", En.TUTORIAL_TIP_HOWTO_START, 0.25f, 1.1f);
						Other.actionBar(p, "neutral", En.TUTORIAL_MAN_RESET_TARGET, 0.25f, 1f);
					}
				} else
					Eng.argsUsage(sen, "[<reset>] [<player name>]", true);
			} else
				Eng.argsUsage(sen, "[<reset>] [<player name>]", true);
		} else
			sen.sendMessage(Eng.CMD_NOT_PLAYER);
		return false;
	}

	public static void finishedTaskVisuals(Player p) {
		p.sendTitle(En.TUTORIAL, En.TUTORIAL_PART_TASK_FINISHED, 10, 80, 20);
		p.playSound(p.getLocation(), Utils.getRandomSound(), 0.6f, 1.1f);
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			p.playSound(p.getLocation(), Utils.getRandomSound(), 0.6f, 1.4f);
		}, 5);
	}
}
