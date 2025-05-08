package simulacionsupermercado.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import simulacionsupermercado.SimulacionSupermercado;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

/**
 * FXML Controller class
 *
 * @author juanchuc
 */
public class SimulationController implements Initializable {

    private SimulacionSupermercado primaryStage;

    private IntegerProperty countQueue = new SimpleIntegerProperty(0);

    private final int MAX_QUEUES = 5;
    private final int MMS_BY_ARTICLE = 2000;

    private Circle[] queueCircles;

    // Timer como variable de instancia para poder cancelarlo cuando sea necesario
    private Timer simulationTimer;
    private boolean isTimerRunning = false;

    @FXML
    private Pane container_stack;

    @FXML
    private Button addQueue;

    @FXML
    private Pane register1;

    @FXML
    private Pane register2;

    @FXML
    private Circle valueRegister1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar el grid
        initializeGrid();

        // Listener para el contador de cola
        countQueue.addListener((observable, oldValue, newValue) -> {
            int newCount = newValue.intValue();
            int oldCount = oldValue.intValue();

            // Comprobar límites
            boolean exceedsLimit = newCount >= MAX_QUEUES;
            addQueue.setDisable(exceedsLimit);
            if (exceedsLimit) {
                addQueue.setStyle("-fx-background-color: #cccccc;");
            } else {
                addQueue.setStyle("-fx-background-color: #4CAF50;");
            }

            if (newCount > 0 && !isTimerRunning) {

                startSimulationTimer();
            } else if (newCount == 0 && isTimerRunning) {

                stopSimulationTimer();
                mostrarAlertaInformacion("No hay clientes en cola",
                        "Clientes atendidos correctamente",
                        "El proceso de atención al cliente ya ha finalizado.");
            }
        });

        // Generar colas iniciales
        generateInitialQueues(MAX_QUEUES);
    }

    /**
     * Inicia el timer para la simulación
     */
    private void startSimulationTimer() {
        if (isTimerRunning) {
            return;
        }

        simulationTimer = new Timer(true);
        TimerTask simulationTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    System.out.println("Ejecución... Clientes en cola: " + countQueue.get());
                    assignQueue();

                    if (countQueue.get() > 0) {
                        removeQueue();
                    }
                });
            }
        };

        simulationTimer.scheduleAtFixedRate(simulationTask, 0, MMS_BY_ARTICLE);
        isTimerRunning = true;
        System.out.println("Timer iniciado");
    }

    /**
     * Detiene el timer de simulación
     */
    private void stopSimulationTimer() {
        if (simulationTimer != null) {
            simulationTimer.cancel();
            simulationTimer = null;
            isTimerRunning = false;
            System.out.println("Timer detenido");
        }
    }

    public int calculateTimeByArticles(int quantity) {
        return quantity * MMS_BY_ARTICLE;
    }

    public void assignQueue() {
        try {
            if (countQueue.get() > 0) {
                valueRegister1.setStyle("-fx-fill: #1e90ff;");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa el grid con posiciones predefinidas para todas las posibles
     * colas
     */
    private void initializeGrid() {
        queueCircles = new Circle[MAX_QUEUES];

        double containerWidth = container_stack.getPrefWidth();
        double spacing = containerWidth / (MAX_QUEUES + 1);

        for (int i = 0; i < MAX_QUEUES; i++) {
            Circle circle = new Circle(25);
            circle.setStyle("-fx-fill: #1e90ff;");
            double x = spacing * (i + 1);
            double y = 50;

            circle.setLayoutX(x);
            circle.setLayoutY(y);
            circle.setVisible(false);

            queueCircles[i] = circle;
            container_stack.getChildren().add(circle);
        }
    }

    private void mostrarAlertaInformacion(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void generateInitialQueues(int initialQueues) {
        // Reiniciar el contador
        countQueue.set(0);
        for (int i = 0; i < initialQueues; i++) {
            if (i < MAX_QUEUES) {
                queueCircles[i].setVisible(true);
                countQueue.set(countQueue.get() + 1);
            }
        }
    }

    public void generateQueue() {
        try {
            if (countQueue.get() >= MAX_QUEUES) {
                mostrarAlertaInformacion("No se puede agregar más colas",
                        "Espacio insuficiente",
                        "No hay más espacio para la cola");
                return;
            }

            queueCircles[countQueue.get()].setVisible(true);

            // Incrementar la propiedad
            countQueue.set(countQueue.get() + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeQueue() {
        if (countQueue.get() > 0) {
            queueCircles[countQueue.get() - 1].setVisible(false);
            countQueue.set(countQueue.get() - 1);
        }
    }

    public void SetPrimaryStage(SimulacionSupermercado simulacionSupermercado) {
        this.primaryStage = (simulacionSupermercado);
    }

    public int getQueueCount() {
        return countQueue.get();
    }

    public void cleanup() {
        stopSimulationTimer();
    }
}
