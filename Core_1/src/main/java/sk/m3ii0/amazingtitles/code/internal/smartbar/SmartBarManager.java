package sk.m3ii0.amazingtitles.code.internal.smartbar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class SmartBarManager {
	
	/*
	*
	* Values
	*
	* */
	
	private final Plugin plugin;
	private final Map<Player, SmartBar> bars = new HashMap<>();
	
	/*
	*
	* Constructor
	*
	* */
	
	public SmartBarManager(Plugin plugin) {
		this.plugin = plugin;
	}
	
	/*
	*
	* API
	*
	* */
	
	public void insertBar(Player player, SmartBar bar) {
		this.bars.put(player, bar);
	}
	
	public SmartBar getBar(Player player) {
		return this.bars.get(player);
	}
	
	public void removeBar(Player player) {
		this.bars.remove(player);
	}
	
}
