package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.service.InicioService;
import eliminatorias.eliminatorias.service.JornadaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inicio")
public class InicioController {
    private final JornadaService jornadaService;

    public InicioController(InicioService inicioService, JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public String list(final Model model){
        model.addAttribute("jornadas", jornadaService.findAll());
        return "home/list";
    }
}
