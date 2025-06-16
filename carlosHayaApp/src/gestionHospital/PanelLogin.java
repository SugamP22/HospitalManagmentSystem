package gestionHospital;

import clases.DBConnection;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class PanelLogin extends JPanel {

	private JTextField usuarioField;
	private JPasswordField contrasenaField;
	private JButton iniciarButton, registrarButton;
	private final PanelImagen panelImagen;
	private Border border = BorderFactory.createLineBorder(Color.black, 1);

	public PanelLogin(PanelImagen panelImagen) {
		this.panelImagen = panelImagen;
		setPreferredSize(new Dimension(400, 300));
		contenidos();
	}

	// Crear los componentes del panel
	private void contenidos() {
		setBackground(Color.decode("#212f3d")); // Fondo azul oscuro

		Font labelFont = new Font("Arial", Font.BOLD, 16);
		Color labelColor = Color.WHITE;

		// Panel de título
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(getBackground());
		JLabel titleLabel = new JLabel("INICIAR SESIÓN");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
		titleLabel.setForeground(Color.decode("#F4D35E"));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		titlePanel.add(titleLabel);

		// Panel de usuario
		JPanel usuarioPanel = new JPanel(new GridBagLayout());
		usuarioPanel.setBackground(getBackground());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);

		// Configuración de la etiqueta de usuario
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel usuarioLabel = new JLabel("Usuario");
		usuarioLabel.setFont(labelFont);
		usuarioLabel.setForeground(labelColor);
		usuarioLabel.setBorder(new EmptyBorder(0, 10, 0, 47));
		usuarioPanel.add(usuarioLabel, gbc);

		// Campo de texto de usuario
		gbc.gridx = 1;
		usuarioField = new JTextField(15);
		usuarioField.setBorder(new EmptyBorder(0, 10, 0, 10));
		usuarioField.setPreferredSize(new Dimension(75, 25));
		usuarioPanel.add(usuarioField, gbc);

		// Panel de contraseña
		JPanel contrasenaPanel = new JPanel(new GridBagLayout());
		contrasenaPanel.setBackground(getBackground());

		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel contrasenaLabel = new JLabel("Contraseña");
		contrasenaLabel.setFont(labelFont);
		contrasenaLabel.setForeground(labelColor);
		contrasenaLabel.setBorder(new EmptyBorder(0, 10, 0, 20));
		contrasenaPanel.add(contrasenaLabel, gbc);

		gbc.gridx = 1;
		contrasenaField = new JPasswordField(15);
		contrasenaField.setBorder(new EmptyBorder(0, 10, 0, 10));
		contrasenaField.setPreferredSize(new Dimension(75, 25));
		contrasenaPanel.add(contrasenaField, gbc);

		// Panel de botones
		JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		botonesPanel.setBackground(getBackground());
		iniciarButton = new JButton("Iniciar");
		registrarButton = new JButton("Registrar");

		styleButton(iniciarButton);
		styleButton(registrarButton);

		botonesPanel.add(iniciarButton);
		botonesPanel.add(registrarButton);

		// Añadir todo al panel principal
		setLayout(new GridLayout(4, 1, 30, 30));
		add(titlePanel);
		add(usuarioPanel);
		add(contrasenaPanel);
		add(botonesPanel);

		registrarButton.addActionListener(e -> panelImagen.cambiarPanel(new PanelRegistrar(panelImagen)));
		iniciarButton.addActionListener(e -> {
			String usuario = usuarioField.getText();
			String contrasena = new String(contrasenaField.getPassword());
			Sesion.setUsuarioLogueado(usuario);

			DBConnection db = new DBConnection();
			db.comprobarPrimerLogin(usuario, contrasena);
			String rol = db.iniciarSesion(usuario, contrasena);

			// Llamada a método que devuelve rol o null
			if (rol == null) {
				JOptionPane.showMessageDialog(null, "Usuario no encontrado o error en la conexión.");
			} else {
				switch (rol) {
				case "administrador":
					panelImagen.cambiarPanel(new PanelAdmin(panelImagen));
					break;
				case "administrativo":
					panelImagen.cambiarPanel(new PanelAdministrativo(panelImagen));

					break;
				case "medico":
					panelImagen.cambiarPanel(new PanelMedico(panelImagen));
					break;
				case "enfermero":
					panelImagen.cambiarPanel(new PanelEnfermero(panelImagen));
					break;
				case "mantenimiento":
					panelImagen.cambiarPanel(new PanelMantenimiento(panelImagen));
					break;

				default:
					JOptionPane.showMessageDialog(null, "Rol no reconocido: " + rol);
					break;
				}
			}
		});
	}

	// Estilo de los botones
	private void styleButton(JButton button) {
		button.setBackground(Color.white);
		button.setForeground(Color.BLACK);
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(100, 30));
		button.setBorder(border);
	}
}