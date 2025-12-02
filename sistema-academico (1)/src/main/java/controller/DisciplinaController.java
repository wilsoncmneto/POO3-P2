package controller;

import dao.CursoDAO;
import dao.DisciplinaDAO;
import dao.ProfessorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Curso;
import model.Disciplina;
import model.Professor;
import utils.AlertaUtil;
import utils.Validador;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private ComboBox<Curso> comboCurso;

    @FXML
    private ListView<Professor> listProfessores;

    @FXML
    private TableView<Disciplina> tabelaDisciplinas;

    @FXML
    private TableColumn<Disciplina, String> colNome;

    @FXML
    private TableColumn<Disciplina, String> colCurso;

    private final DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
    private final CursoDAO cursoDAO = new CursoDAO();
    private final ProfessorDAO professorDAO = new ProfessorDAO();

    private ObservableList<Disciplina> listaDisciplinas;
    private ObservableList<Curso> listaCursos;
    private ObservableList<Professor> listaProfessores;

    private Disciplina disciplinaSelecionada;

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getNome()));
        colCurso.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getCurso() != null ? d.getValue().getCurso().getNome() : ""));

        tabelaDisciplinas.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> selecionar(n)
        );

        listaCursos = FXCollections.observableArrayList(cursoDAO.findAll());
        comboCurso.setItems(listaCursos);

        listaProfessores = FXCollections.observableArrayList(professorDAO.findAll());
        listProfessores.setItems(listaProfessores);
        listProfessores.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        atualizarLista();
    }

    private void selecionar(Disciplina d) {
        disciplinaSelecionada = d;
        if (d != null) {
            txtNome.setText(d.getNome());
            txtDescricao.setText(d.getDescricao());
            comboCurso.setValue(d.getCurso());

            listProfessores.getSelectionModel().clearSelection();
            if (d.getProfessores() != null) {
                for (Professor p : d.getProfessores()) {
                    int idx = listaProfessores.indexOf(p);
                    if (idx >= 0) {
                        listProfessores.getSelectionModel().select(idx);
                    }
                }
            }
        } else {
            txtNome.clear();
            txtDescricao.clear();
            comboCurso.getSelectionModel().clearSelection();
            listProfessores.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void novo() {
        disciplinaSelecionada = null;
        txtNome.clear();
        txtDescricao.clear();
        comboCurso.getSelectionModel().clearSelection();
        listProfessores.getSelectionModel().clearSelection();
        tabelaDisciplinas.getSelectionModel().clearSelection();
    }

    @FXML
    private void editar() {
        Disciplina d = tabelaDisciplinas.getSelectionModel().getSelectedItem();
        if (d == null) {
            AlertaUtil.erro("Erro", "Selecione uma disciplina para editar.");
            return;
        }
        selecionar(d);
    }

    @FXML
    private void salvar() {
        Disciplina d = (disciplinaSelecionada == null) ? new Disciplina() : disciplinaSelecionada;
        d.setNome(txtNome.getText());
        d.setDescricao(txtDescricao.getText());
        d.setCurso(comboCurso.getValue());

        List<Professor> selecionados =
                new ArrayList<>(listProfessores.getSelectionModel().getSelectedItems());
        d.setProfessores(selecionados);

        String erro = Validador.validar(d);
        if (erro != null) {
            AlertaUtil.erro("Validação", erro);
            return;
        }

        if (d.getId() == null) {
            disciplinaDAO.create(d);
        } else {
            disciplinaDAO.update(d);
        }

        AlertaUtil.info("Sucesso", "Disciplina salva.");
        atualizarLista();
        novo();
    }

    @FXML
    private void remover() {
        Disciplina d = tabelaDisciplinas.getSelectionModel().getSelectedItem();
        if (d == null) {
            AlertaUtil.erro("Erro", "Selecione uma disciplina.");
            return;
        }
        if (AlertaUtil.confirmar("Confirmação", "Remover disciplina " + d.getNome() + "?")) {
            disciplinaDAO.delete(d);
            atualizarLista();
            novo();
        }
    }

    @FXML
    private void atualizarLista() {
        listaDisciplinas = FXCollections.observableArrayList(disciplinaDAO.findAll());
        tabelaDisciplinas.setItems(listaDisciplinas);

        listaCursos.setAll(cursoDAO.findAll());
        listaProfessores.setAll(professorDAO.findAll());
    }
}
