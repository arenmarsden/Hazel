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

package org.ammonium.hazel.client.command.moderation;

import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import org.ammonium.hazel.client.command.Command;
import org.ammonium.hazel.client.command.CommandContext;
import reactor.core.publisher.Mono;

/**
 * Represents the ban command.
 */
public class BanCommand extends Command {

  /**
   * Inherit constructor super().
   */
  public BanCommand() {
    super("ban", "Ban a user from the community.", PermissionSet.of(
      Permission.BAN_MEMBERS
    ));
  }

  /**
   * Execute the command from a Mono with an unknown return type.
   *
   * @param context the {@link CommandContext}.
   * @return a {@link Mono} with an unknown generic type.
   */
  @Override
  public Mono<?> execute(CommandContext context) {
    System.out.println("Hello, World!");
    return Mono.empty();
  }
}
