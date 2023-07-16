package sk.m3ii0.amazingtitles.code.providers.R1_14_R1;

import net.minecraft.server.v1_14_R1.NetworkManager;
import net.minecraft.server.v1_14_R1.Packet;
import net.minecraft.server.v1_14_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_14_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.spi.NmsProvider;

import java.lang.reflect.Field;

public class R1_14_R1 implements NmsProvider {
	
	private final Field networkManagerH;
	
	public R1_14_R1() {
		try {
			networkManagerH = PlayerConnection.class.getDeclaredField("networkManager");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Object createActionbarPacket(String text) {
		if (text.isEmpty()) text = " ";
		return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, CraftChatMessage.fromStringOrNull(text));
	}
	
	@Override
	public Object[] createTitlePacket(String title, String subtitle) {
		if (title.isEmpty()) title = " ";
		if (subtitle.isEmpty()) subtitle = " ";
		PacketPlayOutTitle animation = new PacketPlayOutTitle(0, 5, 0);
		PacketPlayOutTitle text = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, CraftChatMessage.fromStringOrNull(title));
		PacketPlayOutTitle subtext = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, CraftChatMessage.fromStringOrNull(subtitle));
		return new Object[] {animation, text, subtext};
	}
	
	@Override
	public void sendTitles(Player player, Object... packets) {
		NetworkManager manager = getNetworkManager(player);
		manager.sendPacket((Packet<?>) packets[0]);
		manager.sendPacket((Packet<?>) packets[1]);
		manager.sendPacket((Packet<?>) packets[2]);
	}
	
	@Override
	public void sendActionbar(Player player, Object packet) {
		NetworkManager manager = getNetworkManager(player);
		manager.sendPacket((Packet<?>) packet);
	}
	
	private NetworkManager getNetworkManager(Player player) {
		try {
			return (NetworkManager) networkManagerH.get(((CraftPlayer) player).getHandle().playerConnection);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}