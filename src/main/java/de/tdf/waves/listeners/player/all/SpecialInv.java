package de.tdf.waves.listeners.player.all;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.listeners.CM;
import de.tdf.waves.listeners.player.waves.WaveStart;
import de.tdf.waves.listeners.player.waves.WavesLobbyHandler;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.enums.Difficulty;
import de.tdf.waves.methods.enums.WaveTypes;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class SpecialInv implements Listener {
	@EventHandler
	public void handle(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player p) {
			Inventory n = e.getClickedInventory(),
					o = p.getOpenInventory().getTopInventory();
			String nt = p.getOpenInventory().getTitle();
			if (n == o) {
				ItemStack i = e.getCurrentItem();
				if (nt.equalsIgnoreCase(CM.navi.getItemMeta().getDisplayName())) {
					e.setCancelled(true);
					if (i == null || i.getType().toString().contains("GLASS_PANE")) return;
					switch (i.getType()) {
						default:
							break;
						case PLAYER_HEAD:
							p.teleport(Waves.city, PlayerTeleportEvent.TeleportCause.PLUGIN);
							break;
						case REDSTONE_LAMP:
							p.teleport(Waves.spLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
							break;
						case PAINTING:
							p.teleport(Waves.supplies, PlayerTeleportEvent.TeleportCause.PLUGIN);
							break;
						case TURTLE_HELMET:
							p.teleport(Waves.arena, PlayerTeleportEvent.TeleportCause.PLUGIN);
							p.removePotionEffect(PotionEffectType.BLINDNESS);
							break;
						case LEATHER_HELMET:
							p.teleport(Waves.tutorialStart.clone().add(0, 10, 0), PlayerTeleportEvent.TeleportCause.PLUGIN);
							break;
						case DIAMOND_HELMET:
							p.teleport(Waves.wavesLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
							break;
					}
					p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 65, 1, false, false, false));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 35, 1, false, false, false));
					p.sendTitle(En.PRE, En.TITLE_TELEPORTED_SUCCESSFULLY, 25, 8, 8);
					p.playSound(p.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 0.25f, 1);
				} else if (nt.equalsIgnoreCase(CM.identifier.getItemMeta().getDisplayName())) {
					e.setCancelled(true);

				} else if (nt.equalsIgnoreCase(CM.waveFinder.getItemMeta().getDisplayName())) {
					e.setCancelled(true);
					if (i == null || i.getType().toString().contains("GLASS_PANE")) return;
					if (i.getType().toString().contains("_HELMET")) {
						String dn = i.getItemMeta().getDisplayName();
						String[] dns = dn.split(" ");
						pConfig pc = pConfig.loadConfig(p, "Waves");
						if (dns[3] != null) {
							if (pc.isSet("Waves.pending." + dns[3])) {
								Difficulty d = Difficulty.valueOf(pc.getString("Waves.pending." + dns[3] + ".difficulty"));
								WaveTypes wt = WaveTypes.valueOf(pc.getString("Waves.pending." + dns[3] + ".waveType"));
								if (d != null && wt != null) {
									Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
										int in = -1;
										Location l = WavesLobbyHandler.cleanArena(p, in);
										in = (int) Objects.requireNonNull(l).getPitch();
										if (l == null || in <= -1) {
											p.sendMessage(String.format(En.WAVES_NONE_FREE, in));
											return;
										}
										p.sendMessage(En.WAVES_ARENA_FOUND + String.format(En.WAVES_FREE_ARENA_TELEPORT, in));
										p.playSound(p.getLocation(), Utils.getRandomSound(), 0.5f, 1);
										WavesLobbyHandler.initiate(p, l, in, d, wt, dns[3]);
										p.closeInventory();
									}, 5);
								}
							}
						}
					}
				}
			}
		}
	}
}
