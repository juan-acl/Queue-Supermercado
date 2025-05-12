/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package queue;

/**
 *
 * @author juanchuc
 */
public class Queue {

    private int MAX_QUEUE = 4; // Cantidad de clientes en la cola
    private int index = 0; // Indice que apunta a la posicion del arreglo de cada elemento
    private int first = 0; // Primero de la cola
    private ClientVisual[] clients;

    public Queue() {
        this.clients = new ClientVisual[this.MAX_QUEUE];
    }

    public void add(ClientVisual client) {
        if (this.isFull()) {
            return;
        }
        this.clients[index] = client;
        this.index++;
    }

    public ClientVisual poll() {
        if (this.isEmpty()) {
            return null;
        }

        ClientVisual copy = this.clients[first];
        this.clients[first] = null;
        this.first++;

        if (this.isEmpty()) {
            this.first = 0;
            this.index = 0;
        }
        return copy;
    }

    public void batchQueue(ClientVisual[] clients) {
        if (this.isFull()) {
            return;
        }
        this.clear();
        for (ClientVisual client : clients) {
            this.add(client);
        }
    }

    public ClientVisual peek() {
        return this.isEmpty() ? null : this.clients[first];
    }

    public boolean isFull() {
        return this.size() == this.MAX_QUEUE;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.index - this.first;
    }

    public void clear() {
        this.clients = new ClientVisual[this.MAX_QUEUE];
        this.index = 0;
        this.first = 0;
    }

}
