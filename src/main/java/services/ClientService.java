/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package services;

import java.io.IOException;
import java.util.List;
import models.Client;

/**
 *
 * @author usuario
 */
public interface ClientService {
     
    public List<Client> getAllClients() throws IOException ;
    public void addClient(Client client) throws IOException ;
    public void updateClient(int id, String newName, String newDni, String newNickname) throws IOException ;
    public void deleteClient(int id) throws IOException ;
    public Client findById(int id) throws IOException ;
}
