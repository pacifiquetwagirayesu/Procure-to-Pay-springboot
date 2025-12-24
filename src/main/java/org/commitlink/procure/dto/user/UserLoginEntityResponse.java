package org.commitlink.procure.dto.user;

import java.util.Map;

public record UserLoginEntityResponse(AuthUserResponseEntity user, Map<String, String> authTokens) {}
