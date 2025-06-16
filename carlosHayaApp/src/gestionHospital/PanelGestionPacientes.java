package gestionHospital;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.List; // Import List
import clases.DBConnection; // Import your DBConnection class
import clases.Paciente; // Import your Paciente class

@SuppressWarnings("serial")
public class PanelGestionPacientes extends JPanel {

	private JPanel cardsPanel;
	private DefaultTableModel modeloPacientes;
	private JTable tablaPacientes;
	private JTextField txtDNIpaciente, txtNombrePaciente, txtApellidoPaciente, txtContactoPaciente,
			txtObraSocialPaciente;

	// Fields for Diagnostico and Habitacion forms
	private JTextArea txtDiagnostico; // For adding/editing patient diagnosis
	private JTextField txtAsignarHabitacion; // For assigning a room number

	private int filaSeleccionadaPaciente = -1;

	// Instance of DBConnection
	private DBConnection dbConnection;

	// Colores y estilos extraídos directamente del PanelAdmin original para
	// consistencia
	private Color mainPanelBgColor = Color.decode("#E3242B"); // Fondo del panel principal de gestión
	private Color cardsPanelBgColor = Color.decode("#B0E0E6"); // Fondo del panel con CardLayout (tabla/formulario)
	private Color formPanelBgColor = Color.decode("#24e3dc"); // Fondo del panel del formulario
	private Color tableButtonsPanelBgColor = Color.decode("#212f3d"); // Fondo del panel de botones de la tabla

	private Color gestionButtonBgColor = Color.decode("#CD7F32"); // Color de fondo de los botones de gestión
	private Color gestionButtonFgColor = Color.white; // Color de texto de los botones de gestión
	private Font gestionButtonFont = new Font("Arial", Font.BOLD, 11); // Fuente de los botones de gestión
	private Border gestionButtonBorder = BorderFactory.createLineBorder(Color.decode("#CD7F32"), 1); // Borde de los
																										// botones de
																										// gestión

	private Color titleFgColor = Color.white; // Color del texto del título principal del panel
	private Color labelFgColor = Color.DARK_GRAY; // Color del texto de las etiquetas del formulario

	private Color tableHeaderBg = Color.decode("#f2f2f2"); // Fondo del encabezado de la tabla
	private Color tableHeaderFg = Color.decode("#333"); // Color de texto del encabezado de la tabla

	public PanelGestionPacientes() {
		this.setLayout(new BorderLayout());
		this.setBackground(mainPanelBgColor); // Aplicar el color de fondo principal

		// Initialize DBConnection
		dbConnection = new DBConnection();
		
		JLabel titulo = new JLabel("Gestión de Pacientes", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		titulo.setForeground(titleFgColor); // Aplicar color de texto del título
		titulo.setBorder(new EmptyBorder(20, 0, 20, 0));
		this.add(titulo, BorderLayout.NORTH);

		cardsPanel = new JPanel(new CardLayout());
		cardsPanel.setBackground(cardsPanelBgColor); // Aplicar color de fondo de las cards

		// --- Vista de Tabla de Pacientes ---
		JPanel panelTablaPacientesLocal = new JPanel(new BorderLayout());
		panelTablaPacientesLocal.setBackground(cardsPanelBgColor); // Fondo de la tabla (dentro de cardsPanel)

		// Updated columns for new patient fields - Diagnostico is still here for data
		// but will be hidden
		String[] columnasPacientes = { "DNI", "Nombre", "Apellido", "Contacto", "Obra Social", "Diagnóstico",
				"Habitación" };
		modeloPacientes = new DefaultTableModel(columnasPacientes, 0) { // Start with 0 rows
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaPacientes = new JTable(modeloPacientes);
		tablaPacientes.setFont(new Font("Arial", Font.PLAIN, 14));
		tablaPacientes.setRowHeight(25);
		tablaPacientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		tablaPacientes.setBackground(Color.WHITE);
		tablaPacientes.setForeground(Color.BLACK);
		tablaPacientes.getTableHeader().setBackground(tableHeaderBg);
		tablaPacientes.getTableHeader().setForeground(tableHeaderFg);
		JScrollPane scrollTablaPacientes = new JScrollPane(tablaPacientes);
		scrollTablaPacientes.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		panelTablaPacientesLocal.add(scrollTablaPacientes, BorderLayout.CENTER);

		// Load patients from DB when the panel is initialized
		cargarPacientesDeDB();

		// Hide the "Diagnóstico" column (index 5) from the table view
		// The column still exists in the model to hold data
		TableColumnModel columnModel = tablaPacientes.getColumnModel();
		// Check if the column exists before attempting to remove it
		if (columnModel.getColumnCount() > 5) { // Ensure there are enough columns
			columnModel.removeColumn(columnModel.getColumn(5)); // Remove the "Diagnóstico" column from view
		}

		JPanel panelBotonesTablaPacientes = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10)); // Reduced hgap
		panelBotonesTablaPacientes.setBackground(tableButtonsPanelBgColor); // Fondo del panel de botones de tabla

		JButton btnRegistrar = new JButton("Registrar Paciente");
		styleGestionButton(btnRegistrar);
		JButton btnEditar = new JButton("Editar Paciente");
		styleGestionButton(btnEditar);
		JButton btnEliminar = new JButton("Eliminar Paciente");
		styleGestionButton(btnEliminar);
		JButton btnAgregarDiagnostico = new JButton("Agregar Diagnóstico");
		styleGestionButton(btnAgregarDiagnostico);
		JButton btnAsignarHabitacion = new JButton("Asignar Habitación");
		styleGestionButton(btnAsignarHabitacion);

		panelBotonesTablaPacientes.add(btnRegistrar);
		panelBotonesTablaPacientes.add(btnEditar);
		panelBotonesTablaPacientes.add(btnEliminar);
		panelBotonesTablaPacientes.add(btnAgregarDiagnostico);
		panelBotonesTablaPacientes.add(btnAsignarHabitacion);
		panelTablaPacientesLocal.add(panelBotonesTablaPacientes, BorderLayout.SOUTH);

		// --- Vista de Formulario de Paciente (Add/Modify) ---
		JPanel panelFormularioPacienteLocal = new JPanel(new GridBagLayout());
		panelFormularioPacienteLocal.setBackground(formPanelBgColor); // Fondo del formulario
		panelFormularioPacienteLocal.setBorder(new EmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 5, 10, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		Font labelFont = new Font("Arial", Font.BOLD, 16);
		Font textFieldFont = new Font("Arial", Font.PLAIN, 16);

		// DNI
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel lblDNIpaciente = new JLabel("DNI:");
		lblDNIpaciente.setFont(labelFont);
		lblDNIpaciente.setForeground(labelFgColor);
		panelFormularioPacienteLocal.add(lblDNIpaciente, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		txtDNIpaciente = new JTextField(20);
		txtDNIpaciente.setFont(textFieldFont);
		panelFormularioPacienteLocal.add(txtDNIpaciente, gbc);

		// Nombre
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel lblNombrePaciente = new JLabel("Nombre:");
		lblNombrePaciente.setFont(labelFont);
		lblNombrePaciente.setForeground(labelFgColor);
		panelFormularioPacienteLocal.add(lblNombrePaciente, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		txtNombrePaciente = new JTextField(20);
		txtNombrePaciente.setFont(textFieldFont);
		panelFormularioPacienteLocal.add(txtNombrePaciente, gbc);

		// Apellido
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel lblApellidoPaciente = new JLabel("Apellido:");
		lblApellidoPaciente.setFont(labelFont);
		lblApellidoPaciente.setForeground(labelFgColor);
		panelFormularioPacienteLocal.add(lblApellidoPaciente, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		txtApellidoPaciente = new JTextField(20);
		txtApellidoPaciente.setFont(textFieldFont);
		panelFormularioPacienteLocal.add(txtApellidoPaciente, gbc);

		// Contacto
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel lblContactoPaciente = new JLabel("Contacto:");
		lblContactoPaciente.setFont(labelFont);
		lblContactoPaciente.setForeground(labelFgColor);
		panelFormularioPacienteLocal.add(lblContactoPaciente, gbc);
		gbc.gridx = 1;
		gbc.gridy = 3;
		txtContactoPaciente = new JTextField(20);
		txtContactoPaciente.setFont(textFieldFont);
		panelFormularioPacienteLocal.add(txtContactoPaciente, gbc);

		// Obra Social
		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel lblObraSocialPaciente = new JLabel("Obra Social:");
		lblObraSocialPaciente.setFont(labelFont);
		lblObraSocialPaciente.setForeground(labelFgColor);
		panelFormularioPacienteLocal.add(lblObraSocialPaciente, gbc);
		gbc.gridx = 1;
		gbc.gridy = 4;
		txtObraSocialPaciente = new JTextField(20);
		txtObraSocialPaciente.setFont(textFieldFont);
		panelFormularioPacienteLocal.add(txtObraSocialPaciente, gbc);

		// Botones del formulario de paciente
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JPanel panelBotonesFormularioPaciente = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		panelBotonesFormularioPaciente.setBackground(formPanelBgColor);
		JButton btnGuardarPaciente = new JButton("Guardar");
		styleGestionButton(btnGuardarPaciente);
		JButton btnCancelarPaciente = new JButton("Cancelar");
		styleGestionButton(btnCancelarPaciente);
		panelBotonesFormularioPaciente.add(btnGuardarPaciente);
		panelBotonesFormularioPaciente.add(btnCancelarPaciente);
		panelFormularioPacienteLocal.add(panelBotonesFormularioPaciente, gbc);

		// --- Vista de Formulario para Agregar Diagnóstico ---
		JPanel panelAgregarDiagnostico = new JPanel(new GridBagLayout());
		panelAgregarDiagnostico.setBackground(formPanelBgColor);
		panelAgregarDiagnostico.setBorder(new EmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbcDiagnostico = new GridBagConstraints();
		gbcDiagnostico.insets = new Insets(10, 5, 10, 5);
		gbcDiagnostico.fill = GridBagConstraints.BOTH; // Allow text area to expand

		// Patient DNI (read-only)
		gbcDiagnostico.gridx = 0;
		gbcDiagnostico.gridy = 0;
		JLabel lblDniDiagnostico = new JLabel("DNI Paciente:");
		lblDniDiagnostico.setFont(labelFont);
		lblDniDiagnostico.setForeground(labelFgColor);
		panelAgregarDiagnostico.add(lblDniDiagnostico, gbcDiagnostico);
		gbcDiagnostico.gridx = 1;
		gbcDiagnostico.gridy = 0;
		JTextField txtDniPacienteDiagnostico = new JTextField(20);
		txtDniPacienteDiagnostico.setFont(textFieldFont);
		txtDniPacienteDiagnostico.setEditable(false);
		panelAgregarDiagnostico.add(txtDniPacienteDiagnostico, gbcDiagnostico);

		// Patient Nombre (read-only)
		gbcDiagnostico.gridx = 0;
		gbcDiagnostico.gridy = 1;
		JLabel lblNombreDiagnostico = new JLabel("Nombre Paciente:");
		lblNombreDiagnostico.setFont(labelFont);
		lblNombreDiagnostico.setForeground(labelFgColor);
		panelAgregarDiagnostico.add(lblNombreDiagnostico, gbcDiagnostico);
		gbcDiagnostico.gridx = 1;
		gbcDiagnostico.gridy = 1;
		JTextField txtNombrePacienteDiagnostico = new JTextField(20);
		txtNombrePacienteDiagnostico.setFont(textFieldFont);
		txtNombrePacienteDiagnostico.setEditable(false);
		panelAgregarDiagnostico.add(txtNombrePacienteDiagnostico, gbcDiagnostico);

		// Patient Apellido (read-only)
		gbcDiagnostico.gridx = 0;
		gbcDiagnostico.gridy = 2;
		JLabel lblApellidoDiagnostico = new JLabel("Apellido Paciente:");
		lblApellidoDiagnostico.setFont(labelFont);
		lblApellidoDiagnostico.setForeground(labelFgColor);
		panelAgregarDiagnostico.add(lblApellidoDiagnostico, gbcDiagnostico);
		gbcDiagnostico.gridx = 1;
		gbcDiagnostico.gridy = 2;
		JTextField txtApellidoPacienteDiagnostico = new JTextField(20);
		txtApellidoPacienteDiagnostico.setFont(textFieldFont);
		txtApellidoPacienteDiagnostico.setEditable(false);
		panelAgregarDiagnostico.add(txtApellidoPacienteDiagnostico, gbcDiagnostico);

		// Diagnóstico input (JTextArea)
		gbcDiagnostico.gridx = 0;
		gbcDiagnostico.gridy = 3;
		JLabel lblDiagnostico = new JLabel("Diagnóstico:");
		lblDiagnostico.setFont(labelFont);
		lblDiagnostico.setForeground(labelFgColor);
		panelAgregarDiagnostico.add(lblDiagnostico, gbcDiagnostico);
		gbcDiagnostico.gridx = 1;
		gbcDiagnostico.gridy = 3;
		gbcDiagnostico.weightx = 1.0;
		gbcDiagnostico.weighty = 1.0; // Allow it to expand vertically
		txtDiagnostico = new JTextArea(5, 30); // 5 rows for multi-line diagnosis
		txtDiagnostico.setFont(textFieldFont);
		txtDiagnostico.setLineWrap(true); // Wrap lines
		txtDiagnostico.setWrapStyleWord(true); // Wrap at word boundaries
		JScrollPane scrollDiagnostico = new JScrollPane(txtDiagnostico);
		panelAgregarDiagnostico.add(scrollDiagnostico, gbcDiagnostico);

		// Buttons for Diagnóstico
		gbcDiagnostico.gridx = 0;
		gbcDiagnostico.gridy = 4;
		gbcDiagnostico.gridwidth = 2;
		gbcDiagnostico.weighty = 0.0; // Reset weight for buttons
		gbcDiagnostico.anchor = GridBagConstraints.CENTER;
		JPanel panelBotonesDiagnostico = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		panelBotonesDiagnostico.setBackground(formPanelBgColor);
		JButton btnGuardarDiagnostico = new JButton("Guardar Diagnóstico");
		styleGestionButton(btnGuardarDiagnostico);
		JButton btnCancelarDiagnostico = new JButton("Cancelar");
		styleGestionButton(btnCancelarDiagnostico);
		panelBotonesDiagnostico.add(btnGuardarDiagnostico);
		panelBotonesDiagnostico.add(btnCancelarDiagnostico);
		panelAgregarDiagnostico.add(panelBotonesDiagnostico, gbcDiagnostico);

		// --- Vista de Formulario para Asignar Habitación ---
		JPanel panelAsignarHabitacion = new JPanel(new GridBagLayout());
		panelAsignarHabitacion.setBackground(formPanelBgColor);
		panelAsignarHabitacion.setBorder(new EmptyBorder(50, 50, 50, 50));
		GridBagConstraints gbcHabitacion = new GridBagConstraints();
		gbcHabitacion.insets = new Insets(10, 5, 10, 5);
		gbcHabitacion.fill = GridBagConstraints.HORIZONTAL;

		// Patient DNI (read-only)
		gbcHabitacion.gridx = 0;
		gbcHabitacion.gridy = 0;
		JLabel lblDniHabitacion = new JLabel("DNI Paciente:");
		lblDniHabitacion.setFont(labelFont);
		lblDniHabitacion.setForeground(labelFgColor);
		panelAsignarHabitacion.add(lblDniHabitacion, gbcHabitacion);
		gbcHabitacion.gridx = 1;
		gbcHabitacion.gridy = 0;
		JTextField txtDniPacienteHabitacion = new JTextField(20);
		txtDniPacienteHabitacion.setFont(textFieldFont);
		txtDniPacienteHabitacion.setEditable(false);
		panelAsignarHabitacion.add(txtDniPacienteHabitacion, gbcHabitacion);

		// Patient Nombre (read-only)
		gbcHabitacion.gridx = 0;
		gbcHabitacion.gridy = 1;
		JLabel lblNombreHabitacion = new JLabel("Nombre Paciente:");
		lblNombreHabitacion.setFont(labelFont);
		lblNombreHabitacion.setForeground(labelFgColor);
		panelAsignarHabitacion.add(lblNombreHabitacion, gbcHabitacion);
		gbcHabitacion.gridx = 1;
		gbcHabitacion.gridy = 1;
		JTextField txtNombrePacienteHabitacion = new JTextField(20);
		txtNombrePacienteHabitacion.setFont(textFieldFont);
		txtNombrePacienteHabitacion.setEditable(false);
		panelAsignarHabitacion.add(txtNombrePacienteHabitacion, gbcHabitacion);

		// Patient Apellido (read-only)
		gbcHabitacion.gridx = 0;
		gbcHabitacion.gridy = 2;
		JLabel lblApellidoHabitacion = new JLabel("Apellido Paciente:");
		lblApellidoHabitacion.setFont(labelFont);
		lblApellidoHabitacion.setForeground(labelFgColor);
		panelAsignarHabitacion.add(lblApellidoHabitacion, gbcHabitacion);
		gbcHabitacion.gridx = 1;
		gbcHabitacion.gridy = 2;
		JTextField txtApellidoPacienteHabitacion = new JTextField(20);
		txtApellidoPacienteHabitacion.setFont(textFieldFont);
		txtApellidoPacienteHabitacion.setEditable(false);
		panelAsignarHabitacion.add(txtApellidoPacienteHabitacion, gbcHabitacion);

		// Habitación input
		gbcHabitacion.gridx = 0;
		gbcHabitacion.gridy = 3;
		JLabel lblHabitacion = new JLabel("Asignar Habitación:");
		lblHabitacion.setFont(labelFont);
		lblHabitacion.setForeground(labelFgColor);
		panelAsignarHabitacion.add(lblHabitacion, gbcHabitacion);
		gbcHabitacion.gridx = 1;
		gbcHabitacion.gridy = 3;
		txtAsignarHabitacion = new JTextField(20);
		txtAsignarHabitacion.setFont(textFieldFont);
		panelAsignarHabitacion.add(txtAsignarHabitacion, gbcHabitacion);

		// Buttons for Asignar Habitación
		gbcHabitacion.gridx = 0;
		gbcHabitacion.gridy = 4;
		gbcHabitacion.gridwidth = 2;
		gbcHabitacion.anchor = GridBagConstraints.CENTER;
		JPanel panelBotonesHabitacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		panelBotonesHabitacion.setBackground(formPanelBgColor);
		JButton btnGuardarHabitacion = new JButton("Guardar Habitación");
		styleGestionButton(btnGuardarHabitacion);
		JButton btnCancelarHabitacion = new JButton("Cancelar");
		styleGestionButton(btnCancelarHabitacion);
		panelBotonesHabitacion.add(btnGuardarHabitacion);
		panelBotonesHabitacion.add(btnCancelarHabitacion);
		panelAsignarHabitacion.add(panelBotonesHabitacion, gbcHabitacion);

		// Add all views to the CardLayout
		cardsPanel.add(panelTablaPacientesLocal, "Tabla");
		cardsPanel.add(panelFormularioPacienteLocal, "Formulario");
		cardsPanel.add(panelAgregarDiagnostico, "AgregarDiagnostico");
		cardsPanel.add(panelAsignarHabitacion, "AsignarHabitacion");
		this.add(cardsPanel, BorderLayout.CENTER);

		// --- Acciones de los botones ---
		btnRegistrar.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Formulario");
			limpiarCamposPaciente();
			txtDNIpaciente.setEditable(true);
			filaSeleccionadaPaciente = -1;
		});

		btnEditar.addActionListener(e -> {
			filaSeleccionadaPaciente = tablaPacientes.getSelectedRow();
			if (filaSeleccionadaPaciente != -1) {
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Formulario");
				txtDNIpaciente.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 0).toString());
				txtNombrePaciente.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 1).toString());
				txtApellidoPaciente.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 2).toString());
				txtContactoPaciente.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 3).toString());
				txtObraSocialPaciente.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 4).toString());
				txtDNIpaciente.setEditable(false);
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un paciente para editar.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		btnEliminar.addActionListener(e -> {
			int fila = tablaPacientes.getSelectedRow();
			if (fila != -1) {
				int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este paciente?",
						"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					String dniPaciente = modeloPacientes.getValueAt(fila, 0).toString();
					if (dbConnection.eliminarPaciente(dniPaciente)) {
						modeloPacientes.removeRow(fila);
						JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente.", "Éxito",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Error al eliminar paciente en la base de datos.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un paciente para eliminar.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// Action listener for "Agregar Diagnóstico"
		btnAgregarDiagnostico.addActionListener(e -> {
			filaSeleccionadaPaciente = tablaPacientes.getSelectedRow();
			if (filaSeleccionadaPaciente != -1) {
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "AgregarDiagnostico");
				txtDniPacienteDiagnostico.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 0).toString());
				txtNombrePacienteDiagnostico
						.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 1).toString());
				txtApellidoPacienteDiagnostico
						.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 2).toString());
				txtDiagnostico.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 5).toString());
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un paciente para agregar diagnóstico.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// Action listener for "Asignar Habitación"
		btnAsignarHabitacion.addActionListener(e -> {
			filaSeleccionadaPaciente = tablaPacientes.getSelectedRow();
			if (filaSeleccionadaPaciente != -1) {
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "AsignarHabitacion");
				txtDniPacienteHabitacion.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 0).toString());
				txtNombrePacienteHabitacion.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 1).toString());
				txtApellidoPacienteHabitacion
						.setText(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 2).toString());
				// Get habitación value, handle empty string for null
				String habitacionActual = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 6).toString();
				txtAsignarHabitacion.setText(habitacionActual.equals("0") ? "" : habitacionActual);
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un paciente para asignar una habitación.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// METODO PARA GUARDAR UN PACIENTE, DIRECTAMENTE SE LE CREA CON BAJA YA QUE
		// VIENE A HOSPITAL, Y SIN CAMA HASTA QUE SE LE ASIGNE
		btnGuardarPaciente.addActionListener(e -> {
			String dni = txtDNIpaciente.getText().trim();
			String nombre = txtNombrePaciente.getText().trim();
			String apellido = txtApellidoPaciente.getText().trim();
			String contacto = txtContactoPaciente.getText().trim();
			String obraSocial = txtObraSocialPaciente.getText().trim();
			String diagnostico = "";
			int salaID = 0; // Default salaID for new patients or if not assigned

			if (filaSeleccionadaPaciente != -1) { // If editing an existing patient
				diagnostico = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 5).toString();
				try {
					salaID = Integer.parseInt(modeloPacientes.getValueAt(filaSeleccionadaPaciente, 6).toString());
				} catch (NumberFormatException ex) {
					salaID = 0; // Default if parsing fails (e.g., empty string or non-numeric)
				}
			}

			if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || contacto.isEmpty() || obraSocial.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Todos los campos principales son obligatorios.",
						"Error de Validación", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (filaSeleccionadaPaciente == -1) {
				Paciente nuevoPaciente = new Paciente(nombre, apellido, dni, "paciente", contacto, obraSocial, false,
						0);
				if (dbConnection.agregarPaciente(nuevoPaciente)) {
					modeloPacientes.addRow(new Object[] { dni, nombre, apellido, contacto, obraSocial, "", "0" });
					JOptionPane.showMessageDialog(this, "Paciente registrado exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Error al registrar paciente en la base de datos.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				String existingDni = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 0).toString();
				String existingNombre = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 1).toString();
				String existingApellido = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 2).toString();
				String existingContacto = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 3).toString();
				String existingObraSocial = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 4).toString();
				String existingDiagnostico = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 5).toString();
				String existingHabitacionStr = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 6).toString();
				int existingHabitacion = 0;
				try {
					existingHabitacion = Integer.parseInt(existingHabitacionStr);
				} catch (NumberFormatException ex) {
					existingHabitacion = 0;
				}

				Paciente pacienteAEditar = new Paciente(nombre, apellido, dni, "paciente", contacto, obraSocial, false,
						existingHabitacion);

				if (dbConnection.modificarPaciente(pacienteAEditar)) {
					modeloPacientes.setValueAt(dni, filaSeleccionadaPaciente, 0);
					modeloPacientes.setValueAt(nombre, filaSeleccionadaPaciente, 1);
					modeloPacientes.setValueAt(apellido, filaSeleccionadaPaciente, 2);
					modeloPacientes.setValueAt(contacto, filaSeleccionadaPaciente, 3);
					modeloPacientes.setValueAt(obraSocial, filaSeleccionadaPaciente, 4);
					modeloPacientes.setValueAt(existingDiagnostico, filaSeleccionadaPaciente, 5);
					modeloPacientes.setValueAt(existingHabitacion, filaSeleccionadaPaciente, 6);
					JOptionPane.showMessageDialog(this, "Paciente actualizado exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Error al actualizar paciente en la base de datos.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			limpiarCamposPaciente();
		});

		// action for cancelar paciente
		btnCancelarPaciente.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			limpiarCamposPaciente();
		});

		// Action listener for guardar diagnostico
		btnGuardarDiagnostico.addActionListener(e -> {
			String diagnosticoContent = txtDiagnostico.getText().trim();
			if (filaSeleccionadaPaciente != -1) {
				String dniPaciente = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 0).toString();
				modeloPacientes.setValueAt(diagnosticoContent, filaSeleccionadaPaciente, 5);
				JOptionPane.showMessageDialog(this, "Diagnóstico actualizado en la tabla.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			}
		});

		// Action listener for canceling Diagnóstico
		btnCancelarDiagnostico.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
		});

		// ACTION LISTENER FOR GUARDAR HABITACION
		btnGuardarHabitacion.addActionListener(e -> {
			String habitacionNumStr = txtAsignarHabitacion.getText().trim();
			Integer habitacionNum = null;

			if (habitacionNumStr.isEmpty()||habitacionNumStr.equalsIgnoreCase("Libre")) {
				habitacionNum = 0;
			} else {
				try {
					habitacionNum = Integer.parseInt(habitacionNumStr);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(this, "El número de habitación debe ser un valor numérico.",
							"Error de Validación", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			if (filaSeleccionadaPaciente != -1) {
				String dniPaciente = modeloPacientes.getValueAt(filaSeleccionadaPaciente, 0).toString();
				if (dbConnection.asignarSalaPaciente(dniPaciente, habitacionNum)) {
					modeloPacientes.setValueAt(habitacionNum.toString(), filaSeleccionadaPaciente, 6);
					JOptionPane.showMessageDialog(this, "Habitación asignada exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Error al asignar habitación en la base de datos.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			}
		});

		// METODO PARA CANCELAR EN ASIGNAR HABITACION
		btnCancelarHabitacion.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
		});
	}

	// METODO PARA LIMPIAR
	private void limpiarCamposPaciente() {
		txtDNIpaciente.setText("");
		txtNombrePaciente.setText("");
		txtApellidoPaciente.setText("");
		txtContactoPaciente.setText("");
		txtObraSocialPaciente.setText("");
		txtDiagnostico.setText("");
		txtAsignarHabitacion.setText("");
	}

	private void styleGestionButton(JButton button) {
		button.setBackground(gestionButtonBgColor);
		button.setForeground(gestionButtonFgColor);
		button.setFont(gestionButtonFont);
		button.setFocusPainted(false);
		button.setBorder(gestionButtonBorder);
		button.setPreferredSize(new Dimension(115, 35));
	}

	// METODO QUE CARGA LOS PACEINTES PERO SOLO LOS QUE NO TIENEN ALTA
	private void cargarPacientesDeDB() {
		modeloPacientes.setRowCount(0);
		List<Paciente> pacientes = dbConnection.obtenerTodosLosPacientes();
		for (Paciente p : pacientes) {
			modeloPacientes.addRow(new Object[] { p.getDni(), p.getNombre(), p.getApellido(), p.getContacto(),
					p.getObraSocial(), p.getDni(), p.getSalaID() == 0 ? "Libre" : String.valueOf(p.getSalaID()) });
		}
	}
}