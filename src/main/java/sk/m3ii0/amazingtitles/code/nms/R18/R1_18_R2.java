package sk.m3ii0.amazingtitles.code.nms.R18;

import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.nms.NmsProvider;

public class R1_18_R2 implements NmsProvider {
	
	@Override
	public Object createActionbarPacket(String text) {
		if (text.isEmpty()) text = " ";
		return new ClientboundSetActionBarTextPacket(CraftChatMessage.fromStringOrNull(text));
	}
	
	@Override
	public Object[] createTitlePacket(String title, String subtitle) {
		if (title.isEmpty()) title = " ";
		if (subtitle.isEmpty()) subtitle = " ";
		ClientboundSetTitlesAnimationPacket animation = new ClientboundSetTitlesAnimationPacket(0, 5, 0);
		ClientboundSetTitleTextPacket text = new ClientboundSetTitleTextPacket(CraftChatMessage.fromStringOrNull(title));
		ClientboundSetSubtitleTextPacket subtext = new ClientboundSetSubtitleTextPacket(CraftChatMessage.fromStringOrNull(subtitle));
		return new Object[] {animation, text, subtext};
	}
	
	@Override
	public void sendTitles(Player player, Object... packets) {
		((CraftPlayer) player).getHandle().b.a().a((ClientboundSetTitlesAnimationPacket) packets[0]);
		((CraftPlayer) player).getHandle().b.a().a((ClientboundSetTitleTextPacket) packets[1]);
		((CraftPlayer) player).getHandle().b.a().a((ClientboundSetSubtitleTextPacket) packets[2]);
	}
	
	@Override
	public void sendActionbar(Player player, Object packet) {
		((CraftPlayer) player).getHandle().b.a().a((ClientboundSetActionBarTextPacket) packet);
	}
	
}
