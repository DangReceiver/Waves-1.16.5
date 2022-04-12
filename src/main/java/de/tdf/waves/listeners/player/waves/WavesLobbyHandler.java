package de.tdf.waves.listeners.player.waves;

import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.enums.Difficulty;
import de.tdf.waves.methods.enums.WaveTypes;
import de.tdf.waves.methods.events.EnterWavesLobbyEvent;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Locale;

public class WavesLobbyHandler implements Listener {


	//	@EventHandler
	public void handle(EnterWavesLobbyEvent e) {
		Player p = e.getPlayer();
	}

	public static Location cleanArena(Player p, int i) {
		i = 0;
		if (Waves.getWaveArenaLocations() == null || Waves.getWaveArenaLocations().size() <= 0) {
			p.sendMessage(En.PRE + En.WAVES_NO_ARENA_LOCATION);
			return null;
		}
		for (Location l : Waves.getWaveArenaLocations()) {
			if (l != null && l.getNearbyPlayers(51, 20, 51).size() <= 0) {
				for (Entity en : l.getNearbyEntities(51, 20, 51))
					if (en.getType() != EntityType.ARMOR_STAND)
						en.remove();
				l.setPitch(i);
				return l;
			} else
				i++;
		}
		p.sendMessage(String.format(En.WAVES_NONE_FREE, i));
		return null;
	}

	public static void initiate(Player p, Location l, int i, Difficulty pDif, WaveTypes pwType, String waveName) {
		p.playSound(p.getLocation(), Utils.getRandomSound(), 0.5f, 0.5f);
		FileConfiguration c = Waves.getWaves().getConfig();
		c.set("WavesUse.arena" + i + ".playerName", p.getName());
		c.set("WavesUse.arena" + i + ".started", System.currentTimeMillis());
		c.set("WavesUse.arena" + i + ".waveDifficulty", pDif.toString());
		c.set("WavesUse.arena" + i + ".waveType", pwType.toString());
		Waves.getWaves().saveConfig();
		int amount = WaveStart.genAmount(p, pDif, pwType);
		p.sendMessage(En.WAVES + String.format(En.WAVES_WAVE_INFO,
				pDif.toString().toLowerCase(), pwType.toString().toLowerCase(), amount));
		WaveStart.start(p, pDif, pwType, amount, i, l, waveName);
	}
}
