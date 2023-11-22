package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Estadio;
import eliminatorias.eliminatorias.domain.Jornada;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.domain.Seleccion;
import eliminatorias.eliminatorias.model.PartidoDTO;
import eliminatorias.eliminatorias.repos.EstadioRepository;
import eliminatorias.eliminatorias.repos.JornadaRepository;
import eliminatorias.eliminatorias.repos.SeleccionRepository;
import eliminatorias.eliminatorias.service.PartidoService;
import eliminatorias.eliminatorias.util.CustomCollectors;
import eliminatorias.eliminatorias.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/partidos")
public class PartidoController {

    private final PartidoService partidoService;
    private final SeleccionRepository seleccionRepository;
    private final JornadaRepository jornadaRepository;
    private final EstadioRepository estadioRepository;

    public PartidoController(final PartidoService partidoService,
                             final SeleccionRepository seleccionRepository,
                             final JornadaRepository jornadaRepository, final EstadioRepository estadioRepository) {
        this.partidoService = partidoService;
        this.seleccionRepository = seleccionRepository;
        this.jornadaRepository = jornadaRepository;
        this.estadioRepository = estadioRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("seleccionLocalValues", seleccionRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Seleccion::getId, Seleccion::getNombre)));
        model.addAttribute("seleccionVisitanteValues", seleccionRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Seleccion::getId, Seleccion::getNombre)));
        model.addAttribute("jornadaValues", jornadaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Jornada::getId, Jornada::getId)));
        model.addAttribute("estadioValues", estadioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Estadio::getId, Estadio::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("partidoes", partidoService.findAll());
        return "partido/list";
    }

    @GetMapping("/listPartidos/{id}")
    public String listPartidos(@PathVariable final Long id, final Model model) {
        model.addAttribute("partidoes", partidoService.findByJornadaId(id));
        return "partido/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("partido") final PartidoDTO partidoDTO) {
        return "partido/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("partido") @Valid final PartidoDTO partidoDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "partido/add";
        }
        partidoService.create(partidoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("partido.create.success"));
        return "redirect:/partidos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("partido", partidoService.get(id));
        return "partido/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
                       @ModelAttribute("partido") @Valid final PartidoDTO partidoDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "partido/edit";
        }
        partidoService.update(id, partidoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("partido.update.success"));
        return "redirect:/partidos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = partidoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            partidoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("partido.delete.success"));
        }
        return "redirect:/partidos";
    }



}
