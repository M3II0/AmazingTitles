package sk.m3ii0.amazingtitles.example;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import sk.m3ii0.amazingtitles.api.AmazingTitlesAPI;
import sk.m3ii0.amazingtitles.api.objects.AmazingTitleExtension;
import sk.m3ii0.amazingtitles.api.objects.types.ActionType;
import sk.m3ii0.amazingtitles.code.notifications.BarNotification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Main implements AmazingTitleExtension {
	
	@Override
	public void load() {
		
		// Do your stuff
		
		// How to register new Animation?
		// Infinite - Text input
		// Repeat - Should animation begin again or keep on last frame
		AmazingTitlesAPI.getApi().createAndRegister("TEST", false, true, true, (type, input, args) -> {
			return new ArrayList<>(Collections.singleton("Frames"));
		 }, "ArgumentHelp....");
		
	}
	
	public void api() {
		
		// Ignore this
		Player receiver = null;
		Player[] receivers = new Player[0];
		
		/*
		* Getting API instance
		* */
		AmazingTitlesAPI api = AmazingTitlesAPI.getApi();
		
		/*
		* Get available animations
		* */
		Set<String> animations = api.getAvailableAnimations();
		
		/*
		* Is plugin booted in legacy mode?
		* NOTE: Without HEX & Gradient support
		* */
		boolean legacyMode = api.bootedInLegacyMode();
		
		/*
		* Checking, if animation is enabled
		* TRUE = enabled
		* */
		boolean isDisabled = api.isAnimationEnabled("RAINBOW");
		
		/*
		* Getting animation's legacy status
		* */
		boolean legacyAnimation = api.isAnimationLegacy("RAINBOW");
		
		/*
		* Checking, if animation can be performed
		* */
		boolean runnable = (legacyMode && legacyAnimation) || (!legacyMode && !legacyAnimation);
		
		/*
		* Sending normal title
		* NOTE: Can be performed asynchronously
		* REMINDER: You can use HEX & Gradient. Format can be found on Wiki
		* */
		api.sendNormalTitle(receiver, "Your Text", "Your SubText", 1, 10, 1);
		api.sendNormalTitle(receivers, "Your Text", "Your SubText", 1, 10, 1);
		
		/*
		* Sending normal title
		* NOTE: Can be performed asynchronously
		* REMINDER: You can use HEX & Gradient. Format can be found on Wiki
		* */
		api.sendNormalTitle(receiver, "Your Text", "Your SubText", 1, 10, 1);
		api.sendNormalTitle(receivers, "Your Text", "Your SubText", 1, 10, 1);
		
		/*
		* Sending animated title without additional arguments
		* NOTE: Can be performed asynchronously
		* REMINDER: You can use HEX & Gradient. Format can be found on Wiki
		* FORMAT: Use \n\ as line separator (Works in Title and SubTitle action type)
		* */
		api.sendAnimatedTitle(receiver, "RAINBOW", ActionType.TITLE, "Your Text\\n\\Your SubText", 1, 20);
		api.sendAnimatedTitle(receivers, "RAINBOW", ActionType.TITLE, "Your Text\\n\\Your SubText", 1, 20);
		
		/*
		* Sending animated title with additional arguments (Arguments can be found on this wiki)
		* NOTE: Can be performed asynchronously
		* REMINDER: You can use HEX & Gradient. Format can be found on Wiki
		* FORMAT: Use \n\ as line separator (Works in Title and SubTitle action type)
		* */
		Object[] arguments = new Object[] {"#ffffff", "#000000"};
		api.sendAnimatedTitle(receiver, "WAVES", ActionType.TITLE, "Your Text\\n\\Your SubText", 1, 20, arguments);
		api.sendAnimatedTitle(receivers, "WAVES", ActionType.TITLE, "Your Text\\n\\Your SubText", 1, 20, arguments);
		
		/*
		* Checking, if player has running animation
		* */
		boolean isPlayerReceiving = api.hasTitle(receiver);
		
		/*
		* Reset title for Player
		* */
		api.resetTitleFor(receiver);
		api.resetTitleFor(receivers);
		
		/*
		* Adding notification for Player (Without ID)
		* REMINDER: You can use HEX & Gradient. Format can be found on Wiki (Symbol & Message)
		* */
		api.addNotification(receiver, BarNotification.create("Symbol", "Message", 5));
		api.addNotification(receivers, BarNotification.create("Symbol", "Message", 5));
		
		/*
		* Adding notification for Player (With ID)
		* REMINDER: You can use HEX & Gradient. Format can be found on Wiki (Symbol & Message)
		* */
		api.addNotification(receiver, "your_id", BarNotification.create("Symbol", "Message", 5));
		api.addNotification(receivers, "your_id", BarNotification.create("Symbol", "Message", 5));
		
		/*
		* Get active notifications by Player
		* */
		int playerNotifications = api.getNotificationsOf(receiver);
		
		/*
		* Building interactive message from raw (Format can be found on Wiki)
		* NOTE: Returns raw BaseComponents
		* */
		BaseComponent[] message = api.interactiveFromRaw("<HOVER=Click to say hello...,SUGGEST_COMMAND=Hello!>&eWelcome here!</>");
		
		/*
		* Sending interactive message from raw (Format can be found on Wiki)
		* */
		api.sendInteractiveFromRaw(receiver, "<HOVER=Click to say hello...,SUGGEST_COMMAND=Hello!>&eWelcome here!</>");
		api.sendInteractiveFromRaw(receivers, "<HOVER=Click to say hello...,SUGGEST_COMMAND=Hello!>&eWelcome here!</>");
		
	}
	
}