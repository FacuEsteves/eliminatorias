package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.DetallePartido;
import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.model.DetalleSustitucionDTO;
import eliminatorias.eliminatorias.repos.DetallePartidoRepository;
import eliminatorias.eliminatorias.repos.JugadorRepository;
import eliminatorias.eliminatorias.service.DetalleSustitucionService;
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
@RequestMapping("/detalleSustitucions")
public class DetalleSustitucionController {

    private final DetalleSustitucionService detalleSustitucionService;
    private final DetallePartidoRepository detallePartidoRepository;
    private final JugadorRepository jugadorRepository;

    public DetalleSustitucionController(final DetalleSustitucionService detalleSustitucionService,
            final DetallePartidoRepository detallePartidoRepository,
            final JugadorRepository jugadorRepository) {
        this.detalleSustitucionService = detalleSustitucionService;
        this.detallePartidoRepository = detallePartidoRepository;
        this.jugadorRepository = jugadorRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("detallePartidoValues", detallePartidoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(DetallePartido::getId, DetallePartido::getId)));
        model.addAttribute("jugadorIngresoValues", jugadorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Jugador::getId, Jugador::getNombre)));
        model.addAttribute("jugadorEgresoValues", jugadorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Jugador::getId, Jugador::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("detalleSustitucions", detalleSustitucionService.findAll());
        return "detalleSustitucion/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("detalleSustitucion") final DetalleSustitucionDTO detalleSustitucionDTO) {
        return "detalleSustitucion/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("detalleSustitucion") @Valid final DetalleSustitucionDTO detalleSustitucionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "detalleSustitucion/add";
        }
        detalleSustitucionService.create(detalleSustitucionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleSustitucion.create.success"));
        return "redirect:/detalleSustitucions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("detalleSustitucion", detalleSustitucionService.get(id));
        return "detalleSustitucion/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("detalleSustitucion") @Valid final DetalleSustitucionDTO detalleSustitucionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "detalleSustitucion/edit";
        }
        detalleSustitucionService.update(id, detalleSustitucionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleSustitucion.update.success"));
        return "redirect:/detalleSustitucions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        detalleSustitucionService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("detalleSustitucion.delete.success"));
        return "redirect:/detalleSustitucions";
    }

}
