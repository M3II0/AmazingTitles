package sk.m3ii0.amazingtitles.code.internal.smartbar;

import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.code.internal.Booter;

import java.util.*;

public class SmartBar {
	
	/*
	*
	* Values
	*
	* */
	
	private final Player owner;
	private final boolean notifications;
	private final boolean staticAnimation;
	private final boolean staticAnimationNotifications;
	
	private boolean hide = true;
	private List<String> staticAnimationContent = new ArrayList<>();
	private Map<String, SmartNotification> notificationsContent = new HashMap<>();
	
	/*
	*
	* Constructor
	*
	* */
	
	public SmartBar(Player owner, boolean notifications, boolean staticAnimation, boolean staticAnimationNotifications) {
		this.owner = owner;
		this.notifications = notifications;
		this.staticAnimation = staticAnimation;
		this.staticAnimationNotifications = staticAnimationNotifications;
	}
	
	/*
	*
	* API
	*
	* */
	
	public void setNotification(String id, SmartNotification notification) {
		this.notificationsContent.put(id, notification);
	}
	
	public void tryToInstantRemoveNotification(String id) {
		SmartNotification notification = notificationsContent.get(id);
		if (notification != null) {
			notification.quickRemove();
		}
	}
	
	public void setHide(boolean hide) {
		this.hide = hide;
	}
	
	public void prepareAndTryToSend() {
		if (hide) return;
		final StringBuilder text = new StringBuilder();
		if (staticAnimation) {
			if (staticAnimationNotifications) {
				if (notificationsContent.isEmpty()) {
					// Set here current frame from static notification
				} else {
					text.append(prepareNotifications());
				}
			} else {
				// Set here current frame from static notification
			}
		} else if (notifications) {
			text.append(prepareNotifications());
		}
		if (text.isEmpty()) return;
		final Object packet = Booter.getNmsProvider().createActionbarPacket(text.toString());
		Booter.getNmsProvider().sendActionbar(owner, packet);
	}
	
	private String prepareNotifications() {
		final StringBuilder notificationsText = new StringBuilder();
		int counter = 0;
		long mills = System.currentTimeMillis();
		int size = notificationsContent.size()-1;
		final Set<String> toRemove = new HashSet<>();
		for (Map.Entry<String, SmartNotification> entry : notificationsContent.entrySet()) {
			boolean latest = counter == size;
			String key = entry.getKey();
			SmartNotification value = entry.getValue();
			if (!value.isStarted()) {
				value.start(mills);
			}
			if (value.isOver(mills)) {
				toRemove.add(key);
			} else {
				notificationsText.append(" ").append(value.getCurrentFrame(mills, latest));
			}
			++counter;
		}
		for (String var : toRemove) {
			notificationsContent.remove(var);
		}
		return notificationsText.substring(1);
	}
	
}
