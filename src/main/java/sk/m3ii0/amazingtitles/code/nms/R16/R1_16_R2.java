package sk.m3ii0.amazingtitles.code.nms.R16;

import net.minecraft.server.v1_16_R2.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.nms.NmsProvider;

public class R1_16_R2 implements NmsProvider {
	
	@Override
	public Object createActionbarPacket(String text) {
		if (text.isEmpty()) text = " ";
		return new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, CraftChatMessage.fromStringOrNull(text));
	}
	
	@Override
	public void sendActionbar(Player player, Object packet) {
		((CraftPlayer) player).getHandle().playerConnection.a().sendPacket((PacketPlayOutTitle) packet);
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
		((CraftPlayer) player).getHandle().playerConnection.a().sendPacket((PacketPlayOutTitle) packets[0]);
		((CraftPlayer) player).getHandle().playerConnection.a().sendPacket((PacketPlayOutTitle) packets[1]);
		((CraftPlayer) player).getHandle().playerConnection.a().sendPacket((PacketPlayOutTitle) packets[2]);
	}
	
}
