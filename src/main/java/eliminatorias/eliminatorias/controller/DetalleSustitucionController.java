package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.DetalleSustitucionDTO;
import eliminatorias.eliminatorias.model.DetalleTarjetaDTO;
import eliminatorias.eliminatorias.repos.JugadorRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.service.DetalleSustitucionService;
import eliminatorias.eliminatorias.util.CustomCollectors;
import eliminatorias.eliminatorias.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/detalleSustitucions")
public class DetalleSustitucionController {

    private final DetalleSustitucionService detalleSustitucionService;
    private final PartidoRepository partidoRepository;
    private final JugadorRepository jugadorRepository;

    public DetalleSustitucionController(final DetalleSustitucionService detalleSustitucionService,
            final PartidoRepository partidoRepository, final JugadorRepository jugadorRepository) {
        this.detalleSustitucionService = detalleSustitucionService;
        this.partidoRepository = partidoRepository;
        this.jugadorRepository = jugadorRepository;
    }

    @ModelAttribute
    public void prepareContext(@RequestParam(name = "partidoId", required = false) Long partidoId,
                               @RequestParam(name = "seleccionId", required = false) Long seleccionId,
                               final Model model) {
        List<Jugador> jugadors;
        if(partidoId == null || seleccionId == null){
            jugadors = jugadorRepository.findAll();
        }else {
            jugadors = jugadorRepository.obtenerJugadoresPorPartidoYSeleccion(partidoId,seleccionId);
        }
        model.addAttribute("jugadorIngresoValues", jugadors
                .stream()
                .collect(CustomCollectors.toSortedMap(Jugador::getId, Jugador::getNombreCompleto)));
        model.addAttribute("jugadorEgresoValues", jugadors
                .stream()
                .collect(CustomCollectors.toSortedMap(Jugador::getId, Jugador::getNombreCompleto)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("detalleSustitucions", detalleSustitucionService.findAll());
        return "detalleSustitucion/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("detalleSustitucion") DetalleSustitucionDTO detalleSustitucionDTO,
                      @RequestParam(name = "partidoId", required = false) Long partidoId,
                      Model model) {
        detalleSustitucionDTO = new DetalleSustitucionDTO();
        if(partidoId != null){
            detalleSustitucionDTO.setPartido(partidoId);
        }
        model.addAttribute("detalleSustitucion", detalleSustitucionDTO);
        return "detalleSustitucion/add";
    }

    @PostMapping("/add/{partidoId}")
    public String add(
            @ModelAttribute("detalleSustitucion") @Valid final DetalleSustitucionDTO detalleSustitucionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes,
            @PathVariable final Long partidoId) {
        if (partidoId == null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("No se encuentra seleccionado un partido"));
            return "redirect:/partidos";
        }
        detalleSustitucionDTO.setPartido(partidoId);

        if (detalleSustitucionDTO.getJugadorIngreso() == detalleSustitucionDTO.getJugadorEgreso()){
            bindingResult.reject("jugadorEgreso", "No se puede seleccinar el mismo jugador.");
            return "detalleSustitucion/add";
        }

        //verificar que el jugador que ingresa no haya salido antes
        //verificar que el que salió no este afuera

        if (bindingResult.hasErrors()) {
            return "detalleSustitucion/add";
        }
        detalleSustitucionService.create(detalleSustitucionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleSustitucion.create.success"));
        return "redirect:/detalles/list/"+partidoId.toString();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("detalleSustitucion", detalleSustitucionService.get(id));
        return "detalleSustitucion/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("detalleSustitucion") @Valid final DetalleSustitucionDTO detalleSustitucionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "detalleSustitucion/edit";
        }
        detalleSustitucionService.update(id, detalleSustitucionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleSustitucion.update.success"));
        return "redirect:/detalleSustitucions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final DetalleSustitucionDTO currentDetalleSustitucionDTO = detalleSustitucionService.get(id);
        Long partidoId = currentDetalleSustitucionDTO.getPartido();
        detalleSustitucionService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("detalleSustitucion.delete.success"));
        return "redirect:/detalles/list/"+partidoId.toString();
    }

}
