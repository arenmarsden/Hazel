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

package org.ammonium.hazel.client.command;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.ammonium.hazel.client.command.moderation.BanCommand;
import reactor.core.publisher.Mono;

/**
 * Represents a command manager.
 */
public final class CommandManager {

  private final List<Command> commandList;

  /**
   * Construct a new Command Manager.
   */
  public CommandManager() {
    this.commandList = Arrays.asList(
      new BanCommand()
    );
  }

  /**
   * Get a command from the list by its name.
   *
   * @param name the name of the command.
   * @return an {@link Optional} containing the command.
   */
  public Optional<Command> getCommand(String name) {
    return this.commandList.stream()
      .filter(command -> command.getName().equalsIgnoreCase(name))
      .findFirst();
  }

  public Mono<?> processCommand(CommandContext context) {
    return Mono.justOrEmpty(context.getEvent())
      .map(MessageCreateEvent::getMessage)
      .map(Message::getContent)
      .filter(content -> content.startsWith("$"))
      .map(content -> content.substring(content.indexOf("$"))
        .substring(content.indexOf("" + content.length())))
      .map(this::getCommand)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .map(cmd -> cmd.execute(context));
  }

  public List<Command> getCommands() {
    return commandList;
  }
}
