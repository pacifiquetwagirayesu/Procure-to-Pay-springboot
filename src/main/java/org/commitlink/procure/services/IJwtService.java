package org.commitlink.procure.services;

import io.jsonwebtoken.JwtException;
import org.commitlink.procure.models.AuthUser;

public interface IJwtService {
  String generateToken(AuthUser user);

  String generateRefreshToken(AuthUser user);

  boolean isTokenValid(String token, AuthUser user) throws JwtException;

  String getUsername(String token);
}
