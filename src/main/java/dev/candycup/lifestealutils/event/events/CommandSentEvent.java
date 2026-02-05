package dev.candycup.lifestealutils.event.events;

import dev.candycup.lifestealutils.event.LSUEvent;

public class CommandSentEvent extends LSUEvent {
   private final String command;

   public CommandSentEvent(String command) {
      this.command = command;
   }

   public String getCommand() {
      return command;
   }

   @Override
   public boolean isCancellable() {
      return false;
   }
}
