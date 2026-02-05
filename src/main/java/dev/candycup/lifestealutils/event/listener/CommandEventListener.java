package dev.candycup.lifestealutils.event.listener;

import dev.candycup.lifestealutils.event.events.CommandSentEvent;

public interface CommandEventListener extends LifestealEventListener {

   default void onCommandSent(CommandSentEvent event) {
   }
}
