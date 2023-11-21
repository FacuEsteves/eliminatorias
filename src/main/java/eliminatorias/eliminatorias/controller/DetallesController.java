package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/detalles")
public class DetallesController {

    private final DetallesService detallesService;

    public DetallesController(DetallesService detallesService) {
        this.detallesService = detallesService;
    }

    @GetMapping("/list/{idPartido}")
    public String list(@PathVariable final Long idPartido, final Model model) {
        model.addAttribute("detalles",
                detallesService.findByPartido(idPartido));
        return "detalles/list";
    }

    @GetMapping("/listpublic/{idPartido}")
    public String listPublic(@PathVariable final Long idPartido, final Model model) {
        model.addAttribute("detalles",
                detallesService.findByPartido(idPartido));
        return "home/details";
    }
}
