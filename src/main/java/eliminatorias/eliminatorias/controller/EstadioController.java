package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Ciudad;
import eliminatorias.eliminatorias.model.EstadioDTO;
import eliminatorias.eliminatorias.repos.CiudadRepository;
import eliminatorias.eliminatorias.service.EstadioService;
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
@RequestMapping("/estadios")
public class EstadioController {

    private final EstadioService estadioService;
    private final CiudadRepository ciudadRepository;

    public EstadioController(final EstadioService estadioService,
            final CiudadRepository ciudadRepository) {
        this.estadioService = estadioService;
        this.ciudadRepository = ciudadRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("ciudadValues", ciudadRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Ciudad::getId, Ciudad::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("estadios", estadioService.findAll());
        return "estadio/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("estadio") final EstadioDTO estadioDTO) {
        return "estadio/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("estadio") @Valid final EstadioDTO estadioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("nombre") && estadioService.nombreExists(estadioDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.estadio.nombre");
        }
        if (bindingResult.hasErrors()) {
            return "estadio/add";
        }
        estadioService.create(estadioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("estadio.create.success"));
        return "redirect:/estadios";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("estadio", estadioService.get(id));
        return "estadio/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("estadio") @Valid final EstadioDTO estadioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final EstadioDTO currentEstadioDTO = estadioService.get(id);
        if (!bindingResult.hasFieldErrors("nombre") &&
                !estadioDTO.getNombre().equalsIgnoreCase(currentEstadioDTO.getNombre()) &&
                estadioService.nombreExists(estadioDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.estadio.nombre");
        }
        if (bindingResult.hasErrors()) {
            return "estadio/edit";
        }
        estadioService.update(id, estadioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("estadio.update.success"));
        return "redirect:/estadios";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        estadioService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("estadio.delete.success"));
        return "redirect:/estadios";
    }

}
