package simulacionsupermercado.Controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

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
    @FXML
    private Button btnEstadisticas;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
    if (mainPane.getScene() != null) {
        // Verificacion de que se aplican las clases del .css
        mainPane.getScene().getStylesheets().forEach(System.out::println);
    }
});

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
        //Limpiar el contenedor visual de clientes
        container_stack.getChildren().removeIf(n -> 
        (n instanceof ImageView && n != valueRegister1 && n != valueRegister2) || (n instanceof Label && n.getStyleClass().contains("cronometro")));

        //Limpiar estado de cajas y etiquetas para evitar duplicacion de info
        valueRegister1.setVisible(false);
        valueRegister2.setVisible(false);
        infoCaja1.setVisible(false);
        infoCaja2.setVisible(false);
        caja1Libre = true;
        caja2Libre = true;
        simulacionPausada = false;

        //Limpiar cola y contador
        colaClientes.clear();
        countQueue.set(0);

        //Vuelve a cargar los clientes originales
        recibirClientes(Arrays.asList(this.clientesOriginales));

    }

    private void asignarClienteACaja() {
        if (simulacionPausada) {
            return;
        }

        if (colaClientes.isEmpty()) {
            //Indica cuando ya mo hay clientes en cola, por ende manda la alerta
            if (caja1Libre && caja2Libre) {
                Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Simulación finalizada");
                alert.setHeaderText(null);
                alert.setContentText("Todos los clientes han sido atendidos.");
                alert.showAndWait();

                // Mostrar botón de estadísticas después del alert
                btnEstadisticas.setVisible(true);
        });
    }
    return;
}
        //while que se encarga de asignar el cliente segun disponibilidad de caja
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
//metodo para realizar el desplazamiento visual de lo clinetes desde el pane a las cajas
private void moverClienteACaja(ClientVisual cliente, Pane caja, ImageView visor, Label info, int numeroCaja) {
    if (cliente == null) return;

    ImageView imagen = cliente.getImagen();
    Client datos = cliente.getDatos();

    // Crear el cronómetro antes de la animación
    Label cronometro = new Label();
    cronometro.getStyleClass().add("cronometro");
    cronometro.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 10px;");

    String nombre = datos.getNombre();
    int articulos = datos.getQuantity();
    int duracion = datos.getDurationMS() / 1000;
    cronometro.setText(nombre + " - " + articulos + " artículos\n⏳ " + duracion + "s");

    // Posicionarlo inicialmente encima del cliente
    double initialX = imagen.getLayoutX() + imagen.getFitWidth() / 2 - 50;
    double initialY = imagen.getLayoutY() - 25;
    cronometro.setLayoutX(initialX);
    cronometro.setLayoutY(initialY);
    container_stack.getChildren().add(cronometro);

    // Animación de movimiento de la imagen del cliente
    TranslateTransition trans = new TranslateTransition(Duration.seconds(1.5), imagen);
    double destinoX = caja.getLayoutX() + 180 - imagen.getLayoutX();
    double destinoY = caja.getLayoutY() + 100 - imagen.getLayoutY();
    trans.setToX(destinoX);
    trans.setToY(destinoY);

    // Animación sincronizada del cronómetro
    TranslateTransition textoAnim = new TranslateTransition(Duration.seconds(1.5), cronometro);
    textoAnim.setToX(destinoX + imagen.getFitWidth() / 2 - 225); // ajustar para que siga encima
    textoAnim.setToY(destinoY - 175); // mantenerlo arriba del cliente

    trans.setOnFinished(e -> {
        imagen.setVisible(false);
        visor.setImage(imagen.getImage());
        visor.setVisible(true);

        // Guardar hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaActual = LocalTime.now().format(formatter);
        datos.setHoraAtencion(horaActual);

        // Cronómetro en cuenta regresiva
        IntegerProperty tiempoRestante = new SimpleIntegerProperty(duracion);
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                tiempoRestante.set(tiempoRestante.get() - 1);
                cronometro.setText(nombre + " - " + articulos + " artículos\n⏳ " + tiempoRestante.get() + "s");
            })
        );
        timeline.setCycleCount(duracion);
        timeline.setOnFinished(ev -> {
            container_stack.getChildren().remove(cronometro);
            visor.setVisible(false);
            if (numeroCaja == 1) caja1Libre = true;
            else caja2Libre = true;
            asignarClienteACaja();
        });

        timeline.play();
    });

    trans.play();
    textoAnim.play();
}
    //metodo en donde se guardan los clientes de manera visual en el pane provenientes de la cola
    public void recibirClientes(List<Client> clientes) {
        this.clientesOriginales = clientes.toArray(new Client[0]);;
        this.colaClientes.clear();
        for (int i = 0; i < clientes.size(); i++) {
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
    //metodo para mostrar el boton de estadisticas y que redirija a la vista
    @FXML
    private void viewStatistics(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/simulacionsupermercado/views/StatisticsView.fxml"));
            Parent root = loader.load();

            StatisticsController controller = loader.getController();
            controller.setMainApp(primaryStage);

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

            Stage currentStage = (Stage) mainPane.getScene().getWindow();
            currentStage.setTitle("Estadísticas");
            currentStage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
