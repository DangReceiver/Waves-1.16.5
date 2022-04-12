package de.tdf.waves.listeners.player.spawn;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.methods.Sb;
import de.tdf.waves.methods.Tl;
import de.tdf.waves.methods.events.EnterWavesLobbyEvent;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WorldChange implements Listener {

	@EventHandler
	public void handle(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		Sb.updateSbWorld(p);
		Tl.setFullTl(p);
		World w = e.getPlayer().getWorld();
		if (w == Bukkit.getWorld("world")) {
			System.out.println(En.SOUT_INFO + String.format(En.SOUT_TELEPORT_CORRECTION, w.getName(), Waves.spLoc.getWorld()));
			p.teleport(Waves.spLoc);
		}
		if (w == Waves.spawn) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false, false));
		} else
			p.removePotionEffect(PotionEffectType.SATURATION);
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (w == Waves.wavesLoc.getWorld()) {
//			Are the waves station already in use?
//			    nope:
			Bukkit.getPluginManager().callEvent(new EnterWavesLobbyEvent(p, e.getFrom(), Bukkit.getWorld(pc.getString("Worlds.lastWorld"))));

		}
		pc.set("Worlds.lastWorld", e.getFrom().getName());
		pc.set("Worlds.currentWorld", p.getWorld().getName());
		pc.savePCon();
	}
}
