/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package queue;

/**
 *
 * @author juanchuc
 */
public class Client {

    private int quantity;
    private String nombre;
    private String nit;
    private String identifier;
    private int durationMS;

    private Client() {

    }

    public Client(int quantity, String nombre, String identifier, int durationMS, String nit) {
        this.quantity = quantity;
        this.nombre = nombre;
        this.identifier = identifier;
        this.nit = nit;
        this.durationMS = durationMS;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public void setDurationMs(int durationMS) {
        this.durationMS = durationMS;
    }

    public String getNit() {
        return this.nit;
    }

    public int getDurationMS() {
        return this.durationMS;
    }

}
