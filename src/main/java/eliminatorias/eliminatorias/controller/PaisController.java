package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.model.PaisDTO;
import eliminatorias.eliminatorias.service.PaisService;
import eliminatorias.eliminatorias.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/paiss")
public class PaisController {

    private final PaisService paisService;

    public PaisController(final PaisService paisService) {
        this.paisService = paisService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("paises", paisService.findAll());
        return "pais/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("pais") final PaisDTO paisDTO) {
        return "pais/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("pais") @Valid final PaisDTO paisDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("nombre") && paisService.nombreExists(paisDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.pais.nombre");
        }
        if (!bindingResult.hasFieldErrors("abreviacion") && paisService.abreviacionExists(paisDTO.getAbreviacion())) {
            bindingResult.rejectValue("abreviacion", "Exists.pais.abreviacion");
        }
        if (bindingResult.hasErrors()) {
            return "pais/add";
        }
        paisService.create(paisDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pais.create.success"));
        return "redirect:/paiss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("pais", paisService.get(id));
        return "pais/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("pais") @Valid final PaisDTO paisDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        final PaisDTO currentPaisDTO = paisService.get(id);
        if (!bindingResult.hasFieldErrors("nombre") &&
                !paisDTO.getNombre().equalsIgnoreCase(currentPaisDTO.getNombre()) &&
                paisService.nombreExists(paisDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.pais.nombre");
        }
        if (!bindingResult.hasFieldErrors("abreviacion") &&
                !paisDTO.getAbreviacion().equalsIgnoreCase(currentPaisDTO.getAbreviacion()) &&
                paisService.abreviacionExists(paisDTO.getAbreviacion())) {
            bindingResult.rejectValue("abreviacion", "Exists.pais.abreviacion");
        }
        if (bindingResult.hasErrors()) {
            return "pais/edit";
        }
        paisService.update(id, paisDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pais.update.success"));
        return "redirect:/paiss";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = paisService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            paisService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("pais.delete.success"));
        }
        return "redirect:/paiss";
    }

}
