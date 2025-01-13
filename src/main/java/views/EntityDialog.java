package views;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

public class EntityDialog extends JDialog {

    private final JPanel fieldPanel;
    private final LinkedHashMap<String, JComponent> fieldComponents;
    private boolean confirmed = false;

    public EntityDialog(JFrame parent, String title, LinkedHashMap<String, Object> fields, Object[] initialValues) {
        super(parent, title, true);

        setSize(400, 300);  // Tamaño inicial ajustado para evitar espacio innecesario
        setLocationRelativeTo(parent);
        setUndecorated(true); // Sin bordes estándar
        setLayout(new BorderLayout(10, 10));

        // Panel para el título
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setOpaque(false);  // Hacer transparente el fondo del título

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));  // Estilo de fuente para el título
        titleLabel.setForeground(new Color(33, 150, 243));  // Color azul para el título
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Panel de campos con GridBagLayout
        fieldPanel = new JPanel(new GridBagLayout());
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(fieldPanel, BorderLayout.CENTER);

        // Configuración de los campos
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Permitir que los campos se expandan proporcionalmente

        fieldComponents = new LinkedHashMap<>();
        int i = 0;
        for (String key : fields.keySet()) {
            JLabel label = new JLabel(key + ":");
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));  // Estilo de la etiqueta
            JComponent component;

            Object fieldType = fields.get(key);
            if (fieldType instanceof String[]) { // Si es un JComboBox
                component = new JComboBox<>((String[]) fieldType);
            } else { // Por defecto, un JTextField
                component = new JTextField();
            }

            // Establecer un tamaño preferido para los componentes
            component.setPreferredSize(new Dimension(250, 30)); // Ajustar el tamaño de los componentes

            // Añadir la etiqueta y el componente al panel
            fieldPanel.add(label, gbc);
            gbc.gridx = 1;
            fieldPanel.add(component, gbc);

            fieldComponents.put(key, component);

            // Establecer valores iniciales (si los hay)
            if (initialValues != null && initialValues.length > i) {
                if (component instanceof JComboBox) {
                    ((JComboBox<String>) component).setSelectedItem(initialValues[i]);
                } else if (component instanceof JTextField) {
                    ((JTextField) component).setText(initialValues[i].toString());
                }
            }
            i++;
            gbc.gridy++; // Mover a la siguiente fila
            gbc.gridx = 0; // Volver a la columna de las etiquetas
        }

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");

        confirmButton.setBackground(new Color(76, 175, 80));  // Estilo de fondo del botón
        confirmButton.setForeground(Color.WHITE);              // Texto blanco
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 14));  // Fuente en negrita
        confirmButton.setFocusPainted(false);                  // Elimina el borde cuando se hace clic
        confirmButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // Borde vacío

        cancelButton.setBackground(new Color(244, 67, 54));   // Estilo de fondo del botón
        cancelButton.setForeground(Color.WHITE);               // Texto blanco
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));  // Fuente en negrita
        cancelButton.setFocusPainted(false);                   // Elimina el borde cuando se hace clic
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // Borde vacío

        // Añadir botones al panel
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Añadir panel del título
        add(titlePanel, BorderLayout.NORTH);

        // Acciones de los botones
        confirmButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public LinkedHashMap<String, String> getFieldValues() {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        for (String key : fieldComponents.keySet()) {
            JComponent component = fieldComponents.get(key);
            if (component instanceof JTextField) {
                values.put(key, ((JTextField) component).getText());
            } else if (component instanceof JComboBox) {
                values.put(key, (String) ((JComboBox<?>) component).getSelectedItem());
            }
        }
        return values;
    }
}
