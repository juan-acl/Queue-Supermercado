package simulacionsupermercado.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import queue.Client;
import simulacionsupermercado.SimulacionSupermercado;
import simulacionsupermercado.Controllers.SimulationController;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class FormularioController {

    @FXML private TextField nombreField;
    @FXML private Spinner<Integer> articulosSpinner;
    @FXML private ListView<String> clientesList;
    @FXML private Button iniciarSimulacionBtn;

    private final int MAX_CLIENTES = 5;
    private final List<Client> clientes = new ArrayList<>();

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        articulosSpinner.setValueFactory(valueFactory);
    }

    @FXML
    public void agregarCliente() {
        String nombre = nombreField.getText().trim();
        int articulos = articulosSpinner.getValue();

        if (nombre.isEmpty()) {
            mostrarAlerta("Error", "El nombre no puede estar vacío.");
            return;
        }

        if (clientes.size() >= MAX_CLIENTES) {
            mostrarAlerta("Límite alcanzado", "Solo puedes agregar hasta 5 clientes.");
            return;
        }

        String nit = String.format("%04d0101", (int)(Math.random() * 10000));
        Client cliente = new Client(articulos, nombre, "", articulos * 2000, nit);
        clientes.add(cliente);
        clientesList.getItems().add(nombre + " - " + articulos + " artículos - NIT: " + nit);
        nombreField.clear();
    }

    @FXML
    public void iniciarSimulacion() {
        if (clientes.isEmpty()) {
            mostrarAlerta("Advertencia", "Debe agregar al menos un cliente para iniciar la simulación.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulacionsupermercado/Views/SimulationView.fxml"));
            AnchorPane simulationRoot = loader.load();
            SimulationController controller = loader.getController();
            controller.recibirClientes(clientes); // Método que debes agregar en SimulationController

            Stage stage = (Stage) iniciarSimulacionBtn.getScene().getWindow();
            stage.setScene(new Scene(simulationRoot));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}