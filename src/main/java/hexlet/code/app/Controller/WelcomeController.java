package hexlet.code.app.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome() {
         return "Welcome to Spring";
    }
}
git add --all && git commit -m "your commit"