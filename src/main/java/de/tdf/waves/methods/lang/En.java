package de.tdf.waves.methods.lang;

import de.tdf.helpy.methods.Other;
import de.tdf.waves.methods.Sb;
import de.tdf.waves.methods.enums.DeliverType;
import de.tdf.waves.methods.enums.Priority;
import de.tdf.waves.methods.enums.RestrictionType;
import de.tdf.waves.methods.enums.Restrictions;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class En {

	public static String PRE = "§b§oWaves§8: §7", AUTHOR = "§3§o§nTearsDontFall", TEST_OUTPUT = "§6You smell rly bad §c>:c",
			LEVEL_UP = "§2§lLevel Up!", TUTORIAL = "§9§lTutorial §8[§bW§8]: §7", WAVES = "§8[§e§kkk§8] §b§lWaves §8[§e§kkk§8]",
			CONGRATS = "§2§kk §a§l§oCongrats§8! §2§kk§7 ";

	public static String SOUT_INFO = "§8[§9Info§8] ‖ §bWaves §8| §9", SOUT_WARN = "§8[§6Warn§8] ‖ §bWaves §8| §6",
			SOUT_ERROR = "§8[§cError§8] ‖ §bWaves §8| §c",
			SOUT_ENTITY_SPAWN_LOOP_STARTING = "The entity spawn loop will be started soon.",
			SOUT_ENTITY_SPAWN_UNGENERATEABLE = "Generating new zombie, another EntityType could not be generated.",
			SOUT_EFFECT_UNGENERATEABLE = "Generating effect \"blindness\", another type could not be generated.",
			ERROR_SAVE_PLAYER_FILE_XP_TASK = "A player file could not be saved (xp point task)",
			SOUT_SPAWNER_NOT_FOUND = "Armor Stand Spawner with the §buuid §9%s §6was removed, since it was not found.",
			SOUT_WAVES_LOCATIONS_INVALID = "A location for a Waves arena is wrong! Check: %s",
			UNREGISTERED_ARENA_DELIVERED = "Could not find arena §9%s §7to spawn mobs in§8!",
			DEBUG_CALCULATED_MOBS = "Calculated %s mobs for the player %s.",
			SOUT_TELEPORT_CORRECTION = "Teleport will be corrected: from world %s to world %s",
			SOUT_GEN_WAVE_LOOP_STARTING = "The Gen-Wave loop will be started soon.";

	public static String AUTHOR_PRE = "§3Author§8: ", VERSION_PRE = "§6Version§8: §e",
			YOU_PLAYED_YOURSELF = "You literally just entered §oyour own §7name§8...";

	public static String WAVES_NONE_FREE = "None of the %s Arenas is currently free§8.\n§7Please try again later§8.",
			WAVES_FREE_ARENA_TELEPORT = " §7Arena §b%s §7is free§8. §7You will be teleported§8.",
			WAVES_ARENA_FOUND = "§b§oArena Found§8!", WAVES_START = "  §7Let the §b§owaves §7begin§8!",
			WAVES_NO_ARENA_LOCATION = "No Waves Arena could be found. If this problem resists, please contact a team member.",
			WAVES_TIME_REMAINING = "  §e%s §6seconds §7until the fight §ebegins§8!",
			WAVES_WAVE_INFO = "\n \n§7Starting wave with§8:" +
					"\n §eDifficulty§8:§6 %s \n §cMob amount§8: §4%s \n §bWave specifics§8: §3%s";

	public static String HOLO_XP_ADD = "§e+%s §aXp", HOLO_RECENTLY_SPAWNED = "§7I just spawned",
			HOLO_NO_FALL_DAMAGE = "§7§oDamage suppressed";

	public static String CMD_CREATE_SPAWNER_INHAND_ITEM = "The §3in-hand §7item must be a block§8!",
			CMD_CREATE_SPAWNER_INVALID_MATERIAL = "That §cMaterial§7 is invalid§8!",
			CMD_CREATE_SPAWNER_NOT_A_BLOCK = "This item is not a §9block§8!",
			CMD_REMOVE_SPAWNER_NONE_REMOVED = "There were §cno §7spawners to be removed§8.",
			PLACE_SPAWNER_INVALID_HELMET = "The used block cannot be set as a helmet§8.",
			SPECIFY_LEVEL = "§2Level§8: §2%s", SPECIFY_POINTS = "§aPoints §8/ §6Max§8: §a%s §8/ §6%s",
			CMD_GEN_WAVE_OUT_OF_SLOTS = "You already are full of pending Wave slots§8. " +
					"§7Upgrade§8, §7or play them§8. [§e%s§8/§6%s§8]";

	public static String CMD_EXECUTION_UNWHITELISTED_CMD = "The command§8. §6\"%s\" §7is §cnot §7valid§8.",
			CMD_EXECUTION_WHILE_TUTORIAL = "You are §cnot §7allowed to execute this command§8," +
					" §7during the tutorial§8! [§6\"%s\"§8].",
			CMD_EXECUTION_NOT_PERMITTED = "You are §cnot §7allowed to execute this command§8. [§6\"%s\"§8].";

	public static String CMD_TOTAL_ENTITY_AMOUNT = "Total entity amount§8: §e%s",
			CMD_WL_ENTITY_AMOUNT = "Whitelisted §cmob §7amount§8: §6%s",
			CMD_REMOVE_SPAWNER_SUCCESS = "Successfully removed §c%s §7spawner§8(§7s§8).";

	public static String TUTORIAL_ERROR_START_INTERRUPT = "§cCouldn't start the tutorial!",
			TUTORIAL_ERROR_STARTED_ALREADY = "§7It is already in progress.",
			TUTORIAL_ERROR_PLAYED_ALREADY = "You played / attempted the tutorial already.",
			TUTORIAL_CONCLUSIVE_INTERRUPTION = "There was an error starting the tutorial§8." +
					" §7If this problem resists, please contact a team member§8.";

	public static String TUTORIAL_TIP_HOWTO_START = "Start the tutorial with ''/tutorial''!",
			TUTORIAL_MAN_RESET_TARGET = "The target's tutorial progress was reset.",
			TUTORIAL_PRE_INV_CLEAR = "Your inventory was cleared§8, §7due to the tutorial§8.",
			TUTORIAL_STARTOFF_AREA = TUTORIAL + "Welcome to the arena§8! §7This is the most used§8," +
					" §7most efficient and the main way to progress§8.",
			TUTORIAL_STARTOFF_ARENA_2 = TUTORIAL + "In here you will be killing §8(§7§oor farming§8)§7 mobs§8." +
					"\n§7Their drops create your way to progress at the beginning§8.",
			TUTORIAL_STARTOFF_ARENA_3 = TUTORIAL + "Later, you may partially automize several parts here§8." +
					"\n§7To learn more about it, refer to the tutorial to a later time§8.",
			TUTORIAL_STARTOFF_ARENA_4 = TUTORIAL + "in order to prepare you, you will now be set to fight§8.\n",
			TUTORIAL_STARTOFF_ARENA_5 = TUTORIAL + "§eYour first quest is to kill a mob§8!§7\n" +
					"§oDon't forget that you lose your items when dying§8.",
			TUTORIAL_TIP_LOOK_DOWN = "§e§o§nLook downwards!",
			TUTORIAL_GOOD_LUCK = "§2§oGood luck§l!",
			TUTORIAL_PART_TASK_FINISHED = CONGRATS + "You just finished your current task§8.",
			TUTORIAL_SOFTEND_ARENA_1 = "Since you now finished your task§8, §7keep on fighting for a time to farm resources§8.",
			TUTORIAL_SOFTEND_ARENA_2 = "To support you§8, §7I even gave you an new sword§8. ^^";

	public static String TITLE_TELEPORTED_SUCCESSFULLY = "§7You were teleported successfully.";

	public static String RESTRICTION_USE_NEAR_SPAWN = "§7You may only use this item near spawn§8.",
			RESTRICTION_USE_FAR_AWAY_SUPPLIES = "§7This can only be used in the area outer the world supplies",
			RESTRICTION_USE_IN_CITY = "§7You may only use this item in the city§8.",
			RESTRICTION_TO_FAR_CITY = "§7You should be closer to the city spawn to use this item§8.",
			RESTRICTION_TO_FAR_WAVES = "§7You should be closer to the Waves spawn to use this item§8.",
			RESTRICTION_USE_IN_WAVES = "§7You may only use this item in the world \"Waves\"§8.";

	public static String ITEM_NAME_INV_NAVI_SPAWN = "§8§l⟬ §2Spawn §8§l⟭",
			ITEM_NAME_INV_NAVI_CITY = "§8§l⟬ §3City §8§l⟭",
			ITEM_NAME_INV_NAVI_SUPPLIES = "§8§l⟬ §eSupplies §8§l⟭",
			ITEM_NAME_CM_WAVEFINDER = "§8⟫ §aWave finder §8⟪", ITEM_NAME_CM_NAVI = "§8⟫ §5Navigator §8⟪",
			ITEM_NAME_CM_IDENTIFIER = "§8⟫ §eIdentifier §8⟪", ITEM_NAME_CM_IDENTIFIERPART = "§e§oidentifier part",
			ITEM_NAME_INV_NAVI_ARENA = "§8⟫ §cArena §8⟪", ITEM_NAME_INV_NAVI_ARENA_SPY = "§8⟫ §c§oArena §e§oSpy §8⟪",
			ITEM_NAME_INV_NAVI_WAVES = "§8⟫ §bWaves §8⟪",
			ITEM_NAME_INV_WAVE_FINDER_NO_WAVES = "§c§oNo Waves available";

	public static List<String> ITEM_LORE_INV_NAVI_SPAWN = new ArrayList<>(Arrays.asList("The spawn is where everything comes " +
			"together.", "Useful information will be here, just as a", "helpful reminder of important information")),
			ITEM_LORE_INV_NAVI_CITY = new ArrayList<>(Arrays.asList("In the city the story enrolls.",
					"Important parts of it, including work,", "social and trading take place here.")),
			ITEM_LORE_INV_NAVI_SUPPLIES = new ArrayList<>(Arrays.asList("Supplies is a highly custom spot,",
					"in which you will be able to farm", "whatever resource you need in your way.")),
			ITEM_LORE_DESC_WAVEFINDER = new ArrayList<>(Arrays.asList("§2§oClick to open the §a§nWave Finder §2§omenu",
					"§2§oin which you can choose weather", "§2§oto play alone or together with other players.")),
			ITEM_LORE_DESC_NAVI = new ArrayList<>(Arrays.asList("§d§oFind your way through the story.",
					"§d§oExplore, work, or meet other players.", "§d§oThis tool helps you traveling.")),
			ITEM_LORE_INV_NAVI_ARENA = new ArrayList<>(Arrays.asList("§6§oOpen Area Mob Wars",
					"§6§oKill mobs and explore the automation diversity§8.")),
			ITEM_LORE_INV_NAVI_ARENA_SPY = new ArrayList<>(Arrays.asList("§6§oOpen Area Mob Wars",
					"§e§oTake a look at the arena from mid air§8.")),
			ITEM_LORE_DESC_IDENTIFIER = new ArrayList<>(Arrays.asList("§6§oThis mighty tool part is able to ",
					"§6§oevolve to drastically game changing item.")),
			ITEM_LORE_DESC_IDENIFIERPART = new ArrayList<>(Arrays.asList("§6§oThis powerful item is used to explore ",
					"§6§o(seemingly) special entities, blocks,", "§6§olocations and further more accurately.")),
			ITEM_LORE_INV_NAVI_DESC_WAVES = new ArrayList<>(Arrays.asList("§3§oIn here, you may fight and hold against mob waves.",
					"§3§oThose are to declare your strength and skill,", "§3§oto progress and unlock new features.")),
			ITEM_LORE_INV_WAVE_FINDER_NO_WAVES = new ArrayList<>(Arrays.asList("§cYou currently have no waves to display.",
					"§cWaves randomly become available from time to time.", "§cTry continue farming in the arena!"));

	// Other - Format able
	public static String ARGS_LENGTH_EXACT = "The only admissible argument length is §6%s§8.",
			ARGS_LENGTH_FROM_TO = "The argument length should be between §e%s §7and §6%r";

	public static void dynamicRestriction(Player p, DeliverType d, Restrictions r, RestrictionType rt) {
		String s = "interact";
		switch (r) {
			case BLOCK_BREAK:
				s = "§9break blocks";
				break;
			case DROP:
				s = "§3drop";
				break;
			case BLOCK_PLACE:
				s = "§eplace blocks";
				break;
			case FLY:
				s = "§btoggle flight";
				break;
		}

		String st = "near spawn.";
		switch (rt) {
			case THIS_ITEM:
				st = "§7this §9item§8.";
				break;
			case INSUFFICIENT_LEVEL:
				st = "§8, §7since your §2level §7is §6to low§8.";
				break;
			case AREA:
				st = "§7in this §earea§8.";
				break;
			case HOLDING_THIS:
				st = "§bholding §7this §9item.";
				break;
			case LEVEL_10:
				st = "§8, §7since your §2level §7is §cnot §aabove 10.";
				break;
			case WRONG_LOCATION:
				st = "§7in this §alocation§8.";
				break;
			case TUTORIAL:
				st = "§7while the tutorial is in progress§8.";
				break;
		}

		switch (d) {
			case CHAT:
				p.sendMessage(En.PRE + "You are §c§onot§7 allowed to " + s + " " + st);
				break;
			case TITLE:
				p.sendTitle(En.PRE, "You are §c§onot§7 allowed to " + s + " " + st, 4, 40, 50);
				break;
			case ACTIONBAR:
				Other.actionBar(p, "error", "You are §c§onot§7 allowed to " + s + " " + st, 0.2f, 1);
				break;
			case SCOREBOARD:
				Sb.sbMessage(p, "You are §c§onot§7 allowed to " + s + " " + st, Priority.LOW, 5);
				break;
			case KICK:
				p.kickPlayer("You are §c§onot§7 allowed to " + s + " " + st);
				break;
		}
	}
}
