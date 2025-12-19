package org.commitlink.procure.utils;

public class Constants {

  // SECURITY
  public static final String[] WHITE_LIST_URL = {
    "/api/v1/auth/**",
    "/v2/api-docs",
    "/v3/api-docs",
    "/v3/api-docs/**",
    "/swagger-resources",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/swagger-ui/**",
    "/webjars/**",
    "/swagger-ui.html",
    "/api-docs",
    "swagger-ui/index.html#",
    "/*",
  };

  public static final String USER_REGISTER_URL = "/api/v1/users/register";
  public static final String AUTH_URL = "/api/v1/auth/*";

  // ROLE AND PERMISSION
  public static final String READ = "read";
  public static final String CREATE = "create";
  public static final String UPLOAD = "upload";
  public static final String APPROVE_ONE = "approve_one";
  public static final String APPROVE_TWO = "approve_two";
  public static final String STAFF = "staff";
  public static final String PREFIX = "ROLE_";
  public static final String INVALID_ROLE_MESSAGE = "Invalid role: '%s' is provided, please select one of: %s";
  public static final String USER_NOT_FOUND_MESSAGE = "user with id: '%s' not found";
  public static final String ROLE = "role";
  public static final String PERMISSIONS = "permissions";

  // SWAGGER
  public static final String SWAGGER_URL = "/swagger-ui/index.html";

  // API RESPONSE
  public static final String ID = "id";
  public static final String USER_NOT_FOUND = "User not found";
  public static final String ACCESS_TOKEN = "accessToken";
  public static final String REFRESH_TOKEN = "refreshToken";

  // HTTP REQUEST
  public static final String INVALID_EMAIL = "Invalid email";
  public static final String EMAIL_REQUIRED = "Invalid email";
  public static final String PASSWORD_REQUIRED = "Password is required";
  public static final String FIRSTNAME_REQUIRED = "First name is required";
  public static final String ROLE_REQUIRED = "First name is required";
  public static final String TOKEN_NOT_FOUND = "Token not found";
  public static final String INVALID_TOKEN = "Invalid Token";
  public static final String MALFORMED_TOKEN = "Malformed Token";
}
