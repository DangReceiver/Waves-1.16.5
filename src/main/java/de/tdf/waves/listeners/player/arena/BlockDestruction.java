package de.tdf.waves.listeners.player.arena;

import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockDestruction implements Listener {

	@EventHandler
	public void handle(EntityExplodeEvent e) {
		e.setCancelled(true);
		e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 0.85f);
		for (Block b : e.blockList()) {
			Material bt = b.getType();
			b.setType(Material.AIR);
			Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
				b.getLocation().getBlock().setType(bt);
				b.getLocation().getWorld().spawnParticle(Particle.FLAME, b.getLocation(), 1);
				b.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, b.getLocation(), 1);
			}, 40);
		}
	}

	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent e) {
		if (e.getEntityType() == EntityType.ENDERMAN) {
			e.setCancelled(true);
		} else if (e.getEntityType() == EntityType.GHAST) {
			e.setCancelled(true);
		}
	}
}
