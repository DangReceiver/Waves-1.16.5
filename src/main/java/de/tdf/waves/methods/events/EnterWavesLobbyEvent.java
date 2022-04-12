package de.tdf.waves.methods.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EnterWavesLobbyEvent extends Event {

	private static final HandlerList handlers;

	public static Player p;
	public static World preLoc, prePreviousWorld;

	public EnterWavesLobbyEvent(final Player p, final World preLoc, final World prePreWorld) {
		EnterWavesLobbyEvent.p = p;
		EnterWavesLobbyEvent.preLoc = preLoc;
		EnterWavesLobbyEvent.prePreviousWorld = prePreWorld;
	}

	public HandlerList getHandlers() {
		return EnterWavesLobbyEvent.handlers;
	}

	public Player getPlayer() {
		return p;
	}

	public World getPreLocation() {
		return preLoc;
	}

	public void setPreLocation(World newLoc) {
		preLoc = newLoc;
	}

	public World getPrePreWorld() {
		return prePreviousWorld;
	}

	public static HandlerList getHandlerList() {
		return EnterWavesLobbyEvent.handlers;
	}

	static {
		handlers = new HandlerList();
	}
}
