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

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Panel de campos
        fieldPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(fieldPanel, BorderLayout.CENTER);

        // Map de componentes
        fieldComponents = new LinkedHashMap<>();
        int i = 0;
        for (String key : fields.keySet()) {
            JLabel label = new JLabel(key + ":");
            JComponent component;

            Object fieldType = fields.get(key);
            if (fieldType instanceof String[]) { // Si es un JComboBox
                component = new JComboBox<>((String[]) fieldType);
            } else { // Por defecto, un JTextField
                component = new JTextField();
            }

            fieldPanel.add(label);
            fieldPanel.add(component);

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
        }

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("Aceptar");
        JButton cancelButton = new JButton("Cancelar");

        confirmButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
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
