package org.commitlink.procure.utils;

import static org.commitlink.procure.utils.Constants.MESSAGE;
import static org.commitlink.procure.utils.Constants.PATH;
import static org.commitlink.procure.utils.Constants.STATUS;
import static org.commitlink.procure.utils.Constants.TIMESTAMP;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class HttpUtil {

  public static void authenticationErrorMessage(
    HttpServletRequest request,
    HttpServletResponse response,
    int status,
    Exception authException,
    ObjectMapper objectMapper
  ) throws IOException {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(MESSAGE, authException.getMessage());
    body.put(STATUS, status);
    body.put(PATH, request.getServletPath());
    body.put(TIMESTAMP, LocalDateTime.now().toString());

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    objectMapper.writeValue(response.getOutputStream(), body);
  }
}
