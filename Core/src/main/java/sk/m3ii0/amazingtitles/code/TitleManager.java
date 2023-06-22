package sk.m3ii0.amazingtitles.code;

import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.api.objects.AmazingComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TitleManager {
	
	/*
	*
	* Values
	*
	* */
	
	private final Map<UUID, AmazingComponent> cache = new HashMap<>();
	
	/*
	* Protected constructor
	* */
	protected TitleManager() {}
	
	/*
	*
	* API
	*
	* */
	
	public void setTitleFor(Player player, AmazingComponent title) {
		AmazingComponent old = cache.remove(player.getUniqueId());
		if (old != null) {
			old.removeFor(player);
		}
		cache.put(player.getUniqueId(), title);
	}
	
	public void unsetTitleFor(Player player) {
		cache.remove(player.getUniqueId());
	}
	
	public AmazingComponent getPlayersAnimation(Player player) {
		return cache.get(player.getUniqueId());
	}
	
	public boolean hasAnimation(Player player) {
		return cache.containsKey(player.getUniqueId());
	}
	
	public void resetTitleAnimationFor(Player... players) {
		for (Player p : players) {
			AmazingComponent title = cache.get(p.getUniqueId());
			if (title != null) {
				title.removeFor(p);
			}
		}
	}
	
}
