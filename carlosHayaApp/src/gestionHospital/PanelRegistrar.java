package gestionHospital;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

import clases.DBConnection;
import clases.Empleado;

public class PanelRegistrar extends JPanel {
    private PanelImagen panelIamgen;
    private Border border = BorderFactory.createLineBorder(Color.black, 2);
	private DBConnection dbConnection;

    public PanelRegistrar(PanelImagen panelIamgen) {
    	this.dbConnection = new DBConnection();
        this.panelIamgen = panelIamgen;
        setPreferredSize(new Dimension(450, 450));
        setBackground(Color.decode("#212f3d"));
        setLayout(new BorderLayout());
        contenidos();

    }

    private void contenidos() {
        // === Título ===
        JLabel titleLabel = new JLabel("REGISTRAR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.decode("#F4D35E"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // === Panel del formulario ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.decode("#212f3d"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Color labelColor = Color.WHITE;

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(labelFont);
        nombreLabel.setForeground(labelColor);
        JTextField nombreField = new JTextField(20);

        JLabel apellidoLabel = new JLabel("Apellido:");
        apellidoLabel.setFont(labelFont);
        apellidoLabel.setForeground(labelColor);
        JTextField apellidoField = new JTextField(20);

        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setFont(labelFont);
        rolLabel.setForeground(labelColor);
        String[] roles = { "administrador", "administrativo", "medico", "mantedimiento" ,"enfermero"};
        JComboBox<String> rolComboBox = new JComboBox<>(roles);
        rolComboBox.setPreferredSize(new Dimension(184, 25));
        rolComboBox.setFocusable(false);

        JLabel usuarioLabel = new JLabel("DNI/NIE:");
        usuarioLabel.setFont(labelFont);
        usuarioLabel.setForeground(labelColor);
        JTextField usuarioField = new JTextField(20);

        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaLabel.setFont(labelFont);
        contrasenaLabel.setForeground(labelColor);
        JPasswordField contrasenaField = new JPasswordField(20);

        // Añadir campos
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nombreLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(apellidoLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(apellidoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(rolLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(rolComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(usuarioLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usuarioField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(contrasenaLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(contrasenaField, gbc);

        // === Botones ===
        JButton guardarBtn = new JButton("Guardar");
        JButton borrarBtn = new JButton("Borrar");
        JButton inicioBtn = new JButton("Inicio");

        guardarBtn.setFocusable(false);
        borrarBtn.setFocusable(false);
        inicioBtn.setFocusable(false);

        // Acción para Borrar
        borrarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nombreField.setText("");
                apellidoField.setText("");
                rolComboBox.setSelectedIndex(0);
                usuarioField.setText("");
                contrasenaField.setText("");

            }
        });

        // Acción para Inicio (volver al login)
        inicioBtn.addActionListener(e -> panelIamgen.cambiarPanel(new PanelLogin(panelIamgen)));

        guardarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText().trim();
                String apellido = apellidoField.getText().trim();
                String rol = (String) rolComboBox.getSelectedItem();
                String usuario = usuarioField.getText().trim();
                String contrasena = new String(contrasenaField.getPassword());

                // 1. Comprobar campos vacíos
                if (nombre.isEmpty() || apellido.isEmpty() || rol.equals("Seleccionar") || usuario.isEmpty()
                        || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Campos obligatorios",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 2. Validar nombre (sin espacios ni números)
                if (!nombre.matches("^[A-Za-z]+$")) {
                    JOptionPane.showMessageDialog(null, "El nombre no debe tener espacios ni números.",
                            "Nombre inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 3. Validar apellidos (puede tener espacios, pero no números)
                if (!apellido.matches("^[A-Za-z\\s]+$")) {
                    JOptionPane.showMessageDialog(null, "El apellido no debe tener números.", "Apellido inválido",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 5. Validación completada con éxito
                JOptionPane.showMessageDialog(null, "Usuario registrado correctamente.", "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

                try {
                    Empleado nuevoEmpleado = new Empleado(0, nombre, apellido, usuario, rol, contrasena);

                    if (dbConnection.registrar(nuevoEmpleado)) {
                        JOptionPane.showMessageDialog(null, "Usuario registrado correctamente.", "Registro exitoso",
                                JOptionPane.INFORMATION_MESSAGE);
                        panelIamgen.cambiarPanel(new PanelLogin(panelIamgen)); // Volver al login después del registro
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al registrar el usuario. El DNI/NIE podría ya existir o hubo un problema en la base de datos.", "Error de registro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error inesperado: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            
                panelIamgen.cambiarPanel(new PanelLogin(panelIamgen));
            }

        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#212f3d"));
        styleButton(inicioBtn);
        styleButton(guardarBtn);
        styleButton(borrarBtn);
        buttonPanel.add(guardarBtn);
        buttonPanel.add(borrarBtn);
        buttonPanel.add(inicioBtn);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

    }

    private void styleButton(JButton button) {
        button.setBackground(Color.white);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 30));
        button.setBorder(border);
    }
}