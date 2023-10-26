package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Pais;
import eliminatorias.eliminatorias.model.SeleccionDTO;
import eliminatorias.eliminatorias.repos.PaisRepository;
import eliminatorias.eliminatorias.service.SeleccionService;
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
@RequestMapping("/seleccions")
public class SeleccionController {

    private final SeleccionService seleccionService;
    private final PaisRepository paisRepository;

    public SeleccionController(final SeleccionService seleccionService,
            final PaisRepository paisRepository) {
        this.seleccionService = seleccionService;
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
        model.addAttribute("seleccions", seleccionService.findAll());
        return "seleccion/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("seleccion") final SeleccionDTO seleccionDTO) {
        return "seleccion/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("seleccion") @Valid final SeleccionDTO seleccionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("nombre") && seleccionService.nombreExists(seleccionDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.seleccion.nombre");
        }
        if (!bindingResult.hasFieldErrors("pais") && seleccionDTO.getPais() != null && seleccionService.paisExists(seleccionDTO.getPais())) {
            bindingResult.rejectValue("pais", "Exists.seleccion.pais");
        }
        if (bindingResult.hasErrors()) {
            return "seleccion/add";
        }
        seleccionService.create(seleccionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("seleccion.create.success"));
        return "redirect:/seleccions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("seleccion", seleccionService.get(id));
        return "seleccion/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("seleccion") @Valid final SeleccionDTO seleccionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final SeleccionDTO currentSeleccionDTO = seleccionService.get(id);
        if (!bindingResult.hasFieldErrors("nombre") &&
                !seleccionDTO.getNombre().equalsIgnoreCase(currentSeleccionDTO.getNombre()) &&
                seleccionService.nombreExists(seleccionDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.seleccion.nombre");
        }
        if (!bindingResult.hasFieldErrors("pais") && seleccionDTO.getPais() != null &&
                !seleccionDTO.getPais().equals(currentSeleccionDTO.getPais()) &&
                seleccionService.paisExists(seleccionDTO.getPais())) {
            bindingResult.rejectValue("pais", "Exists.seleccion.pais");
        }
        if (bindingResult.hasErrors()) {
            return "seleccion/edit";
        }
        seleccionService.update(id, seleccionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("seleccion.update.success"));
        return "redirect:/seleccions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = seleccionService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            seleccionService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("seleccion.delete.success"));
        }
        return "redirect:/seleccions";
    }

}
