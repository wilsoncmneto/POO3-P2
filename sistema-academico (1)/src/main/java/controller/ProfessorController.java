package controller;

import dao.ProfessorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Disciplina;
import model.Professor;
import utils.AlertaUtil;
import utils.Validador;

import java.util.stream.Collectors;

public class ProfessorController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFormacao;

    @FXML
    private ListView<String> listDisciplinas;

    @FXML
    private TableView<Professor> tabelaProfessores;

    @FXML
    private TableColumn<Professor, String> colNome;

    @FXML
    private TableColumn<Professor, String> colEmail;

    private final ProfessorDAO professorDAO = new ProfessorDAO();
    private ObservableList<Professor> listaProfessores;
    private Professor profSelecionado;

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getNome()));
        colEmail.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getEmail()));

        tabelaProfessores.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> selecionar(n)
        );

        atualizarLista();
    }

    private void selecionar(Professor p) {
        profSelecionado = p;
        if (p != null) {
            txtNome.setText(p.getNome());
            txtEmail.setText(p.getEmail());
            txtFormacao.setText(p.getFormacao());

            if (p.getDisciplinas() != null) {
                listDisciplinas.setItems(FXCollections.observableArrayList(
                        p.getDisciplinas().stream().map(Disciplina::getNome).collect(Collectors.toList())
                ));
            } else {
                listDisciplinas.setItems(FXCollections.observableArrayList());
            }
        } else {
            listDisciplinas.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void novo() {
        profSelecionado = null;
        txtNome.clear();
        txtEmail.clear();
        txtFormacao.clear();
        listDisciplinas.setItems(FXCollections.observableArrayList());
        tabelaProfessores.getSelectionModel().clearSelection();
    }

    @FXML
    private void editar() {
        Professor p = tabelaProfessores.getSelectionModel().getSelectedItem();
        if (p == null) {
            AlertaUtil.erro("Erro", "Selecione um professor para editar.");
            return;
        }
        selecionar(p);
    }

    @FXML
    private void salvar() {
        Professor p = (profSelecionado == null) ? new Professor() : profSelecionado;
        p.setNome(txtNome.getText());
        p.setEmail(txtEmail.getText());
        p.setFormacao(txtFormacao.getText());

        String erro = Validador.validar(p);
        if (erro != null) {
            AlertaUtil.erro("Validação", erro);
            return;
        }

        if (p.getId() == null) {
            professorDAO.create(p);
        } else {
            professorDAO.update(p);
        }

        AlertaUtil.info("Sucesso", "Professor salvo.");
        atualizarLista();
        novo();
    }

    @FXML
    private void remover() {
        Professor p = tabelaProfessores.getSelectionModel().getSelectedItem();
        if (p == null) {
            AlertaUtil.erro("Erro", "Selecione um professor.");
            return;
        }
        if (AlertaUtil.confirmar("Confirmação", "Remover professor " + p.getNome() + "?")) {
            professorDAO.delete(p);
            atualizarLista();
            novo();
        }
    }

    @FXML
    private void atualizarLista() {
        listaProfessores = FXCollections.observableArrayList(professorDAO.findAll());
        tabelaProfessores.setItems(listaProfessores);
    }
}
