package simulacionsupermercado.Controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import simulacionsupermercado.SimulacionSupermercado;
import queue.Client;
import queue.ClientVisual;
import queue.Queue;
import utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SimulationController implements Initializable {

    private SimulacionSupermercado primaryStage;
    @FXML
    private AnchorPane mainPane;
    private final String RESOURCE_IMAGE = "/images/cliente.png";
    private SimulacionSupermercado stage;

    private IntegerProperty countQueue = new SimpleIntegerProperty(0);
    private Queue colaClientes = new Queue();
    private Client[] clientesOriginales = new Client[0];

    private boolean caja1Libre = true;
    private boolean caja2Libre = true;
    private boolean simulacionPausada = false;

    @FXML
    private Pane container_stack;
    @FXML
    private Button toggleBtn;
    @FXML
    private Pane register1;
    @FXML
    private Pane register2;
    @FXML
    private ImageView valueRegister1;
    @FXML
    private ImageView valueRegister2;
    @FXML
    private Label infoCaja1;
    @FXML
    private Label infoCaja2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void pausarOReanudarSimulacion() {
        simulacionPausada = !simulacionPausada;

        toggleBtn.setText(simulacionPausada ? "Reanudar" : "Detener");

        if (!simulacionPausada) {
            asignarClienteACaja();
        }
    }

    @FXML
    public void reiniciarSimulacion() {
        // 1. Limpiar el contenedor visual de clientes
        container_stack.getChildren().removeIf(n -> n instanceof ImageView && n != valueRegister1 && n != valueRegister2);

        // 2. Limpiar estado de cajas y etiquetas
        valueRegister1.setVisible(false);
        valueRegister2.setVisible(false);
        infoCaja1.setVisible(false);
        infoCaja2.setVisible(false);
        caja1Libre = true;
        caja2Libre = true;
        simulacionPausada = false;

        // 3. Limpiar cola y contador
        colaClientes.clear();
        countQueue.set(0);

        // 4. Volver a cargar los clientes originales
        recibirClientes(Arrays.asList(this.clientesOriginales));

    }

    private void asignarClienteACaja() {
        if (simulacionPausada) {
            return;
        }

        if (colaClientes.isEmpty()) {
            if (caja1Libre && caja2Libre) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Simulación finalizada");
                alert.setHeaderText(null);
                alert.setContentText("Todos los clientes han sido atendidos.");
                alert.showAndWait();
            }
            return;
        }

        while ((!colaClientes.isEmpty()) && (caja1Libre || caja2Libre)) {
            ClientVisual cliente = colaClientes.poll();
            countQueue.set(countQueue.get() - 1);

            if (caja1Libre) {
                moverClienteACaja(cliente, register1, valueRegister1, infoCaja1, 1);
                caja1Libre = false;
            } else if (caja2Libre) {
                moverClienteACaja(cliente, register2, valueRegister2, infoCaja2, 2);
                caja2Libre = false;
            }
        }
    }

    private void moverClienteACaja(ClientVisual cliente, Pane caja, ImageView visor, Label info, int numeroCaja) {
        if (cliente == null) {
            // Validamos porque se cambio de una lista enlazada con un tama;o ajustable a uno fijo
            return;
        }
        ImageView imagen = cliente.getImagen();
        TranslateTransition trans = new TranslateTransition(Duration.seconds(1.5), imagen);
        double destinoX = caja.getLayoutX() + 180 - imagen.getLayoutX();
        double destinoY = caja.getLayoutY() + 100 - imagen.getLayoutY();
        trans.setToX(destinoX);
        trans.setToY(destinoY);

        trans.setOnFinished(e -> {
            imagen.setVisible(false);
            visor.setImage(imagen.getImage());
            visor.setVisible(true);

            Client datos = cliente.getDatos();
            info.setText(datos.getNombre() + " - " + datos.getQuantity() + " artículos");
            info.setVisible(true);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String horaActual = LocalTime.now().format(formatter);
            datos.setHoraAtencion(horaActual);
            new Thread(() -> {
                try {
                    Thread.sleep(datos.getDurationMS());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                Platform.runLater(() -> {
                    visor.setVisible(false);
                    info.setVisible(false);
                    if (numeroCaja == 1) {
                        caja1Libre = true;
                    } else {
                        caja2Libre = true;
                    }
                    asignarClienteACaja();
                });
            }).start();
        });

        trans.play();
    }

    public void recibirClientes(List<Client> clientes) {
        this.clientesOriginales = clientes.toArray(new Client[0]);;
        this.colaClientes.clear();
        for (int i = 0; i < clientes.size() ; i++) {
            Client c = clientes.get(i);
            double spacing = container_stack.getPrefWidth() / (Constants.QUEUE_MAX_CLIENTS + 1);
            double x = spacing * (i + 1);
            double y = 50;

            Image img = new Image(getClass().getResource(RESOURCE_IMAGE).toExternalForm());
            ClientVisual visual = new ClientVisual(c, img, x, y);
            container_stack.getChildren().add(visual.getImagen());
            visual.getImagen().setVisible(true);
            colaClientes.add(visual);
        }

        countQueue.set(clientes.size());
        asignarClienteACaja();
    }

    public void SetPrimaryStage(SimulacionSupermercado simulacionSupermercado) {
        this.primaryStage = simulacionSupermercado;
    }

    @FXML
 private void viewStatistics(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulacionsupermercado/views/StatisticsView.fxml"));
        Parent root = loader.load();

        StatisticsController controller = loader.getController();

        List<Client> clientes = Arrays.asList(this.clientesOriginales);

        int acumulador = 0;
        for (Client c : clientes) {
            c.setTiempoEnCola(acumulador);
            acumulador += c.getDurationMS();
        }

        double promedioCaja = clientes.stream().mapToInt(Client::getDurationMS).average().orElse(0) / 1000.0;

        double totalCola = 0;
        int maxColaMS = 0;
        Client clienteMasTiempoCola = null;

        for (Client c : clientes) {
            int tiempo = c.getTiempoEnCola();
            totalCola += tiempo;
            if (tiempo > maxColaMS) {
                maxColaMS = tiempo;
                clienteMasTiempoCola = c;
            }
        }

        double promedioCola = totalCola / clientes.size() / 1000.0;
        double maxCola = maxColaMS / 1000.0;

        controller.setDatosEstadisticos(clientes, promedioCaja, promedioCola, maxCola);

        Stage stage = new Stage();
        stage.setTitle("Estadísticas");
        stage.setScene(new Scene(root));
        stage.show();

        Stage currentStage = (Stage) mainPane.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
