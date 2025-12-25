package org.commitlink.procure.utils;

import java.util.List;

public class Constants {

  // SECURITY
  public static final String[] WHITE_LIST_URL = {
    "/api/v1/auth/**",
    "/v2/api-docs",
    "/v3/api-docs",
    "/v3/api-docs/**",
    "/swagger-resources/**",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/swagger-ui/**",
    "/webjars/**",
    "/swagger-ui.html",
    "/api-docs",
    "/swagger-ui/index.html",
    "/error",
    "/",
  };

  public static final List<String> EXEMPT_FOR_AUTH_FILTER = List.of("/", "/swagger-ui/index.html");

  public static final String USER_URLS = "/api/v1/users/**";
  public static final String AUTH_URL = "/api/v1/auth/**";
  public static final String REQUEST_PURCHASE_URL = "/api/v1/requests/**";
  public static final String URL = "url";

  // ROLE AND PERMISSION
  public static final String READ = "read";
  public static final String CREATE = "create";
  public static final String DELETE = "delete";
  public static final String UPLOAD = "upload";
  public static final String APPROVE_ONE = "approve_one";
  public static final String APPROVE_TWO = "approve_two";
  public static final String PREFIX = "ROLE_";
  public static final String INVALID_ROLE_MESSAGE = "Invalid role: '%s' is provided, please select one of: %s";
  public static final String USER_NOT_FOUND_MESSAGE = "user with id: '%s' not found";
  public static final String ROLE = "role";
  public static final String PERMISSIONS = "permissions";

  // USER
  public static final String ADMIN_EMAIL = "admin@commitlink.org";
  public static final String PASSWORD_ADMIN = "admincommitlink";

  // SWAGGER
  public static final String SWAGGER_URL = "/swagger-ui/index.html";

  // API RESPONSE
  public static final String ID = "id";
  public static final String USER_NOT_FOUND = "User not found";
  public static final String PURCHASE_REQUEST_NOT_FOUND = "Purchase request not found";
  public static final String ACCESS_TOKEN = "accessToken";
  public static final String REFRESH_TOKEN = "refreshToken";
  public static final String MESSAGE = "message";
  public static final String STATUS = "status";
  public static final String PATH = "path";
  public static final String TIMESTAMP = "timestamp";

  // HTTP REQUEST
  public static final String INVALID_EMAIL = "Invalid email";
  public static final String EMAIL_REQUIRED = "Invalid email";
  public static final String PASSWORD_REQUIRED = "Password is required";
  public static final String FIRSTNAME_REQUIRED = "First name is required";
  public static final String ROLE_REQUIRED = "First name is required";
  public static final String INVALID_TOKEN = "Invalid Token";
  public static final String MALFORMED_TOKEN = "Malformed Token";
  public static final String BEARER_KEY = "Bearer ";

  // CLOUDINARY
  public static final String CLOUDINARY_UPLOAD_FOLDER = "commitlink/purchase";
  public static final String CLOUD_NAME = "cloud_name";
  public static final String API_KEY = "api_key";
  public static final String SECRET_KEY = "api_secret";
  public static final String SECURE = "secure";
  public static final String FOLDER = "folder";
  public static final String USE_FILENAME = "use_filename";
  public static final String UNIQUE_FILENAME = "unique_filename";
  public static final String OVERWRITE = "overwrite";
  public static final String PUBLIC_ID = "public_id";

  // LOGS
  public static final String ERROR = "Error: {}";
}
