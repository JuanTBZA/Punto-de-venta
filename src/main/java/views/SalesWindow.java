package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;

public class SalesWindow extends JFrame {

    private final DefaultTableModel productTableModel;
    private final DefaultTableModel cartTableModel;
    private final JLabel totalLabel;
    private final JTextField searchField;
    private final JTextField selectedProductField;
    private final JTextField quantityField;
    private final JTextField clientField;
    private final JTextField dateField;
    private final JTextField timeField;

    public SalesWindow() {
        setTitle("Ventas");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Top section: Product table and search
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Search field and button
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchField = new JTextField();
        searchField.setToolTipText("Buscar producto por nombre");
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = new JButton("Buscar");
        styleButton(searchButton, new Color(33, 150, 243));
        searchPanel.add(searchButton, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.NORTH);

        // Product table
        String[] productColumns = {"ID", "Nombre", "Precio"};
        productTableModel = new DefaultTableModel(productColumns, 0);
        JTable productTable = new JTable(productTableModel);
        productTable.setRowHeight(30);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        topPanel.add(productScrollPane, BorderLayout.CENTER);

        // Right section: Product details and add to cart
        JPanel rightPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        topPanel.add(rightPanel, BorderLayout.EAST);

        JLabel selectedProductLabel = new JLabel("Producto seleccionado: ");
        rightPanel.add(selectedProductLabel);

        selectedProductField = new JTextField();
        selectedProductField.setEnabled(false);
        selectedProductField.setToolTipText("Nombre del producto seleccionado");
        rightPanel.add(selectedProductField);

        quantityField = new JTextField();
        quantityField.setToolTipText("Cantidad");
        rightPanel.add(quantityField);

        JButton addButton = new JButton("Agregar");
        styleButton(addButton, new Color(76, 175, 80));
        rightPanel.add(addButton);

        // Bottom section: Cart table and sale details
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        // Cart table
        String[] cartColumns = {"Nombre", "Cantidad", "Precio Unitario", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumns, 0);
        JTable cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(30);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        bottomPanel.add(cartScrollPane, BorderLayout.CENTER);

        // Sale details
        JPanel saleDetailsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        bottomPanel.add(saleDetailsPanel, BorderLayout.SOUTH);

        JPanel clientPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        clientField = new JTextField();
        clientField.setToolTipText("Cliente");
        clientPanel.add(clientField);

        dateField = new JTextField(LocalDate.now().toString());
        dateField.setToolTipText("Fecha");
        clientPanel.add(dateField);

        timeField = new JTextField(LocalTime.now().toString());
        timeField.setToolTipText("Hora");
        clientPanel.add(timeField);

        saleDetailsPanel.add(clientPanel);

        JPanel totalPanel = new JPanel(new BorderLayout(10, 10));
        totalLabel = new JLabel("TOTAL: $0.00", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalPanel.add(totalLabel, BorderLayout.CENTER);

        JButton sellButton = new JButton("Vender");
        styleButton(sellButton, new Color(244, 67, 54));
        totalPanel.add(sellButton, BorderLayout.EAST);

        saleDetailsPanel.add(totalPanel);

        // Actions
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchField.getText().trim();
                if (!searchQuery.isEmpty()) {
                    JOptionPane.showMessageDialog(SalesWindow.this, "Buscar producto: " + searchQuery);
                } else {
                    JOptionPane.showMessageDialog(SalesWindow.this, "Introduce un nombre para buscar.");
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    String name = productTableModel.getValueAt(selectedRow, 1).toString();
                    double price = Double.parseDouble(productTableModel.getValueAt(selectedRow, 2).toString());
                    int quantity = Integer.parseInt(quantityField.getText());
                    double subtotal = quantity * price;

                    selectedProductField.setText(name);
                    cartTableModel.addRow(new Object[]{name, quantity, price, subtotal});
                    updateTotal();
                } else {
                    JOptionPane.showMessageDialog(SalesWindow.this, "Selecciona un producto.");
                }
            }
        });

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cartTableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(SalesWindow.this, "No hay productos en el carrito.");
                    return;
                }

                String client = clientField.getText();
                String date = dateField.getText();
                String time = timeField.getText();

                if (client.isEmpty()) {
                    JOptionPane.showMessageDialog(SalesWindow.this, "Introduce el nombre del cliente.");
                    return;
                }

                JOptionPane.showMessageDialog(SalesWindow.this, "Venta realizada exitosamente.");
                cartTableModel.setRowCount(0);
                clientField.setText("");
                selectedProductField.setText("");
                updateTotal();
            }
        });

        // Dummy data for the product table
        loadDummyProducts();
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < cartTableModel.getRowCount(); i++) {
            total += (double) cartTableModel.getValueAt(i, 3);
        }
        totalLabel.setText("TOTAL: $" + String.format("%.2f", total));
    }

    private void loadDummyProducts() {
        productTableModel.addRow(new Object[]{1, "Producto A", 10.00});
        productTableModel.addRow(new Object[]{2, "Producto B", 15.50});
        productTableModel.addRow(new Object[]{3, "Producto C", 8.75});
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SalesWindow window = new SalesWindow();
            window.setVisible(true);
        });
    }
}
