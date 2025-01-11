/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositories;

import java.io.IOException;
import java.util.List;
import models.Client;

/**
 *
 * @author usuario
 */
public interface ClientRepository {
    public List<Client> findAll() throws IOException ;
    public void add(Client client) throws IOException;
    public void update(int id, String newName, String newDni, String newNickname) throws IOException ;
    public void delete(int id) throws IOException;
    public Client findById(int id) throws IOException;
}
