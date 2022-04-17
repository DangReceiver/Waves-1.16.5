package de.tdf.waves.waves;

import de.tdf.helpy.methods.items.IB;
import de.tdf.helpy.methods.worldGenerator.UseVoid;
import de.tdf.waves.comamnds.*;
import de.tdf.waves.listeners.CM;
import de.tdf.waves.listeners.player.all.*;
import de.tdf.waves.listeners.player.arena.BlockDestruction;
import de.tdf.waves.listeners.player.arena.EntityKill;
import de.tdf.waves.listeners.player.arena.EntitySpawnLoop;
import de.tdf.waves.listeners.player.spawn.*;
import de.tdf.waves.listeners.player.supplies.BlockBreak;
import de.tdf.waves.listeners.player.waves.EntityProcess;
import de.tdf.waves.listeners.player.waves.WavesLobbyHandler;
import de.tdf.waves.methods.Sb;
import de.tdf.waves.methods.Utils;
import de.tdf.waves.methods.Xp;
import de.tdf.waves.methods.lang.En;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.*;

public final class Waves extends JavaPlugin {

	private static Waves waves;
	public static String VERSION = "";

	public static World spawn;
	public static Location spLoc, city, supplies, arena, tutorialStart, wavesLoc;

	public static ItemStack filler = IB.name(IB.flag(IB.ench(new ItemStack(Material.CYAN_STAINED_GLASS_PANE),
			Enchantment.DURABILITY, 1), ItemFlag.HIDE_ENCHANTS), " §0 ");

	@Override
	public void onEnable() {
		waves = this;
		VERSION = waves.getVersion();
		FileConfiguration c = getConfig();
		Xp xp = Xp.load();
		if (Xp.exists() || !xp.getConfig().isSet("Logic.levelOne") || xp.getConfig().getInt("Logic.levelOne") <= 0)
			xp.balance();
		for (Player p : Bukkit.getOnlinePlayers())
			Sb.setDefaultScoreBoard(p);

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CM(), this);
		pm.registerEvents(new Death(), this);
		pm.registerEvents(new DisableSpecial(), this);
		pm.registerEvents(new FallDamage(), this);
		pm.registerEvents(new de.tdf.waves.listeners.tutorial.Tutorial(), this);
		pm.registerEvents(new SpecialInteract(), this);
		pm.registerEvents(new SpecialInv(), this);
		pm.registerEvents(new WorldChange(), this);
		pm.registerEvents(new Sneak(), this);
		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new ArmorChange(), this);
		pm.registerEvents(new PlaceSpawner(), this);
		pm.registerEvents(new CommandExecution(), this);
		pm.registerEvents(new EntityKill(), this);
		pm.registerEvents(new BlockDestruction(), this);
		pm.registerEvents(new WavesLobbyHandler(), this);
		pm.registerEvents(new EntityProcess(), this);
//		pm.registerEvents(new (), this);
//		pm.registerEvents(new (), this);

		getCommand("Info").setExecutor(new Info());
		getCommand("Tutorial").setExecutor(new Tutorial());
		getCommand("UpdateVisuals").setExecutor(new UpdateVisuals());
		getCommand("Xp").setExecutor(new de.tdf.waves.comamnds.Xp());
		getCommand("KillSpawner").setExecutor(new KillSpawner());
		getCommand("CreateSpawner").setExecutor(new CreateSpawner());
		getCommand("EntityAmount").setExecutor(new EntityAmount());
		getCommand("GenWave").setExecutor(new GenWave());
//		getCommand("").setExecutor(new ());
//		getCommand("").setExecutor(new ());

		if (!c.isSet("Arena.allowedMobs")) {
			List<String> list = new ArrayList<>(Arrays.asList("ZOMBIE", "BAT", "CREEPER", "BLAZE", "CAVE_SPIDER",
					"DROWNED", "ENDERMAN", "EVOKER", "EVOKER_FANGS", "ENDERMITE", "GHAST", "GIANT", "GUARDIAN",
					"ILLUSIONER", "MAGMA_CUBE", "PILLAGER", "RAVAGER", "SHULKER", "SILVERFISH", "SKELETON",
					"SLIME", "SPIDER", "STRAY", "VEX", "VINDICATOR", "WITCH", "WITHER_SKELETON"));
			c.set("Arena.allowedMobs", list);
			saveConfig();
		}
		for (String set : EntitySpawnLoop.wlMonstersAsStrings)
			EntitySpawnLoop.wlMonsterList.add(EntityType.fromName(set));
		genLocs();
	}

	@Override
	public void onDisable() {
		Sb.unsetAllScoreboards();
		FileConfiguration c = getConfig();
		if (c.isSet("supplies.spawner")) {
			for (String s : c.getConfigurationSection("supplies.spawner").getKeys(false)) {
				ArmorStand a = (ArmorStand) Bukkit.getEntity(UUID.fromString(s));
				if (a == null) return;
				a.setRotation(0, 0);
				for (Entity e : a.getLocation().getNearbyEntities(6, 12, 6))
					if (e.getType() == EntityType.DROPPED_ITEM) e.remove();
			}
		}
	}

	public static Waves getWaves() {
		return waves;
	}

	public synchronized String getVersion() {
		String v = null;
		try {
			Properties p = new Properties();
			InputStream is = getClass().getResourceAsStream("/META-INF/maven/de.tdf/Waves/pom.properties");
			if (is != null) {
				p.load(is);
				v = p.getProperty("version", "");
			}
		} catch (Exception ignored) {
		}
		if (v == null) {
			Package ap = getClass().getPackage();
			if (ap != null) {
				v = ap.getImplementationVersion();
				if (v == null)
					v = ap.getSpecificationVersion();
			}
		}
		return v;
	}

	@Nullable
	public static List<Location> getWaveArenaLocations() {
		List<Location> l = new ArrayList<>();
		FileConfiguration c = waves.getConfig();
		if (c.getConfigurationSection("WaveArenas") == null) return null;
		for (String s : c.getConfigurationSection("WaveArenas").getKeys(false)) {
			Location tempLoc = c.getLocation("WaveArenas." + s);
			if (tempLoc != null && tempLoc.getWorld() != null)
				l.add(tempLoc);
			else
				System.out.println(En.SOUT_WARN + String.format(En.SOUT_WAVES_LOCATIONS_INVALID, s));
		}
		return l;
	}

	public void genLocs() {
		List<Location> wl = new ArrayList<>();
		FileConfiguration c = getConfig();
		spawn = Bukkit.getWorld("Spawn");
		Collections.addAll(wl, spLoc, supplies, city, arena);
		List<String> invalidWorlds = new ArrayList<>();
		if (spawn == null) {
			spawn = Bukkit.getWorld("Spawn");
			if (spawn == null)
				invalidWorlds.add("Spawn");
		}
		if (Bukkit.getWorld("Supplies") == null)
			invalidWorlds.add("Supplies");
		if (Bukkit.getWorld("City") == null)
			invalidWorlds.add("City");
		if (Bukkit.getWorld("Arena") == null)
			UseVoid.createVoidWorld("Arena", true);
		if (Bukkit.getWorld("Waves") == null)
			invalidWorlds.add("Waves");
		if (!invalidWorlds.isEmpty())
			for (String s : invalidWorlds) {
				UseVoid.createVoidWorld(s, true);
				System.out.println(En.SOUT_INFO + String.format("The world \"%s\" will now be created.%n", s));
			}
		Waves.getWaves().saveConfig();
		if (!c.isSet("Teleports") && spawn != null && (!c.isSet("Teleports.Spawn") || !c.isSet("Teleports.City"))) {
			spLoc = new Location(spawn, 0.5D, 100.1D, 0.5D);
			System.out.println(" " + spawn.getName() + " | " + spLoc.getWorld().getName());
			city = new Location(Bukkit.getWorld("City"), 0.5D, 80.1D, 0.5D);
			supplies = new Location(Bukkit.getWorld("Supplies"), 0.5D, 80.1D, 0.5D);
			arena = new Location(Bukkit.getWorld("Arena"), 0.5D, 80.1D, 0.5D);
			tutorialStart = new Location(Bukkit.getWorld("Arena"), 0.5D, 110.1D, 0.5D);
			wavesLoc = new Location(Bukkit.getWorld("Waves"), 0.5D, 80.1D, 0.5D);
			c.set("Teleports.Spawn", spLoc);
			c.set("Teleports.City", city);
			c.set("Teleports.Supplies", supplies);
			c.set("Teleports.Arena", arena);
			c.set("Teleports.Waves", wavesLoc);
			c.set("Teleports.tutorial.start", tutorialStart);
			Waves.getWaves().saveConfig();
		} else {
			spLoc = c.getLocation("Teleports.Spawn");
			city = c.getLocation("Teleports.City");
			supplies = c.getLocation("Teleports.Supplies");
			arena = c.getLocation("Teleports.Arena");
			tutorialStart = c.getLocation("Teleports.tutorial.start");
			wavesLoc = c.getLocation("Teleports.Waves");
		}
		Utils.genSoundList();
		Location spLocC = spLoc.clone();
		spLocC.setY(spLoc.getY() - 1);
		if (spLocC.getBlock().getType() == Material.AIR && spawn.getBlockAt(spLocC.set(spLocC.getX(),
				spLocC.getY() - 1, spLocC.getZ())).getType() == Material.AIR) {
			spLocC.setY(spLoc.getY() - 1);
			if (spLocC.getBlock().getType() == Material.AIR)
				System.out.println(En.SOUT_WARN + "The spawn point is most likely insecure!\n ");
		}
		loadSpawners();
	}

	public void loadSpawners() {
		FileConfiguration c = getConfig();
		HashMap<UUID, Material> sp = new HashMap<>();
		if (c.isSet("supplies.spawner")) {
			for (String s : c.getConfigurationSection("supplies.spawner").getKeys(false)) {
				Material m = Material.getMaterial(c.getString("supplies.spawner." + s + ".material"));
				if (m != null) sp.put(UUID.fromString(s), m);
			}
			PlaceSpawner.spawners = sp;
		}
		EntitySpawnLoop.entitySpawnLoop();
		PlaceSpawner.spawnerLoop();
		GenWave.genWaveLoop();
	}
}
