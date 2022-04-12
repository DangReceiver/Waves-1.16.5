package de.tdf.waves.listeners;

import de.tdf.helpy.methods.Other;
import de.tdf.helpy.methods.items.Item;
import de.tdf.helpy.methods.items.IB;
import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.listeners.tutorial.Tutorial;
import de.tdf.waves.methods.Sb;
import de.tdf.waves.methods.Tl;
import de.tdf.waves.methods.Xp;
import de.tdf.waves.methods.enums.Difficulty;
import de.tdf.waves.methods.enums.WaveTypes;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CM implements Listener {

	// Strings -> En
	public static ItemStack waveFinder = IB.lore(IB.name(IB.flag(IB.ench(new ItemStack(Material.SOUL_TORCH),
					Enchantment.DURABILITY, 1), ItemFlag.HIDE_ENCHANTS), En.ITEM_NAME_CM_WAVEFINDER),
			En.ITEM_LORE_DESC_WAVEFINDER),
			navi = IB.lore(IB.name(IB.flag(IB.ench(new ItemStack(Material.CRIMSON_SIGN),
							Enchantment.DURABILITY, 1), ItemFlag.HIDE_ENCHANTS), En.ITEM_NAME_CM_NAVI),
					En.ITEM_LORE_DESC_NAVI),
			identifierPart = IB.lore(IB.name(IB.flag(IB.ench(new ItemStack(Material.BLAZE_ROD),
							Enchantment.DURABILITY, 1), ItemFlag.HIDE_ENCHANTS), En.ITEM_NAME_CM_IDENTIFIERPART),
					En.ITEM_LORE_DESC_IDENTIFIER),
			identifier = IB.lore(IB.name(IB.flag(IB.ench(new ItemStack(Material.BREWING_STAND),
							Enchantment.DURABILITY, 1), ItemFlag.HIDE_ENCHANTS), En.ITEM_NAME_CM_IDENTIFIER),
					En.ITEM_LORE_DESC_IDENIFIERPART);

	@EventHandler
	public void handle(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Sb.setDefaultScoreBoard(p);
		Tl.setFullTl(p);
		PlayerInventory i = p.getInventory();
		pConfig pc = pConfig.loadConfig(p, "Waves");
		pc.set("Name", p.getName());
		pc.set("Worlds.currentWorld", p.getWorld().getName());
		pc.savePCon();
		if (!pc.getBoolean("Tutorial.done") && !pc.getBoolean("Tutorial.inProgress") &&
				pc.getInt("Tutorial.progress.part") <= 1) {
			i.setItem(6, identifierPart);
			Tutorial.start(p, false);
			pc.savePCon();
			return;
		}

		if (Item.getItemAmountSpecial(waveFinder, i) < 1 && Item.getItemAmountSpecial(navi, i) < 1 &&
				Item.getItemAmountSpecial(identifier, i) < 1) {
			i.setItem(1, waveFinder);
			i.setItem(4, navi);
			i.setItem(7, identifier);
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 0.4f, 1.1f);
		}

		for (final Player ap : Bukkit.getOnlinePlayers()) {
			Tl.updateTlHeader(ap);
			if (ap != p)
				Other.actionBar(ap, "succes", "The §6player §e" + p.getName() + " §aconnected§8.", 0.25F, 1.25F);
		}
		Bukkit.getScheduler().runTaskLater(Waves.getWaves(), () -> {
			if (p.getWorld() != Bukkit.getWorld("Spawn")) p.teleport(Waves.spLoc);
			p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 0.2f, 1.2f);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false, false));
			i.setHeldItemSlot(4);
		}, 1L);
		Xp xp = Xp.load();
		xp.loadPlayer(p);
		if (xp.getXpLevel() <= 0)
			xp.levelUp();
	}

	@EventHandler
	public void handle(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		for (final Player ap : Bukkit.getOnlinePlayers()) {
			Tl.updateTlHeader(ap);
			if (ap != p)
				Other.actionBar(ap, "neutral", "The §6player §e" + p.getName() + " §6disconnected§8.", 0.25F, 1.25F);
			else
				Other.actionBar(ap, "error", "§a§oSee you soon§8. §d<3", 0.4F, 1.4F);
		}
		pConfig pc = pConfig.loadConfig(p, "Waves");
		if (pc.getString("Worlds.currentWorld") != null) {
			World w = Bukkit.getWorld(pc.getString("Worlds.currentWorld"));
			if (w != Waves.spawn)
				if (!p.hasPermission("Waves.quitWorldChange"))
					p.teleport(Waves.spLoc);
		}
		if (pc.getBoolean("Tutorial.inProgress")) {
			pc.set("Tutorial.inProgress", null);
			pc.set("Tutorial.forcedStop", true);
		}
		pc.savePCon();
	}
}
