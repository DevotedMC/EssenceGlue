package com.github.maxopoly.essenceglue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import vg.civcraft.mc.civmodcore.playersettings.PlayerSettingAPI;
import vg.civcraft.mc.civmodcore.playersettings.impl.LongSetting;

public class PlayTimeCounter implements Listener {
	
	private static final long maxPlayTime = 10 * 60;
	private static final String bypassPerm = "devoted.hrs";
	
	private LongSetting minutesPlayed;
	
	public PlayTimeCounter(EssenceGluePlugin plugin) {
		this.minutesPlayed = new LongSetting(plugin, 0L, "essenceGlueTimeCounter", "essenceGlueTimeCounter");
		PlayerSettingAPI.registerSetting(minutesPlayed, null);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::incrementAll, 20 * 60, 20 * 60);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPermission(bypassPerm) && getTime(event.getPlayer()) > maxPlayTime) {
			System.out.println("Lobbying " +event.getPlayer().getName() + ", playtime was " + maxPlayTime);
			event.getPlayer().chat("/lobby");
			return;
		}
	}
	
	public long getTime(Player player) {
		return minutesPlayed.getValue(player);
	}
	
	private void incrementAll() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			minutesPlayed.setValue(player, minutesPlayed.getValue(player) + 1);
		}
	}

}
