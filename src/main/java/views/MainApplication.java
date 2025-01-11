/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import com.formdev.flatlaf.FlatLightLaf;
import controllers.BrandController;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import repositories.BrandRepositoryImpl;
import services.BrandServiceImpl;
import utilities.ExcelDatabaseConnection;

public class MainApplication extends JFrame {

    public MainApplication() throws IOException {
        setTitle("Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Aplicar tema moderno
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Botones para cada sección
        JButton productsButton = createStyledButton("Productos");
        JButton categoriesButton = createStyledButton("Categorías");
        JButton brandsButton = createStyledButton("Marcas");
        JButton clientsButton = createStyledButton("Clientes");
        JButton salesButton = createStyledButton("Ventas");

        // Agregar botones al panel
        mainPanel.add(productsButton);
        mainPanel.add(categoriesButton);
        mainPanel.add(brandsButton);
        mainPanel.add(clientsButton);
        mainPanel.add(salesButton);

        // Crear instancias necesarias para Brands
        BrandRepositoryImpl brandRepository = new BrandRepositoryImpl(new ExcelDatabaseConnection());
        BrandServiceImpl brandService = new BrandServiceImpl(brandRepository);
        BrandController brandController = new BrandController(brandService);

        // Listeners para abrir ventanas específicas
        productsButton.addActionListener(e -> new ProductWindow().setVisible(true));
        categoriesButton.addActionListener(e -> new CategoryWindow().setVisible(true));
        brandsButton.addActionListener(e -> new BrandWindow(brandController).setVisible(true));

        clientsButton.addActionListener(e -> new ClientWindow().setVisible(true));
        salesButton.addActionListener(e -> new SalesWindow().setVisible(true));

        // Configurar el frame
        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MainApplication mainApp = new MainApplication();
                mainApp.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
