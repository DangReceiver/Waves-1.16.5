package de.tdf.waves.listeners.player.spawn;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class Sneak implements Listener {

	//@EventHandler
	public void handle(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (p.hasPermission("Waves.glide"))
			if (p.getWorld() == Waves.spawn)
				if (!pc.getBoolean("Assets.isGliding")) {
					if (p.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR)
						if (p.getInventory().getItemInOffHand().getType() == Material.PHANTOM_MEMBRANE) {
							glide(p);
							pc.set("Assets.isGliding", true);
							pc.savePCon();
						}
				}
	}

	public void glide(Player p) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(Waves.getWaves(), () -> {
			if (p.isSneaking() && p.getInventory().getItemInOffHand().getType() == Material.PHANTOM_MEMBRANE)
				if (p.isSneaking() && p.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR) {
					double x = p.getLocation().getDirection().getX(), z = p.getLocation().getDirection().getZ();
					if (!(p.getLocation().getDirection().getY() >= 1 || p.getLocation().getDirection().getY() <= -1))
						p.setVelocity(new Vector(x * 1.003, p.getLocation().getDirection().getY() + 0.05, z * 1.003));
					else
						p.setVelocity(new Vector(x, p.getLocation().getDirection().getY() + 0.04, z));
					glide(p);
				}
			pConfig pc = pConfig.loadConfig(p, "Waves");
			pc.set("Assets.isGliding", null);
			pc.savePCon();
		}, 2);
	}
}
