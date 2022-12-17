package calendar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

    @RequestMapping(value = "sayhi", method = RequestMethod.GET)
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("Hi from calendar project");
    }
}
