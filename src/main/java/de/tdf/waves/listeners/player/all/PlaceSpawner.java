package de.tdf.waves.listeners.player.all;

import de.tdf.helpy.methods.lang.Eng;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PlaceSpawner implements Listener {

	public static float yaw;

	public static HashMap<UUID, Material> spawners = new HashMap<>();

	@EventHandler
	public void handle(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		ItemStack i = e.getItemInHand();
		if (i.hasItemMeta() && i.getItemMeta().getLore().toString().contains("Spawner")) {
			if (p.hasPermission("Waves.supplies.setSpawner")) {
				Block b = e.getBlockPlaced();
				String[] s1 = i.getItemMeta().getLore().toString().split("Drop:");
				s1 = s1[1].split(", Head:");
				String[] h1 = i.getItemMeta().getLore().toString().split("Head:");
				h1 = h1[1].split("]");
				Material m = Material.getMaterial(s1[0]),
						h = Material.getMaterial(h1[0]);
				p.sendMessage("§9" + h + " | §7" + m + " | §6" + s1[0] + " | §a" + h1[0]);
				if (h != null)
					h = b.getType();
				ArmorStand a = (ArmorStand) b.getLocation().getWorld().spawnEntity(b.getLocation()
						.add(0.5, 0.2, 0.5), EntityType.ARMOR_STAND);
				a.setHelmet(new ItemStack(h));
				if (a.getHelmet().getType() == Material.AIR) {
					p.sendMessage(En.PRE + En.PLACE_SPAWNER_INVALID_HELMET);
					e.getBlockPlaced().setType(Material.AIR);
					a.remove();
					return;
				}
				a.setInvulnerable(true);
				a.setGravity(false);
				a.setSmall(true);
				a.setMarker(true);
				a.setInvisible(true);
				a.setCustomNameVisible(true);
				a.setCustomName("§eSpawner §8§l| §3Drop§8: §a" + m);
				FileConfiguration c = Waves.getWaves().getConfig();
				c.set("supplies.spawner." + a.getUniqueId() + ".material", m.toString());
				Waves.getWaves().saveConfig();
				spawners.put(a.getUniqueId(), m);
				e.getBlockPlaced().setType(Material.AIR);
			} else
				Eng.permissionShow(p, "Waves.supplies.setSpawner");
		}
	}

	public static void spawnerLoop() {
		FileConfiguration c = Waves.getWaves().getConfig();
		System.out.println(En.SOUT_INFO + "The spawner loop will be started soon.");
		Bukkit.getScheduler().runTaskTimer(Waves.getWaves(), () -> {
			if (c.isSet("supplies.spawner")) {
				for (String s : c.getConfigurationSection("supplies.spawner").getKeys(false)) {
					ArmorStand a = (ArmorStand) Bukkit.getEntity(UUID.fromString(s));
					ItemStack i = new ItemStack(Material.getMaterial(c.getString("supplies.spawner." + s + ".material")));
					a.getWorld().dropItemNaturally(a.getLocation().add(-0.5, 0.2, -0.5), i);
				}
			}
		}, 10 * 20, 12 * 20);

		Bukkit.getScheduler().runTaskTimer(Waves.getWaves(), () -> {
			if (c.isSet("supplies.spawner")) {
				yaw += 45;
				for (String s : c.getConfigurationSection("supplies.spawner").getKeys(false)) {
					ArmorStand a = (ArmorStand) Bukkit.getEntity(UUID.fromString(s));
					if (a == null) {
						c.set("supplies.spawner." + s, null);
						Waves.getWaves().saveConfig();
						System.out.println(En.SOUT_WARN + String.format(En.SOUT_SPAWNER_NOT_FOUND, s));
					} else
						a.setRotation(yaw, 0);
				}
			}
		}, 8 * 20, 3);
	}
}
