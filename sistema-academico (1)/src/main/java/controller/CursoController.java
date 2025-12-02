package controller;

import dao.CursoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Curso;
import utils.AlertaUtil;
import utils.Validador;

public class CursoController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCargaHoraria;

    @FXML
    private TableView<Curso> tabelaCursos;

    @FXML
    private TableColumn<Curso, String> colNome;

    @FXML
    private TableColumn<Curso, Integer> colCargaHoraria;

    private final CursoDAO cursoDAO = new CursoDAO();
    private ObservableList<Curso> listaCursos;
    private Curso cursoSelecionado;

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        colCargaHoraria.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCargaHoraria()).asObject());

        tabelaCursos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> selecionarCurso(newSel)
        );

        atualizarLista();
    }

    private void selecionarCurso(Curso c) {
        cursoSelecionado = c;
        if (c != null) {
            txtNome.setText(c.getNome());
            txtCargaHoraria.setText(String.valueOf(c.getCargaHoraria()));
        }
    }

    @FXML
    private void novo() {
        cursoSelecionado = null;
        txtNome.clear();
        txtCargaHoraria.clear();
        tabelaCursos.getSelectionModel().clearSelection();
    }

    @FXML
    private void editar() {
        Curso c = tabelaCursos.getSelectionModel().getSelectedItem();
        if (c == null) {
            AlertaUtil.erro("Erro", "Selecione um curso para editar.");
            return;
        }
        selecionarCurso(c);
    }

    @FXML
    private void salvar() {
        try {
            String nome = txtNome.getText();
            Integer carga = Integer.parseInt(txtCargaHoraria.getText());

            Curso c = (cursoSelecionado == null) ? new Curso() : cursoSelecionado;
            c.setNome(nome);
            c.setCargaHoraria(carga);

            String erro = Validador.validar(c);
            if (erro != null) {
                AlertaUtil.erro("Validação", erro);
                return;
            }

            if (c.getId() == null) {
                cursoDAO.create(c);
            } else {
                cursoDAO.update(c);
            }

            AlertaUtil.info("Sucesso", "Curso salvo com sucesso.");
            atualizarLista();
            novo();
        } catch (NumberFormatException e) {
            AlertaUtil.erro("Erro", "Carga horária deve ser um número inteiro.");
        }
    }

    @FXML
    private void remover() {
        Curso c = tabelaCursos.getSelectionModel().getSelectedItem();
        if (c == null) {
            AlertaUtil.erro("Erro", "Selecione um curso para remover.");
            return;
        }
        if (AlertaUtil.confirmar("Confirmação", "Deseja realmente remover o curso " + c.getNome() + "?")) {
            cursoDAO.delete(c);
            atualizarLista();
            novo();
        }
    }

    @FXML
    private void atualizarLista() {
        listaCursos = FXCollections.observableArrayList(cursoDAO.findAll());
        tabelaCursos.setItems(listaCursos);
    }
}
