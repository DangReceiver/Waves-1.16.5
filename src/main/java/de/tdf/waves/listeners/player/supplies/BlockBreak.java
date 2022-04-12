package de.tdf.waves.listeners.player.supplies;

import de.tdf.helpy.methods.items.IB;
import de.tdf.waves.methods.Sb;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.Xp;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreak implements Listener {

	@EventHandler
	public void handle(BlockBreakEvent e) {
		Player p = e.getPlayer();
		World w = p.getWorld();
		if (p.getGameMode() != GameMode.SURVIVAL) return;
		if (w == Waves.supplies.getWorld()) {
			Block b = e.getBlock();
			Material t = b.getType();
			Location l = b.getLocation();
			switch (t) {
				case POLISHED_ANDESITE:
					e.setDropItems(false);
					w.dropItemNaturally(b.getLocation().add(0, 0.3, 0),
							IB.name(new ItemStack(Material.ANDESITE), "§7Broken polished andesite"));
					Xp xp = Xp.load();
					xp.loadPlayer(p);
					xp.addXpPoint();
					Utils.hologram(b.getLocation(), String.format(En.HOLO_XP_ADD, 1), true, true);
					Sb.updateLevel(p);
					l.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, l, 1);
					Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
						l.getBlock().setType(Material.POLISHED_ANDESITE);
						l.getWorld().spawnParticle(Particle.SMOKE_LARGE, l, 3);
					}, 120);
					break;
				case BARRIER:
					break;
				default:
					e.setCancelled(true);
					break;
			}
		}
	}
}
