/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityDialog extends JDialog {

    private final LinkedHashMap<String, JTextField> fieldMap = new LinkedHashMap<>();
    private boolean confirmed;

    public EntityDialog(Frame owner, String title, LinkedHashMap<String, String> fields, String[] initialValues) {
        super(owner, title, true);
        setSize(400, 500);
        setLocationRelativeTo(owner);
        setUndecorated(true); // Sin bordes estándar
        setLayout(new BorderLayout(10, 10));

        // Panel principal con bordes redondeados
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(250, 250, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título estilizado
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 150, 243));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel para los campos
        JPanel fieldPanel = new JPanel(new GridLayout(fields.size(), 2, 10, 10));
        fieldPanel.setOpaque(false);

        int index = 0;
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            String key = entry.getKey();
            String label = entry.getValue();
            JLabel fieldLabel = new JLabel(label + ":");
            fieldLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            JTextField textField = new JTextField(initialValues != null ? initialValues[index++] : "");
            textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
            textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
            fieldPanel.add(fieldLabel);
            fieldPanel.add(textField);
            fieldMap.put(key, textField);
        }

        mainPanel.add(fieldPanel, BorderLayout.CENTER);

        // Botones
        JButton confirmButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");

        confirmButton.setBackground(new Color(76, 175, 80));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        confirmButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        add(mainPanel);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public LinkedHashMap<String, String> getFieldValues() {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        fieldMap.forEach((key, field) -> values.put(key, field.getText()));
        return values;
    }
}
