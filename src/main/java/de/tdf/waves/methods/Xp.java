package de.tdf.waves.methods;

import de.tdf.helpy.methods.pConfig;
import de.tdf.waves.methods.lang.En;
import de.tdf.waves.waves.Waves;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class Xp {

	private static final String path = "plugins/Waves/XpBalance.yml";

	Player p;

	static pConfig pc;

	YamlConfiguration con;
	File file;

	public YamlConfiguration getConfig() {
		return this.con;
	}

	public File getFile() {
		return this.file;
	}


	int XpLevel;
	long XpPoints;

	public Xp() {
		this.file = new File(path);
		this.con = YamlConfiguration.loadConfiguration(this.file);
	}

	public static Xp load() {
		if (!exists()) create();
		return new Xp();
	}

	@Nullable
	public Player getPlayer() {
		return p;
	}

	public Xp loadPlayer(Player input) {
		if (!exists()) create();
		this.p = input;
		this.pc = pConfig.loadConfig(p, "Waves");
		if (!pc.isSet("Xp.level") || pc.getInt("Xp.level") <= 0) {
			Xp xp = Xp.load();
			if (xp.getXpLevel() <= 0) xp.levelUp();
		}
		XpLevel = pc.getInt("Xp.level");
		XpPoints = pc.getLong("Xp.points");

		return new Xp();
	}

	public static boolean exists() {
		return (new File(path)).exists();
	}

	public boolean balance() {
		if (exists() && this.con.isSet("Logic.levelOne") && this.con.getLong("Logic.levelOne") >= 1) {
			return true;
		} else {
			this.con.set("Logic.levelOne", 9);
			this.con.set("Logic.multiplier", 1.12);
			this.con.set("Logic.multiplierAdd", 3);
			try {
				this.con.save(this.file);
			} catch (IOException e) {
				System.out.println("[Waves] §6There was an error generating the Xp balancing. [1]");
			}
		}
		return false;
	}

	public static Xp create() {
		File file = new File(path);
		if (!file.exists())
			try {
				file.createNewFile();
				if (!file.exists()) {
					System.out.println("[Waves] §6There was an error generating the Xp balancing. [0]");
					return null;
				}
			} catch (IOException e) {
				System.out.println("[Waves] §6There was an error generating the Xp balancing. [0]");
			}
		return new Xp();
	}

	public void levelUp() {
		try {
			if (this.p != null) {
				XpLevel++;
				pc.set("Xp.level", XpLevel);
				pc.set("Xp.points", 0);

				int lev = con.getInt("Logic.levelOne", 10), mulAdd = con.getInt("Logic.multiplierAdd", 8);
				double m = con.getDouble("Logic.multiplier", 0.5) * XpLevel;

				if (!pc.isSet("Xp.customMultiplier") || pc.getDouble("Xp.customMultiplier") <= 0)
					pc.set("Xp.maxPoints", XpLevel * lev * (m + mulAdd / 100));
				else pc.set("Xp.maxPoints", (XpLevel * lev * (m + mulAdd / 100)) * pc.getDouble("Xp.customMultiplier"));
				pc.savePCon();

				p.sendTitle(En.LEVEL_UP, "§2§kk §a§l§oCongrats§8! §7You are §alevel §2" + XpLevel + " §7now §2§kk", 5, 60, 0);
				Bukkit.getScheduler().runTaskLaterAsynchronously(Waves.getWaves(), () -> {
					p.sendTitle(En.LEVEL_UP, "§e§kk §7You will need §e§o" + getMaxXpPoints() + " §ePoints §7to level up now§8. §e§kk ", 0, 50, 15);
				}, 68);
				p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 0.2f, 1.2f);
				p.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 0.66f, 1.15f);
				Sb.updateLevel(p);
				Bukkit.getScheduler().runTaskLaterAsynchronously(Waves.getWaves(), () -> {
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.25f, 1.1f);
					pc.setTokens(pc.getTokens() + XpLevel * 50);
					Sb.updateSbTokens(p);
				}, 25);
			}
		} catch (NullPointerException e) {
			System.out.println("[Waves] §cYou cannot levelUp an undefined player!");
		}
	}

	public long getXpPoints() {
		return XpPoints;
	}

	public long getMaxXpPoints() {
		if (pc != null)
			return pc.getLong("Xp.maxPoints");
		return -1;
	}

	public int getXpLevel() {
		return XpLevel;
	}

	public boolean setXpPoints(long points) {
		if (pc != null) {
			XpPoints = points;
			pc.set("Xp.points", XpPoints);
			if (XpPoints >= getMaxXpPoints())
				levelUp();
			return true;
		}
		return false;
	}

	public boolean addXpPoints(long points) {
		if (pc != null) {
			XpPoints = XpPoints + points;
			if (XpPoints <= 0) XpPoints = 1;
			pc.set("Xp.points", XpPoints);
			if (XpPoints >= getMaxXpPoints()) {
				levelUp();
				return true;
			}
			return true;
		}
		return false;
	}

	public void addXpPoint() {
		XpPoints++;
		if (XpPoints >= getMaxXpPoints()) {
			levelUp();
			return;
		}
		pc.set("Xp.points", XpPoints);
	}

	public boolean addXpLevels(int l) {
		if (p != null) {
			levelUp();
			return true;
		}
		return false;
	}

	public boolean savePlayer() {
		if (p != null && pc != null) {
			pc.savePCon();
			return true;
		}
		return false;
	}
}
