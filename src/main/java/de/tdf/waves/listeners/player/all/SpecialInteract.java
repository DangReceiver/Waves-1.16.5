package de.tdf.waves.listeners.player.all;

import de.tdf.helpy.methods.items.IB;
import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.listeners.CM;
import de.tdf.waves.listeners.player.waves.WaveStart;
import de.tdf.waves.methods.enums.*;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SpecialInteract implements Listener {

	@EventHandler
	public void handle(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		if (i == null) return;
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (i.hasItemMeta() && i.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
			String dn = i.getItemMeta().getDisplayName();
			if (!(dn.contains("⟫") && dn.contains("⟪"))) return;
			e.setCancelled(true);
			if (pc.getBoolean("Tutorial.inProgress")) {
				En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.INTERACT, RestrictionType.TUTORIAL);
				return;
			}
			specialInteract(p, i, dn);
		}
	}

	@EventHandler
	public void handle(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		ItemStack i = e.getItemDrop().getItemStack();
		if (i.getType() == Material.AIR) return;
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (i.hasItemMeta() && i.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
			String dn = i.getItemMeta().getDisplayName();
			if (!(dn.contains("⟫") && dn.contains("⟪"))) return;
			e.setCancelled(true);
			if (pc.getBoolean("Tutorial.inProgress")) {
				En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.INTERACT, RestrictionType.TUTORIAL);
				return;
			}
			specialInteract(p, i, dn);
		}
	}

	@EventHandler
	public void handle(PlayerInteractAtEntityEvent e) {
		ItemStack i = e.getPlayer().getInventory().getItemInMainHand();
		if (i.getType() != Material.AIR) {
			if (i.hasItemMeta() && i.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
				e.setCancelled(true);
				Player p = e.getPlayer();
				Inventory c = Bukkit.createInventory(null, 5 * 9, i.getItemMeta().getDisplayName());
				IB.invFiller(c, Waves.filler);
				if (i.equals(CM.identifier))
					p.openInventory(c);
			}
		}
	}

	public void specialInteract(Player p, ItemStack i, String dn) {
		if (i != null && i.hasItemMeta() && i.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
			Inventory c = Bukkit.createInventory(null, 5 * 9, dn);
			IB.invFiller(c, Waves.filler);
			if (i.equals(CM.navi)) {
				c.setItem(c.getSize() / 2, IB.lore(IB.name(new ItemStack(Material.REDSTONE_LAMP),
						En.ITEM_NAME_INV_NAVI_SPAWN), En.ITEM_LORE_INV_NAVI_SPAWN));
				c.setItem(c.getSize() / 2 - 7, IB.lore(IB.name(new ItemStack(Material.PLAYER_HEAD),
						En.ITEM_NAME_INV_NAVI_CITY), En.ITEM_LORE_INV_NAVI_CITY));
				c.setItem(c.getSize() / 2 + 7, IB.lore(IB.name(new ItemStack(Material.PAINTING),
						En.ITEM_NAME_INV_NAVI_SUPPLIES), En.ITEM_LORE_INV_NAVI_SUPPLIES));
				c.setItem(c.getSize() / 2 - 11, IB.lore(IB.name(new ItemStack(Material.TURTLE_HELMET),
						En.ITEM_NAME_INV_NAVI_ARENA), En.ITEM_LORE_INV_NAVI_ARENA));
				c.setItem(c.getSize() / 2 - 21, IB.lore(IB.name(new ItemStack(Material.LEATHER_HELMET),
						En.ITEM_NAME_INV_NAVI_ARENA_SPY), En.ITEM_LORE_INV_NAVI_ARENA_SPY));
				c.setItem(c.getSize() / 2 + 11, IB.lore(IB.name(new ItemStack(Material.DIAMOND_HELMET),
						En.ITEM_NAME_INV_NAVI_WAVES), En.ITEM_LORE_INV_NAVI_DESC_WAVES));
			} else if (i.equals(CM.waveFinder)) {
				if (p.getWorld() != Waves.wavesLoc.getWorld()) {
					p.sendTitle(En.PRE, En.RESTRICTION_USE_IN_WAVES, 5, 50, 20);
					return;
				}
				if (p.getLocation().getX() >= 20 || p.getLocation().getZ() >= 20) {
					p.sendTitle(En.PRE, En.RESTRICTION_TO_FAR_WAVES, 5, 50, 20);
					return;
				}

				pConfig pc = pConfig.loadConfig(p, "Waves");
				if (pc.isSet("Waves.pending")) {
					int count = 8;
					for (String s : pc.getConfigurationSection("Waves.pending").getKeys(false)) {
						if (count >= c.getSize()) return;
						String difficulty = pc.getString("Waves.pending." + s + ".difficulty"),
								waveType = pc.getString("Waves.pending." + s + ".waveType"),
								number = pc.getString("Waves.pending." + s + ".number");
						WaveTypes wt = WaveTypes.valueOf(waveType);
						Difficulty dif = Difficulty.valueOf(difficulty);
						while (!allowedSlot(count, c.getSize())) {
							if (count >= c.getSize()) continue;
							count++;
						}
						if (allowedSlot(count, c.getSize())) {
							Material helmet = getDifMaterial(dif);
							c.setItem(count, IB.lore(IB.name(new ItemStack(helmet),
											String.format("§b§oWave §3#%s §8§l|§e %s", number, s)),
									"§eDifficulty§8: §6" + dif.toString().toLowerCase(),
									"§4Amount of mobs§8: §4" + WaveStart.genAmount(p, dif, wt),
									"§bSpecifics§8: §3" + wt.toString().toLowerCase()));
							count++;
						}
					}
				} else
					c.setItem(c.getSize() / 2, IB.lore(IB.name(new ItemStack(Material.CRIMSON_FUNGUS),
							En.ITEM_NAME_INV_WAVE_FINDER_NO_WAVES), En.ITEM_LORE_INV_WAVE_FINDER_NO_WAVES));

			} else if (i.equals(CM.identifierPart)) {
				if (p.getWorld() != Waves.supplies.getWorld()) {
					p.sendTitle(En.PRE, En.RESTRICTION_USE_FAR_AWAY_SUPPLIES, 5, 50, 20);
					return;
				}
				if (p.getLocation().getY() >= 80) {
					p.sendTitle(En.PRE, En.RESTRICTION_USE_FAR_AWAY_SUPPLIES, 5, 50, 20);
					return;
				}
			} else
				return;
			p.openInventory(c);
		}
	}


	public static boolean allowedSlot(final int i, final int size) {
		return i < size && ((i >= 10 && i < 17) || (i >= 19 && i < 26)
				|| (i >= 28 && i < 35) || (i >= 37 && i < 44));
	}

	public static Material getDifMaterial(Difficulty d) {
		Material m = Material.AIR;
		switch (d) {
			case EZ:
			case EASY:
				m = Material.LEATHER_HELMET;
				break;
			case PRE_NORMAL:
				m = Material.GOLDEN_HELMET;
				break;
			case NORMAL:
			case PAST_NORMAL:
				m = Material.CHAINMAIL_HELMET;
				break;
			case HARDER:
				m = Material.IRON_HELMET;
				break;
			case HARD:
				m = Material.TURTLE_HELMET;
			case VERY_HARD:
				m = Material.DIAMOND_HELMET;
				break;
			case DIFFICULT:
				m = Material.NETHERITE_HELMET;
				break;
			case INSANE:
				m = Material.SHIELD;
				break;
		}

		return m;
	}
}