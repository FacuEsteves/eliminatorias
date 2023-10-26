package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Pais;
import eliminatorias.eliminatorias.model.CiudadDTO;
import eliminatorias.eliminatorias.repos.PaisRepository;
import eliminatorias.eliminatorias.service.CiudadService;
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
@RequestMapping("/ciudads")
public class CiudadController {

    private final CiudadService ciudadService;
    private final PaisRepository paisRepository;

    public CiudadController(final CiudadService ciudadService,
            final PaisRepository paisRepository) {
        this.ciudadService = ciudadService;
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
        model.addAttribute("ciudads", ciudadService.findAll());
        return "ciudad/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("ciudad") final CiudadDTO ciudadDTO) {
        return "ciudad/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("ciudad") @Valid final CiudadDTO ciudadDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("nombre") && ciudadService.nombreExists(ciudadDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.ciudad.nombre");
        }
        if (bindingResult.hasErrors()) {
            return "ciudad/add";
        }
        ciudadService.create(ciudadDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ciudad.create.success"));
        return "redirect:/ciudads";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("ciudad", ciudadService.get(id));
        return "ciudad/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("ciudad") @Valid final CiudadDTO ciudadDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final CiudadDTO currentCiudadDTO = ciudadService.get(id);
        if (!bindingResult.hasFieldErrors("nombre") &&
                !ciudadDTO.getNombre().equalsIgnoreCase(currentCiudadDTO.getNombre()) &&
                ciudadService.nombreExists(ciudadDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.ciudad.nombre");
        }
        if (bindingResult.hasErrors()) {
            return "ciudad/edit";
        }
        ciudadService.update(id, ciudadDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ciudad.update.success"));
        return "redirect:/ciudads";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = ciudadService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            ciudadService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("ciudad.delete.success"));
        }
        return "redirect:/ciudads";
    }

}
