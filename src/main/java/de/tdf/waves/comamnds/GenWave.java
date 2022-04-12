package de.tdf.waves.comamnds;

import de.tdf.helpy.methods.lang.Eng;
import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.enums.Difficulty;
import de.tdf.waves.methods.enums.WaveTypes;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class GenWave implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		if (sen instanceof Player p) {
			if (!p.hasPermission("Waves.genWave.self")) {
				Eng.permissionShow(p, "Waves.genWave.self");
				return true;
			}
			pConfig pc = pConfig.loadConfig(p, "Waves");
			String wName = genWaveName();
			if (pc.isSet("Waves.pending." + wName)) return true;
			int i = 0;
			for (String s : pc.getConfigurationSection("Waves.pending").getKeys(false))
				i++;
			int maxPending = pc.isSet("Waves.maxPending") ? pc.getInt("Waves.maxPending") : 0;
			if (i >= 6 + maxPending) {
				p.sendMessage(En.PRE + String.format(En.CMD_GEN_WAVE_OUT_OF_SLOTS, i, 6 + maxPending));
				return true;
			}
			int num = pc.getInt("Waves.pendingLogic.number");
			pc.set("Waves.pendingLogic.number", num + 1);
			num++;
			Random r = new Random();
			Difficulty d = Difficulty.getFromId(r.nextInt(Difficulty.values().length));
			WaveTypes wt = WaveTypes.getFromId(r.nextInt(WaveTypes.values().length));
			pc.set("Waves.pending." + wName + ".difficulty", d.toString());
			pc.set("Waves.pending." + wName + ".waveType", wt.toString());
			pc.set("Waves.pending." + wName + ".number", num);
			pc.savePCon();
			p.sendTitle("", String.format("§7New wave found! ⇢ %s, Difficulty: %s, Type: %s", wName,
					d.toString().toLowerCase(), wt.toString().toLowerCase()), 20, 40, 20);

		}
		return false;
	}

	public static String genWaveName() {
		FileConfiguration c = Waves.getWaves().getConfig();
		List<String> names = null;
		if (c.isSet("Waves.names")) {
			names = c.getStringList("Waves.names");
		} else {
			names = new ArrayList<>(Arrays.asList("Heinrich", "Peter", "Dieter", "Niemand", "Purity",
					"Isabelle", "Reihner", "Maxes", "Polar", "Gem", "Rock", "Break", "Etwas", "Flesh",
					"Verify", "Issue", "Mice", "Fear", "Weich", "Treets", "Villain", "Vanilla"));
			c.set("Waves.names", names);
			Waves.getWaves().saveConfig();
		}
		Random r = new Random();
		return names.get(r.nextInt(names.size()));
	}

	public static void genWaveLoop() {
		System.out.println(En.SOUT_INFO + En.SOUT_GEN_WAVE_LOOP_STARTING);
		Bukkit.getScheduler().runTaskTimer(Waves.getWaves(), () -> {
			Random r = new Random();
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (r.nextInt(5) >= 3) {
					pConfig pc = pConfig.loadConfig(p, "Waves");
					String wName = genWaveName();
					if (pc.isSet("Waves.pending." + wName)) return;
					int i = 0;
					for (String s : pc.getConfigurationSection("Waves.pending").getKeys(false))
						i++;
					int maxPending = pc.isSet("Waves.maxPending") ? pc.getInt("Waves.maxPending") : 0;
					if (i >= 6 + maxPending) {
						p.sendMessage(En.PRE + String.format(En.CMD_GEN_WAVE_OUT_OF_SLOTS, i, 6 + maxPending));
						continue;
					}
					int num = pc.getInt("Waves.pendingLogic.number");
					pc.set("Waves.pendingLogic.number", num + 1);
					num++;
					Difficulty d = Difficulty.getFromId(r.nextInt(Difficulty.values().length));
					WaveTypes wt = WaveTypes.getFromId(r.nextInt(WaveTypes.values().length));
					pc.set("Waves.pending." + wName + ".difficulty", d.toString());
					pc.set("Waves.pending." + wName + ".waveType", wt.toString());
					pc.set("Waves.pending." + wName + ".number", num);
					pc.savePCon();
					p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 0.25f, 0.75f);
					p.sendTitle(En.WAVES, String.format("§7New wave found! ⇢ %s, Difficulty: %s, Type: %s", wName,
							d.toString().toLowerCase(), wt.toString().toLowerCase()), 20, 40, 20);
				}
			}
		}, 20 * 20, 5 * 60 * 20);
	}
}
