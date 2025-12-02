package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import utils.AlertaUtil;

public class MainController {

    @FXML
    private StackPane conteudoPrincipal;

    private void carregarTela(String fxml) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/view/" + fxml));
            conteudoPrincipal.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
            AlertaUtil.erro("Erro", "Não foi possível carregar a tela: " + fxml);
        }
    }

    @FXML
    private void abrirCursos() {
        carregarTela("curso-view.fxml");
    }

    @FXML
    private void abrirDisciplinas() {
        carregarTela("disciplina-view.fxml");
    }

    @FXML
    private void abrirProfessores() {
        carregarTela("professor-view.fxml");
    }

    @FXML
    private void abrirTurmas() {
        carregarTela("turma-view.fxml");
    }
}
