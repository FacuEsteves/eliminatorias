package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.DetallePartidoDTO;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.service.DetallePartidoService;
import eliminatorias.eliminatorias.util.CustomCollectors;
import eliminatorias.eliminatorias.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/detallePartidos")
public class DetallePartidoController {

    @Autowired
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
    public String add(@ModelAttribute("detallePartido") DetallePartidoDTO detallePartidoDTO,
                      @RequestParam(name = "partidoId", required = false) Long partidoId,
                      Model model) {
        if (detallePartidoDTO.isEmpty()) {
            detallePartidoDTO = new DetallePartidoDTO();
        }
        if (partidoId != null) {
            detallePartidoDTO.setPartido(partidoId);
        }

        model.addAttribute("detallePartido", detallePartidoDTO);
        return "detallePartido/add";
    }

    @PostMapping("/add/{partidoId}")
    public String add(@ModelAttribute("detallePartido") @Valid DetallePartidoDTO detallePartidoDTO,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes,
                      @PathVariable final Long partidoId) {
        if (partidoId == null) {
            bindingResult.reject("partidoId", "El partidoId no puede ser nulo.");
            return "redirect:/partidos";
        }

        detallePartidoDTO.setPartido(partidoId);

        if (!bindingResult.hasFieldErrors("partido") && detallePartidoDTO.getPartido() != null && detallePartidoService.partidoExists(detallePartidoDTO.getPartido())) {
            bindingResult.rejectValue("partido", "Exists.detallePartido.partido");
        }
        if (bindingResult.hasErrors()) {
            return "redirect:/detalles/list/"+partidoId.toString();
        }
        detallePartidoService.create(detallePartidoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detallePartido.create.success"));
        return "redirect:/detalles/list/"+partidoId.toString();
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
        String partidoId = currentDetallePartidoDTO.getPartido().toString();
        if (!bindingResult.hasFieldErrors("partido") && detallePartidoDTO.getPartido() != null &&
                !detallePartidoDTO.getPartido().equals(currentDetallePartidoDTO.getPartido()) &&
                detallePartidoService.partidoExists(detallePartidoDTO.getPartido())) {
            bindingResult.rejectValue("partido", "Exists.detallePartido.partido");
        }
        if (bindingResult.hasErrors()) {
            return "redirect:/detalles/list/"+partidoId;
        }
        detallePartidoDTO.setPartido(currentDetallePartidoDTO.getPartido());
        detallePartidoService.update(id, detallePartidoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detallePartido.update.success"));
        return "redirect:/detalles/list/"+detallePartidoDTO.getPartido().toString();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final DetallePartidoDTO currentDetallePartidoDTO = detallePartidoService.get(id);
        String partidoId = currentDetallePartidoDTO.getPartido().toString();
        detallePartidoService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("detallePartido.delete.success"));
        return "redirect:/detalles/list/"+partidoId;
    }

}