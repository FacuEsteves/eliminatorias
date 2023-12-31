package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.service.JornadaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    private final JornadaService jornadaService;

    public HomeController(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping("/")
    public String list(final Model model){
        model.addAttribute("jornadas", jornadaService.findAll());
        return "home/list";
    }
    /*public String index() {
        return "home/index";
    }*/

}
