package com.continuehub.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping({"/", "/login", "/index", "/index.html"})
    public String index() {
        return "forward:/index.html";
    }
}
