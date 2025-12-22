package org.commitlink.procure.services;

import io.jsonwebtoken.JwtException;
import org.commitlink.procure.models.user.AuthUser;

public interface IJwtService {
  String generateToken(AuthUser user);

  String generateRefreshToken(AuthUser user);

  boolean isTokenValid(String token) throws JwtException;

  String getUsername(String token);
  boolean isTokenExpired(String token) throws JwtException;
}
