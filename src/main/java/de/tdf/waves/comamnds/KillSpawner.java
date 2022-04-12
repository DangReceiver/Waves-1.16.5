package de.tdf.waves.comamnds;

import de.tdf.helpy.methods.lang.Eng;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class KillSpawner implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		if (sen instanceof Player p) {
			if (p.hasPermission("supplies.killSpawner")) {
				int i = 0;
				for (Entity e : p.getNearbyEntities(1.5, 1.5, 1.5)) {
					if (e instanceof ArmorStand a) {
						UUID u = a.getUniqueId();
						if (a.isMarker() && !a.hasGravity() && a.isSmall() && a.getHelmet().getType() != Material.AIR) {
							FileConfiguration c = Waves.getWaves().getConfig();
							c.set("supplies.spawner." + u, null);
							Waves.getWaves().saveConfig();
							e.remove();
							i++;
						}
					}
				}
				if (i >= 1)
					p.sendMessage(En.PRE + String.format(En.CMD_REMOVE_SPAWNER_SUCCESS, i));
				else
					p.sendMessage(En.PRE + En.CMD_REMOVE_SPAWNER_NONE_REMOVED);
			} else
				Eng.permissionShow(p, "supplies.killSpawner");
		} else
			sen.sendMessage(Eng.CMD_NOT_PLAYER);
		return false;
	}
}
