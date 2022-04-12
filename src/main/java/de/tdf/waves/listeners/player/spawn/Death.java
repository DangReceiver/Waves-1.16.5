package de.tdf.waves.listeners.player.spawn;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.listeners.CM;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Death implements Listener {
	@EventHandler
	public void handle(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (p.getWorld() == Waves.spawn) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 0, true, false, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0, true, false, false));
			e.setDeathSound(Sound.ENTITY_IRON_GOLEM_DEATH);
		}
		e.getDrops().remove(CM.waveFinder);
		e.getDrops().remove(CM.navi);
		e.getDrops().remove(CM.identifier);
		e.setDeathMessage("");
	}

	@EventHandler
	public void handle(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
//			e.getRespawnLocation();
		if (p.getWorld() == Waves.spawn)
			p.playSound(p.getLocation(), Utils.getRandomSound(), 0.5f, 0.75f);
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (pc.getInt("Tutorial.progress.part") == 3 && pc.getBoolean("Tutorial.task.killAMob")) {
			e.setRespawnLocation(Waves.arena);
			return;
		}
		e.setRespawnLocation(Waves.spLoc);
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, true, false, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90, 1, true, false, false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 180, 1, true, false, false));
		}, 4);
		Inventory i = p.getInventory();
		i.setItem(1, CM.waveFinder);
		i.setItem(4, CM.navi);
		i.setItem(7, CM.identifier);
	}
}
