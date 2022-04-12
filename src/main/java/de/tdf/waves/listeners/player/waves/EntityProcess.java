package de.tdf.waves.listeners.player.waves;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.Xp;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class EntityProcess implements Listener {

	@EventHandler
	private void handle(EntityDeathEvent e) {
		if (e.isCancelled()) return;
		LivingEntity en = e.getEntity();
		if (en.getWorld() != Waves.wavesLoc.getWorld()) return;
		if (en.getLastDamageCause() == null) return;
		FileConfiguration c = Waves.getWaves().getConfig();
		String s = en.getUniqueId().toString();
		if (WaveStart.arena0.contains(s)) {
			WaveStart.arena0.remove(s);
			c.set("WavesUse.arena0.spawnedMobIDs", WaveStart.arena0);
		} else if (WaveStart.arena1.contains(s)) {
			WaveStart.arena1.remove(s);
			c.set("WavesUse.arena1.spawnedMobIDs", WaveStart.arena1);
		} else if (WaveStart.arena2.contains(s)) {
			WaveStart.arena2.remove(s);
			c.set("WavesUse.arena2.spawnedMobIDs", WaveStart.arena2);
		} else if (WaveStart.arena3.contains(s)) {
			WaveStart.arena3.remove(s);
			c.set("WavesUse.arena3.spawnedMobIDs", WaveStart.arena3);
		} else
			return;
		Waves.getWaves().saveConfig();
		EntityDamageEvent.DamageCause dc = en.getLastDamageCause().getCause();
		if (dc == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
				dc == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK ||
				dc == EntityDamageEvent.DamageCause.PROJECTILE) {
			Player p = en.getKiller();
			if (dc == EntityDamageEvent.DamageCause.PROJECTILE) {
				if (en.getKiller() instanceof Projectile pj) {
					ProjectileSource ps = pj.getShooter();
					if (ps instanceof Player)
						p = (Player) ps;
				}
			}
			if (p == null) {
				p = killPNull(en, c, p);
				if (p == null) {
					quickKill(p, false, en);
					return;
				}
				quickKill(p, true, en);
				return;
			}
			Xp xp = Xp.load();
			xp.loadPlayer(p);
			quickKill(p, true, en);
		} else {
			Player p = null;
			killPNull(en, c, p);
			if (p != null)
				quickKill(p, false, en);
			else {
				quickKill(p, true, en);
				Xp xp = Xp.load();
				xp.loadPlayer(p);
			}
		}
	}

	public void quickKill(Player p, boolean perHand, Entity e) {
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (!pc.isSet("Wave.currentWave")) return;
		int i = pc.getInt("Wave.currentWave.mobAmount"),
				k = pc.getInt("Wave.currentWave.killedYet");
		pc.set("Wave.currentWave.killedYet", k + 1);
		k++;
		pc.savePCon();
		p.sendTitle("", "§e" + k + " §8/ §6" + i + "  §c⁂", 8, 20, 10);
		int arena = pc.getInt("Wave.currentWave.arena");

		FileConfiguration c = Waves.getWaves().getConfig();
		if (c.isSet("WavesUse.arena" + arena + ".spawnedMobIDs")) {
			List<String> sl = c.getStringList("WavesUse.arena" + arena + ".spawnedMobIDs");
			if (!sl.isEmpty())
				for (String id : sl) {
					Entity en = Bukkit.getEntity(UUID.fromString(id));
					if (en == null || en.isDead()) {
						sl.remove(id);
						c.set("WavesUse.arena" + arena + ".spawnedMobIDs", sl);
						i--;
						pc.set("Wave.currentWave.mobAmount", i);
					}
				}
		}
		if (k >= i) {
			switch (arena) {
				case 0:
					WaveStart.arena0.clear();
					break;
				case 1:
					WaveStart.arena1.clear();
					break;
				case 2:
					WaveStart.arena2.clear();
					break;
				case 3:
					WaveStart.arena3.clear();
					break;
				default:
					break;
			}
			c.set("WavesUse.arena" + arena, null);
			Waves.getWaves().saveConfig();
			pc.set("Waves.pending", null);
			pc.set("Wave.currentWave." + pc.getString("Wave.currentWave.name"), null);
			pc.set("Wave.currentWave.mobAmount", null);
			pc.set("Wave.currentWave.killedYet", null);
			pc.savePCon();
			p.sendTitle(En.CONGRATS, "§a" + (k) + " §8/ §6" + i, 8, 20, 10);
			p.teleport(Waves.spLoc);
			Xp xp = Xp.load();
			xp.loadPlayer(p);
			xp.addXpPoints(8);
			pc.setTokens(pc.getTokens() + 100);
			return;
		}
		if (perHand) {
			p.sendTitle("", "§e" + k + " §8/ §6" + i + "  §2⚔", 8, 20, 10);
			if (i - k <= 4) {
				Location tLoc = c.getLocation("WaveArenas.Arena" + arena);
				int count = 0;
				p.sendMessage("\n" + En.PRE + "Entity locations:");
				for (Entity en : tLoc.getNearbyEntities(51, 25, 51)) {
					if (Utils.isMonster(en.getType()) && !en.isDead()) {
						count++;
						Location ttl = en.getLocation();
						p.sendMessage(En.PRE + "§5#§9" + count + "§8: §e" + Math.round(ttl.getX())
								+ "§8, §6" + Math.round(ttl.getY()) + "§8, §e" + Math.round(ttl.getZ()));
					}
				}
			}
			Xp xp = Xp.load();
			xp.loadPlayer(p);
			xp.addXpPoint();
			Utils.hologram(e.getLocation(), String.format(En.HOLO_XP_ADD, 1), true, true);
		}
	}

	public Player killPNull(Entity en, FileConfiguration c, @Nullable Player p) {
		String s = en.getUniqueId().toString();
		if (WaveStart.arena0.contains(s)) {
			WaveStart.arena0.remove(s);
			c.set("WavesUse.arena0.spawnedMobIDs", WaveStart.arena0);
			p = Bukkit.getPlayer(c.getString("WavesUse.arena0.playerName"));
		} else if (WaveStart.arena1.contains(s)) {
			WaveStart.arena1.remove(s);
			c.set("WavesUse.arena1.spawnedMobIDs", WaveStart.arena1);
			p = Bukkit.getPlayer(c.getString("WavesUse.arena1.playerName"));
		} else if (WaveStart.arena2.contains(s)) {
			WaveStart.arena2.remove(s);
			c.set("WavesUse.arena2.spawnedMobIDs", WaveStart.arena2);
			p = Bukkit.getPlayer(c.getString("WavesUse.arena2.playerName"));
		} else if (WaveStart.arena3.contains(s)) {
			WaveStart.arena3.remove(s);
			c.set("WavesUse.arena3.spawnedMobIDs", WaveStart.arena3);
			p = Bukkit.getPlayer(c.getString("WavesUse.arena3.playerName"));
		} else
			return null;
		Waves.getWaves().saveConfig();
		return p;
	}
}