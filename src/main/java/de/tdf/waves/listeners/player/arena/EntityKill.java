package de.tdf.waves.listeners.player.arena;

import de.tdf.helpy.methods.items.Item;
import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.comamnds.Tutorial;
import de.tdf.waves.listeners.CM;
import de.tdf.waves.listeners.player.spawn.FallDamage;
import de.tdf.waves.listeners.player.supplies.BlockBreak;
import de.tdf.waves.methods.Sb;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.Xp;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;

public class EntityKill implements Listener {
	@EventHandler
	public void handle(EntityDeathEvent e) {
		if (e.isCancelled()) return;
		LivingEntity en = e.getEntity();
		if (en.getWorld() != Waves.arena.getWorld()) return;
		if (en.getLastDamageCause() == null || en.getWorld() != Waves.arena.getWorld() || en.getKiller() == null)
			return;
		EntityDamageEvent.DamageCause dc = en.getLastDamageCause().getCause();
		if (dc == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
				dc == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK ||
				dc == EntityDamageEvent.DamageCause.PROJECTILE) {
			Player p = en.getKiller();
			if (dc == EntityDamageEvent.DamageCause.PROJECTILE) {
				if (en.getKiller() instanceof Projectile pj) {
					ProjectileSource s = pj.getShooter();
					if (s instanceof Player)
						p = (Player) s;
				}
			}
			pConfig pc = pConfig.loadConfig(p, "Waves");
			Xp xp = Xp.load();
			xp.loadPlayer(p);
			if (pc.getInt("Tutorial.progress.part") == 3) {
				if (Utils.isMonster(en.getType()))
					if (pc.getBoolean("Tutorial.task.killAMob") && !pc.getBoolean("Tutorial.inProgress")) {
						PlayerInventory in = p.getInventory();
						in.addItem(CM.waveFinder);
						in.addItem(CM.navi);
						in.addItem(CM.identifier);
						Utils.hologram(en.getLocation().add(0, 1, 0), String.format(En.HOLO_XP_ADD, 5), true, false);
						int i = xp.getXpLevel();
						xp.loadPlayer(p);
						xp.addXpPoints(5);
						if (xp.getXpLevel() > i) {
							final Player p1 = p;
							Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
								Tutorial.finishedTaskVisuals(p1);
							}, 132);
						} else
							Tutorial.finishedTaskVisuals(p);
						pc.set("Tutorial.progress.part", 4);
						pc.set("Tutorial.task.killAMob", false);
						pc.savePCon();
						p.sendMessage(En.PRE + En.TUTORIAL_SOFTEND_ARENA_1);
						Item.removeItems(Material.GOLDEN_SWORD, 1, in);
						in.addItem(new ItemStack(Material.GOLDEN_SWORD));
						p.sendMessage(En.PRE + En.TUTORIAL_SOFTEND_ARENA_2);
					}
			}
			xp.loadPlayer(p);
			xp.addXpPoint();
			if (!xp.savePlayer()) System.out.println(En.SOUT_WARN + En.ERROR_SAVE_PLAYER_FILE_XP_TASK);
			Sb.updateLevel(p);
			Utils.hologram(en.getLocation().add(0, 1.75, 0), String.format(En.HOLO_XP_ADD, 1), true, true);
		} else if (en instanceof Player p && dc == EntityDamageEvent.DamageCause.FALL) {
			if (pConfig.loadConfig(p, "Waves").getBoolean("Tutorial.progress.arena")) e.setCancelled(true);
		}
	}
}
