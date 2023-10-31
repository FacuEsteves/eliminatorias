package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.DetalleTarjetaDTO;
import eliminatorias.eliminatorias.repos.JugadorRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.service.DetalleTarjetaService;
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


@Controller
@RequestMapping("/detalleTarjetas")
public class DetalleTarjetaController {

    private final DetalleTarjetaService detalleTarjetaService;
    private final PartidoRepository partidoRepository;
    private final JugadorRepository jugadorRepository;

    public DetalleTarjetaController(final DetalleTarjetaService detalleTarjetaService,
            final PartidoRepository partidoRepository, final JugadorRepository jugadorRepository) {
        this.detalleTarjetaService = detalleTarjetaService;
        this.partidoRepository = partidoRepository;
        this.jugadorRepository = jugadorRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("partidoValues", partidoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Partido::getId, Partido::getId)));
        model.addAttribute("jugadorValues", jugadorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Jugador::getId, Jugador::getNombreCompleto)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("detalleTarjetas", detalleTarjetaService.findAll());
        return "detalleTarjeta/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("detalleTarjeta") final DetalleTarjetaDTO detalleTarjetaDTO) {
        return "detalleTarjeta/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("detalleTarjeta") @Valid final DetalleTarjetaDTO detalleTarjetaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "detalleTarjeta/add";
        }
        detalleTarjetaService.create(detalleTarjetaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleTarjeta.create.success"));
        return "redirect:/detalleTarjetas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("detalleTarjeta", detalleTarjetaService.get(id));
        return "detalleTarjeta/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("detalleTarjeta") @Valid final DetalleTarjetaDTO detalleTarjetaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "detalleTarjeta/edit";
        }
        detalleTarjetaService.update(id, detalleTarjetaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleTarjeta.update.success"));
        return "redirect:/detalleTarjetas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        detalleTarjetaService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("detalleTarjeta.delete.success"));
        return "redirect:/detalleTarjetas";
    }

}
