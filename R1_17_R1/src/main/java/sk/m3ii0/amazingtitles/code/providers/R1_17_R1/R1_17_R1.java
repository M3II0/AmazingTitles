package sk.m3ii0.amazingtitles.code.providers.R1_17_R1;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsProvider;

import java.lang.reflect.Field;

public class R1_17_R1 implements NmsProvider {
	
	private final Field networkManagerH;
	
	public R1_17_R1() {
		try {
			networkManagerH = PlayerConnection.class.getDeclaredField("a");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Object createActionbarPacket(String text) {
		if (text.isEmpty()) text = " ";
		return new ClientboundSetActionBarTextPacket(CraftChatMessage.fromStringOrNull(text));
	}
	
	@Override
	public Object[] createTitlePacket(String title, String subtitle, int in, int keep, int out) {
		if (title.isEmpty()) title = " ";
		if (subtitle.isEmpty()) subtitle = " ";
		ClientboundSetTitlesAnimationPacket animation = new ClientboundSetTitlesAnimationPacket(in, keep, out);
		ClientboundSetTitleTextPacket text = new ClientboundSetTitleTextPacket(CraftChatMessage.fromStringOrNull(title));
		ClientboundSetSubtitleTextPacket subtext = new ClientboundSetSubtitleTextPacket(CraftChatMessage.fromStringOrNull(subtitle));
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
			return (NetworkManager) networkManagerH.get(((CraftPlayer) player).getHandle().b);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}