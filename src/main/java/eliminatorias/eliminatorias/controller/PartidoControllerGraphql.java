package eliminatorias.eliminatorias.controller;


import eliminatorias.eliminatorias.domain.Partido;
import eliminatorias.eliminatorias.model.PartidoDTO;
import eliminatorias.eliminatorias.repos.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PartidoControllerGraphql {

 @Autowired
 private PartidoRepository partidoRepository;

 @QueryMapping
 public List<Partido> listarPartidos() {
  return partidoRepository.findAll();
 }
}