package simulacionsupermercado.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import queue.Client;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.List;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import simulacionsupermercado.SimulacionSupermercado;

import java.io.IOException;

public class StatisticsController {

    @FXML
    private BarChart<String, Number> barChartEstadisticas;
    @FXML
    private Label lblClientesAtendidos;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblTiempoPromCaja;
    @FXML
    private Label lblTiempoPromCola;
    @FXML
    private Label lblTiempoMaxCola;

    @FXML
    private TableView<Client> tablaClientes;
    @FXML
    private TableColumn<Client, Integer> colCorrelativo;
    @FXML
    private TableColumn<Client, String> colNombre;
    @FXML
    private TableColumn<Client, String> colNIT;
    @FXML
    private TableColumn<Client, Integer> colArticulos;
    @FXML
    private TableColumn<Client, String> colHoraAtencion;
    @FXML
    private TableColumn<Client, String> colTiempoAtencion;

    @FXML
    public void initialize() {
        colCorrelativo.setCellValueFactory(new PropertyValueFactory<>("correlativo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNIT.setCellValueFactory(new PropertyValueFactory<>("nit"));
        colArticulos.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colHoraAtencion.setCellValueFactory(c
                -> new ReadOnlyObjectWrapper<>(c.getValue().getHoraAtencion())
        );
        colTiempoAtencion.setCellValueFactory(c
                -> new ReadOnlyObjectWrapper<>(c.getValue().getTiempoAtencionString())
        );
    }

    public void setDatosEstadisticos(List<Client> clientes, double promCaja, double promCola, double maxCola) {
        lblClientesAtendidos.setText(String.valueOf(clientes.size()));
        lblTiempoPromCaja.setText(String.format("%.2f s", promCaja));
        lblTiempoPromCola.setText(String.format("%.2f s", promCola));
        lblTiempoMaxCola.setText(String.format("%.2f s", maxCola));
        
        int contador = 1;
        for (Client c : clientes) {
            c.setCorrelativo(contador++);
        }
        tablaClientes.getItems().setAll(clientes);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tiempos");

        series.getData().add(new XYChart.Data<>("Prom. Caja", promCaja));
        series.getData().add(new XYChart.Data<>("Prom. Cola", promCola));
        series.getData().add(new XYChart.Data<>("Máx. Cola", maxCola));

        barChartEstadisticas.getData().clear();
        barChartEstadisticas.getData().add(series);
    }

    private SimulacionSupermercado mainApp;

    public void setMainApp(SimulacionSupermercado app) {
        this.mainApp = app;
    }

    @FXML
    private void volverAlFormulario(ActionEvent event) {
        if (mainApp != null) {
            mainApp.getViewForm(); 
           
        }
    }

}
