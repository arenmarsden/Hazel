package org.ammonium.hazel.client.command;

import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Mono;

public abstract class Command {

  private final String name;
  private final String description;

  private final PermissionSet permissions;

  public Command(String name, String description, PermissionSet permissions) {
    this.name = name;
    this.description = description;
    this.permissions = permissions;
  }

  public abstract Mono<?> execute();

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
