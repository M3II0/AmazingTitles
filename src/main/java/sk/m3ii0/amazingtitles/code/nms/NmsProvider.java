package sk.m3ii0.amazingtitles.code.nms;

import org.bukkit.entity.Player;

public interface NmsProvider {
	
	Object createActionbarPacket(String text);
	Object[] createTitlePacket(String title, String subtitle);
	
	void sendTitles(Player player, Object... packets);
	void sendActionbar(Player player, Object packet);
	
}
