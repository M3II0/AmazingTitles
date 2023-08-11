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
	
	private boolean hide = false;
	private static List<String> staticAnimationContent = new ArrayList<>();
	private int staticAnimationContentCounter = 0;
	private final Map<String, SmartNotification> notificationsContent = new HashMap<>();
	
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
	
	public static void setStaticAnimationContent(List<String> staticAnimationContent) {
		SmartBar.staticAnimationContent = staticAnimationContent;
	}
	
	public void setNotification(String id, SmartNotification notification) {
		double time = notification.getTime();
		for (SmartNotification notification1 : notificationsContent.values()) {
			notification1.extend(time);
		}
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
					text.append(pickCurrentStaticFrame());
				} else {
					text.append(prepareNotifications());
				}
			} else {
				text.append(pickCurrentStaticFrame());
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
		int size = notificationsContent.size();
		final Set<String> toRemove = new HashSet<>();
		for (Map.Entry<String, SmartNotification> entry : notificationsContent.entrySet()) {
			int next = counter+1;
			boolean latest = true;
			if (next < size) {
				SmartNotification nextOne = null;
				int internalCounter = 0;
				for (Map.Entry<String, SmartNotification> entry1 : notificationsContent.entrySet()) {
					if (internalCounter == next) {
						nextOne = entry1.getValue();
					}
					++internalCounter;
				}
				if (nextOne != null) {
					if (!nextOne.isEnding(mills)) latest = false;
				}
			}
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
		if (notificationsText.length() < 2) return "";
		return notificationsText.substring(1);
	}
	
	private String pickCurrentStaticFrame() {
		String frame = staticAnimationContent.get(staticAnimationContentCounter);
		++staticAnimationContentCounter;
		if (staticAnimationContentCounter >= staticAnimationContent.size()) {
			staticAnimationContentCounter = 0;
		}
		if (frame == null) return "";
		return frame;
	}
	
}
