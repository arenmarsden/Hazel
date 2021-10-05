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

package org.ammonium.hazel.client;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.core.shard.MemberRequestFilter;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.response.ResponseFunction;
import discord4j.rest.util.AllowedMentions;
import java.util.Objects;

/**
 * Represents the main entrypoint for the Hazel Discord bot.
 */
public final class Hazel {

  public static void main(String[] args) {
    final String token = args[0];
    Objects.requireNonNull(token, "Missing token");

    final DiscordClient client = DiscordClientBuilder.create(token)
      .onClientResponse(ResponseFunction.emptyIfNotFound())
      .setDefaultAllowedMentions(AllowedMentions.suppressEveryone())
      .build();

    client.gateway()
      .setEnabledIntents(IntentSet.all())
      // Apparently ___ ignores the parameter.
      .setInitialPresence(___ -> ClientPresence.online(ClientActivity.competing("$help")))
      .setMemberRequestFilter(MemberRequestFilter.none())
      .withGateway(GatewayDiscordClient::onDisconnect)
      .block();
  }
}
