package gestionHospital;

import clases.DBConnection;
import clases.Empleado;

import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class PanelGestionEmpleados extends JPanel {

	private JPanel cardsPanel; // Panel con CardLayout para vistas de tabla/formulario
	private DefaultTableModel modeloEmpleados;
	private JTable tablaEmpleados;
	// Campos del formulario general de Empleados
	private JTextField txtNombreEmpleado, txtDNIEmpleado, txtApellidoEmpleado, txtPuestoEmpleado;

	// Campos para el formulario de Asignar Sala
	private JTextField txtAsignarSala;

	// Campos para el formulario de Asignar Turno
	private JTextField txtDniEmpleadoTurno; // DNI Empleado ahora es editable y se precarga
	private JTextField txtDniPacienteTurno;
	private JTextField txtDiaTurno;
	private JTextField txtHoraComienzoTurno;
	private JTextField txtHoraFinalTurno;

	private int filaSeleccionadaEmpleado = -1; // Para saber qué fila se está editando

	// Instancia de DBConnection para interactuar con la base de datos
	private DBConnection dbConnection;

	// Colores y estilos extraídos directamente del PanelAdmin original para
	// consistencia
	private Color mainPanelBgColor = Color.decode("#E3242B"); // Fondo del panel principal de gestión
	private Color cardsPanelBgColor = Color.decode("#B0E0E6"); // Fondo del panel con CardLayout (tabla/formulario)
	private Color formPanelBgColor = Color.decode("#24e3dc"); // Fondo del panel del formulario
	private Color tableButtonsPanelBgColor = Color.decode("#212f3d"); // Fondo del panel de botones de la tabla

	private Color gestionButtonBgColor = Color.decode("#CD7F32"); // Color de fondo de los botones de gestión
	private Color gestionButtonFgColor = Color.white; // Color de texto de los botones de gestión

	private Font gestionButtonFont = new Font("Arial", Font.BOLD, 11); // Further reduced font size to 11

	private Border gestionButtonBorder = BorderFactory.createLineBorder(Color.decode("#CD7F32"), 1); // Borde de los
																										// botones de
																										// gestión

	private Color titleFgColor = Color.white; // Color del texto del título principal del panel
	private Color labelFgColor = Color.DARK_GRAY; // Color del texto de las etiquetas del formulario

	private Color tableHeaderBg = Color.decode("#f2f2f2"); // Fondo del encabezado de la tabla
	private Color tableHeaderFg = Color.decode("#333"); // Color de texto del encabezado de la tabla

	// TextField para Sala en el formulario general de Empleado (AHORA editable)
	private JTextField txtSalaGeneral;

	public PanelGestionEmpleados() {
		// Inicializar la conexión a la base de datos
		dbConnection = new DBConnection();

		this.setLayout(new BorderLayout());
		this.setBackground(mainPanelBgColor);

		JLabel titulo = new JLabel("Gestión de Empleados", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		titulo.setForeground(titleFgColor);
		titulo.setBorder(new EmptyBorder(20, 0, 20, 0));
		this.add(titulo, BorderLayout.NORTH);

		cardsPanel = new JPanel(new CardLayout());
		cardsPanel.setBackground(cardsPanelBgColor);

		// --- Vista de Tabla de Empleados ---
		JPanel panelTablaEmpleadosLocal = new JPanel(new BorderLayout());
		panelTablaEmpleadosLocal.setBackground(cardsPanelBgColor);

		// Columnas de la tabla: DNI, Nombre, Apellido, Puesto, Sala
		String[] columnasEmpleados = { "DNI", "Nombre", "Apellido", "Puesto", "Sala" };
		modeloEmpleados = new DefaultTableModel(columnasEmpleados, 0) { // 0 filas iniciales, se llenará desde DB
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaEmpleados = new JTable(modeloEmpleados);
		tablaEmpleados.setFont(new Font("Arial", Font.PLAIN, 14));
		tablaEmpleados.setRowHeight(25);
		tablaEmpleados.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		tablaEmpleados.setBackground(Color.WHITE);
		tablaEmpleados.setForeground(Color.BLACK);
		tablaEmpleados.getTableHeader().setBackground(tableHeaderBg);
		tablaEmpleados.getTableHeader().setForeground(tableHeaderFg);
		JScrollPane scrollTablaEmpleados = new JScrollPane(tablaEmpleados);
		scrollTablaEmpleados.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		panelTablaEmpleadosLocal.add(scrollTablaEmpleados, BorderLayout.CENTER);

		JPanel panelBotonesTablaEmpleados = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
		panelBotonesTablaEmpleados.setBackground(tableButtonsPanelBgColor);

		JButton btnAgregar = new JButton("Añadir Empleado");
		styleGestionButton(btnAgregar);

		JButton btnModificar = new JButton("Modificar Empleado");
		styleGestionButton(btnModificar);

		JButton btnBorrar = new JButton("Borrar Empleado");
		styleGestionButton(btnBorrar);

		JButton btnAsignarTurnos = new JButton("Asignar Turnos");
		styleGestionButton(btnAsignarTurnos);

		JButton btnAsignarSalas = new JButton("Asignar Salas");
		styleGestionButton(btnAsignarSalas);

		panelBotonesTablaEmpleados.add(btnAgregar);
		panelBotonesTablaEmpleados.add(btnModificar);
		panelBotonesTablaEmpleados.add(btnBorrar);
		panelBotonesTablaEmpleados.add(btnAsignarTurnos);
		panelBotonesTablaEmpleados.add(btnAsignarSalas);
		panelTablaEmpleadosLocal.add(panelBotonesTablaEmpleados, BorderLayout.SOUTH);

		// --- Vista de Formulario de Empleado (Add/Modify) ---
		JPanel panelFormularioEmpleadoLocal = new JPanel(new GridBagLayout());
		panelFormularioEmpleadoLocal.setBackground(formPanelBgColor);
		panelFormularioEmpleadoLocal.setBorder(new EmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 5, 10, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		Font labelFont = new Font("Arial", Font.BOLD, 16);
		Font textFieldFont = new Font("Arial", Font.PLAIN, 16);

		// DNI
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel lblDNI = new JLabel("DNI:");
		lblDNI.setFont(labelFont);
		lblDNI.setForeground(labelFgColor);
		panelFormularioEmpleadoLocal.add(lblDNI, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		txtDNIEmpleado = new JTextField(20);
		txtDNIEmpleado.setFont(textFieldFont);
		panelFormularioEmpleadoLocal.add(txtDNIEmpleado, gbc);

		// Nombre
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(labelFont);
		lblNombre.setForeground(labelFgColor);
		panelFormularioEmpleadoLocal.add(lblNombre, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		txtNombreEmpleado = new JTextField(20);
		txtNombreEmpleado.setFont(textFieldFont);
		panelFormularioEmpleadoLocal.add(txtNombreEmpleado, gbc);

		// Apellido
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setFont(labelFont);
		lblApellido.setForeground(labelFgColor);
		panelFormularioEmpleadoLocal.add(lblApellido, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		txtApellidoEmpleado = new JTextField(20);
		txtApellidoEmpleado.setFont(textFieldFont);
		panelFormularioEmpleadoLocal.add(txtApellidoEmpleado, gbc);

		// Puesto
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel lblPuesto = new JLabel("Puesto:");
		lblPuesto.setFont(labelFont);
		lblPuesto.setForeground(labelFgColor);
		panelFormularioEmpleadoLocal.add(lblPuesto, gbc);
		gbc.gridx = 1;
		gbc.gridy = 3;
		txtPuestoEmpleado = new JTextField(20);
		txtPuestoEmpleado.setFont(textFieldFont);
		panelFormularioEmpleadoLocal.add(txtPuestoEmpleado, gbc);

		// Sala (editable)
		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel lblSalaGeneral = new JLabel("Sala:"); // Etiqueta sin "(solo lectura)"
		lblSalaGeneral.setFont(labelFont);
		lblSalaGeneral.setForeground(labelFgColor);
		panelFormularioEmpleadoLocal.add(lblSalaGeneral, gbc);
		gbc.gridx = 1;
		gbc.gridy = 4;
		txtSalaGeneral = new JTextField(20);
		txtSalaGeneral.setFont(textFieldFont);
		txtSalaGeneral.setEditable(true); // Habilitado para edición
		panelFormularioEmpleadoLocal.add(txtSalaGeneral, gbc);

		// Para el campo de contraseña en el formulario de añadir empleado
		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setFont(labelFont);
		lblContrasena.setForeground(labelFgColor);
		JPasswordField txtContrasena = new JPasswordField(20);
		txtContrasena.setFont(textFieldFont);

		// Botones del formulario de empleado
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JPanel panelBotonesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		panelBotonesFormulario.setBackground(formPanelBgColor);
		JButton btnGuardarEmpleado = new JButton("Guardar");
		styleGestionButton(btnGuardarEmpleado);
		JButton btnCancelarEmpleado = new JButton("Cancelar");
		styleGestionButton(btnCancelarEmpleado);
		panelBotonesFormulario.add(btnGuardarEmpleado);
		panelBotonesFormulario.add(btnCancelarEmpleado);
		panelFormularioEmpleadoLocal.add(panelBotonesFormulario, gbc);

		// --- Vista de Formulario para Asignar Sala ---
		JPanel panelAsignarSala = new JPanel(new GridBagLayout());
		panelAsignarSala.setBackground(formPanelBgColor);
		panelAsignarSala.setBorder(new EmptyBorder(50, 50, 50, 50));
		GridBagConstraints gbcSala = new GridBagConstraints();
		gbcSala.insets = new Insets(10, 5, 10, 5);
		gbcSala.fill = GridBagConstraints.HORIZONTAL;

		// Employee DNI (read-only en este formulario)
		gbcSala.gridx = 0;
		gbcSala.gridy = 0;
		JLabel lblDniSala = new JLabel("DNI Empleado:");
		lblDniSala.setFont(labelFont);
		lblDniSala.setForeground(labelFgColor);
		panelAsignarSala.add(lblDniSala, gbcSala);
		gbcSala.gridx = 1;
		gbcSala.gridy = 0;
		JTextField txtDniEmpleadoSala = new JTextField(20);
		txtDniEmpleadoSala.setFont(textFieldFont);
		txtDniEmpleadoSala.setEditable(false); // Sigue siendo read-only en este formulario
		panelAsignarSala.add(txtDniEmpleadoSala, gbcSala);

		// Employee Name (read-only en este formulario)
		gbcSala.gridx = 0;
		gbcSala.gridy = 1;
		JLabel lblNombreSala = new JLabel("Nombre Empleado:");
		lblNombreSala.setFont(labelFont);
		lblNombreSala.setForeground(labelFgColor);
		panelAsignarSala.add(lblNombreSala, gbcSala);
		gbcSala.gridx = 1;
		gbcSala.gridy = 1;
		JTextField txtNombreEmpleadoSala = new JTextField(20);
		txtNombreEmpleadoSala.setFont(textFieldFont);
		txtNombreEmpleadoSala.setEditable(false); // Sigue siendo read-only en este formulario
		panelAsignarSala.add(txtNombreEmpleadoSala, gbcSala);

		// Sala input
		gbcSala.gridx = 0;
		gbcSala.gridy = 2;
		JLabel lblSala = new JLabel("Asignar Sala (ID):"); // Más claro que es el ID de la sala
		lblSala.setFont(labelFont);
		lblSala.setForeground(labelFgColor);
		panelAsignarSala.add(lblSala, gbcSala);
		gbcSala.gridx = 1;
		gbcSala.gridy = 2;
		txtAsignarSala = new JTextField(20);
		txtAsignarSala.setFont(textFieldFont);
		panelAsignarSala.add(txtAsignarSala, gbcSala);

		// Buttons for Asignar Sala
		gbcSala.gridx = 0;
		gbcSala.gridy = 3;
		gbcSala.gridwidth = 2;
		gbcSala.anchor = GridBagConstraints.CENTER;
		JPanel panelBotonesAsignarSala = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		panelBotonesAsignarSala.setBackground(formPanelBgColor);
		JButton btnGuardarSala = new JButton("Guardar Sala");
		styleGestionButton(btnGuardarSala);
		JButton btnCancelarSala = new JButton("Cancelar");
		styleGestionButton(btnCancelarSala);
		panelBotonesAsignarSala.add(btnGuardarSala);
		panelBotonesAsignarSala.add(btnCancelarSala);
		panelAsignarSala.add(panelBotonesAsignarSala, gbcSala);

		// --- Vista de Formulario para Asignar Turno ---
		JPanel panelAsignarTurno = new JPanel(new GridBagLayout());
		panelAsignarTurno.setBackground(formPanelBgColor);
		panelAsignarTurno.setBorder(new EmptyBorder(50, 50, 50, 50));
		GridBagConstraints gbcTurno = new GridBagConstraints();
		gbcTurno.insets = new Insets(10, 5, 10, 5);
		gbcTurno.fill = GridBagConstraints.HORIZONTAL;

		// DNI Empleado (editable y se precarga si hay una fila seleccionada)
		gbcTurno.gridx = 0;
		gbcTurno.gridy = 0;
		JLabel lblDniTurno = new JLabel("DNI Empleado:");
		lblDniTurno.setFont(labelFont);
		lblDniTurno.setForeground(labelFgColor);
		panelAsignarTurno.add(lblDniTurno, gbcTurno);
		gbcTurno.gridx = 1;
		gbcTurno.gridy = 0;
		txtDniEmpleadoTurno = new JTextField(20);
		txtDniEmpleadoTurno.setFont(textFieldFont);
		panelAsignarTurno.add(txtDniEmpleadoTurno, gbcTurno);

		// DNI Paciente
		gbcTurno.gridx = 0;
		gbcTurno.gridy = 1;
		JLabel lblDniPacienteTurno = new JLabel("DNI Paciente:");
		lblDniPacienteTurno.setFont(labelFont);
		lblDniPacienteTurno.setForeground(labelFgColor);
		panelAsignarTurno.add(lblDniPacienteTurno, gbcTurno);
		gbcTurno.gridx = 1;
		gbcTurno.gridy = 1;
		txtDniPacienteTurno = new JTextField(20);
		txtDniPacienteTurno.setFont(textFieldFont);
		panelAsignarTurno.add(txtDniPacienteTurno, gbcTurno);

		// Día
		gbcTurno.gridx = 0;
		gbcTurno.gridy = 2;
		JLabel lblDiaTurno = new JLabel("Día (YYYY-MM-DD):"); // Añadido formato para claridad
		lblDiaTurno.setFont(labelFont);
		lblDiaTurno.setForeground(labelFgColor);
		panelAsignarTurno.add(lblDiaTurno, gbcTurno);
		gbcTurno.gridx = 1;
		gbcTurno.gridy = 2;
		txtDiaTurno = new JTextField(20);
		txtDiaTurno.setFont(textFieldFont);
		panelAsignarTurno.add(txtDiaTurno, gbcTurno);

		// Hora Comienzo
		gbcTurno.gridx = 0;
		gbcTurno.gridy = 3;
		JLabel lblHoraComienzoTurno = new JLabel("Hora Comienzo (HH:MM:SS):"); // Añadido formato
		lblHoraComienzoTurno.setFont(labelFont);
		lblHoraComienzoTurno.setForeground(labelFgColor);
		panelAsignarTurno.add(lblHoraComienzoTurno, gbcTurno);
		gbcTurno.gridx = 1;
		gbcTurno.gridy = 3;
		txtHoraComienzoTurno = new JTextField(20);
		txtHoraComienzoTurno.setFont(textFieldFont);
		panelAsignarTurno.add(txtHoraComienzoTurno, gbcTurno);

		// Hora Final
		gbcTurno.gridx = 0;
		gbcTurno.gridy = 4;
		JLabel lblHoraFinalTurno = new JLabel("Hora Final (HH:MM:SS):"); // Añadido formato
		lblHoraFinalTurno.setFont(labelFont);
		lblHoraFinalTurno.setForeground(labelFgColor);
		panelAsignarTurno.add(lblHoraFinalTurno, gbcTurno);
		gbcTurno.gridx = 1;
		gbcTurno.gridy = 4;
		txtHoraFinalTurno = new JTextField(20);
		txtHoraFinalTurno.setFont(textFieldFont);
		panelAsignarTurno.add(txtHoraFinalTurno, gbcTurno);

		// Buttons for Asignar Turno
		gbcTurno.gridx = 0;
		gbcTurno.gridy = 5;
		gbcTurno.gridwidth = 2;
		gbcTurno.anchor = GridBagConstraints.CENTER;
		JPanel panelBotonesAsignarTurno = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		panelBotonesAsignarTurno.setBackground(formPanelBgColor);
		JButton btnGuardarTurno = new JButton("Guardar Turno");
		styleGestionButton(btnGuardarTurno);
		JButton btnCancelarTurno = new JButton("Cancelar");
		styleGestionButton(btnCancelarTurno);
		panelBotonesAsignarTurno.add(btnGuardarTurno);
		panelBotonesAsignarTurno.add(btnCancelarTurno);
		panelAsignarTurno.add(panelBotonesAsignarTurno, gbcTurno);

		// Añadir las nuevas vistas al CardLayout
		cardsPanel.add(panelTablaEmpleadosLocal, "Tabla");
		cardsPanel.add(panelFormularioEmpleadoLocal, "Formulario");
		cardsPanel.add(panelAsignarSala, "AsignarSala");
		cardsPanel.add(panelAsignarTurno, "AsignarTurno");
		this.add(cardsPanel, BorderLayout.CENTER);

		// Cargar empleados al inicio
		cargarEmpleadosEnTabla();

		// --- Acciones de los botones ---
		btnAgregar.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Formulario");
			limpiarCamposEmpleado();
			txtDNIEmpleado.setEditable(true);
			
			gbc.gridx = 0; // Reset x to 0 for buttons
			gbc.gridy = 5; // Vuelve a la posición original sin contraseña
			panelFormularioEmpleadoLocal.add(panelBotonesFormulario, gbc);
			
			panelFormularioEmpleadoLocal.revalidate();
			panelFormularioEmpleadoLocal.repaint();

			filaSeleccionadaEmpleado = -1;
		});

		btnModificar.addActionListener(e -> {
			filaSeleccionadaEmpleado = tablaEmpleados.getSelectedRow();
			if (filaSeleccionadaEmpleado != -1) {
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Formulario");
				txtDNIEmpleado.setText(modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 0).toString());
				txtNombreEmpleado.setText(modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 1).toString());
				txtApellidoEmpleado.setText(modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 2).toString());
				txtPuestoEmpleado.setText(modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 3).toString());
				// Asegurarse de que el valor de sala no sea nulo antes de convertir a String
				Object salaValue = modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 4);
				txtSalaGeneral.setText(salaValue != null ? salaValue.toString() : "");

				txtDNIEmpleado.setEditable(false);
				// Eliminar el campo de contraseña si está visible (solo para añadir)
				panelFormularioEmpleadoLocal.remove(lblContrasena);
				panelFormularioEmpleadoLocal.remove(txtContrasena);
				// Re-posicionar botones
				gbc.gridy = 5; // Vuelve a la posición original sin contraseña
				panelFormularioEmpleadoLocal.add(panelBotonesFormulario, gbc);
				panelFormularioEmpleadoLocal.revalidate();
				panelFormularioEmpleadoLocal.repaint();
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un empleado para modificar.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		btnBorrar.addActionListener(e -> {
			int fila = tablaEmpleados.getSelectedRow();
			if (fila != -1) {
				String dniABorrar = modeloEmpleados.getValueAt(fila, 0).toString();
				int confirm = JOptionPane.showConfirmDialog(this,
						"¿Está seguro de que desea borrar al empleado con DNI: " + dniABorrar + "?",
						"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					if (dbConnection.eliminarEmpleado(dniABorrar)) {
						JOptionPane.showMessageDialog(this, "Empleado eliminado exitosamente.", "Éxito",
								JOptionPane.INFORMATION_MESSAGE);
						cargarEmpleadosEnTabla(); // Recargar la tabla
					} else {
						JOptionPane.showMessageDialog(this, "Error al eliminar el empleado.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un empleado para borrar.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// Action listener for "Asignar Turnos"
		btnAsignarTurnos.addActionListener(e -> {
		    filaSeleccionadaEmpleado = tablaEmpleados.getSelectedRow();
		    if (filaSeleccionadaEmpleado != -1) { 
		        ((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "AsignarTurno");
		        txtDniPacienteTurno.setText("");
		        txtDiaTurno.setText("");
		        txtHoraComienzoTurno.setText("");
		        txtHoraFinalTurno.setText("");

		        txtDniEmpleadoTurno.setText(modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 0).toString());
		    } else {
		        JOptionPane.showMessageDialog(this, "Seleccione un empleado para asignar un turno.", "Error",
		                JOptionPane.WARNING_MESSAGE);
		    }
		});

		// Action listener for "Asignar Salas"
		btnAsignarSalas.addActionListener(e -> {
			filaSeleccionadaEmpleado = tablaEmpleados.getSelectedRow();
			if (filaSeleccionadaEmpleado != -1) {
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "AsignarSala");
				txtDniEmpleadoSala.setText(modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 0).toString());
				txtNombreEmpleadoSala.setText(modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 1).toString());
				Object salaValue = modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 4);
				txtAsignarSala.setText(salaValue != null ? salaValue.toString() : "");
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un empleado para asignar una sala.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// action listener for "Anadir Empleado y tambien modificarlo"
		btnGuardarEmpleado.addActionListener(e -> {
			String dni = txtDNIEmpleado.getText().trim();
			String nombre = txtNombreEmpleado.getText().trim();
			String apellido = txtApellidoEmpleado.getText().trim();
			String puesto = txtPuestoEmpleado.getText().trim();
			String salaStr = txtSalaGeneral.getText().trim();

			// Validar campos obligatorios
			if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || puesto.isEmpty() || salaStr.isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"Todos los campos (DNI, Nombre, Apellido, Puesto, Sala) son obligatorios.",
						"Error de Validación", JOptionPane.WARNING_MESSAGE);
				return;
			}
			//Parceo de String al int la fase
			Integer salaId = null;
			try {
				if (!salaStr.isEmpty()) {
					salaId = Integer.parseInt(salaStr);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El campo 'Sala' debe ser un número entero válido.",
						"Error de Formato", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (filaSeleccionadaEmpleado == -1) { // Agregar nuevo empleado
				Empleado nuevoEmpleado = new Empleado(salaId != null ? salaId : 0, nombre, apellido, dni, puesto, null); 
																														
				if (dbConnection.agregarEmpleado(nuevoEmpleado)) {
					JOptionPane.showMessageDialog(this, "Empleado añadido exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
					cargarEmpleadosEnTabla(); // Recargar la tabla
					((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
					limpiarCamposEmpleado();
				} else {
					JOptionPane.showMessageDialog(this,
							"Error al añadir el empleado. Asegúrese de que el DNI no esté duplicado.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else { // Editar empleado existente
				String dniEmpleadoOriginal = modeloEmpleados.getValueAt(filaSeleccionadaEmpleado, 0).toString();
				Empleado empleadoModificado = new Empleado(salaId != null ? salaId : 0, nombre, apellido, dni, puesto,
						null);
				empleadoModificado.setDni(dniEmpleadoOriginal);

				if (dbConnection.modificarEmpleado(empleadoModificado)) {
					JOptionPane.showMessageDialog(this, "Empleado modificado exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
					dbConnection.asignarSala(dniEmpleadoOriginal, salaId);
					cargarEmpleadosEnTabla();
					((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
					limpiarCamposEmpleado();
				} else {
					JOptionPane.showMessageDialog(this, "Error al modificar el empleado.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Action Listener for "boton de Cancelar de Empleado"
		btnCancelarEmpleado.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			limpiarCamposEmpleado();
			panelFormularioEmpleadoLocal.remove(lblContrasena);
			panelFormularioEmpleadoLocal.remove(txtContrasena);
			gbc.gridy = 5;
			panelFormularioEmpleadoLocal.add(panelBotonesFormulario, gbc);
			panelFormularioEmpleadoLocal.revalidate();
			panelFormularioEmpleadoLocal.repaint();
		});

		// Action Listener for "Guardar la Sala"
		btnGuardarSala.addActionListener(e -> {
			String dniEmpleado = txtDniEmpleadoSala.getText().trim();
			String salaIdStr = txtAsignarSala.getText().trim();
			Integer salaId = null;

			if (dniEmpleado.isEmpty()) {
				JOptionPane.showMessageDialog(this, "El DNI del empleado no puede estar vacío.", "Error de Validación",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (!salaIdStr.isEmpty()) {
				try {
					salaId = Integer.parseInt(salaIdStr);
					if (salaId == 0) {
						salaId = null;
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(this,
							"El ID de Sala debe ser un número entero válido o dejarse vacío para desasignar.",
							"Error de Formato", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			if (dbConnection.asignarSala(dniEmpleado, salaId)) {
				JOptionPane.showMessageDialog(this, "Sala asignada/desasignada exitosamente.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				cargarEmpleadosEnTabla();
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			} else {
				JOptionPane.showMessageDialog(this, "Error al asignar/desasignar la sala.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		// Action listener for "Cancelar la Sala"
		btnCancelarSala.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
		});

		// Action listener for "Guardar el turnooo!"
		btnGuardarTurno.addActionListener(e -> {
			String dniEmpleado = txtDniEmpleadoTurno.getText().trim();
			String dniPaciente = txtDniPacienteTurno.getText().trim();
			String diaStr = txtDiaTurno.getText().trim();
			String horaComienzoStr = txtHoraComienzoTurno.getText().trim();
			String horaFinalStr = txtHoraFinalTurno.getText().trim();

			if (dniEmpleado.isEmpty() || dniPaciente.isEmpty() || diaStr.isEmpty() || horaComienzoStr.isEmpty()
					|| horaFinalStr.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Todos los campos para asignar un turno son obligatorios.",
						"Error de Validación", JOptionPane.WARNING_MESSAGE);
				return;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date dia = null;
			Time horaComienzo = null;
			Time horaFinal = null;

			try {
				java.util.Date parsedDate = dateFormat.parse(diaStr);
				dia = new Date(parsedDate.getTime());

				java.util.Date parsedTimeComienzo = timeFormat.parse(horaComienzoStr);
				horaComienzo = new Time(parsedTimeComienzo.getTime());

				java.util.Date parsedTimeFinal = timeFormat.parse(horaFinalStr);
				horaFinal = new Time(parsedTimeFinal.getTime());

			} catch (ParseException ex) {
				JOptionPane.showMessageDialog(this,
						"Formato de fecha u hora incorrecto. Use YYYY-MM-DD para el día y HH:MM:SS para las horas.",
						"Error de Formato", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (dbConnection.asignarTurno(dniEmpleado, dniPaciente, dia, horaComienzo, horaFinal)) {
				JOptionPane.showMessageDialog(this, "Turno asignado exitosamente.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			} else {
				JOptionPane.showMessageDialog(this, "Error al asignar el turno.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Listening Event for "Cancelar en el Turno"
		btnCancelarTurno.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
		});
	}

	//Limpia los campos de texto del formulario de empleado.
	private void limpiarCamposEmpleado() {
		txtDNIEmpleado.setText("");
		txtNombreEmpleado.setText("");
		txtApellidoEmpleado.setText("");
		txtPuestoEmpleado.setText("");
		txtSalaGeneral.setText("");
		
	}

	// Sobrecargar limpiarCamposEmpleado para que funcione con la nueva estructura
	private void limpiarCamposEmpleadoConContrasena() {
		txtDNIEmpleado.setText("");
		txtNombreEmpleado.setText("");
		txtApellidoEmpleado.setText("");
		txtPuestoEmpleado.setText("");
		txtSalaGeneral.setText("");
	}

	// Carga los datos de los empleados desde la base de datos y actualiza la tabla.
	private void cargarEmpleadosEnTabla() {
		modeloEmpleados.setRowCount(0); // Limpiar la tabla existente
		List<Empleado> empleados = dbConnection.obtenerTodosLosEmpleados();
		for (Empleado empleado : empleados) {
			String salaMostrar = (empleado.getSalaId() == 0) ? "" : String.valueOf(empleado.getSalaId());
			modeloEmpleados.addRow(new Object[] { empleado.getDni(), empleado.getNombre(), empleado.getApellido(),
					empleado.getRol(), salaMostrar });
		}
	}

	private void styleGestionButton(JButton button) {
		button.setBackground(gestionButtonBgColor);
		button.setForeground(gestionButtonFgColor);
		button.setFont(gestionButtonFont);
		button.setFocusPainted(false);
		button.setBorder(gestionButtonBorder);
		button.setPreferredSize(new Dimension(115, 35));
	}
}