package br.unipar.backend.trab1.controller;

import br.unipar.backend.trab1.model.Pratos;
import br.unipar.backend.trab1.model.Restaurante;
import br.unipar.backend.trab1.repository.RestauranteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;

    public RestauranteController(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    // CRIAR RESTAURANTE
    @PostMapping("/criarRestaurante")
    public ResponseEntity<Restaurante> criarRestaurante(
            @RequestBody Restaurante restaurante) {

        if (restaurante == null) {
            return ResponseEntity.badRequest().build();
        }

        Restaurante novoRestaurante =
                restauranteRepository.save(restaurante);

        return ResponseEntity.ok(novoRestaurante);
    }


    // CRIAR PRATO
    @PostMapping("/{id}/adicionarPrato")
    public ResponseEntity<Pratos> criarPrato(
            @PathVariable Long id,
            @RequestBody Pratos prato) {

        Restaurante restaurante =
                restauranteRepository.findById(id).orElse(null);

        if (restaurante == null) {
            return ResponseEntity.notFound().build();
        }

        prato.setRestaurante(restaurante);

        restaurante.getMenu().add(prato);

        restauranteRepository.save(restaurante);

        return ResponseEntity.ok(prato);
    }


    // EDITAR RESTAURANTE
    @PutMapping("/editRestaurante/{idRest}")
    public ResponseEntity<Restaurante> editRestaurante(
            @PathVariable Long idRest,
            @RequestBody Restaurante restaurante) {

        Restaurante r =
                restauranteRepository.findById(idRest).orElse(null);

        if (r == null) {
            return ResponseEntity.notFound().build();
        }

        r.setNomeRest(restaurante.getNomeRest());
        r.setCntt(restaurante.getCntt());

        Restaurante atualizado =
                restauranteRepository.save(r);

        return ResponseEntity.ok(atualizado);
    }


    // EDITAR PRATO
    @PutMapping("/{idRest}/editPrato/{idPrato}")
    public ResponseEntity<Pratos> editarPrato(
            @PathVariable Long idRest,
            @PathVariable Long idPrato,
            @RequestBody Pratos prato) {

        Restaurante restaurante =
                restauranteRepository.findById(idRest).orElse(null);

        if (restaurante == null) {
            return ResponseEntity.notFound().build();
        }

        for (Pratos p : restaurante.getMenu()) {

            if (p.getId().equals(idPrato)) {

                p.setNomePrato(prato.getNomePrato());
                p.setOrigem(prato.getOrigem());
                p.setPreco(prato.getPreco());
                p.setQntServe(prato.getQntServe());

                restauranteRepository.save(restaurante);

                return ResponseEntity.ok(p);
            }
        }

        return ResponseEntity.notFound().build();
    }


    // DELETAR RESTAURANTE
    @DeleteMapping("/deleteRestaurante/{idRest}")
    public ResponseEntity<String> deleteRestaurante(
            @PathVariable Long idRest) {

        if (!restauranteRepository.existsById(idRest)) {
            return ResponseEntity.notFound().build();
        }

        restauranteRepository.deleteById(idRest);

        return ResponseEntity.ok(
                "Restaurante de ID " + idRest +
                        " deletado com sucesso!"
        );
    }


    // DELETAR PRATO
    @DeleteMapping("/{idRest}/deletarPrato/{idPrato}")
    public ResponseEntity<String> deletarPrato(
            @PathVariable Long idRest,
            @PathVariable Long idPrato) {

        Restaurante restaurante =
                restauranteRepository.findById(idRest).orElse(null);

        if (restaurante == null) {
            return ResponseEntity.notFound().build();
        }

        for (Pratos prato : restaurante.getMenu()) {

            if (prato.getId().equals(idPrato)) {

                String nome = prato.getNomePrato();

                restaurante.getMenu().remove(prato);

                restauranteRepository.save(restaurante);

                return ResponseEntity.ok(
                        "Prato deletado com sucesso! " +
                                "ID: " + idPrato +
                                " | Nome: " + nome
                );
            }
        }

        return ResponseEntity.notFound().build();
    }


    // LISTAR RESTAURANTES
    @GetMapping("/listarRestaurante")
    public ResponseEntity<List<Restaurante>> listarRestaurantes() {

        return ResponseEntity.ok(
                restauranteRepository.findAll()
        );
    }


    // LISTAR PRATOS COM 3 FILTROS
    @GetMapping("/{idRest}/listarPratos")
    public ResponseEntity<List<Pratos>> listarPratos(
            @PathVariable Long idRest,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) Double precoMax) {

        Restaurante restaurante =
                restauranteRepository.findById(idRest).orElse(null);

        if (restaurante == null) {
            return ResponseEntity.notFound().build();
        }

        List<Pratos> resultado = restaurante.getMenu()
                .stream()
                .filter(prato ->
                        nome == null ||
                                prato.getNomePrato()
                                        .toLowerCase()
                                        .contains(nome.toLowerCase())
                )
                .filter(prato ->
                        origem == null ||
                                prato.getOrigem()
                                        .equalsIgnoreCase(origem)
                )
                .filter(prato ->
                        precoMax == null ||
                                prato.getPreco() <= precoMax
                )
                .toList();

        return ResponseEntity.ok(resultado);
    }
}