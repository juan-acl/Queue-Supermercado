/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package simulacionsupermercado;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.io.InputStream;
import javafx.scene.layout.AnchorPane;
import simulacionsupermercado.Controllers.SimulationController;
import java.io.IOException;

/**
 *
 * @author juanchuc
 */
public class SimulacionSupermercado extends Application {

    private final String PATH_VIEW = "/simulacionsupermercado/Views/";

    private Stage stage;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.stage.setTitle("Supermercado App");
        getSceneSimulation();
        primaryStage.show();
    }

    public void getSceneSimulation() {
        try {
            SimulationController simulation = (SimulationController) getScene("SimulationView.fxml");
            simulation.SetPrimaryStage(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Initializable getScene(String nameViewFXML) throws IOException {
        Initializable currentScene = null;
        String currentPath = PATH_VIEW + nameViewFXML;
        FXMLLoader managerFxml = new FXMLLoader();
        InputStream filePath = SimulacionSupermercado.class.getResourceAsStream(currentPath);
        managerFxml.setLocation(SimulacionSupermercado.class.getResource(currentPath));
        scene = new Scene((AnchorPane) managerFxml.load(filePath), 900, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        currentScene = (Initializable) managerFxml.getController();
        return currentScene;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Lanzar la aplicación JavaFX
        launch(args);
    }

}
