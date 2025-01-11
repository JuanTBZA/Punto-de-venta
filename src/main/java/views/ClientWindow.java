package views;

import controllers.ClientController;
import models.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
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
        // Configurar campos para EntityDialog
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("name", "Nombre");
        fields.put("dni", "DNI");
        fields.put("nickname", "Apodo");

        // Crear el diálogo
        EntityDialog dialog = new EntityDialog(this, "Agregar Cliente", fields, null);
        dialog.setVisible(true);

        // Verificar si el usuario confirmó la acción
        if (dialog.isConfirmed()) {
            LinkedHashMap<String, String> values = dialog.getFieldValues();
            String name = values.get("name");
            String dni = values.get("dni");
            String nickname = values.get("nickname");

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
            // Obtener datos de la fila seleccionada
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String currentName = (String) tableModel.getValueAt(selectedRow, 1);
            String currentDni = (String) tableModel.getValueAt(selectedRow, 2);
            String currentNickname = (String) tableModel.getValueAt(selectedRow, 3);

            // Configurar campos para EntityDialog
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("name", "Nuevo Nombre");
            fields.put("dni", "Nuevo DNI");
            fields.put("nickname", "Nuevo Apodo");
            Object[] initialValues = {currentName, currentDni, currentNickname};

            // Crear el diálogo
            EntityDialog dialog = new EntityDialog(this, "Editar Cliente", fields, initialValues);
            dialog.setVisible(true);

            // Verificar si el usuario confirmó la acción
            if (dialog.isConfirmed()) {
                LinkedHashMap<String, String> values = dialog.getFieldValues();
                String newName = values.get("name");
                String newDni = values.get("dni");
                String newNickname = values.get("nickname");

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
