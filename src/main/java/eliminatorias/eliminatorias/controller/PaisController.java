package eliminatorias.eliminatorias.controller;

import eliminatorias.eliminatorias.model.PaisDTO;
import eliminatorias.eliminatorias.service.PaisService;
import eliminatorias.eliminatorias.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


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
                      @RequestParam(value="file", required = false) MultipartFile bandera,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) throws IOException {
        if (!bindingResult.hasFieldErrors("nombre") && paisService.nombreExists(paisDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.pais.nombre");
        }
        if (!bindingResult.hasFieldErrors("abreviacion") && paisService.abreviacionExists(paisDTO.getAbreviacion())) {
            bindingResult.rejectValue("abreviacion", "Exists.pais.abreviacion");
        }
        if (bandera!=null && !bandera.isEmpty()) {
            paisDTO.setBandera(bandera.getBytes());
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
                       @RequestParam(value="file",required = false) MultipartFile bandera,
            final RedirectAttributes redirectAttributes) throws IOException {
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
        if(bandera==null){
            paisDTO.setBandera(currentPaisDTO.getBandera());
        }
        if (bandera!=null && !bandera.isEmpty()) {
            paisDTO.setBandera(bandera.getBytes());
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

    @GetMapping("/imagen/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> obtenerImagen( @PathVariable Long id) {
        byte[] imagenBytes = paisService.obtenerImagen(id);

        if (imagenBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
