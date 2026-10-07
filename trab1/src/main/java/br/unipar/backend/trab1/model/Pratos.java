package br.unipar.backend.trab1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
public class Pratos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomePrato;
    private int qntServe;
    private String origem;
    private double preco;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;



    public Pratos() {

    }
}
