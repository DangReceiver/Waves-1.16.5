package de.tdf.waves.comamnds;

import de.tdf.helpy.methods.Other;
import de.tdf.helpy.methods.lang.Eng;
import de.tdf.waves.methods.Sb;
import de.tdf.waves.methods.Tl;
import de.tdf.waves.methods.Xp;
import de.tdf.waves.methods.lang.En;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdateVisuals implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		if (sen instanceof Player p) {
			Sb.updateFullScoreBoard(p);
			Tl.updateTlHeader(p);
			Tl.updateTlFooter(p);
			p.sendTitle("", "", 0, 0, 0);
			Other.actionBar(p, "normal", "", 0.1f, 1);
			Xp xp = Xp.load();
			xp.loadPlayer(p);
			p.sendMessage(En.PRE + Eng.CMD_ACTION_CONFIRMED);
		} else
			sen.sendMessage(Eng.CMD_NOT_PLAYER);
		return false;
	}
}
