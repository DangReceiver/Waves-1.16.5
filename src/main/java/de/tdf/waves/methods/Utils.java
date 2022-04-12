package de.tdf.waves.methods;

import de.tdf.waves.listeners.player.arena.EntitySpawnLoop;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

	public static List<String> noteBlock = new ArrayList<>();

	public static Entity genRandomMob(Location l) {
		int max = EntityType.values().length;
		Random r = new Random();
		EntityType et = EntityType.fromId(r.nextInt(max));
		int i = 0;
		while (et == null || !isMonster(et)) {
			et = EntityType.fromId(r.nextInt(max));
			i++;
			if (i >= 25) {
				System.out.println(En.SOUT_ERROR + En.SOUT_ENTITY_SPAWN_UNGENERATEABLE);
				return l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
			}
		}
		return l.getWorld().spawnEntity(l, et);
	}

	public static boolean isMonster(EntityType et) {
		if (EntitySpawnLoop.wlMonsterList.contains(et)) return true;
		return false;
	}

	public static void genSoundList() {
		for (Sound s : Sound.values()) {
			if (s.name().contains("BLOCK_NOTE_BLOCK"))
				noteBlock.add(s.toString());
		}
	}

	public static Sound getRandomSound() {
		Random r = new Random();
		return Sound.valueOf(noteBlock.get(r.nextInt(noteBlock.size())));
	}

	public static void hologram(Location l, String s, boolean uprise, boolean head) {
		if (l.getWorld() == null) return;
		ArmorStand a = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
		a.setMarker(true);
		a.setGravity(false);
		a.setCustomNameVisible(true);
		if (head)
			a.setHelmet(new ItemStack(Material.EXPERIENCE_BOTTLE));
		a.setCustomName(s);
		a.setInvisible(true);
		a.setSmall(true);
		a.setInvulnerable(true);
		if (uprise) {
			int i = 0;
			Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
				a.teleport(a.getLocation().add(0, 0.15, 0));
				upriseHologram(a, i);
			}, 8);
		} else
			Bukkit.getScheduler().runTaskLater(Waves.getWaves(), a::remove, 40);
	}

	private static void upriseHologram(ArmorStand a, int i) {
		if (i >= 12) {
			Bukkit.getScheduler().runTaskLater(Waves.getWaves(), a::remove, 10);
			return;
		}
		i++;
		final int in = i;
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			a.teleport(a.getLocation().add(0, 0.15, 0));
			upriseHologram(a, in);
		}, 2);
	}
}
