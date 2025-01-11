/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import controllers.ClientController;
import models.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ClientWindow extends JFrame {

    private final DefaultTableModel tableModel;
    private final ClientController controller;

    public ClientWindow(ClientController controller) {
        this.controller = controller;

        setTitle("Gestión de Clientes");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Título
        JLabel titleLabel = new JLabel("Clientes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 150, 243));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"ID", "Nombre", "DNI", "Apodo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable clientTable = new JTable(tableModel);
        clientTable.setRowHeight(25);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(clientTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton addButton = new JButton("Agregar");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");

        styleButton(addButton, new Color(76, 175, 80));
        styleButton(editButton, new Color(33, 150, 243));
        styleButton(deleteButton, new Color(244, 67, 54));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> openAddDialog());
        editButton.addActionListener(e -> openEditDialog(clientTable));
        deleteButton.addActionListener(e -> deleteSelectedClient(clientTable));

        // Cargar datos iniciales
        loadClients();
    }

    private void loadClients() {
        try {
            tableModel.setRowCount(0);
            List<Client> clients = controller.listClients();
            for (Client client : clients) {
                tableModel.addRow(new Object[]{client.getId(), client.getName(), client.getDni(), client.getNickname()});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField dniField = new JTextField();
        JTextField nicknameField = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("DNI:"));
        panel.add(dniField);
        panel.add(new JLabel("Apodo:"));
        panel.add(nicknameField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String dni = dniField.getText().trim();
            String nickname = nicknameField.getText().trim();

            if (!name.isEmpty() && !dni.isEmpty() && !nickname.isEmpty()) {
                try {
                    controller.addClient(name, dni, nickname);
                    loadClients();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al agregar el cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor ingresa un nombre, DNI y apodo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void openEditDialog(JTable clientTable) {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String currentName = (String) tableModel.getValueAt(selectedRow, 1);
            String currentDni = (String) tableModel.getValueAt(selectedRow, 2);
            String currentNickname = (String) tableModel.getValueAt(selectedRow, 3);

            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            JTextField nameField = new JTextField(currentName);
            JTextField dniField = new JTextField(currentDni);
            JTextField nicknameField = new JTextField(currentNickname);

            panel.add(new JLabel("Nuevo Nombre:"));
            panel.add(nameField);
            panel.add(new JLabel("Nuevo DNI:"));
            panel.add(dniField);
            panel.add(new JLabel("Nuevo Apodo:"));
            panel.add(nicknameField);

            int option = JOptionPane.showConfirmDialog(this, panel, "Editar Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                String newName = nameField.getText().trim();
                String newDni = dniField.getText().trim();
                String newNickname = nicknameField.getText().trim();

                if (!newName.isEmpty() && !newDni.isEmpty() && !newNickname.isEmpty()) {
                    try {
                        controller.updateClient(id, newName, newDni, newNickname);
                        loadClients();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, "Error al actualizar el cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor ingresa un nuevo nombre, DNI y apodo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedClient(JTable clientTable) {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            int confirmation = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteClient(id);
                    loadClients();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}

