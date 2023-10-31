package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Arbitro;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.DetalleArbitroDTO;
import eliminatorias.eliminatorias.repos.ArbitroRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.service.DetalleArbitroService;
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
@RequestMapping("/detalleArbitros")
public class DetalleArbitroController {

    private final DetalleArbitroService detalleArbitroService;
    private final PartidoRepository partidoRepository;
    private final ArbitroRepository arbitroRepository;

    public DetalleArbitroController(final DetalleArbitroService detalleArbitroService,
            final PartidoRepository partidoRepository, final ArbitroRepository arbitroRepository) {
        this.detalleArbitroService = detalleArbitroService;
        this.partidoRepository = partidoRepository;
        this.arbitroRepository = arbitroRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("partidoValues", partidoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Partido::getId, Partido::getId)));
        model.addAttribute("arbitroValues", arbitroRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Arbitro::getId, Arbitro::getNombreCompleto)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("detalleArbitroes", detalleArbitroService.findAll());
        return "detalleArbitro/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("detalleArbitro") final DetalleArbitroDTO detalleArbitroDTO) {
        return "detalleArbitro/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("detalleArbitro") @Valid final DetalleArbitroDTO detalleArbitroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "detalleArbitro/add";
        }
        detalleArbitroService.create(detalleArbitroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleArbitro.create.success"));
        return "redirect:/detalleArbitros";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("detalleArbitro", detalleArbitroService.get(id));
        return "detalleArbitro/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("detalleArbitro") @Valid final DetalleArbitroDTO detalleArbitroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "detalleArbitro/edit";
        }
        detalleArbitroService.update(id, detalleArbitroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleArbitro.update.success"));
        return "redirect:/detalleArbitros";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        detalleArbitroService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("detalleArbitro.delete.success"));
        return "redirect:/detalleArbitros";
    }

}
