package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.DetallePartidoDTO;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.service.DetallePartidoService;
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
@RequestMapping("/detallePartidos")
public class DetallePartidoController {

    private final DetallePartidoService detallePartidoService;
    private final PartidoRepository partidoRepository;

    public DetallePartidoController(final DetallePartidoService detallePartidoService,
            final PartidoRepository partidoRepository) {
        this.detallePartidoService = detallePartidoService;
        this.partidoRepository = partidoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("partidoValues", partidoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Partido::getId, Partido::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("detallePartidoes", detallePartidoService.findAll());
        return "detallePartido/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("detallePartido") final DetallePartidoDTO detallePartidoDTO) {
        return "detallePartido/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("detallePartido") @Valid final DetallePartidoDTO detallePartidoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("partido") && detallePartidoDTO.getPartido() != null && detallePartidoService.partidoExists(detallePartidoDTO.getPartido())) {
            bindingResult.rejectValue("partido", "Exists.detallePartido.partido");
        }
        if (bindingResult.hasErrors()) {
            return "detallePartido/add";
        }
        detallePartidoService.create(detallePartidoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detallePartido.create.success"));
        return "redirect:/detallePartidos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("detallePartido", detallePartidoService.get(id));
        return "detallePartido/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("detallePartido") @Valid final DetallePartidoDTO detallePartidoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final DetallePartidoDTO currentDetallePartidoDTO = detallePartidoService.get(id);
        if (!bindingResult.hasFieldErrors("partido") && detallePartidoDTO.getPartido() != null &&
                !detallePartidoDTO.getPartido().equals(currentDetallePartidoDTO.getPartido()) &&
                detallePartidoService.partidoExists(detallePartidoDTO.getPartido())) {
            bindingResult.rejectValue("partido", "Exists.detallePartido.partido");
        }
        if (bindingResult.hasErrors()) {
            return "detallePartido/edit";
        }
        detallePartidoService.update(id, detallePartidoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detallePartido.update.success"));
        return "redirect:/detallePartidos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        detallePartidoService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("detallePartido.delete.success"));
        return "redirect:/detallePartidos";
    }

}
