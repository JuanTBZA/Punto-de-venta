package views;

import controllers.CategoryController;
import models.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class CategoryWindow extends JFrame {

    private final DefaultTableModel tableModel;
    private final CategoryController controller;

    public CategoryWindow(CategoryController controller) {
        this.controller = controller;

        setTitle("Gestión de Categorías");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Título
        JLabel titleLabel = new JLabel("Categorías", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 150, 243));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"ID", "Nombre", "Descripción"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(25);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);
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
        editButton.addActionListener(e -> openEditDialog(categoryTable));
        deleteButton.addActionListener(e -> deleteSelectedCategory(categoryTable));

        // Cargar datos iniciales
        loadCategories();
    }

    private void loadCategories() {
        try {
            tableModel.setRowCount(0);
            List<Category> categories = controller.listCategories();
            for (Category category : categories) {
                tableModel.addRow(new Object[]{category.getId(), category.getName(), category.getDescription()});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las categorías: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddDialog() {
        // Configurar campos para EntityDialog
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("name", "Nombre");
        fields.put("description", "Descripción");

        // Crear el diálogo
        EntityDialog dialog = new EntityDialog(this, "Agregar Categoría", fields, null);
        dialog.setVisible(true);

        // Verificar si el usuario confirmó la acción
        if (dialog.isConfirmed()) {
            LinkedHashMap<String, String> values = dialog.getFieldValues();
            String name = values.get("name");
            String description = values.get("description");

            if (!name.isEmpty() && !description.isEmpty()) {
                try {
                    controller.addCategory(name, description);
                    loadCategories();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al agregar la categoría: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor ingresa un nombre y una descripción.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void openEditDialog(JTable categoryTable) {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow != -1) {
            // Obtener datos de la fila seleccionada
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String currentName = (String) tableModel.getValueAt(selectedRow, 1);
            String currentDescription = (String) tableModel.getValueAt(selectedRow, 2);

            // Configurar campos para EntityDialog
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("name", "Nuevo Nombre");
            fields.put("description", "Nueva Descripción");
            Object[] initialValues = {currentName, currentDescription};

            // Crear el diálogo
            EntityDialog dialog = new EntityDialog(this, "Editar Categoría", fields, initialValues);
            dialog.setVisible(true);

            // Verificar si el usuario confirmó la acción
            if (dialog.isConfirmed()) {
                LinkedHashMap<String, String> values = dialog.getFieldValues();
                String newName = values.get("name");
                String newDescription = values.get("description");

                if (!newName.isEmpty() && !newDescription.isEmpty()) {
                    try {
                        controller.updateCategory(id, newName, newDescription);
                        loadCategories();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, "Error al actualizar la categoría: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor ingresa un nuevo nombre y descripción.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedCategory(JTable categoryTable) {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            int confirmation = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar esta categoría?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteCategory(id);
                    loadCategories();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la categoría: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una categoría para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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
