package dev.candycup.lifestealutils.features.qol;

import dev.candycup.lifestealutils.Config;
import dev.candycup.lifestealutils.FeatureFlagController;
import dev.candycup.lifestealutils.event.EventPriority;
import dev.candycup.lifestealutils.event.events.ChatMessageReceivedEvent;
import dev.candycup.lifestealutils.event.events.ClientTickEvent;
import dev.candycup.lifestealutils.event.listener.ChatEventListener;
import dev.candycup.lifestealutils.event.listener.TickEventListener;
import dev.candycup.lifestealutils.interapi.MessagingUtils;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * automatically joins the Lifesteal gamemode when connecting to the lifesteal.net hub.
 * triggered by detecting the hub welcome message from the remote registry.
 */
public class AutoJoinLifesteal implements ChatEventListener, TickEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("lifestealutils/autojoin");
    private static final String TRIGGER_KEY = "join-lsn-hub";
    
    private int pendingJoinTicks = -1; // -1 = no join pending, 0+ = ticks until join

    @Override
    public boolean isEnabled() {
        return Config.isAutoJoinLifestealOnHub();
    }

    @Override
    public EventPriority getPriority() {
        return EventPriority.NORMAL;
    }

    @Override
    public void onChatMessageReceived(ChatMessageReceivedEvent event) {
        String message = event.getMessage().getString();
        if (message == null || message.isBlank()) {
            return;
        }

        // get trigger from remote registry
        String trigger = FeatureFlagController.getTrigger(TRIGGER_KEY);
        if (trigger == null || trigger.isBlank()) {
            return;
        }

        // check if message contains the trigger
        if (message.contains(trigger)) {
            // schedule join for 1 second later (20 ticks)
            pendingJoinTicks = 20;
            LOGGER.debug("[lsu-autojoin] detected hub join trigger, scheduling /joinlifesteal in 1 second");
        }
    }

    @Override
    public void onClientTick(ClientTickEvent event) {
        if (pendingJoinTicks < 0) {
            return;
        }

        if (pendingJoinTicks == 0) {
            // execute the join command
            Minecraft client = Minecraft.getInstance();
            if (client.player != null && client.player.connection != null) {
                client.player.connection.sendCommand("joinlifesteal");
                MessagingUtils.showMiniMessage("<gray><italic>[Lifesteal Utils Join Macro] Forwarding you to the lifesteal gamemode... this can be disabled in /lsu config!</italic></gray>");
                LOGGER.info("[lsu-autojoin] executed /joinlifesteal command");
            }
            pendingJoinTicks = -1; // reset
        } else {
            pendingJoinTicks--;
        }
    }
}
