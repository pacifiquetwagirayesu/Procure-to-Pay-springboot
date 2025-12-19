package org.commitlink.procure.models;

import static org.commitlink.procure.utils.Constants.APPROVE_ONE;
import static org.commitlink.procure.utils.Constants.APPROVE_TWO;
import static org.commitlink.procure.utils.Constants.CREATE;
import static org.commitlink.procure.utils.Constants.READ;
import static org.commitlink.procure.utils.Constants.UPLOAD;

import java.util.Set;

public enum Role {
  STAFF(Set.of(CREATE, UPLOAD, READ)) {
    @Override
    public Set<String> getPermissions() {
      return Set.of(CREATE, UPLOAD, READ);
    }
  },
  APPROVER_LEVEL_1(Set.of(APPROVE_ONE, READ)) {
    @Override
    public Set<String> getPermissions() {
      return Set.of(APPROVE_ONE, READ);
    }
  },
  APPROVER_LEVEL_2(Set.of(APPROVE_TWO, READ)) {
    @Override
    public Set<String> getPermissions() {
      return Set.of(APPROVE_TWO, READ);
    }
  },
  FINANCE(Set.of(UPLOAD, READ)) {
    @Override
    public Set<String> getPermissions() {
      return Set.of(UPLOAD, READ);
    }
  };

  private final Set<String> permissions;

  Role(Set<String> permissions) {
    this.permissions = permissions;
  }

  public abstract Set<String> getPermissions();

  public boolean hasPermission(String permission) {
    return this.permissions.contains(permission);
  }
}
