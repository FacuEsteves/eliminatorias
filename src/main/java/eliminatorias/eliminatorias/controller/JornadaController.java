package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.model.JornadaDTO;
import eliminatorias.eliminatorias.service.JornadaService;
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
@RequestMapping("/jornadas")
public class JornadaController {

    private final JornadaService jornadaService;

    public JornadaController(final JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("jornadas", jornadaService.findAll());
        return "jornada/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("jornada") final JornadaDTO jornadaDTO) {
        return "jornada/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("jornada") @Valid final JornadaDTO jornadaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jornada/add";
        }
        jornadaService.create(jornadaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("jornada.create.success"));
        return "redirect:/jornadas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("jornada", jornadaService.get(id));
        return "jornada/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("jornada") @Valid final JornadaDTO jornadaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jornada/edit";
        }
        jornadaService.update(id, jornadaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("jornada.update.success"));
        return "redirect:/jornadas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = jornadaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            jornadaService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("jornada.delete.success"));
        }
        return "redirect:/jornadas";
    }

}
