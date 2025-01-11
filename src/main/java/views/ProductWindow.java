package views;

import controllers.ProductController;
import models.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class ProductWindow extends JFrame {

    private final DefaultTableModel tableModel;
    private final ProductController controller;

    public ProductWindow(ProductController controller) {
        this.controller = controller;

        setTitle("Gestión de Productos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Título
        JLabel titleLabel = new JLabel("Productos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 150, 243));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"ID", "Nombre", "Categoría", "Marca", "Stock", "Precio", "Ubicación"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable productTable = new JTable(tableModel);
        productTable.setRowHeight(25);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
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
        editButton.addActionListener(e -> openEditDialog(productTable));
        deleteButton.addActionListener(e -> deleteSelectedProduct(productTable));

        // Cargar datos iniciales
        loadProducts();
    }

    private void loadProducts() {
        try {
            tableModel.setRowCount(0);
            List<Product> products = controller.listProducts();
            for (Product product : products) {
                tableModel.addRow(new Object[]{product.getId(), product.getName(), product.getCategoryName(),
                        product.getBrandName(), product.getStock(), product.getPrice(), product.getLocation()});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddDialog() {
        // Configurar campos para EntityDialog
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("name", "Nombre");
        fields.put("category", "Categoría");
        fields.put("brand", "Marca");
        fields.put("stock", "Stock");
        fields.put("price", "Precio");
        fields.put("location", "Ubicación");

        // Crear el diálogo
        EntityDialog dialog = new EntityDialog(this, "Agregar Producto", fields, null);
        dialog.setVisible(true);

        // Verificar si el usuario confirmó la acción
        if (dialog.isConfirmed()) {
            LinkedHashMap<String, String> values = dialog.getFieldValues();
            String name = values.get("name");
            String category = values.get("category");
            String brand = values.get("brand");
            int stock = Integer.parseInt(values.get("stock"));
            int price = Integer.parseInt(values.get("price"));
            String location = values.get("location");

            if (!name.isEmpty() && !category.isEmpty() && !brand.isEmpty() && stock > 0 && price > 0 && !location.isEmpty()) {
                try {
                    controller.addProduct(name, category, brand, stock, price, location);
                    loadProducts();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al agregar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor ingresa todos los campos correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void openEditDialog(JTable productTable) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            // Obtener datos de la fila seleccionada
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String currentName = (String) tableModel.getValueAt(selectedRow, 1);
            String currentCategory = (String) tableModel.getValueAt(selectedRow, 2);
            String currentBrand = (String) tableModel.getValueAt(selectedRow, 3);
            int currentStock = (int) tableModel.getValueAt(selectedRow, 4);
            int currentPrice = (int) tableModel.getValueAt(selectedRow, 5);
            String currentLocation = (String) tableModel.getValueAt(selectedRow, 6);

            // Configurar campos para EntityDialog
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("name", "Nuevo Nombre");
            fields.put("category", "Nueva Categoría");
            fields.put("brand", "Nueva Marca");
            fields.put("stock", "Nuevo Stock");
            fields.put("price", "Nuevo Precio");
            fields.put("location", "Nueva Ubicación");

            Object[] initialValues = {currentName, currentCategory, currentBrand, currentStock, currentPrice, currentLocation};

            // Crear el diálogo
            EntityDialog dialog = new EntityDialog(this, "Editar Producto", fields, initialValues);
            dialog.setVisible(true);

            // Verificar si el usuario confirmó la acción
            if (dialog.isConfirmed()) {
                LinkedHashMap<String, String> values = dialog.getFieldValues();
                String newName = values.get("name");
                String newCategory = values.get("category");
                String newBrand = values.get("brand");
                int newStock = Integer.parseInt(values.get("stock"));
                int newPrice = Integer.parseInt(values.get("price"));
                String newLocation = values.get("location");

                if (!newName.isEmpty() && !newCategory.isEmpty() && !newBrand.isEmpty() && newStock > 0 && newPrice > 0 && !newLocation.isEmpty()) {
                    try {
                        controller.updateProduct(id, newName, newCategory, newBrand, newStock, newPrice, newLocation);
                        loadProducts();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, "Error al actualizar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor ingresa todos los campos correctamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedProduct(JTable productTable) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            int confirmation = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteProduct(id);
                    loadProducts();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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
