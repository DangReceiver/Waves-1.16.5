package de.tdf.waves.comamnds;

import de.tdf.helpy.methods.lang.Eng;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.lang.En;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

public class EntityAmount implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		if (sen instanceof Player p) {
			Location l = p.getLocation();
			double d;
			if (args.length >= 1) {
				try {
					d = Double.parseDouble(args[0]);
				} catch (NumberFormatException ex) {
					Eng.entryType(p, "0", "double");
					return true;
				}
			} else
				d = 40;
			Collection<Entity> ne = l.getNearbyEntities(d, d, d);
			int i = ne.size(), mi = 0;
			for (Entity e : ne)
				if (Utils.isMonster(e.getType())) mi++;
			p.sendMessage(Eng.LINE_SPACE);
			p.sendMessage(En.PRE + String.format(En.CMD_TOTAL_ENTITY_AMOUNT, i));
			p.sendMessage(Eng.SPACE);
			p.sendMessage(En.PRE + String.format(En.CMD_WL_ENTITY_AMOUNT, mi));
			p.sendMessage(Eng.LINE_SPACE);
		} else
			sen.sendMessage(Eng.CMD_NOT_PLAYER);
		return false;
	}
}
