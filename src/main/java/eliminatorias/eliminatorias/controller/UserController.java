package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.User;
import eliminatorias.eliminatorias.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user/")
public class UserController {
    @GetMapping("/")
    public String registrationForm() {
        return "user";
    }

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users")
    public List<User> findAll() {
        return userService.findAll();
    }
}