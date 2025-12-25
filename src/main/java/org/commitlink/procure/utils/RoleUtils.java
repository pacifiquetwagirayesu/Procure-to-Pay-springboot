package org.commitlink.procure.utils;

import java.util.List;
import org.commitlink.procure.models.purchase.PurchaseRequest;
import org.commitlink.procure.models.purchase.Status;
import org.commitlink.procure.models.user.AuthUser;
import org.commitlink.procure.models.user.Role;
import org.commitlink.procure.models.user.User;

public class RoleUtils {

  public static boolean isApprover(AuthUser authUser) {
    return Role.APPROVER_LEVEL_1.name().equals(authUser.getRole()) || Role.APPROVER_LEVEL_2.name().equals(authUser.getRole());
  }

  public static boolean isLevelTwoApprover(List<String> roles, AuthUser authUser) {
    return roles.contains(Role.APPROVER_LEVEL_1.name()) && authUser.getRole().equals(Role.APPROVER_LEVEL_2.name());
  }

  public static boolean canNotBeUpdated(List<String> roles, AuthUser authUser) {
    return roles.contains(Role.APPROVER_LEVEL_1.name()) && authUser.getRole().equals(Role.APPROVER_LEVEL_1.name());
  }

  public static boolean isLevelOneApprover(List<String> roles, AuthUser authUser) {
    return roles.isEmpty() && authUser.getRole().equals(Role.APPROVER_LEVEL_1.name());
  }

  public static boolean statusCanNotBeChanged(PurchaseRequest purchaseRequest) {
    return purchaseRequest.getStatus().equals(Status.APPROVED) || purchaseRequest.getStatus().equals(Status.REJECTED);
  }

  public static List<String> getRoles(PurchaseRequest purchaseRequest) {
    List<User> approvers = purchaseRequest.getApprovedBy();
    return approvers.stream().map(User::getRole).toList();
  }
}
