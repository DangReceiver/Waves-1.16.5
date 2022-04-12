package de.tdf.waves.listeners.player.all;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorChange implements Listener {

	@EventHandler
	public void handle(PlayerArmorChangeEvent e) {
		Player p = e.getPlayer();
		if (e.getNewItem() == null) return;
		ItemStack i = e.getNewItem();
		if (!i.hasItemMeta() || !i.getItemMeta().hasEnchant(Enchantment.ARROW_FIRE)) return;
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (e.getSlotType() == PlayerArmorChangeEvent.SlotType.FEET) {
			if (i.getType() == Material.GOLDEN_BOOTS) {
				pc.set("assets", null);
				pc.set("assets.heartsTrail", true);
				heartsTrailGen(p);
			} else if (i.getType() == Material.CHAINMAIL_BOOTS) {
				pc.set("assets", null);
				pc.set("assets.notesTrail", true);
				notesTrailGen(p);
			} else
				pc.set("assets", null);
			pc.savePCon();
		} else if (e.getSlotType() == PlayerArmorChangeEvent.SlotType.CHEST) {
			if (i.getType() == Material.GOLDEN_CHESTPLATE) {
				pc.set("assets", null);
				pc.set("assets.bigHeartTrail", true);
				bigHeartTrail(p);
			} else
				pc.set("assets", null);
			pc.savePCon();
		}
	}

	public void bigHeartTrail(Player p) {
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			pConfig pc = pConfig.loadConfig(p, "Waves");
			if (!pc.getBoolean("assets.bigHeartTrail")) return;
			Location l = p.getEyeLocation().add(0, -0.75, 0.5);
			p.getWorld().spawnParticle(Particle.HEART, l, 1);
			p.getWorld().spawnParticle(Particle.HEART, l.clone().add(0, 0.4, 0), 1);
			p.getWorld().spawnParticle(Particle.HEART, l.clone().add(-0.5, 0.4, 0), 1);
			p.getWorld().spawnParticle(Particle.HEART, l.clone().add(0.5, 0.4, 0), 1);
			p.getWorld().spawnParticle(Particle.HEART, l.clone().add(0.5, 0.8, 0), 1);
			p.getWorld().spawnParticle(Particle.HEART, l.clone().add(-0.5, 0.8, 0), 1);
			bigHeartTrail(p);
		}, 5);
	}

	public void heartsTrailGen(Player p) {
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			pConfig pc = pConfig.loadConfig(p, "Waves");
			if (!pc.getBoolean("assets.heartsTrail")) return;
			Location l = p.getLocation().add(0, 0.25, 0);
			p.getWorld().spawnParticle(Particle.HEART, l, 1);
			pc.set("assets.heartsTrailFifth", pc.getInt("assets.heartsTrailFifth") + 1);
			if (pc.getInt("assets.heartsTrailFifth") >= 5) {
				pc.set("assets.heartsTrailFifth", 0);
				l.setY(l.getY() + 0.75);
				Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
					p.getWorld().spawnParticle(Particle.HEART, l, 1);
				}, 12);
			}
			pc.savePCon();
			heartsTrailGen(p);
		}, 2);
	}

	public void notesTrailGen(Player p) {
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			pConfig pc = pConfig.loadConfig(p, "Waves");
			if (!pc.getBoolean("assets.notesTrail")) return;
			Location l = p.getLocation().add(0, 0.25, 0);
			p.getWorld().spawnParticle(Particle.NOTE, l, 1);
			pc.set("assets.notesTrailFifth", pc.getInt("assets.notesTrailFifth") + 1);
			if (pc.getInt("assets.notesTrailFifth") >= 6) {
				pc.set("assets.notesTrailFifth", 0);
				l.setY(l.getY() + 0.75);
				Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
					p.getWorld().spawnParticle(Particle.NOTE, l.add(0, 0.45, 0), 1);
					Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
						p.getWorld().spawnParticle(Particle.NOTE, l.add(0, 0.7, 0), 1);
					}, 3);
				}, 3);
			}
			pc.savePCon();
			notesTrailGen(p);
		}, 2);
	}
}
