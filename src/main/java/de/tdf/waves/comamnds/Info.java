package de.tdf.waves.comamnds;

import de.tdf.helpy.methods.lang.Eng;
import de.tdf.waves.methods.Sb;
import de.tdf.waves.methods.enums.Priority;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class Info implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		if (args.length != 1) {
			sen.sendMessage(Eng.LINE_SPACE);
			sen.sendMessage(En.PRE);
			sen.sendMessage(En.AUTHOR_PRE + En.AUTHOR);
			sen.sendMessage(En.VERSION_PRE + Waves.VERSION);
			sen.sendMessage(Eng.LINE_SPACE);
			Random r = new Random();
			int i = 5;
			if (sen instanceof Player p) {
				if (p.getWorld() == Waves.spawn)
					Sb.sbMessage(p, En.TEST_OUTPUT, Priority.LOWEST, 8);
				if (r.nextInt(i) >= 4) {
					Sb.sbMessage(p, null, Priority.LOWEST, 8);
					p.sendTitle(En.PRE, En.TEST_OUTPUT, 20, 20, 40);
				}
			}
		} else {
			if (!sen.hasPermission("Waves.info.uuidCheck")) {
				Eng.permissionShow(sen, "Waves.info.uuidCheck");
				return true;
			}
			if (!args[0].contains("-")) {
				sen.sendMessage(En.PRE + "This is not a valid uuid!");
				return true;
			}
			UUID id = UUID.fromString(args[0]);
			Entity e = Bukkit.getEntity(id);
			if (e == null) {
				sen.sendMessage(En.PRE + "Entity not found");
				return true;
			}
			sen.sendMessage("Type:" + e.getType().toString() +
					"\nLocation: " + e.getLocation().toString() +
					"\nName / Custom name:" + e.getName() + "/" + e.getCustomName());
		}
		return false;
	}
}
