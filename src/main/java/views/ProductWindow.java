/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashMap;

public class ProductWindow extends JFrame {

    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductWindow() {
        setTitle("Gestión de Productos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Configuración de la tabla
        String[] columnNames = {"ID", "Nombre", "Categoría", "Marca", "Stock", "Precio"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        productTable.setRowHeight(25);
        JScrollPane tableScrollPane = new JScrollPane(productTable);

        // Botones
        JButton createButton = new JButton("Crear");
        JButton modifyButton = new JButton("Modificar");
        JButton deleteButton = new JButton("Eliminar");

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Listeners
        createButton.addActionListener(e -> showDialog(null, -1));
        modifyButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                String[] currentValues = new String[tableModel.getColumnCount()];
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    currentValues[i] = (String) tableModel.getValueAt(selectedRow, i);
                }
                showDialog(currentValues, selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para modificar.");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            }
        });
    }

    private void showDialog(String[] initialValues, int rowIndex) {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        fields.put("id", "ID");
        fields.put("nombre", "Nombre");
        fields.put("categoria", "Categoría");
        fields.put("marca", "Marca");
        fields.put("stock", "Stock");
        fields.put("precio", "Precio");

        EntityDialog dialog = new EntityDialog(this, "Producto", fields, initialValues);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            LinkedHashMap<String, String> fieldValues = dialog.getFieldValues();
            if (rowIndex == -1) {
                tableModel.addRow(fieldValues.values().toArray());
            } else {
                int colIndex = 0;
                for (String value : fieldValues.values()) {
                    tableModel.setValueAt(value, rowIndex, colIndex++);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductWindow().setVisible(true));
    }
}

