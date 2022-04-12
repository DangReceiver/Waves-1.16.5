package de.tdf.waves.listeners.player.all;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandExecution implements Listener {
	@EventHandler
	public void handle(PlayerCommandPreprocessEvent e) {
		if (e.isCancelled()) return;
		String s = e.getMessage();
		String[] s1 = s.split(" ");
		Player p = e.getPlayer();
		if (s.equalsIgnoreCase("/info")) {
			e.setMessage("/Waves:info");
		} else if (s.contains("/minecraft:") || s.contains("/bukkit:")) {
			if (p.hasPermission("ignore.minecraftCommand")) return;
			e.setCancelled(true);
			p.sendMessage(En.PRE + String.format(En.CMD_EXECUTION_NOT_PERMITTED, s));
			return;
		} else if (s.contains("spawn") && !s.contains("spawner")) {
			pConfig pc = pConfig.loadConfig(p, "Waves");
			e.setCancelled(true);
			if (pc.getBoolean("Tutorial.inProgress") && !p.hasPermission("ignore.tutorialRestriction")) {
				p.sendMessage(En.PRE + String.format(En.CMD_EXECUTION_WHILE_TUTORIAL, s));
				return;
			}
			p.teleport(Waves.spLoc);
			p.playSound(p.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.4f, 0.85f);
		}
//		Command cmd = Bukkit.getServer().getPluginCommand(s);
//		if (cmd == null) {
//			cmd = Waves.getWaves().getCommand(s);
//			if (cmd != null) return;
//			cmd = Helpy.getPlugin().getCommand(s);
//			if (cmd != null) return;
//			if (p.hasPermission("ignore.invalidCommand")) return;
//			p.sendMessage(En.PRE + String.format(En.CMD_EXECUTION_UNWHITELISTED_CMD, s));
//			e.setCancelled(true);
//		}
	}
}
