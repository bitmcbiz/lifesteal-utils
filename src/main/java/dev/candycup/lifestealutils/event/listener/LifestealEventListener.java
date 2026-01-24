package dev.candycup.lifestealutils.event.listener;

import dev.candycup.lifestealutils.event.EventPriority;
import dev.candycup.lifestealutils.event.LSUEvent;

/**
 * base interface for all Lifesteal Utils event listeners.
 * implement specific listener interfaces (CombatEventListener, ChatEventListener, etc.)
 * and override the event handler methods you need.
 */
public interface LifestealEventListener {

    /**
     * @return the priority of this listener (higher priority executes first)
     */
    default EventPriority getPriority() {
        return EventPriority.NORMAL;
    }

    /**
     * @return true if this listener should receive events (checks config)
     */
    boolean isEnabled();

    /**
     * internal method to dispatch events to the appropriate handler.
     * do not override this method - implement specific listener interfaces instead.
     *
     * @param event the event to handle
     */
    default void handleEvent(LSUEvent event) {
        // dispatch to specific handler based on event type
        if (this instanceof CombatEventListener combatListener) {
            if (event instanceof dev.candycup.lifestealutils.event.events.ClientAttackEvent e) {
                combatListener.onClientAttack(e);
            } else if (event instanceof dev.candycup.lifestealutils.event.events.DamageConfirmedEvent e) {
                combatListener.onDamageConfirmed(e);
            } else if (event instanceof dev.candycup.lifestealutils.event.events.PlayerDamagedEvent e) {
                combatListener.onPlayerDamaged(e);
            }
        }

        if (this instanceof ChatEventListener chatListener) {
            if (event instanceof dev.candycup.lifestealutils.event.events.ChatMessageReceivedEvent e) {
                chatListener.onChatMessageReceived(e);
            } else if (event instanceof dev.candycup.lifestealutils.event.events.ChatMessageSentEvent e) {
                chatListener.onChatMessageSent(e);
            }
        }

        if (this instanceof TickEventListener tickListener) {
            if (event instanceof dev.candycup.lifestealutils.event.events.ClientTickEvent e) {
                tickListener.onClientTick(e);
            }
        }

        if (this instanceof ServerEventListener serverListener) {
            if (event instanceof dev.candycup.lifestealutils.event.events.ServerChangeEvent e) {
                serverListener.onServerChange(e);
            }
        }

        if (this instanceof RenderEventListener renderListener) {
            if (event instanceof dev.candycup.lifestealutils.event.events.ItemRenderEvent e) {
                renderListener.onItemRender(e);
            } else if (event instanceof dev.candycup.lifestealutils.event.events.PlayerNameRenderEvent e) {
                renderListener.onPlayerNameRender(e);
            }
        }

        if (this instanceof UIEventListener uiListener) {
            if (event instanceof dev.candycup.lifestealutils.event.events.TitleScreenInitEvent e) {
                uiListener.onTitleScreenInit(e);
            } else if (event instanceof dev.candycup.lifestealutils.event.events.SplashTextRequestEvent e) {
                uiListener.onSplashTextRequest(e);
            }
        }
    }
}
