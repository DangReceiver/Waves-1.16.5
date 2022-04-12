package de.tdf.waves.listeners.tutorial;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.listeners.player.spawn.FallDamage;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Tutorial implements Listener {

	public static boolean start(Player p, boolean force) {
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (!force)
			if (pc.getBoolean("Tutorial.done")) {
				p.sendTitle(En.TUTORIAL_ERROR_START_INTERRUPT, En.TUTORIAL_ERROR_PLAYED_ALREADY, 2, 40, 20);
				return false;
			} else if (pc.getBoolean("Tutorial.inProgress")) {
				p.sendTitle(En.TUTORIAL_ERROR_START_INTERRUPT, En.TUTORIAL_ERROR_STARTED_ALREADY, 2, 40, 20);
				return false;
			} else if (pc.getInt("Tutorial.progress.part") >= 2) {
				switch (pc.getInt("Tutorial.progress.part")) {
					case 2:
						announceThrowIn(p, Waves.tutorialStart.clone(), false);
						break;
					case 3:
						p.sendMessage(En.TUTORIAL_STARTOFF_ARENA_5);
						break;
					case 4:
					default:
						p.sendMessage(En.TUTORIAL + "This way is not implemented yet§8. [§9"
								+ pc.getInt("Tutorial.progress.part") + "§8]");
						break;
				}
				return false;
			}
		pc.set("Tutorial.inProgress", true);
		pc.set("Tutorial.progress.part", 1);
		pc.savePCon();
		StringBuilder sb = new StringBuilder("§aStarting tutorial§8");
		p.sendTitle(En.PRE, sb.toString(), 4, 25, 0);
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			p.sendTitle(En.PRE, sb.append(".").toString(), 0, 25, 0);
			p.setGameMode(GameMode.SURVIVAL);
			Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
				p.sendTitle(En.PRE, sb.append(".").toString(), 0, 25, 0);
				Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
					p.sendTitle(En.PRE, sb.append(".").toString(), 0, 25, 0);
					Location l = Waves.tutorialStart.clone();
					l.clone().add(0, -1, 0).getBlock().setType(Material.BARRIER);
					p.teleport(l);
					p.sendMessage(En.TITLE_TELEPORTED_SUCCESSFULLY);
					p.playSound(p.getLocation(), Utils.getRandomSound(), 0.5f, 0.8f);
					p.sendMessage(En.TUTORIAL_STARTOFF_AREA);
					p.sendTitle(En.TUTORIAL, En.TUTORIAL_TIP_LOOK_DOWN, 8, 40, 15);
					Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
						p.playSound(p.getLocation(), Utils.getRandomSound(), 0.5f, 0.8f);
						p.sendMessage(En.TUTORIAL_STARTOFF_ARENA_2);
						Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
							p.playSound(p.getLocation(), Utils.getRandomSound(), 0.5f, 0.8f);
							p.sendMessage(En.TUTORIAL_STARTOFF_ARENA_3);
							Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
								extPreFightPart(p, l);
								p.sendMessage(En.TUTORIAL_STARTOFF_ARENA_4);
								Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
									announceThrowIn(p, l, true);
								}, 3 * 20);
							}, 9 * 20);
						}, 6 * 20);
					}, 6 * 20);
				}, 15);
			}, 15);
		}, 15);
		return true;
	}

	public static void announceThrowIn(Player p, Location l, boolean preTutorial) {
		if (!p.isOnline()) return;
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (!preTutorial) {
			p.getInventory().clear();
			extPreFightPart(p, l);
			pc.set("Tutorial.inProgress", true);
		}
		pc.set("Tutorial.progress.part", 2);
		pc.savePCon();
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			p.playSound(p.getLocation(), Utils.getRandomSound(), 0.5f, 1);
			l.clone().add(0, -1, 0).getBlock().setType(Material.YELLOW_STAINED_GLASS);
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 0.5f, 0.8f);
			p.setSaturation(19);
			Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
				p.playSound(p.getLocation(), Utils.getRandomSound(), 0.5f, 1);
				l.clone().add(0, -1, 0).getBlock().setType(Material.LIME_STAINED_GLASS);
				PlayerInventory i = p.getInventory();
				i.setBoots(new ItemStack(Material.GOLDEN_BOOTS));
				i.setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
				i.setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
				i.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 0.5f, 0.8f);
				Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
					p.playSound(p.getLocation(), Utils.getRandomSound(), 0.75f, 1.25f);
					p.sendTitle(En.TUTORIAL, En.TUTORIAL_GOOD_LUCK, 2, 20, 20);
					l.clone().add(0, -1, 0).getBlock().setType(Material.AIR);
					pc.set("Tutorial.progress.arena", true);
					pc.set("Tutorial.progress.part", 3);
					pc.savePCon();
					Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
						l.clone().add(-1, 0, 0).getBlock().setType(Material.AIR);
						l.clone().add(1, 0, 0).getBlock().setType(Material.AIR);
						l.clone().add(0, 0, -1).getBlock().setType(Material.AIR);
						l.clone().add(0, 0, 1).getBlock().setType(Material.AIR);
						pc.set("Tutorial.inProgress", false);
						pc.set("Tutorial.task.killAMob", true);
						pc.savePCon();
						l.clone().add(0, -1, 0).getBlock().setType(Material.BARRIER);
					}, 25);
				}, 20);
			}, 20);
		}, 20);
	}

	private static void extPreFightPart(Player p, Location l) {
		l.setYaw(0);
		l.setPitch(90);
		p.teleport(l);
		p.playSound(p.getLocation(), Utils.getRandomSound(), 0.5f, 0.8f);
		p.sendMessage(En.TUTORIAL_STARTOFF_ARENA_5);
		l.clone().add(0, -1, 0).getBlock().setType(Material.RED_STAINED_GLASS);
		l.clone().add(-1, 0, 0).getBlock().setType(Material.BARRIER);
		l.clone().add(1, 0, 0).getBlock().setType(Material.BARRIER);
		l.clone().add(0, 0, -1).getBlock().setType(Material.BARRIER);
		l.clone().add(0, 0, 1).getBlock().setType(Material.BARRIER);
		PlayerInventory i = p.getInventory();
		i.addItem(new ItemStack(Material.BOW, 1, (short) 320));
		i.addItem(new ItemStack(Material.GOLDEN_SWORD));
		i.addItem(new ItemStack(Material.DRIED_KELP, 24));
		i.addItem(new ItemStack(Material.ARROW, 24));
	}
}
