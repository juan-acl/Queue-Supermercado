package simulacionsupermercado;

import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import queue.Client;
import simulacionsupermercado.Controllers.FormularioController;
import simulacionsupermercado.Controllers.SimulationController;

public class SimulacionSupermercado extends Application {

    private final String PATH_VIEW = "/simulacionsupermercado/Views/";

    private Stage stage;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        getViewForm();
        primaryStage.show();
    }

    public void SetTitle(String title) {
        this.stage.setTitle(title);
    }

    public void getViewForm() {
        try {
            FormularioController formController = (FormularioController) getSceneFXML("FormularioView.fxml");
            this.stage.setTitle("Formulario para ingreso de clientes");
            formController.SetPrimaryStage(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getViewSimulation(List<Client> client) {
        try {
            SimulationController simulationController = (SimulationController) getSceneFXML("SimulationView.fxml");
            this.stage.setTitle("Simulacion de clientes pagando en caja");
            simulationController.recibirClientes(client);
            simulationController.SetPrimaryStage(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T getSceneFXML(String viewfxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_VIEW + viewfxml));
        AnchorPane root = loader.load();
        scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
        stage.setScene(scene);
        return loader.getController();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getStage() {
        return this.stage;
    }
}
