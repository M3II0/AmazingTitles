package sk.m3ii0.amazingtitles.code;

import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.async.AmazingTitle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TitleManager {
	
	/*
	*
	* Values
	*
	* */
	
	private final Map<UUID, AmazingTitle> cache = new HashMap<>();
	
	/*
	* Protected constructor
	* */
	protected TitleManager() {}
	
	/*
	*
	* API
	*
	* */
	
	public void setTitleFor(Player player, AmazingTitle title) {
		AmazingTitle old = cache.remove(player.getUniqueId());
		if (old != null) {
			old.removeFor(player);
		}
		cache.put(player.getUniqueId(), title);
	}
	
	public void unsetTitleFor(Player player) {
		cache.remove(player.getUniqueId());
	}
	
	public AmazingTitle getPlayersAnimation(Player player) {
		return cache.get(player.getUniqueId());
	}
	
	public boolean hasAnimation(Player player) {
		return cache.containsKey(player.getUniqueId());
	}
	
	public void resetTitleAnimationFor(Player... players) {
		for (Player p : players) {
			AmazingTitle title = cache.get(p.getUniqueId());
			if (title != null) {
				title.removeFor(p);
			}
		}
	}
	
}
