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

package org.ammonium.hazel.client.command.moderation;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.spec.BanQuerySpec;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import java.util.EnumSet;
import org.ammonium.hazel.client.command.Command;

public class BanCommand extends Command {

  public BanCommand() {
    super("ban", "Remove a user from the community permanently.", Permission.BAN_MEMBERS);
  }

  @Override
  public void execute(Message message, Member member, String[] args) {
    Member memberToPunish = message.getGuild()
      .flatMap(guild -> guild.getMemberById(Snowflake.of(args[0])))
      .block();

    if (memberToPunish == null) {
      message.getChannel()
        .map(channel -> channel.createMessage("The user you tried to ban does not exist!"))
        .subscribe();
      return;
    }

    EnumSet<Permission> permissions = memberToPunish.getBasePermissions()
      .map(PermissionSet::asEnumSet)
      .block();

    if (permissions == null) {
      return; // fail silently
    }

    Role highestRole = member.getHighestRole().block();
    Role targetedHighestRole = memberToPunish.getHighestRole().block();

    if (highestRole == null || targetedHighestRole == null) {
      return;
    }

    int highestRolePos = highestRole.getPosition().block();
    int targetHighestRolePos = targetedHighestRole.getPosition().block();

    if ((permissions.contains(Permission.BAN_MEMBERS)) || (highestRolePos
      <= targetHighestRolePos)) {
      return;
    }

    memberToPunish.ban(BanQuerySpec.builder()
      .reason(args[1])
      .build());
  }
}
