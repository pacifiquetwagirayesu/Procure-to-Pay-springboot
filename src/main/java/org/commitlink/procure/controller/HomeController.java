package org.commitlink.procure.controller;

import static org.commitlink.procure.utils.Constants.SWAGGER_URL;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomeController {
  @GetMapping
  public void homePage(HttpServletResponse httpServletResponse) throws IOException {
    httpServletResponse.sendRedirect(SWAGGER_URL);
  }
}
