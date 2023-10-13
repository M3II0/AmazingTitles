package sk.m3ii0.amazingtitles.code.providers.R1_20_R2;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.internal.spi.NmsProvider;

public class R1_20_R2 implements NmsProvider {
	
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
		ServerCommonPacketListenerImpl manager = getServerCommonPacketListenerImpl(player);
		manager.b((Packet<?>) packets[0]);
		manager.b((Packet<?>) packets[1]);
		manager.b((Packet<?>) packets[2]);
	}
	
	@Override
	public void sendActionbar(Player player, Object packet) {
		ServerCommonPacketListenerImpl manager = getServerCommonPacketListenerImpl(player);
		manager.b((Packet<?>) packet);
	}
	
	private ServerCommonPacketListenerImpl getServerCommonPacketListenerImpl(Player player) {
		return ((CraftPlayer) player).getHandle().c;
	}
	
}