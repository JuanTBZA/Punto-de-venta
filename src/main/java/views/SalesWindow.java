/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashMap;

public class SalesWindow extends JFrame {

    private final DefaultTableModel tableModel;

    public SalesWindow() {
        setTitle("Gestión de Ventas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Título
        JLabel titleLabel = new JLabel("Ventas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 150, 243));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla
        String[] columnNames = {"ID Venta", "Producto", "Cliente", "Cantidad", "Fecha", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable salesTable = new JTable(tableModel);
        salesTable.setRowHeight(25);
        salesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(salesTable);
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
        addButton.addActionListener(e -> openDialog("Agregar Venta", null));
        editButton.addActionListener(e -> {
            int selectedRow = salesTable.getSelectedRow();
            if (selectedRow != -1) {
                String[] initialValues = {
                        tableModel.getValueAt(selectedRow, 0).toString(),
                        tableModel.getValueAt(selectedRow, 1).toString(),
                        tableModel.getValueAt(selectedRow, 2).toString(),
                        tableModel.getValueAt(selectedRow, 3).toString(),
                        tableModel.getValueAt(selectedRow, 4).toString(),
                        tableModel.getValueAt(selectedRow, 5).toString()
                };
                openDialog("Editar Venta", initialValues);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una venta para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = salesTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirmation = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar esta venta?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una venta para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void openDialog(String title, String[] initialValues) {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        fields.put("id", "ID Venta");
        fields.put("product", "Producto");
        fields.put("client", "Cliente");
        fields.put("quantity", "Cantidad");
        fields.put("date", "Fecha");
        fields.put("total", "Total");

        EntityDialog dialog = new EntityDialog(this, title, fields, initialValues);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            LinkedHashMap<String, String> fieldValues = dialog.getFieldValues();
            if (initialValues == null) { // Agregar
                tableModel.addRow(new Object[]{
                        fieldValues.get("id"),
                        fieldValues.get("product"),
                        fieldValues.get("client"),
                        fieldValues.get("quantity"),
                        fieldValues.get("date"),
                        fieldValues.get("total")
                });
            } else { // Editar
                int selectedRow = getSelectedRowById(fieldValues.get("id"));
                if (selectedRow != -1) {
                    tableModel.setValueAt(fieldValues.get("id"), selectedRow, 0);
                    tableModel.setValueAt(fieldValues.get("product"), selectedRow, 1);
                    tableModel.setValueAt(fieldValues.get("client"), selectedRow, 2);
                    tableModel.setValueAt(fieldValues.get("quantity"), selectedRow, 3);
                    tableModel.setValueAt(fieldValues.get("date"), selectedRow, 4);
                    tableModel.setValueAt(fieldValues.get("total"), selectedRow, 5);
                }
            }
        }
    }

    private int getSelectedRowById(String id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SalesWindow().setVisible(true));
    }
}
