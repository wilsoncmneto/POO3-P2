package model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(length = 120)
    private String formacao;

    @ManyToMany(mappedBy = "professores")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Disciplina> disciplinas;

    @Override
    public String toString() {
        return nome;
    }
}
