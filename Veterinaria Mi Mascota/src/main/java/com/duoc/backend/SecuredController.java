package com.duoc.backend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
// 1. Agregar este import
import org.springframework.web.util.HtmlUtils;

@RestController
public class SecuredController {

    @RequestMapping("greetings")
    public String greetings(@RequestParam(value="name", defaultValue="World") String name) {
        // 2. Sanitizar la entrada del usuario
        String cleanName = HtmlUtils.htmlEscape(name); 
        // 3. Devolver la variable ya sanitizada
        return "Hello {" + cleanName + "}";
    }
}   