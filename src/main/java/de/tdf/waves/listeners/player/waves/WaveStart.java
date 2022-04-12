package de.tdf.waves.listeners.player.waves;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.enums.Difficulty;
import de.tdf.waves.methods.enums.WaveTypes;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaveStart {

	public static List<String> arena0 = new ArrayList<>(), arena1 = new ArrayList<>(),
			arena2 = new ArrayList<>(), arena3 = new ArrayList<>();


	public static int genAmount(Player p, Difficulty d, WaveTypes wt) {
		double multiplier = switch (d) {
			case EZ, EASY -> 0.75;
			case NORMAL, PRE_NORMAL -> 1;
			case PAST_NORMAL -> 1.25;
			case HARD, HARDER -> 1.8;
			case VERY_HARD, DIFFICULT -> 2.4;
			case INSANE -> 4;
		};

		pConfig pc = pConfig.loadConfig(p, "Waves");
		int i = pc.isSet("Waves.pastWaves.amount") ? pc.getInt("Waves.pastWaves.amount") : 1;

		int amount = 12;
		switch (wt) {
			case DEFAULT:
				amount = (int) Math.round(multiplier * i / 10 * amount) + 5;
				break;
			case RUSH:
				amount = (int) Math.round(multiplier * 3 * i * amount) + 2;
				break;
			case NO_AI:
				amount = (int) Math.round(multiplier * 4 * amount * i / 2);
				break;
			case SINGLE:
				amount = 1;
				multiplier = 1;
				break;
			case INSANE:
				amount = (int) Math.round(amount * amount * multiplier);
			default:
				break;
		}
		int round = (int) Math.round(amount * multiplier);
//		System.out.println(En.SOUT_INFO + String.format(En.DEBUG_CALCULATED_MOBS, round, p.getName()));
		return round;
	}


	public static void start(Player p, Difficulty d, WaveTypes wt, int amount, int arena, Location l, String waveName) {
		p.playSound(p.getLocation(), Utils.getRandomSound(), 0.6f, 1.5f);
		p.teleport(l, PlayerTeleportEvent.TeleportCause.PLUGIN);
		Sound rs = Utils.getRandomSound();
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			p.playSound(p.getLocation(), rs, 0.5f, 0.8f);
			p.sendMessage(En.WAVES + String.format(En.WAVES_TIME_REMAINING, 3));
			Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
				p.playSound(p.getLocation(), rs, 0.5f, 0.8f);
				p.sendMessage(En.WAVES + String.format(En.WAVES_TIME_REMAINING, 2));
				Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
					p.playSound(p.getLocation(), rs, 0.5f, 0.8f);
					p.sendMessage(En.WAVES + String.format(En.WAVES_TIME_REMAINING, 1));
					Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
						p.playSound(p.getLocation(), rs, 0.5f, 1.2f);
						p.sendMessage(En.WAVES + En.WAVES_START);
						pConfig pc = pConfig.loadConfig(p, "Waves");
						pc.set("Wave.currentWave.mobAmount", amount);
						pc.set("Wave.currentWave.killedYet", 0);
						pc.set("Wave.currentWave.arena", arena);
						pc.set("Wave.currentWave.name", waveName);
						pc.savePCon();
						int a = 0;
						delayedSpawn(l, a, arena, amount, d, wt);
					}, 20);
				}, 20);
			}, 20);
		}, 60);
	}

	public static void delayedSpawn(Location l, int i, int arena, int amount, Difficulty d, WaveTypes wt) {
		if (i > amount) return;

		long delay = switch (d) {
			case EZ, EASY -> 25;
			case NORMAL, PRE_NORMAL, PAST_NORMAL -> 12;
			case HARD, HARDER -> 8;
			case VERY_HARD, DIFFICULT -> 6;
			case INSANE -> 4;
		};

		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			int i1 = i;
			Random r = new Random();
			int a = 98, x = r.nextInt(a), z = r.nextInt(a);
			Location tempEnSpawn = new Location(l.getWorld(),
					l.getX() + x - 49, l.getY(), l.getZ() + z - 49);
			Entity e = Utils.genRandomMob(tempEnSpawn);
			if (e instanceof LivingEntity le)
				switch (wt) {
					case POTION -> {
						le.addPotionEffect(new PotionEffect(genRandomEffectType(), Integer.MAX_VALUE, r.nextInt(7), true, true, true));
						le.addPotionEffect(new PotionEffect(genRandomEffectType(), Integer.MAX_VALUE, r.nextInt(7), true, true, true));
					}
					case SINGLE -> {
						le.setMaxHealth(((LivingEntity) e).getMaxHealth() * 20);
						le.setGlowing(true);
						le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, true, true, true));
						return;
					}
					case NO_AI -> {
						le.setAI(false);
						e.setSilent(true);
						e.setGlowing(true);
					}
					case JUMP -> le.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 10, false, false, false));
					case INSANE -> {
						le.setMaxHealth(le.getMaxHealth() * 1.5);
						le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false, false));
						le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false, false));
					}
					case RUSH -> {
						le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1, false, false, false));
						le.setMaxHealth(le.getMaxHealth() * 0.5);
					}
				}
			FileConfiguration c = Waves.getWaves().getConfig();
			switch (arena) {
				case 0:
					arena0.add(e.getUniqueId().toString());
					c.set("WavesUse.arena" + arena + ".spawnedMobIDs", arena0);
					break;
				case 1:
					arena1.add(e.getUniqueId().toString());
					c.set("WavesUse.arena" + arena + ".spawnedMobIDs", arena1);
					break;
				case 2:
					arena2.add(e.getUniqueId().toString());
					c.set("WavesUse.arena" + arena + ".spawnedMobIDs", arena2);
					break;
				case 3:
					arena3.add(e.getUniqueId().toString());
					c.set("WavesUse.arena" + arena + ".spawnedMobIDs", arena3);
					break;
				default:
					System.out.println(En.SOUT_WARN + String.format(En.UNREGISTERED_ARENA_DELIVERED, arena));
					break;
			}
			Waves.getWaves().saveConfig();
			l.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, tempEnSpawn, 1);
			i1++;
			delayedSpawn(l, i1, arena, amount, d, wt);
		}, delay);
	}

	public static PotionEffectType genRandomEffectType() {
		int max = PotionEffectType.values().length;
		Random r = new Random();
		PotionEffectType pet = PotionEffectType.getById(r.nextInt(max));
		int i = 0;
		while (pet == null) {
			pet = PotionEffectType.getById(r.nextInt(max));
			i++;
			if (i >= 12) {
				System.out.println(En.SOUT_ERROR + En.SOUT_EFFECT_UNGENERATEABLE);
				return PotionEffectType.SATURATION;
			}
		}
		return pet;
	}

}
