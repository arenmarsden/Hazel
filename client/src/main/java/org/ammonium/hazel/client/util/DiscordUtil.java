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

package org.ammonium.hazel.client.util;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.AllowedMentions;
import discord4j.rest.util.AllowedMentions.Type;
import discord4j.rest.util.Permission;
import org.ammonium.hazel.client.command.exception.InsufficientPermissionException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Represents a class for holding various Discord-related utilities.
 */
public final class DiscordUtil {

  /**
   * Send a message as a plain string into a channel inheriting {@link
   * #sendMessage(MessageCreateSpec, MessageChannel, boolean)}.
   *
   * @param content the content of the message.
   * @param channel the channel.
   * @return a {@link Mono} containing the result of the function.
   */
  public static Mono<Message> sendMessage(String content, MessageChannel channel) {
    return DiscordUtil.sendMessage(MessageCreateSpec.create().withContent(content), channel, false);
  }

  /**
   * Send a message to a specified channel as a String or an embed with a {@link MessageCreateSpec},
   * checking if we have permission to post in that specific and handling it accordingly.
   *
   * @param spec     the {@link MessageCreateSpec}.
   * @param channel  the channel to post in.
   * @param hasEmbed if the message is an embed.
   * @return a {@link Mono} containing the result of the function.
   */
  public static Mono<Message> sendMessage(MessageCreateSpec spec, MessageChannel channel,
    boolean hasEmbed) {
    return Mono.zip(
        DiscordUtil.hasPermission(channel, channel.getClient().getSelfId(), Permission.SEND_MESSAGES),
        DiscordUtil.hasPermission(channel, channel.getClient().getSelfId(), Permission.EMBED_LINKS))
      .flatMap(results -> {
        boolean canSendMessage = results.getT1();
        boolean canSendEmbed = results.getT2();

        if (!canSendMessage) {
          return Mono.empty();
        }

        if (!canSendEmbed && hasEmbed) {
          return DiscordUtil.sendMessage(
            "I have insufficient privileges to send messages in this channel.", channel);
        }
        return channel.createMessage(spec.withAllowedMentions(AllowedMentions.builder()
          .parseType(Type.ROLE, Type.USER)
          .build()));
      });
  }

  /**
   * Checks if the user has a permission in the specified channel.
   *
   * @param channel    the channel.
   * @param userId     the user id.
   * @param permission the permission to check.
   * @return a {@link Mono} with the result of the check.
   */
  public static Mono<Boolean> hasPermission(Channel channel, Snowflake userId,
    Permission permission) {
    if (channel instanceof PrivateChannel) {
      return Mono.just(true);
    }
    return ((GuildChannel) channel).getEffectivePermissions(userId)
      .map(permissions -> permissions.asEnumSet().contains(permission));
  }

  /**
   * Checks if the user has a permission in the guild.
   *
   * @param guild      the guild.
   * @param userId     the user id.
   * @param permission the permission to check.
   * @return a {@link Mono} with the result of the check.
   */
  public static Mono<Boolean> hasPermission(Guild guild, Snowflake userId, Permission permission) {
    return guild.getMemberById(userId).flatMap(Member::getBasePermissions)
      .map(permissions -> permissions.asEnumSet().contains(permission));
  }

  /**
   * Requires permissions within a channel to execute something.
   *
   * @param channel     the channel.
   * @param permissions the permissions to require.
   * @return a {@link Mono} containing an exception.
   */
  public static Mono<Void> requirePermissions(Channel channel, Permission... permissions) {
    return Flux.fromArray(permissions)
      .flatMap(permission ->
        DiscordUtil.hasPermission(channel, channel.getClient().getSelfId(), permission)
          .filter(Boolean.TRUE::equals)
          .switchIfEmpty(Mono.error(new InsufficientPermissionException(permission))))
      .then();
  }

  private DiscordUtil() {
    // Just for style reasons mostly.
    throw new UnsupportedOperationException();
  }
}
