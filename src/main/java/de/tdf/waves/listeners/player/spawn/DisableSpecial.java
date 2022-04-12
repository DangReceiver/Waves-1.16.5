package de.tdf.waves.listeners.player.spawn;

import de.tdf.waves.listeners.CM;
import de.tdf.waves.methods.enums.DeliverType;
import de.tdf.waves.methods.enums.RestrictionType;
import de.tdf.waves.methods.enums.Restrictions;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DisableSpecial implements Listener {

	@EventHandler
	public void handle(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.SPECTATOR || p.getGameMode() == GameMode.CREATIVE) return;
		if (e.getItemDrop().getItemStack().hasItemMeta() &&
				e.getItemDrop().getItemStack().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
			ItemStack i = e.getItemDrop().getItemStack();
			if (i.getItemMeta().hasDisplayName()) {
				String dn = i.getItemMeta().getDisplayName();
				if (!(dn.contains("⟫") && dn.contains("⟪"))) return;
			}
			e.setCancelled(true);
			En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.DROP, RestrictionType.THIS_ITEM);
			return;
		}
		if (p.getWorld() == Waves.spawn || p.getWorld() == Waves.arena.getWorld()) {
			e.setCancelled(true);
			En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.DROP, RestrictionType.WRONG_LOCATION);
			return;
		}
		if (p.getExpToLevel() <= 9) {
			e.setCancelled(true);
			En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.DROP, RestrictionType.LEVEL_10);
			return;
		}
	}

	@EventHandler
	public void handle(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.SPECTATOR || p.getGameMode() == GameMode.CREATIVE) return;
		if (e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
			e.setCancelled(true);
			En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.BLOCK_PLACE, RestrictionType.HOLDING_THIS);
			return;
		}
		if (p.getWorld() == Waves.spawn || p.getWorld() == Waves.arena.getWorld() && !p.hasPermission("Waves.buildArena")) {
			e.setCancelled(true);
			En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.BLOCK_PLACE, RestrictionType.WRONG_LOCATION);
			return;
		}
	}

	@EventHandler
	public void handle(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.SPECTATOR || p.getGameMode() == GameMode.CREATIVE) return;
		if (p.getInventory().getItemInMainHand().hasItemMeta() &&
				p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
			e.setCancelled(true);
			En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.BLOCK_BREAK, RestrictionType.THIS_ITEM);
			return;
		}
		if (p.getWorld() == Waves.spawn || p.getWorld() == Waves.arena.getWorld()) {
			e.setCancelled(true);
			En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.BLOCK_BREAK, RestrictionType.WRONG_LOCATION);
			return;
		}
	}

	@EventHandler
	public void handle(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_AIR) return;
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.SPECTATOR || p.getGameMode() == GameMode.CREATIVE) return;
		if (p.getInventory().getItemInMainHand().hasItemMeta() &&
				p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.DURABILITY))
			return;
		if (p.getWorld() != Waves.spawn && p.getWorld() != Waves.arena.getWorld() && p.getWorld() != Waves.wavesLoc.getWorld()) {
			e.setCancelled(true);
			if (!p.getInventory().getItemInMainHand().equals(CM.waveFinder))
				En.dynamicRestriction(p, DeliverType.ACTIONBAR, Restrictions.INTERACT, RestrictionType.WRONG_LOCATION);
			return;
		}
	}
}
