/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package queue;

import java.util.ArrayList;

/**
 *
 * @author juanchuc
 */
public class Queue {

    private int MAX_QUEUE = 4;
    private final ArrayList<Client> clients = new ArrayList<Client>();

    private boolean isEmpty() {
        return this.clients.size() == 0;
    }

    // Encolar cliente
    public void enqueue(Client client) {
        this.clients.add(client);
    }

    // Remover de la cola
    public Client dequeue() {
        return this.isEmpty() ? null : this.clients.remove(0);
    }

    // Obtener el primero de la cola sin eliminarlo
    public Client peek() {
        return this.isEmpty() ? null : this.clients.get(0);
    }

    public int size() {
        return this.clients.size();
    }

    public boolean isFull() {
        return this.size() >= this.MAX_QUEUE;
    }

    // Agregar a la cola por medio de batch
    public void batchEnqueue(ArrayList<Client> clients) {
        if (clients.size() + this.size() > this.MAX_QUEUE) {
            throw new IllegalStateException(
                    "No hay espacio para encolar " + clients.size()
                    + " clientes (capacidad: " + MAX_QUEUE + ")"
            );
        }
        this.clients.addAll(clients);
    }

}
