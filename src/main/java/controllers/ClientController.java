/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.util.List;
import models.Client;
import services.ClientServiceImpl;

public class ClientController {

    private final ClientServiceImpl service;

    public ClientController(ClientServiceImpl service) {
        this.service = service;
    }

    public List<Client> listClients() throws IOException {
        return service.getAllClients();
    }

    public void addClient(String name, String dni, String nickname) throws IOException {
        Client newClient = new Client(0, name, dni, nickname); // El ID se asignará automáticamente
        service.addClient(newClient);
    }

    public void updateClient(int id, String newName, String newDni, String newNickname) throws IOException {
        service.updateClient(id, newName, newDni, newNickname);
        System.out.println("Client updated successfully!");
    }

    public void deleteClient(int id) throws IOException {
        service.deleteClient(id);
        System.out.println("Client deleted successfully!");
    }

    public Client findById(int id) throws IOException {
        return service.findById(id);
    }
}
