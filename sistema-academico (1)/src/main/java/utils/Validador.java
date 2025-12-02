package utils;

import model.Curso;
import model.Disciplina;
import model.Professor;
import model.Turma;

public class Validador {

    public static String validar(Curso c) {
        if (c.getNome() == null || c.getNome().isBlank()) {
            return "Nome do curso é obrigatório.";
        }
        if (c.getCargaHoraria() == null || c.getCargaHoraria() <= 0) {
            return "Carga horária deve ser maior que zero.";
        }
        return null;
    }

    public static String validar(Professor p) {
        if (p.getNome() == null || p.getNome().isBlank()) {
            return "Nome do professor é obrigatório.";
        }
        if (p.getEmail() == null || p.getEmail().isBlank()) {
            return "E-mail é obrigatório.";
        }
        return null;
    }

    public static String validar(Disciplina d) {
        if (d.getNome() == null || d.getNome().isBlank()) {
            return "Nome da disciplina é obrigatório.";
        }
        if (d.getCurso() == null) {
            return "Selecione um curso para a disciplina.";
        }
        return null;
    }

    public static String validar(Turma t) {
        if (t.getSemestre() == null || t.getSemestre().isBlank()) {
            return "Semestre é obrigatório.";
        }
        if (t.getDisciplina() == null) {
            return "Selecione uma disciplina.";
        }
        if (t.getProfessor() == null) {
            return "Selecione um professor.";
        }
        return null;
    }
}
