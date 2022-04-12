package de.tdf.waves.comamnds;

import de.tdf.helpy.methods.lang.Eng;
import de.tdf.waves.methods.Sb;
import de.tdf.waves.methods.lang.En;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Xp implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		if (sen instanceof Player p) {
			de.tdf.waves.methods.Xp xp = de.tdf.waves.methods.Xp.load();
			xp.loadPlayer(p);
			if (args.length == 0) {
				p.sendMessage(Eng.LINE_SPACE);
				p.sendMessage(En.PRE + String.format(En.SPECIFY_LEVEL, xp.getXpLevel()));
				p.sendMessage(En.PRE + String.format(En.SPECIFY_POINTS, xp.getXpPoints(), xp.getMaxXpPoints()));
				p.sendMessage(Eng.LINE_SPACE);
				return true;
			}
			if (p.hasPermission("Waves.Xp.manageSelf")) {
				if (args.length == 2) {
					long l;
					try {
						l = Long.parseLong(args[1]);
					} catch (NumberFormatException e) {
						Eng.entryType(p, "3", "long");
						return true;
					}
					if (args[0].equalsIgnoreCase("add")) {
						xp.addXpPoints(l);
					} else if (args[0].equalsIgnoreCase("remove")) {
						xp.addXpPoints(-l);
					} else if (args[0].equalsIgnoreCase("set")) {
						xp.setXpPoints(l);
					} else {
						Eng.argsUsage(p, "/Xp [<player name>] <add; remove; set> <amount (points)>", true);
						return true;
					}
					xp.savePlayer();
					Sb.updateLevel(p);
					p.sendMessage(Eng.CMD_ACTION_CONFIRMED);
				} else if (args.length == 3) {
					if (!p.hasPermission("Waves.Xp.manageOthers")) {
						Eng.permissionShow(p, "Waves.Xp.manageOthers");
						return true;
					}
					Player t = Bukkit.getPlayer(args[0]);
					if (t == null) {
						p.sendMessage(Eng.CMD_TARGET_NOT_EXI_SAFE);
						return true;
					}
					xp.loadPlayer(t);
					long l;
					try {
						l = Long.parseLong(args[2]);
					} catch (NumberFormatException e) {
						Eng.entryType(p, "3", "long");
						return true;
					}
					if (args[1].equalsIgnoreCase("add")) {
						xp.addXpPoints(l);
					} else if (args[1].equalsIgnoreCase("remove")) {
						xp.addXpPoints(-l);
					} else if (args[1].equalsIgnoreCase("set")) {
						xp.setXpPoints(l);
					} else {
						Eng.argsUsage(p, "/Xp [<player name>] <add; remove; set> <amount (points)>", true);
						return true;
					}
					xp.savePlayer();
					Sb.updateLevel(t);
					p.sendMessage(Eng.CMD_ACTION_CONFIRMED);
				} else
					p.sendMessage(Eng.CMD_ARGS_LENGHT);
			} else
				Eng.permissionShow(p, "Waves.Xp.manageSelf");
		} else
			sen.sendMessage(Eng.CMD_NOT_PLAYER);
		return false;
	}
}
