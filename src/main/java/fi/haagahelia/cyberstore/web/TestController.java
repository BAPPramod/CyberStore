package fi.haagahelia.cyberstore.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping("/test-auth")
    @ResponseBody
    public String testAuth(Authentication auth) {
        if (auth == null) {
            return "Not authenticated";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Authenticated as: ").append(auth.getName()).append("<br/>");
        sb.append("Authorities: ").append(auth.getAuthorities()).append("<br/>");
        sb.append("Principal: ").append(auth.getPrincipal()).append("<br/>");
        sb.append("Details: ").append(auth.getDetails()).append("<br/>");

        return sb.toString();
    }
}