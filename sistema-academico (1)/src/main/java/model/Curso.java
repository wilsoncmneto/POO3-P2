package model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @OneToMany(mappedBy = "curso")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Disciplina> disciplinas;

    @Override
    public String toString() {
        return nome;
    }
}
