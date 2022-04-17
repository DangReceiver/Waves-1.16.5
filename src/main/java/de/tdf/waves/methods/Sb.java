package de.tdf.waves.methods;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.methods.enums.Priority;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Sb {

	public static void setDefaultScoreBoard(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective(En.PRE.replace(": ", ""), "abc", En.PRE
				.replace(": ", "") + "      ");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score space = obj.getScore("§0 "), space1 = obj.getScore(" §0"), space2 = obj.getScore(" §0 "),
				space3 = obj.getScore("  §0  "), space4 = obj.getScore(" §0  "),
				obb = obj.getScore("  §8\u00BB §aLevel"),
				world = obj.getScore("  §8\u00BB §3Current world"),
				tokens = obj.getScore("  §8\u00BB §6Tokens"),
				message = obj.getScore("  §8⚠ §cMessage");
		space.setScore(17);
		world.setScore(16);
		space1.setScore(14);
		tokens.setScore(13);
		space2.setScore(11);
		obb.setScore(10);
		space3.setScore(8);
		message.setScore(7);
		space4.setScore(5);

		Team dObb = sb.registerNewTeam("dXp"),
				dWorld = sb.registerNewTeam("dWorld"),
				dTokens = sb.registerNewTeam("dTokens"),
				dMessage = sb.registerNewTeam("dMessage");
		dWorld.addEntry("" + ChatColor.AQUA);
		dTokens.addEntry("" + ChatColor.YELLOW);
		dObb.addEntry("" + ChatColor.DARK_RED);
		dMessage.addEntry("" + ChatColor.BLACK);
		pConfig pc = pConfig.loadConfig(p, "Waves");
		dWorld.setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
		dTokens.setPrefix("    §e➥ \u26C3 " + pc.getTokens());
		dObb.setPrefix("    §2➥ \u2692 §2");
		dMessage.setPrefix("    §5➥ ");
		obj.getScore("" + ChatColor.AQUA).setScore(15);
		obj.getScore("" + ChatColor.YELLOW).setScore(12);
		obj.getScore("" + ChatColor.DARK_RED).setScore(9);
		obj.getScore("" + ChatColor.BLACK).setScore(6);
		p.setScoreboard(sb);
		updateLevel(p);
	}

	public static void unsetAllScoreboards() {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective(En.PRE.replace(": ", ""), "abc", En.PRE
				.replace(": ", "") + "      ");
		for (Player p : Bukkit.getOnlinePlayers())
			p.setScoreboard(sb);
	}

	public static void unsetScoreboard(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective(En.PRE.replace(": ", ""), "abc", En.PRE
				.replace(": ", "") + "      ");
		p.setScoreboard(sb);
	}

	public static void updateFullScoreBoard(Player p) {
		Scoreboard sb = p.getScoreboard();
		updateLevel(p);
		sb.getTeam("dWorld").setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
		sb.getTeam("dTokens").setPrefix("    §e➥ \u26C3 " + pConfig.loadConfig(p, "Waves").getTokens());
		sbMessage(p, null, Priority.LOWEST, 3);
	}

	public static void updateLevel(Player p) {
		Scoreboard sb = p.getScoreboard();
		Xp xp = new Xp();
		xp.loadPlayer(p);
		String display = pointsToVisual(p, xp);
		sb.getTeam("dXp").setPrefix("    §2➥ \u2692 §2" + xp.getXpLevel() + " §8§l| §a" + display);
	}

	public static String pointsToVisual(Player p, Xp xp) {
		long max = xp.getMaxXpPoints(),
				points = xp.getXpPoints();
		if (max <= 0 && points <= 0 && xp.getXpLevel() <= 0) {
			xp.levelUp();
			max = xp.getMaxXpPoints();
		}
		double per = (Math.round(10 * points / max));
		int ten = 10;
		StringBuilder dp = new StringBuilder("§a");
		for (int i = 1; i <= per; i++) {
			dp.append("|");
			ten--;
		}
		dp.append("§c");
		for (int in = 1; in <= ten; ten--) {
			dp.append("|");
			if (ten <= 0) return dp.toString();
		}
		return dp.toString();
	}

	public static void updateSbWorld(Player p) {
		Scoreboard sb = p.getScoreboard();
		sb.getTeam("dWorld").setPrefix("    §b➥ \u06E9 " + p.getWorld().getName());
	}

	public static void updateSbTokens(Player p) {
		Scoreboard sb = p.getScoreboard();
		sb.getTeam("dTokens").setPrefix("    §e➥ \u26C3 " + pConfig.loadConfig(p, "Waves").getTokens());
	}

	public static void sbMessage(Player p, String message, Priority e, long showDurationSec) {
		Scoreboard sb = p.getScoreboard();
		if (message == null) {
			sb.getTeam("dMessage").setPrefix("    §6➥ §9This seems to work ^^");
			Bukkit.getScheduler().runTaskLaterAsynchronously(Waves.getWaves(), () -> {
				sb.getTeam("dMessage").setPrefix("    §6➥ ");
			}, 20 * 3);
			return;
		}
		String col = "§5⚠ §4";
		switch (e) {
			case HIGH:
				col = "§4⚠ §c";
			case MEDIUM:
				col = "§c⚠ §6";
			case LOW:
				col = "§6⚠ §e";
			case LOWEST:
				col = "§e⚠ §a";
		}
		sb.getTeam("dMessage").setPrefix("    §6➥ " + col + message);
		Bukkit.getScheduler().runTaskLaterAsynchronously(Waves.getWaves(), () -> {
			sb.getTeam("dMessage").setPrefix("    §6➥ ");
		}, 20 * showDurationSec);
	}
}