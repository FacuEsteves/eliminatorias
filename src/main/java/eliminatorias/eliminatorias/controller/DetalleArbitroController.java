package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Arbitro;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.domain.TipoArbitro;
import eliminatorias.eliminatorias.model.DetalleArbitroDTO;
import eliminatorias.eliminatorias.model.DetallePartidoDTO;
import eliminatorias.eliminatorias.repos.ArbitroRepository;
import eliminatorias.eliminatorias.repos.DetalleArbitroRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.repos.TipoArbitroRepository;
import eliminatorias.eliminatorias.service.DetalleArbitroService;
import eliminatorias.eliminatorias.util.CustomCollectors;
import eliminatorias.eliminatorias.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;


@Controller
@RequestMapping("/detalleArbitros")
public class DetalleArbitroController {

    private final DetalleArbitroService detalleArbitroService;
    private final DetalleArbitroRepository detalleArbitroRepository;
    private final PartidoRepository partidoRepository;
    private final ArbitroRepository arbitroRepository;
    private final TipoArbitroRepository tipoArbitroRepository;

    public DetalleArbitroController(final DetalleArbitroService detalleArbitroService,
                                    DetalleArbitroRepository detalleArbitroRepository, final PartidoRepository partidoRepository, final ArbitroRepository arbitroRepository, TipoArbitroRepository tipoArbitroRepository) {
        this.detalleArbitroService = detalleArbitroService;
        this.detalleArbitroRepository = detalleArbitroRepository;
        this.partidoRepository = partidoRepository;
        this.arbitroRepository = arbitroRepository;
        this.tipoArbitroRepository = tipoArbitroRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("partidoValues", partidoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Partido::getId, Partido::getId)));
        model.addAttribute("arbitroValues", arbitroRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Arbitro::getId, Arbitro::getNombreCompleto)));
        model.addAttribute("tipoArbitroValues", tipoArbitroRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(TipoArbitro::getId, TipoArbitro::getTipo)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("detalleArbitroes", detalleArbitroService.findAll());
        return "detalleArbitro/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("detalleArbitro") DetalleArbitroDTO detalleArbitroDTO,
                      @RequestParam(name = "partidoId", required = false) Long partidoId,
                      Model model) {
        detalleArbitroDTO = new DetalleArbitroDTO();

        if (partidoId != null) {
            detalleArbitroDTO.setPartido(partidoId);
        }

        model.addAttribute("detalleArbitro", detalleArbitroDTO);
        return "detalleArbitro/add";
    }

    @PostMapping("/add/{partidoId}")
    public String add(@ModelAttribute("detalleArbitro") @Valid DetalleArbitroDTO detalleArbitroDTO,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes,
                      @PathVariable final Long partidoId) {
        Integer arbitro = detalleArbitroRepository.buscarArbitroEnPartido(partidoId, detalleArbitroDTO.getArbitro());
        Integer tipoArbitro = detalleArbitroRepository.buscarTipoArbitroEnPartido(partidoId, detalleArbitroDTO.getTipoArbitro());

        if (partidoId == null) {
            bindingResult.reject("partidoId", "El partidoId no puede ser nulo.");
            return "redirect:/partidos";
        }

        if (arbitro != 0){
            bindingResult.reject("arbitroId", "Ya existe el arbitro en este partido");
            return "redirect:/detalles/list/"+partidoId.toString();
        }

        if (tipoArbitro != 0){
            bindingResult.reject("tipoArbitroId", "Ya existe este tipo de arbitro en este partido");
            return "redirect:/detalles/list/"+partidoId.toString();
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/detalles/list/"+partidoId.toString();
        }

        detalleArbitroDTO.setPartido(partidoId);
        detalleArbitroService.create(detalleArbitroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleArbitro.create.success"));
        return "redirect:/detalles/list/"+partidoId.toString();
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
        final DetalleArbitroDTO currentDetalleArbitroDTO = detalleArbitroService.get(id);
        Long partidoId = currentDetalleArbitroDTO.getPartido();
        Integer arbitro = detalleArbitroRepository.buscarArbitroEnPartido(partidoId, detalleArbitroDTO.getArbitro());
        Integer tipoArbitro = detalleArbitroRepository.buscarTipoArbitroEnPartido(partidoId, detalleArbitroDTO.getTipoArbitro());

        if (partidoId == null) {
            bindingResult.reject("partidoId", "El partidoId no puede ser nulo.");
            return "redirect:/partidos";
        }

        if (!Objects.equals(currentDetalleArbitroDTO.getArbitro(), detalleArbitroDTO.getArbitro())) {
            if (arbitro != 0){
                bindingResult.reject("arbitroId", "Ya existe el arbitro en este partido");
                return "redirect:/detalles/list/"+partidoId.toString();
            }
        }

        if (currentDetalleArbitroDTO.getTipoArbitro() != detalleArbitroDTO.getTipoArbitro()){
            if (tipoArbitro != 0){
                bindingResult.reject("tipoArbitroId", "Ya existe este tipo de arbitro en este partido");
                return "redirect:/detalles/list/"+partidoId.toString();
            }
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/detalles/list/"+partidoId.toString();
        }
        detalleArbitroDTO.setPartido(partidoId);
        detalleArbitroService.update(id, detalleArbitroDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleArbitro.update.success"));
        return "redirect:/detalles/list/"+partidoId.toString();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final DetalleArbitroDTO currentDetalleArbitroDTO = detalleArbitroService.get(id);
        String partidoId = currentDetalleArbitroDTO.getPartido().toString();
        detalleArbitroService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("detalleArbitro.delete.success"));
        return "redirect:/detalles/list/"+partidoId;
    }

}