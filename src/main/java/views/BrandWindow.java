package views;

import controllers.BrandController;
import models.Brand;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import repositories.BrandRepositoryImpl;
import services.BrandServiceImpl;
import utilities.ExcelDatabaseConnection;

public class BrandWindow extends JFrame {

    private final DefaultTableModel tableModel;
    private final BrandController controller;

    public BrandWindow(BrandController controller) {
        this.controller = controller;

        setTitle("Gestión de Marcas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Título
        JLabel titleLabel = new JLabel("Marcas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 150, 243));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"ID", "Nombre"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable brandTable = new JTable(tableModel);
        brandTable.setRowHeight(25);
        brandTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(brandTable);
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
        editButton.addActionListener(e -> openEditDialog(brandTable));
        deleteButton.addActionListener(e -> deleteSelectedBrand(brandTable));

        // Cargar datos iniciales
        loadBrands();
    }

    private void loadBrands() {
        try {
            tableModel.setRowCount(0);
            List<Brand> brands = controller.listBrands();
            for (Brand brand : brands) {
                tableModel.addRow(new Object[]{brand.getId(), brand.getName()});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las marcas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddDialog() {
        String name = JOptionPane.showInputDialog(this, "Nombre de la nueva marca:", "Agregar Marca", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            try {
                controller.addBrand(name);
                loadBrands();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar la marca: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openEditDialog(JTable brandTable) {
        int selectedRow = brandTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String currentName = (String) tableModel.getValueAt(selectedRow, 1);

            String newName = JOptionPane.showInputDialog(this, "Nuevo nombre de la marca:", currentName);
            if (newName != null && !newName.trim().isEmpty()) {
                try {
                    controller.updateBrand(id, newName);
                    loadBrands();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar la marca: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una marca para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedBrand(JTable brandTable) {
        int selectedRow = brandTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            int confirmation = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar esta marca?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    controller.deleteBrand(id);
                    loadBrands();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la marca: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una marca para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public static void main(String[] args) throws IOException {
        // Aquí se debe inicializar el servicio y controlador
        BrandServiceImpl service = new BrandServiceImpl(new BrandRepositoryImpl(new ExcelDatabaseConnection())); // Implementación ficticia
        BrandController controller = new BrandController(service);

        SwingUtilities.invokeLater(() -> new BrandWindow(controller).setVisible(true));
    }
}
