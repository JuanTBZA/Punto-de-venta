package views;


import controllers.CategoryController;
import controllers.ProductController;
import models.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Category;

import repositories.CategoryRepositoryImpl;

import services.CategoryServiceImpl;
import utilities.ExcelDatabaseConnection;

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
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();

        List<String> categoriesArray = new ArrayList<>();  // Usamos ArrayList en lugar de un arreglo fijo

        try {
            ExcelDatabaseConnection connection = new ExcelDatabaseConnection();

            CategoryRepositoryImpl categoryRepository = new CategoryRepositoryImpl(connection);
            CategoryServiceImpl categoryService = new CategoryServiceImpl(categoryRepository);
            CategoryController categoryController = new CategoryController(categoryService);

            List<Category> categoryList = categoryController.listCategories();

            // Recorrer las categorías y agregar el nombre de cada una a la lista categories
            for (Category category : categoryList) {
                categoriesArray.add(category.getName());  // Suponiendo que getName() devuelve el nombre de la categoría
            }

            // Si necesitas convertir la lista a un arreglo de String, puedes hacerlo así:
        } catch (IOException ex) {
            Logger.getLogger(ProductWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] categories = categoriesArray.toArray(new String[0]);

        fields.put("Nombre", "Nombre");
        fields.put("Categoria", categories);
        fields.put("Marca", "Marca");
        fields.put("Stock", "Stock");
        fields.put("Precio", "Precio");
        fields.put("Ubicacion", "Ubicación");

        EntityDialog dialog = new EntityDialog(this, "Agregar Producto", fields, null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                LinkedHashMap<String, String> values = dialog.getFieldValues();
                String name = values.get("Nombre");
                String category = values.get("Categoria");
                String brand = values.get("Marca");
                int stock = Integer.parseInt(values.get("Stock"));
                Double price = Double.valueOf(values.get("Precio"));
                String location = values.get("Ubicacion");

                controller.addProduct(name, category, brand, stock, price, location);
                loadProducts();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al agregar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openEditDialog(JTable productTable) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String currentName = (String) tableModel.getValueAt(selectedRow, 1);
            String currentCategory = (String) tableModel.getValueAt(selectedRow, 2);
            String currentBrand = (String) tableModel.getValueAt(selectedRow, 3);
            int currentStock = (int) tableModel.getValueAt(selectedRow, 4);
            Double currentPrice = (Double) tableModel.getValueAt(selectedRow, 5);
            String currentLocation = (String) tableModel.getValueAt(selectedRow, 6);

            List<String> categoriesArray = new ArrayList<>();  // Usamos ArrayList en lugar de un arreglo fijo

            try {
                ExcelDatabaseConnection connection = new ExcelDatabaseConnection();

                CategoryRepositoryImpl categoryRepository = new CategoryRepositoryImpl(connection);
                CategoryServiceImpl categoryService = new CategoryServiceImpl(categoryRepository);
                CategoryController categoryController = new CategoryController(categoryService);

                List<Category> categoryList = categoryController.listCategories();

                // Recorrer las categorías y agregar el nombre de cada una a la lista categories
                for (Category category : categoryList) {
                    categoriesArray.add(category.getName());  // Suponiendo que getName() devuelve el nombre de la categoría
                }

                // Si necesitas convertir la lista a un arreglo de String, puedes hacerlo así:
            } catch (IOException ex) {
                Logger.getLogger(ProductWindow.class.getName()).log(Level.SEVERE, null, ex);
            }

            String[] categories = categoriesArray.toArray(new String[0]);

            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("Nombre", "Nombre");
            fields.put("Categoria", categories);
            fields.put("Marca", "Marca");
            fields.put("Stock", "Stock");
            fields.put("Precio", "Precio");
            fields.put("Ubicacion", "Ubicación");

            Object[] initialValues = {currentName, currentCategory, currentBrand, currentStock, currentPrice, currentLocation};
            EntityDialog dialog = new EntityDialog(this, "Editar Producto", fields, initialValues);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                try {
                    LinkedHashMap<String, String> values = dialog.getFieldValues();
                    String newName = values.get("Nombre");
                    String newCategory = values.get("Categoria");
                    String newBrand = values.get("Marca");
                    int newStock = Integer.parseInt(values.get("Stock"));
                    Double newPrice = Double.valueOf(values.get("Precio"));
                    String newLocation = values.get("Ubicacion");

                    controller.updateProduct(id, newName, newCategory, newBrand, newStock, newPrice, newLocation);
                    loadProducts();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
