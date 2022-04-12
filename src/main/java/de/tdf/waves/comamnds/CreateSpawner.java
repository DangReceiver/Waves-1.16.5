package de.tdf.waves.comamnds;

import de.tdf.helpy.methods.items.IB;
import de.tdf.helpy.methods.lang.Eng;
import de.tdf.waves.methods.lang.En;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CreateSpawner implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		if (sen instanceof Player p) {
			if (p.hasPermission("Waves.createSpawner")) {
				if (!(args.length == 1 || args.length == 2)) {
					p.sendMessage(En.PRE + String.format(En.ARGS_LENGTH_EXACT, 1));
					return true;
				}
				ItemStack i = p.getInventory().getItemInMainHand();
				if (i.getType() == Material.AIR) {
					p.sendMessage(En.PRE + En.CMD_CREATE_SPAWNER_INHAND_ITEM);
					return true;
				}
				if (i.getType().isBlock()) {
					try {
						Material m = Material.getMaterial(args[0].toUpperCase());
						Material h = i.getType();
						if (args.length == 2)
							h = Material.getMaterial(args[1].toUpperCase());
						if(h == null) {
							p.sendMessage(En.PRE + En.CMD_CREATE_SPAWNER_INVALID_MATERIAL);
							return true;
						}
						p.getInventory().setItem(p.getInventory().getHeldItemSlot(), IB.flag(IB.ench(IB.lore(
								IB.name(new ItemStack(i.getType(), i.getAmount()), "§6§oSpawner"), "Spawner",
								"Drop:" + m, "Head:" + h), Enchantment.ARROW_DAMAGE, 1), ItemFlag.HIDE_ENCHANTS));
						p.sendMessage(Eng.CMD_ACTION_CONFIRMED);
					} catch (Exception e) {
						p.sendMessage(En.PRE + En.CMD_CREATE_SPAWNER_INVALID_MATERIAL);
					}
				} else
					p.sendMessage(En.PRE + En.CMD_CREATE_SPAWNER_NOT_A_BLOCK);
			} else
				Eng.permissionShow(p, "Waves.createSpawner");
		} else
			sen.sendMessage(Eng.CMD_NOT_PLAYER);
		return false;
	}
}