package sk.m3ii0.amazingtitles.api;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.api.objects.AmazingCreator;
import sk.m3ii0.amazingtitles.api.objects.FramesBuilder;
import sk.m3ii0.amazingtitles.api.objects.types.ActionType;
import sk.m3ii0.amazingtitles.code.AmazingTitles;
import sk.m3ii0.amazingtitles.code.colors.ColorTranslator;
import sk.m3ii0.amazingtitles.code.commands.dispatcher.TitleDispatcher;
import sk.m3ii0.amazingtitles.code.notifications.BarNotification;
import sk.m3ii0.amazingtitles.code.notifications.DynamicBar;

import java.util.Set;

public class AmazingTitlesAPI {
	
	private final static AmazingTitlesAPI api = new AmazingTitlesAPI();
	
	protected AmazingTitlesAPI() {}
	
	public static AmazingTitlesAPI getApi() {
		return api;
	}
	
	public void createAndRegister(String name, boolean repeat, boolean infinite, boolean legacy, FramesBuilder framesBuilder, String... arguments) {
		AmazingTitles.tryToSetPathAnimation(name);
		boolean enabled = AmazingTitles.getOptions().getBoolean("ExtensionsManager." + name);
		if (!enabled) return;
		AmazingCreator creator = new AmazingCreator(repeat, infinite, legacy, framesBuilder, arguments);
		AmazingTitles.addCustomComponent(name, creator);
	}
	
	public boolean bootedInLegacyMode() {
		return AmazingTitles.legacy();
	}
	
	public Set<String> getAvailableAnimations() {
		return AmazingTitles.getCustomComponents().keySet();
	}
	
	public boolean isAnimationEnabled(String animation) {
		return AmazingTitles.getCustomComponents().containsKey(animation);
	}
	
	public boolean isAnimationLegacy(String animation) {
		if (!isAnimationEnabled(animation)) return false;
		AmazingCreator creator = AmazingTitles.getCustomComponents().get(animation);
		if (creator == null) return false;
		return creator.isLegacy();
	}
	
	public void sendAnimatedTitle(Player[] receivers, String animation, ActionType type, String input, int speed, int duration, Object... args) {
		if (!isAnimationEnabled(animation)) return;
		AmazingCreator creator = AmazingTitles.getCustomComponents().get(animation);
		String[] split = input.split("\\\\n\\\\");
		String main = (split.length > 0)? split[0] : "";
		String sub = ColorTranslator.parse((split.length > 1)? split[1] : "");
		try {
			creator.dispatch(receivers, type, speed, duration, main, sub, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendAnimatedTitle(Player receiver, String animation, ActionType type, String input, int speed, int duration, Object... args) {
		sendAnimatedTitle(new Player[] {receiver}, animation, type, input, speed, duration, args);
	}
	
	public void sendNormalTitle(Player[] receivers, String title, String subtitle, int in, int keep, int out) {
		title = ColorTranslator.parse(title);
		subtitle = ColorTranslator.parse(subtitle);
		Object[] packets = AmazingTitles.getProvider().createTitlePacket(title, subtitle, in, keep, out);
		for (Player p : receivers) {
			AmazingTitles.getProvider().sendTitles(p, packets);
		}
	}
	
	public void sendNormalTitle(Player receiver, String title, String subtitle, int in, int keep, int out) {
		sendNormalTitle(new Player[] {receiver}, title, subtitle, in, keep, out);
	}
	
	public void resetTitleFor(Player[] players) {
		AmazingTitles.getTitleManager().resetTitleAnimationFor(players);
	}
	
	public void resetTitleFor(Player player) {
		AmazingTitles.getTitleManager().unsetTitleFor(player);
	}
	
	public boolean hasTitle(Player player) {
		return AmazingTitles.getTitleManager().hasAnimation(player);
	}
	
	public void addNotification(Player player, BarNotification notification) {
		addNotification(new Player[] {player}, notification);
	}
	
	public void addNotification(Player player, String id, BarNotification notification) {
		addNotification(new Player[] {player}, id, notification);
	}
	
	public void addNotification(Player[] players, BarNotification notification) {
		for (Player player : players) {
			DynamicBar bar = AmazingTitles.getBars().get(player.getUniqueId());
			if (bar != null) {
				bar.notification("id-" + (bar.getNotifications().size() + 1), notification);
			}
		}
	}
	
	public void addNotification(Player[] players, String id, BarNotification notification) {
		for (Player player : players) {
			DynamicBar bar = AmazingTitles.getBars().get(player.getUniqueId());
			if (bar != null) {
				bar.notification(id, notification);
			}
		}
	}
	
	public int getNotificationsOf(Player player) {
		DynamicBar bar = AmazingTitles.getBars().get(player.getUniqueId());
		if (bar == null) return 0;
		return bar.getNotifications().size();
	}
	
	public BaseComponent[] interactiveFromRaw(String raw) {
		return TitleDispatcher.getMessageFromRaw(raw);
	}
	
	public void sendInteractiveFromRaw(Player[] receivers, String raw) {
		BaseComponent[] interactive = TitleDispatcher.getMessageFromRaw(raw);
		for (Player p : receivers) {
			p.spigot().sendMessage(interactive);
		}
	}
	
	public void sendInteractiveFromRaw(Player receiver, String raw) {
		sendInteractiveFromRaw(new Player[] {receiver}, raw);
	}
	
}