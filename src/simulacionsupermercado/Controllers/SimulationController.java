package simulacionsupermercado.Controllers;

import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import simulacionsupermercado.SimulacionSupermercado;
import queue.Client;
import queue.ClientVisual;

public class SimulationController implements Initializable {

    private SimulacionSupermercado primaryStage;

    private final int MAX_QUEUES = 5;
    private final int MMS_BY_ARTICLE = 2000;

    private IntegerProperty countQueue = new SimpleIntegerProperty(0);
    private Queue<ClientVisual> colaClientes = new LinkedList<>();
    private List<Client> clientesOriginales = new LinkedList<>();

    private boolean caja1Libre = true;
    private boolean caja2Libre = true;
    private boolean simulacionPausada = false;

    @FXML private Pane container_stack;
    @FXML private Button toggleBtn;
    //@FXML private Button addQueue; // puede estar oculto si ya no se usa
    @FXML private Pane register1;
    @FXML private Pane register2;
    @FXML private ImageView valueRegister1;
    @FXML private ImageView valueRegister2;
    @FXML private Label infoCaja1;
    @FXML private Label infoCaja2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    // Ya no se necesita observar countQueue porque no hay botón que dependa de él
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
    recibirClientes(clientesOriginales);
}

    private void asignarClienteACaja() {
        if (simulacionPausada) return;

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

            new Thread(() -> {
                try {
                    Thread.sleep(datos.getDurationMS());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                Platform.runLater(() -> {
                    visor.setVisible(false);
                    info.setVisible(false);
                    if (numeroCaja == 1) caja1Libre = true;
                    else caja2Libre = true;
                    asignarClienteACaja();
                });
            }).start();
        });

        trans.play();
    }

    public void recibirClientes(List<Client> clientes) {
        this.clientesOriginales = new LinkedList<>(clientes);
        this.colaClientes.clear();
        for (int i = 0; i < clientes.size(); i++) {
            Client c = clientes.get(i);
            double spacing = container_stack.getPrefWidth() / (MAX_QUEUES + 1);
            double x = spacing * (i + 1);
            double y = 50;

            Image img = new Image(getClass().getResource("/images/cliente.png").toExternalForm());
            ClientVisual visual = new ClientVisual(c, img, x, y);
            container_stack.getChildren().add(visual.getImagen());
            visual.getImagen().setVisible(true);
            colaClientes.add(visual);
        }

        countQueue.set(clientes.size());
        asignarClienteACaja();
    }

    private void mostrarAlerta(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void SetPrimaryStage(SimulacionSupermercado simulacionSupermercado) {
        this.primaryStage = simulacionSupermercado;
    }
}