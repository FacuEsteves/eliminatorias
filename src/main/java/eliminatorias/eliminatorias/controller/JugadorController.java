package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Seleccion;
import eliminatorias.eliminatorias.model.JugadorDTO;
import eliminatorias.eliminatorias.repos.SeleccionRepository;
import eliminatorias.eliminatorias.service.JugadorService;
import eliminatorias.eliminatorias.util.CustomCollectors;
import eliminatorias.eliminatorias.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/jugadors")
public class JugadorController {

    private final JugadorService jugadorService;
    private final SeleccionRepository seleccionRepository;

    public JugadorController(final JugadorService jugadorService,
            final SeleccionRepository seleccionRepository) {
        this.jugadorService = jugadorService;
        this.seleccionRepository = seleccionRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("seleccionValues", seleccionRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Seleccion::getId, Seleccion::getNombre)));

        List<String> pos=new ArrayList<>();
        pos.add("Portero");
        pos.add("Defensa");
        pos.add("Medio");
        pos.add("Delantero");
        model.addAttribute("posValues",pos);

    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("jugadors", jugadorService.findAll());
        return "jugador/list";
    }

    @GetMapping("/listBySeleccion/{id}")
    public String listBySeleccion(@PathVariable final Long id, final Model model) {
        if (id == null)
            model.addAttribute("jugadors", jugadorService.findAll());
        else
            model.addAttribute("jugadors", jugadorService.findSeleccionJugador(id));
        return "jugador/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("jugador") final JugadorDTO jugadorDTO) {
        return "jugador/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("jugador") @Valid final JugadorDTO jugadorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jugador/add";
        }
        jugadorService.create(jugadorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("jugador.create.success"));
        return "redirect:/jugadors";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("jugador", jugadorService.get(id));
        return "jugador/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("jugador") @Valid final JugadorDTO jugadorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jugador/edit";
        }
        jugadorService.update(id, jugadorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("jugador.update.success"));
        return "redirect:/jugadors";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = jugadorService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            jugadorService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("jugador.delete.success"));
        }
        return "redirect:/jugadors";
    }

}
