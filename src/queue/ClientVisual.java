/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package queue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ClientVisual {
    private Client datos;
    private ImageView imagen;

    public ClientVisual(Client datos, Image imagen, double x, double y) {
        this.datos = datos;
        this.imagen = new ImageView(imagen);
        this.imagen.setFitWidth(70);
        this.imagen.setFitHeight(70);
        this.imagen.setPreserveRatio(true);
        this.imagen.setLayoutX(x);
        this.imagen.setLayoutY(y);
        this.imagen.setVisible(false);
    }

    public Client getDatos() {
        return this.datos;
    }

    public ImageView getImagen() {
        return this.imagen;
    }
}