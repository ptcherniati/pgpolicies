package fr.ptcherniati.pg_policies.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HomeResources {

    @GetMapping(value = "/")
    public RedirectView home() {
        RedirectView result = new RedirectView();
        result.setContextRelative(true);
        result.setUrl("/static/index.html");
        return result;
    }

    @GetMapping(value = "/api")
    public RedirectView swagger() {
        RedirectView result = new RedirectView();
        result.setContextRelative(true);
        result.setUrl("/swagger-ui.html");
        return result;
    }
}
