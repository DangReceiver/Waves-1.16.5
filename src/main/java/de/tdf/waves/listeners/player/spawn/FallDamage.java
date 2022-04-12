package de.tdf.waves.listeners.player.spawn;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.listeners.player.supplies.BlockBreak;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FallDamage implements Listener {

	@EventHandler
	public void handle(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player p) {
			if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
				pConfig pc = pConfig.loadConfig(p, "Waves");
				if (p.getWorld() == Waves.spawn) {
					Location l = p.getLocation();
					if (l.getY() <= 82 && l.add(0, -1, 0).getBlock().getType() == Material.BARRIER ||
							l.getY() <= 82 && l.add(0, -2, 0).getBlock().getType() == Material.BARRIER) {
						e.setDamage(p.getHealth() - 1);
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 7, true, false, false));
						p.teleport(Waves.spLoc);
						Sound s = Utils.getRandomSound();
						p.playSound(l, s, 0.25f, 0.75f);
						Bukkit.getScheduler().runTaskLaterAsynchronously(Waves.getWaves(), () -> {
							p.playSound(l, s, 0.3f, 1);
						}, 2);
					} else
						p.sendMessage("§3-1: " + l.add(0, -1, 0).getBlock().getType()
								+ " §8| §6-2:" + l.add(0, -2, 0).getBlock().getType());
				} else if (pc.getBoolean("Tutorial.task.killAMob") && pc.getBoolean("Tutorial.progress.arena")) {
					e.setCancelled(true);
					Utils.hologram(p.getLocation(), En.HOLO_NO_FALL_DAMAGE, false, false);
				}
			}
		}
	}
}
