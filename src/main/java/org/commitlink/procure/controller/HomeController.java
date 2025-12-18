package org.commitlink.procure.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.commitlink.procure.services.impl.JwtService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static org.commitlink.procure.utils.Constants.SWAGGER_URL;

@Controller
@RequestMapping
@AllArgsConstructor
public class HomeController {
    private final JwtService jwtService;

    @GetMapping
    public void homePage(HttpServletResponse httpServletResponse) throws IOException {
        System.out.println("jwtService = " + jwtService.getJwtSecretKey());
        httpServletResponse.sendRedirect(SWAGGER_URL);

    }
}
