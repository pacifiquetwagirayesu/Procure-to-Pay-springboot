package org.commitlink.procure.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static org.commitlink.procure.utils.Constants.SWAGGER_URL;

@Controller
@RequestMapping
public class HomeController {
    @GetMapping
    public void homePage(HttpServletResponse httpServletResponse) throws IOException {
//        System.out.println("httpServletResponse = " + httpServletResponse);
        httpServletResponse.sendRedirect(SWAGGER_URL);

    }
}
