package simulacionsupermercado.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import queue.Client;
import java.util.ArrayList;
import java.util.List;
import simulacionsupermercado.SimulacionSupermercado;
import utils.Constants;

public class FormularioController {
    private SimulacionSupermercado stage;

    @FXML
    private TextField nombreField;
    @FXML
    private Spinner<Integer> articulosSpinner;
    @FXML
    private ListView<String> clientesList;
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

        if (clientes.size() > Constants.QUEUE_MAX_CLIENTS) {
            mostrarAlerta("Límite alcanzado", "Solo puedes agregar hasta 5 clientes.");
            return;
        }

        String nit = String.format("%04d0101", (int) (Math.random() * 10000));
        Client cliente = new Client(articulos, nombre, "", articulos * Constants.CLIENT_TIME_PER_ARTICLE_MS, nit);
        clientes.add(cliente);
        clientesList.getItems().add(nombre + " - " + articulos + " artículos - NIT: " + nit);
        nombreField.clear();
        articulosSpinner.getValueFactory().setValue(1);
    }

    @FXML
    public void iniciarSimulacion() {
        try {
            if (clientes.isEmpty()) {
                mostrarAlerta("Advertencia", "Debe agregar al menos un cliente para iniciar la simulación.");
                return;
            }
            this.stage.getViewSimulation(clientes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public void SetPrimaryStage(SimulacionSupermercado simulacionSupermercado) {
        this.stage = simulacionSupermercado;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
} 