package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Pais;
import eliminatorias.eliminatorias.model.ArbitroDTO;
import eliminatorias.eliminatorias.repos.PaisRepository;
import eliminatorias.eliminatorias.service.ArbitroService;
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
@RequestMapping("/arbitros")
public class ArbitroController {

    private final ArbitroService arbitroService;
    private final PaisRepository paisRepository;

    public ArbitroController(final ArbitroService arbitroService,
            final PaisRepository paisRepository) {
        this.arbitroService = arbitroService;
        this.paisRepository = paisRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("paisValues", paisRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Pais::getId, Pais::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("arbitroes", arbitroService.findAll());
        return "arbitro/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("arbitro") final ArbitroDTO arbitroDTO) {
        return "arbitro/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("arbitro") @Valid final ArbitroDTO arbitroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("pais") && arbitroDTO.getPais() != null && arbitroService.paisExists(arbitroDTO.getPais())) {
            bindingResult.rejectValue("pais", "Exists.arbitro.pais");
        }
        if (bindingResult.hasErrors()) {
            return "arbitro/add";
        }
        arbitroService.create(arbitroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("arbitro.create.success"));
        return "redirect:/arbitros";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("arbitro", arbitroService.get(id));
        return "arbitro/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("arbitro") @Valid final ArbitroDTO arbitroDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final ArbitroDTO currentArbitroDTO = arbitroService.get(id);
        if (!bindingResult.hasFieldErrors("pais") && arbitroDTO.getPais() != null &&
                !arbitroDTO.getPais().equals(currentArbitroDTO.getPais()) &&
                arbitroService.paisExists(arbitroDTO.getPais())) {
            bindingResult.rejectValue("pais", "Exists.arbitro.pais");
        }
        if (bindingResult.hasErrors()) {
            return "arbitro/edit";
        }
        arbitroService.update(id, arbitroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("arbitro.update.success"));
        return "redirect:/arbitros";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        arbitroService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("arbitro.delete.success"));
        return "redirect:/arbitros";
    }

}
