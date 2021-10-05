package org.ammonium.hazel.client.command;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import java.util.concurrent.atomic.AtomicLong;

public class CommandContext {

  private final MessageCreateEvent event;
  private final AtomicLong replyId;

  public CommandContext(MessageCreateEvent event) {
    this.event = event;
    this.replyId = new AtomicLong();
  }

  public MessageCreateEvent getEvent() {
    return this.event;
  }

  public GatewayDiscordClient getClient() {
    return this.event.getClient();
  }
}
