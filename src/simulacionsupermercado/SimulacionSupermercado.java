package simulacionsupermercado;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class SimulacionSupermercado extends Application {

    private final String PATH_VIEW = "/simulacionsupermercado/Views/";

    private Stage stage;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.stage.setTitle("Formulario de Clientes");
        getSceneFormulario();  // ✅ Ahora inicia con el formulario
        primaryStage.show();
    }

    public void getSceneFormulario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_VIEW + "FormularioView.fxml"));
            AnchorPane root = loader.load();
            scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSceneSimulation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_VIEW + "SimulationView.fxml"));
            AnchorPane root = loader.load();
            scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm());
            stage.setScene(scene);

            // Si necesitas el controlador
            simulacionsupermercado.Controllers.SimulationController controller = loader.getController();
            controller.SetPrimaryStage(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public Stage getStage() {
    return this.stage;
}
}