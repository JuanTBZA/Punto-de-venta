/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import repositories.*;

import java.io.IOException;
import java.util.List;
import models.Client;

public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Client> getAllClients() throws IOException {
        return repository.findAll();
    }

    @Override
    public void addClient(Client client) throws IOException {
        repository.add(client);
    }

    @Override
    public void updateClient(int id, String newName, String newDni, String newNickname) throws IOException {
        repository.update(id, newName, newDni, newNickname);
    }

    @Override
    public void deleteClient(int id) throws IOException {
        repository.delete(id);
    }

    @Override
    public Client findById(int id) throws IOException {
        return repository.findById(id);
    }
}
