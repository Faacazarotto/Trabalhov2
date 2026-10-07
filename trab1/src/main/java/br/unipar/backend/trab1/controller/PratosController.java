package br.unipar.backend.trab1.controller;

import br.unipar.backend.trab1.model.Pratos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/pratos")
public class PratosController {
    List<Pratos> pratos = new ArrayList<>();

    //CRIAR PRATO
    @PostMapping("/{id}/criarPrato")
    public ResponseEntity<Pratos> criarPrato(@PathVariable int id, @RequestBody(required = false) Pratos prato){
        if(pratos == null){
            return ResponseEntity.badRequest().build();
        }



        pratos.add(prato);
        return ResponseEntity.ok(prato);
    }

    //EDITAR PRATO
    @PutMapping("/editPrato/{Id}")
    public ResponseEntity editPrato(@PathVariable int Id, @RequestBody Pratos prato){
        Pratos p = pratos.get(Id);
        p.setId(prato.getId());
        p.setNomePrato(prato.getNomePrato());
        p.setOrigem(prato.getOrigem());
        p.setPreco(prato.getPreco());
        p.setQntServe(prato.getQntServe());

        pratos.set(Id, p);
        return ResponseEntity.ok(p);


    }

    //DELETAR PRATO
    @DeleteMapping("/deletePrato/{Id}")
    public ResponseEntity deletePrato(@PathVariable int Id){
        pratos.remove(Id);
        return ResponseEntity.ok("Prato da posição " + Id + " deletado com sucesso!");

    }

    //LISTAR PRATO
    @GetMapping("/listarPrato")
    public ResponseEntity<List<Pratos>> listarPratos(){
        return ResponseEntity.ok(pratos);
    }

}
