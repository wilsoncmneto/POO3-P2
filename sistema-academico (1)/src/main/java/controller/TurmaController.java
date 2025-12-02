package controller;

import dao.DisciplinaDAO;
import dao.ProfessorDAO;
import dao.TurmaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Disciplina;
import model.Professor;
import model.Turma;
import utils.AlertaUtil;
import utils.Validador;

public class TurmaController {

    @FXML
    private TextField txtSemestre;

    @FXML
    private TextField txtHorario;

    @FXML
    private ComboBox<Disciplina> comboDisciplina;

    @FXML
    private ComboBox<Professor> comboProfessor;

    @FXML
    private TableView<Turma> tabelaTurmas;

    @FXML
    private TableColumn<Turma, String> colDisciplina;

    @FXML
    private TableColumn<Turma, String> colProfessor;

    @FXML
    private TableColumn<Turma, String> colSemestre;

    private final TurmaDAO turmaDAO = new TurmaDAO();
    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    private ObservableList<Turma> listaTurmas;
    private ObservableList<Disciplina> listaDisciplinas;
    private ObservableList<Professor> listaProfessores;

    private Turma turmaSelecionada;

    @FXML
    private void initialize() {
        colDisciplina.setCellValueFactory(t ->
                new javafx.beans.property.SimpleStringProperty(
                        t.getValue().getDisciplina() != null ? t.getValue().getDisciplina().getNome() : ""));
        colProfessor.setCellValueFactory(t ->
                new javafx.beans.property.SimpleStringProperty(
                        t.getValue().getProfessor() != null ? t.getValue().getProfessor().getNome() : ""));
        colSemestre.setCellValueFactory(t ->
                new javafx.beans.property.SimpleStringProperty(
                        t.getValue().getSemestre()));

        tabelaTurmas.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> selecionar(n)
        );

        listaDisciplinas = FXCollections.observableArrayList(disciplinaDAO.findAll());
        listaProfessores = FXCollections.observableArrayList(professorDAO.findAll());
        comboDisciplina.setItems(listaDisciplinas);
        comboProfessor.setItems(listaProfessores);

        atualizarLista();
    }

    private void selecionar(Turma t) {
        turmaSelecionada = t;
        if (t != null) {
            txtSemestre.setText(t.getSemestre());
            txtHorario.setText(t.getHorario());
            comboDisciplina.setValue(t.getDisciplina());
            comboProfessor.setValue(t.getProfessor());
        } else {
            txtSemestre.clear();
            txtHorario.clear();
            comboDisciplina.getSelectionModel().clearSelection();
            comboProfessor.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void novo() {
        turmaSelecionada = null;
        txtSemestre.clear();
        txtHorario.clear();
        comboDisciplina.getSelectionModel().clearSelection();
        comboProfessor.getSelectionModel().clearSelection();
        tabelaTurmas.getSelectionModel().clearSelection();
    }

    @FXML
    private void editar() {
        Turma t = tabelaTurmas.getSelectionModel().getSelectedItem();
        if (t == null) {
            AlertaUtil.erro("Erro", "Selecione uma turma para editar.");
            return;
        }
        selecionar(t);
    }

    @FXML
    private void salvar() {
        Turma t = (turmaSelecionada == null) ? new Turma() : turmaSelecionada;
        t.setSemestre(txtSemestre.getText());
        t.setHorario(txtHorario.getText());
        t.setDisciplina(comboDisciplina.getValue());
        t.setProfessor(comboProfessor.getValue());

        String erro = Validador.validar(t);
        if (erro != null) {
            AlertaUtil.erro("Validação", erro);
            return;
        }

        if (t.getId() == null) {
            turmaDAO.create(t);
        } else {
            turmaDAO.update(t);
        }

        AlertaUtil.info("Sucesso", "Turma salva.");
        atualizarLista();
        novo();
    }

    @FXML
    private void remover() {
        Turma t = tabelaTurmas.getSelectionModel().getSelectedItem();
        if (t == null) {
            AlertaUtil.erro("Erro", "Selecione uma turma.");
            return;
        }
        if (AlertaUtil.confirmar("Confirmação", "Remover turma " + t + "?")) {
            turmaDAO.delete(t);
            atualizarLista();
            novo();
        }
    }

    @FXML
    private void atualizarLista() {
        listaTurmas = FXCollections.observableArrayList(turmaDAO.findAll());
        tabelaTurmas.setItems(listaTurmas);

        listaDisciplinas.setAll(disciplinaDAO.findAll());
        listaProfessores.setAll(professorDAO.findAll());
    }
}
