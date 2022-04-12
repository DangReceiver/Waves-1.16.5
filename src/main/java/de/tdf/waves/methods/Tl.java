package de.tdf.waves.methods;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.methods.lang.En;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Tl {
	public static String h = "       §7Welcome to " + En.PRE.replace(": ", "")
			+ "§8!       \n \n §8\u00BB §2Online  \n ";

	public static String f = "\n§8\u00BB §9Current task§8:  \n";

	public static void setFullTl(Player p) {
		pConfig pc = pConfig.loadConfig((OfflinePlayer) p, "Waves");
		String ph = h, pf = f;
		if (Bukkit.getOnlinePlayers().size() <= 0) {
			ph = ph + " §a\u27A5 \u263B §c0§8/§a45";
		} else {
			ph = ph + " §a\u27A5 \u263B " + ph + "§8/§a45";
		}
		pf = f + "    §2\u27A5 \u2692 §2";
		p.setPlayerListHeader(ph);
		p.setPlayerListFooter(pf);
	}

	public static void updateTlHeader(Player p) {
		if (Bukkit.getOnlinePlayers().size() <= 0) {
			p.setPlayerListHeader("§7Welcome to " + En.PRE.replace(": ", "")
					+ "§8!\n  §8\u00BB §2 Online \n \n  §a\u27A5 \u263B §c0§8/§a45");
		} else {
			p.setPlayerListHeader("§7Welcome to " + En.PRE.replace(": ", "")
					+ "§8!\n  §8\u00BB §2 Online \n  §a\u27A5 \u263B §a" +
					Bukkit.getOnlinePlayers().size() + "§8/§a45\n ");
		}
	}

	public static void updateTlFooter(Player p) {
		pConfig pc = pConfig.loadConfig(p, "Waves");
		p.setPlayerListFooter(f + "  §2\u27A5 \u2692 §2");
	}
}
