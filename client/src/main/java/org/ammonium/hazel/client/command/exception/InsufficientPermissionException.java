package org.ammonium.hazel.client.command.exception;

import discord4j.rest.util.Permission;

/**
 * Thrown whenever the bot or a user is missing the required permissions to execute a task.
 */
public class InsufficientPermissionException extends RuntimeException {

  private final Permission permission;

  public InsufficientPermissionException(Permission permission) {
    super("Missing permission " + permission.name());
    this.permission = permission;
  }

  public Permission getPermission() {
    return permission;
  }
}
