package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.domain.Jugador;
import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.DetalleTarjetaDTO;
import eliminatorias.eliminatorias.model.JugadorDTO;
import eliminatorias.eliminatorias.repos.JugadorRepository;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import eliminatorias.eliminatorias.service.DetalleTarjetaService;
import eliminatorias.eliminatorias.service.JugadorService;
import eliminatorias.eliminatorias.util.CustomCollectors;
import eliminatorias.eliminatorias.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/detalleTarjetas")
public class DetalleTarjetaController {

    private final DetalleTarjetaService detalleTarjetaService;
    private final PartidoRepository partidoRepository;
    private final JugadorRepository jugadorRepository;
    private final JugadorService jugadorService;

    public DetalleTarjetaController(final DetalleTarjetaService detalleTarjetaService,
                                    final PartidoRepository partidoRepository, final JugadorRepository jugadorRepository, JugadorService jugadorService) {
        this.detalleTarjetaService = detalleTarjetaService;
        this.partidoRepository = partidoRepository;
        this.jugadorRepository = jugadorRepository;
        this.jugadorService = jugadorService;
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
        model.addAttribute("jugadorValues", jugadors
                .stream()
                .collect(CustomCollectors.toSortedMap(Jugador::getId, Jugador::getNombreCompleto)));

        List<String> colores = new ArrayList<>();
        colores.add("Amarilla");
        colores.add("Roja");
        model.addAttribute("colorValues", colores);
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("detalleTarjetas", detalleTarjetaService.findAll());
        return "detalleTarjeta/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("detalleTarjeta") DetalleTarjetaDTO detalleTarjetaDTO,
                      @RequestParam(name = "partidoId", required = false) Long partidoId,
                      Model model) {
        detalleTarjetaDTO = new DetalleTarjetaDTO();

        if(partidoId != null){
            detalleTarjetaDTO.setPartido(partidoId);
        }
        model.addAttribute("detalleTarjeta", detalleTarjetaDTO);
        return "detalleTarjeta/add";
    }

    @PostMapping("/add/{partidoId}")
    public String add(
            @ModelAttribute("detalleTarjeta") @Valid final DetalleTarjetaDTO detalleTarjetaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes,
            @PathVariable final Long partidoId) {
        if (partidoId == null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("No se encuentra seleccionado un partido"));
            return "redirect:/partidos";
        }
        detalleTarjetaDTO.setPartido(partidoId);

        if (detalleTarjetaDTO.getJugador() == null){
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("No se asocio un jugador a la tarjeta."));
            //bindingResult.reject("jugador", "No se selecciono el jugador.");
            return "redirect:/detalles/list/"+partidoId.toString();
        }

        //debería mostrar el error en la misma pantalla de agregar o editar, pero al estar cargando los datos de jugadores con parametros no se cargan bien los jugadores

        if (bindingResult.hasErrors()) {
            return "redirect:/detalles/list/"+partidoId.toString();
        }
        detalleTarjetaService.create(detalleTarjetaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleTarjeta.create.success"));
        return "redirect:/detalles/list/"+partidoId.toString();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("detalleTarjeta", detalleTarjetaService.get(id));
        return "detalleTarjeta/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("detalleTarjeta") @Valid final DetalleTarjetaDTO detalleTarjetaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final DetalleTarjetaDTO currentDetalleTarjetaDTO = detalleTarjetaService.get(id);
        Long partidoId = currentDetalleTarjetaDTO.getPartido();

        //consultar si ya tenia roja
        //consultar si ya tenia dos amarillas antes

        if (bindingResult.hasErrors()) {
            return "detalleTarjeta/edit/"+id.toString();
        }

        detalleTarjetaDTO.setJugador(currentDetalleTarjetaDTO.getJugador());
        detalleTarjetaDTO.setPartido(partidoId);
        detalleTarjetaService.update(id, detalleTarjetaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("detalleTarjeta.update.success"));
        return "redirect:/detalles/list/"+partidoId.toString();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final DetalleTarjetaDTO currentDetalleTarjetaDTO = detalleTarjetaService.get(id);
        Long partidoId = currentDetalleTarjetaDTO.getPartido();
        detalleTarjetaService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("detalleTarjeta.delete.success"));
        return "redirect:/detalles/list/"+partidoId.toString();
    }
}
