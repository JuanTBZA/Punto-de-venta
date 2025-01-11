package views;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityDialog extends JDialog {

    private final LinkedHashMap<String, JComponent> fieldMap = new LinkedHashMap<>();
    private boolean confirmed;

    public EntityDialog(Frame owner, String title, LinkedHashMap<String, Object> fields, Object[] initialValues) {
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
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            JLabel fieldLabel = new JLabel(value.toString() + ":");
            fieldLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            fieldPanel.add(fieldLabel);

            // Determina si el campo es un JTextField o JComboBox
            JComponent fieldComponent;
            if (value instanceof String[]) { // Si es un JComboBox
                String[] options = (String[]) value;
                JComboBox<String> comboBox = new JComboBox<>(options);
                if (initialValues != null && initialValues[index] instanceof String) {
                    comboBox.setSelectedItem(initialValues[index]);
                }
                fieldComponent = comboBox;
            } else { // Si es un JTextField
                JTextField textField = new JTextField(initialValues != null ? initialValues[index].toString() : "");
                textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
                textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
                fieldComponent = textField;
            }

            fieldPanel.add(fieldComponent);
            fieldMap.put(key, fieldComponent);
            index++;
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
        fieldMap.forEach((key, field) -> {
            if (field instanceof JTextField) {
                values.put(key, ((JTextField) field).getText());
            } else if (field instanceof JComboBox) {
                values.put(key, ((JComboBox<?>) field).getSelectedItem().toString());
            }
        });
        return values;
    }
}
