package de.tdf.waves.listeners.player.arena;

import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntitySpawnLoop {

	public static List<EntityType> wlMonsterList = new ArrayList<>();
	public static List<String> wlMonstersAsStrings =  Waves.getWaves().getConfig().getStringList("Arena.allowedMobs");

	public static void entitySpawnLoop() {
		System.out.println(En.SOUT_INFO + En.SOUT_ENTITY_SPAWN_LOOP_STARTING);
		if (Waves.arena == null) {
			System.out.println(En.SOUT_WARN + "Location is null! Starting loop interrupted.");
			return;
		}
		Location l = Waves.arena.clone();
		Bukkit.getScheduler().runTaskTimer(Waves.getWaves(), () -> {
			if (l.getNearbyEntities(50, 50, 50).size() <= 16) {
				Random r = new Random();
				int i = 80;
				int x = r.nextInt(i), z = r.nextInt(i);
				x -= 40;
				z -= 40;
				Entity e = Utils.genRandomMob(new Location(Waves.arena.getWorld(), x, 80.01, z));
				Utils.hologram(e.getLocation().add(0, 1, 0), En.HOLO_RECENTLY_SPAWNED, true, false);
			}
		}, 10 * 20, 6 * 20);
	}
}
