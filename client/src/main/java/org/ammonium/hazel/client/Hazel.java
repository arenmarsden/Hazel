/*
 * This file is licensed under the MIT License.
 *
 * Copyright (c) 2022 Aren Marsden
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ammonium.hazel.client;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.core.shard.MemberRequestFilter;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.response.ResponseFunction;
import discord4j.rest.util.AllowedMentions;
import java.time.Duration;
import java.util.Objects;
import org.ammonium.hazel.client.command.CommandManager;
import org.ammonium.hazel.client.listener.Listener;
import org.ammonium.hazel.client.listener.create.CommandHandlingListener;
import reactor.core.publisher.Mono;

/**
 * Represents the main entrypoint for the Hazel Discord bot.
 */
public final class Hazel {

  /**
   * Run the main project.
   *
   * @param args the arguments.
   */
  public static void main(String[] args) {
    final String token = args[0];
    Objects.requireNonNull(token, "Missing token");

    CommandManager commandManager = new CommandManager();

    final DiscordClient client = DiscordClientBuilder.create(token)
      .onClientResponse(ResponseFunction.emptyIfNotFound())
      .setDefaultAllowedMentions(AllowedMentions.suppressEveryone())
      .build();

    client.gateway()
      .setEnabledIntents(IntentSet.of(
        Intent.GUILD_BANS,
        Intent.GUILD_MEMBERS,
        Intent.GUILD_PRESENCES,
        Intent.GUILD_MESSAGES,
        Intent.GUILD_MESSAGE_REACTIONS,
        Intent.DIRECT_MESSAGES))
      .setInitialPresence(ignored -> ClientPresence.online(ClientActivity.playing("$help")))
      .setMemberRequestFilter(MemberRequestFilter.none())
      .withGateway(gateway -> {
        register(gateway, new CommandHandlingListener(commandManager));
        return gateway.onDisconnect();
      }).block();
  }

  @SafeVarargs
  private static <T extends Event> void register(GatewayDiscordClient client,
    Listener<T>... events) {
    for (Listener<T> event : events) {
      client.getEventDispatcher()
        .on(event.getEventClass())
        .flatMap(e -> event.execute(e)
          .timeout(Duration.ofSeconds(30), Mono.error(new RuntimeException("Event timed out.")))
          .onErrorResume(err -> Mono.fromRunnable(() -> event.handleError(err)))
        ).subscribe(null, event::handleError);
    }
  }

}
