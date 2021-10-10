/*
 * Ammonium Studios, Inc. ("Ammonium") CONFIDENTIAL
 * Unpublished Copyright (c) 2021 Ammonium, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of Ammonium.
 * The intellectual and technical concepts contained herein are proprietary to Ammonium and may be
 * covered by U.K. and Foreign Patents, patents in process, and are protected by trade secret or
 * copyright law. Dissemination of this information or reproduction of this material is strictly
 * forbidden unless prior written permission is obtained from Ammonium. Access to the source code
 * contained herein is hereby forbidden to anyone except current Ammonium employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements explicitly covering
 * such access.
 *
 * The copyright notice above does not evidence any actual or intended publication or disclosure of
 * this source code, which includes information that is confidential and/or proprietary, and is a
 * trade secret, of  Ammonium.   ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS  SOURCE CODE  WITHOUT  THE EXPRESS WRITTEN CONSENT
 * OF Ammonium IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE LAWS AND
 * INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF  THIS SOURCE CODE AND/OR RELATED INFORMATION
 * DOES NOT CONVEY OR IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR
 * TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.
 */

package org.ammonium.hazel.client.listener.create;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;
import org.ammonium.hazel.client.command.CommandContext;
import org.ammonium.hazel.client.command.CommandManager;
import org.ammonium.hazel.client.listener.Listener;
import org.ammonium.hazel.client.util.ReactorUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import reactor.core.publisher.Mono;

/**
 * Represents the listener to handle messages.
 */
public class CommandHandlingListener implements Listener<MessageCreateEvent> {

  private final CommandManager commandManager;

  public CommandHandlingListener(CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public @NonNull Class<MessageCreateEvent> getEventClass() {
    return MessageCreateEvent.class;
  }

  @Override
  public Mono<Void> execute(MessageCreateEvent event) {
    return event.getMessage().getChannel()
      .ofType(TextChannel.class)
      .flatMap(channel -> channel.getEffectivePermissions(event.getClient().getSelfId()))

      .filterWhen(ReactorUtil.filterOrExecute(permissions ->
        permissions.contains(Permission.SEND_MESSAGES)
          && permissions.contains(Permission.VIEW_CHANNEL),
        Mono.empty()))

      .flatMap(ignored -> Mono.justOrEmpty(event.getGuildId()))
      .map(guildId -> new CommandContext(event))
      .map(commandManager::processCommand)
      .then();
  }
}
