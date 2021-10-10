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

import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Mono;

/**
 * Represents a command abstraction to implement commands.
 */
public abstract class Command {

  private final String name;
  private final String description;

  private final PermissionSet permissions;

  /**
   * Construct a new {@link Command}.
   *
   * @param name        the name of the command.
   * @param description the description of the command.
   * @param permissions the required permissions.
   */
  public Command(String name, String description, PermissionSet permissions) {
    this.name = name;
    this.description = description;
    this.permissions = permissions;
  }

  /**
   * Execute the command from a Mono with an unknown return type.
   *
   * @param context the {@link CommandContext}.
   * @return a {@link Mono} with an unknown generic type.
   */
  public abstract Mono<?> execute(CommandContext context);

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public PermissionSet getPermissions() {
    return permissions;
  }
}
