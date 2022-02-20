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
    this.commandList = List.of(
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

  /**
   * Process the command context and execute the command.
   *
   * @param context the context
   * @return a {@link Mono}.
   */
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
