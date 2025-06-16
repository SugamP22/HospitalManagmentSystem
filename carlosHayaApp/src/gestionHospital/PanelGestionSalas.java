package gestionHospital;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

// Importar las clases de tu paquete 'clases'
import clases.DBConnection;
import clases.Sala; // Asegúrate de que la clase Sala esté en el paquete 'clases'

@SuppressWarnings("serial")
public class PanelGestionSalas extends JPanel {

	private JPanel cardsPanel;
	private DefaultTableModel modeloSalas;
	private JTable tablaSalas;
	private JTextField txtNumeroSala, txtTipoSala; // Campos para agregar/editar sala

	private JTextField txtDNIEmpleadoMantenimiento;
	private JFormattedTextField txtFechaMantenimiento;

	private JTextField txtNumeroSalaMantenimiento;

	private int filaSeleccionadaSala = -1;

	// Instancia de DBConnection
	private DBConnection dbConnection;

	// Colores y estilos
	private Color mainPanelBgColor = Color.decode("#E3242B");
	private Color cardsPanelBgColor = Color.decode("#B0E0E6");
	private Color formPanelBgColor = Color.decode("#24e3dc");
	private Color tableButtonsPanelBgColor = Color.decode("#212f3d");

	private Color gestionButtonBgColor = Color.decode("#CD7F32");
	private Color gestionButtonFgColor = Color.white;
	private Font gestionButtonFont = new Font("Arial", Font.BOLD, 11);
	private Border gestionButtonBorder = BorderFactory.createLineBorder(Color.decode("#CD7F32"), 1);

	private Color titleFgColor = Color.white;
	private Color labelFgColor = Color.DARK_GRAY;

	private Color tableHeaderBg = Color.decode("#f2f2f2");
	private Color tableHeaderFg = Color.decode("#333");

	public PanelGestionSalas() {
		this.setLayout(new BorderLayout());
		this.setBackground(mainPanelBgColor);

		// Inicializar la conexión a la base de datos
		dbConnection = new DBConnection();

		JLabel titulo = new JLabel("Gestión de Salas", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		titulo.setForeground(titleFgColor);
		titulo.setBorder(new EmptyBorder(20, 0, 20, 0));
		this.add(titulo, BorderLayout.NORTH);

		cardsPanel = new JPanel(new CardLayout());
		cardsPanel.setBackground(cardsPanelBgColor);

		// --- Vista de Tabla de Salas ---
		JPanel panelTablaSalasLocal = new JPanel(new BorderLayout());
		panelTablaSalasLocal.setBackground(cardsPanelBgColor);

		String[] columnasSalas = { "ID", "Tipo", "Disponibilidad" }; // Cambiado "Número" a "ID"
		modeloSalas = new DefaultTableModel(columnasSalas, 0) { // 0 filas iniciales
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaSalas = new JTable(modeloSalas);
		tablaSalas.setFont(new Font("Arial", Font.PLAIN, 14));
		tablaSalas.setRowHeight(25);
		tablaSalas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		tablaSalas.setBackground(Color.WHITE);
		tablaSalas.setForeground(Color.BLACK);
		tablaSalas.getTableHeader().setBackground(tableHeaderBg);
		tablaSalas.getTableHeader().setForeground(tableHeaderFg);
		JScrollPane scrollTablaSalas = new JScrollPane(tablaSalas);
		scrollTablaSalas.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		panelTablaSalasLocal.add(scrollTablaSalas, BorderLayout.CENTER);

		JPanel panelBotonesTablaSalas = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
		panelBotonesTablaSalas.setBackground(tableButtonsPanelBgColor);

		JButton btnAgregar = new JButton("Agregar Sala");
		styleGestionButton(btnAgregar);
		JButton btnEditar = new JButton("Editar Sala");
		styleGestionButton(btnEditar);
		JButton btnEliminar = new JButton("Eliminar Sala");
		styleGestionButton(btnEliminar);
		JButton btnMantenimiento = new JButton("Mantenimiento");
		styleGestionButton(btnMantenimiento);

		panelBotonesTablaSalas.add(btnAgregar);
		panelBotonesTablaSalas.add(btnEditar);
		panelBotonesTablaSalas.add(btnEliminar);
		panelBotonesTablaSalas.add(btnMantenimiento);
		panelTablaSalasLocal.add(panelBotonesTablaSalas, BorderLayout.SOUTH);

		// --- Vista de Formulario de Sala (Add/Modify) ---
		JPanel panelFormularioSalaLocal = new JPanel(new GridBagLayout());
		panelFormularioSalaLocal.setBackground(formPanelBgColor);
		panelFormularioSalaLocal.setBorder(new EmptyBorder(20, 50, 20, 50));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 5, 10, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		Font labelFont = new Font("Arial", Font.BOLD, 16);
		Font textFieldFont = new Font("Arial", Font.PLAIN, 16);

		// ID de Sala (solo para mostrar en edición, no para agregar)
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel lblNumeroSala = new JLabel("ID de Sala:");
		lblNumeroSala.setFont(labelFont);
		lblNumeroSala.setForeground(labelFgColor);
		panelFormularioSalaLocal.add(lblNumeroSala, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		txtNumeroSala = new JTextField(20);
		txtNumeroSala.setFont(textFieldFont);
		txtNumeroSala.setEditable(false); // Por defecto no editable, se habilita en "Agregar"
		panelFormularioSalaLocal.add(txtNumeroSala, gbc);

		// Tipo de Sala
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel lblTipoSala = new JLabel("Tipo:");
		lblTipoSala.setFont(labelFont);
		lblTipoSala.setForeground(labelFgColor);
		panelFormularioSalaLocal.add(lblTipoSala, gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		txtTipoSala = new JTextField(20);
		txtTipoSala.setFont(textFieldFont);
		panelFormularioSalaLocal.add(txtTipoSala, gbc);

		// Disponibilidad de Sala (para agregar/modificar)
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel lblDisponibilidadSala = new JLabel("Disponibilidad:");
		lblDisponibilidadSala.setFont(labelFont);
		lblDisponibilidadSala.setForeground(labelFgColor);
		panelFormularioSalaLocal.add(lblDisponibilidadSala, gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		JCheckBox chkDisponibilidad = new JCheckBox("Disponible");
		chkDisponibilidad.setFont(textFieldFont);
		chkDisponibilidad.setBackground(formPanelBgColor);
		chkDisponibilidad.setForeground(labelFgColor);
		panelFormularioSalaLocal.add(chkDisponibilidad, gbc);

		// Botones del formulario de sala (Guardar/Cancelar)
		gbc.gridx = 0;
		gbc.gridy = 3; // Ajustado el gridY
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JPanel panelBotonesFormularioSala = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		panelBotonesFormularioSala.setBackground(formPanelBgColor);
		JButton btnGuardarSala = new JButton("Guardar");
		styleGestionButton(btnGuardarSala);
		JButton btnCancelarSala = new JButton("Cancelar");
		styleGestionButton(btnCancelarSala);
		panelBotonesFormularioSala.add(btnGuardarSala);
		panelBotonesFormularioSala.add(btnCancelarSala);
		panelFormularioSalaLocal.add(panelBotonesFormularioSala, gbc);

		// --- Vista de Formulario para Mantenimiento de Sala ---
		JPanel panelMantenimientoSala = new JPanel(new GridBagLayout());
		panelMantenimientoSala.setBackground(formPanelBgColor);
		panelMantenimientoSala.setBorder(new EmptyBorder(50, 50, 50, 50));
		GridBagConstraints gbcMantenimiento = new GridBagConstraints();
		gbcMantenimiento.insets = new Insets(10, 5, 10, 5);
		gbcMantenimiento.fill = GridBagConstraints.HORIZONTAL;

		int currentGridY = 0;

		// ID de Sala (solo lectura, pre-seleccionable de la tabla)
		gbcMantenimiento.gridx = 0;
		gbcMantenimiento.gridy = currentGridY++;
		JLabel lblNumeroSalaMantenimiento = new JLabel("ID de Sala:");
		lblNumeroSalaMantenimiento.setFont(labelFont);
		lblNumeroSalaMantenimiento.setForeground(labelFgColor);
		panelMantenimientoSala.add(lblNumeroSalaMantenimiento, gbcMantenimiento);
		gbcMantenimiento.gridx = 1;
		gbcMantenimiento.gridy = currentGridY - 1;
		txtNumeroSalaMantenimiento = new JTextField(20);
		txtNumeroSalaMantenimiento.setFont(textFieldFont);
		txtNumeroSalaMantenimiento.setEditable(false);
		panelMantenimientoSala.add(txtNumeroSalaMantenimiento, gbcMantenimiento);

		// DNI de Empleado
		gbcMantenimiento.gridx = 0;
		gbcMantenimiento.gridy = currentGridY++;
		JLabel lblDNIEmpleadoMantenimiento = new JLabel("DNI de Empleado:");
		lblDNIEmpleadoMantenimiento.setFont(labelFont);
		lblDNIEmpleadoMantenimiento.setForeground(labelFgColor);
		panelMantenimientoSala.add(lblDNIEmpleadoMantenimiento, gbcMantenimiento);
		gbcMantenimiento.gridx = 1;
		gbcMantenimiento.gridy = currentGridY - 1;
		txtDNIEmpleadoMantenimiento = new JTextField(20);
		txtDNIEmpleadoMantenimiento.setFont(textFieldFont);
		panelMantenimientoSala.add(txtDNIEmpleadoMantenimiento, gbcMantenimiento);

		// Fecha de Mantenimiento (día, mes, año)
		gbcMantenimiento.gridx = 0;
		gbcMantenimiento.gridy = currentGridY++;
		JLabel lblFechaMantenimiento = new JLabel("Fecha (YYYY-MM-DD):");
		lblFechaMantenimiento.setFont(labelFont);
		lblFechaMantenimiento.setForeground(labelFgColor);
		panelMantenimientoSala.add(lblFechaMantenimiento, gbcMantenimiento);
		gbcMantenimiento.gridx = 1;
		gbcMantenimiento.gridy = currentGridY - 1;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormatter dateFormatter = new DateFormatter(dateFormat);
		txtFechaMantenimiento = new JFormattedTextField(new DefaultFormatterFactory(dateFormatter));
		txtFechaMantenimiento.setFont(textFieldFont);
		txtFechaMantenimiento.setColumns(10);
		panelMantenimientoSala.add(txtFechaMantenimiento, gbcMantenimiento);

		// Botones del formulario de Mantenimiento
		gbcMantenimiento.gridx = 0;
		gbcMantenimiento.gridy = currentGridY++;
		gbcMantenimiento.gridwidth = 2;
		gbcMantenimiento.anchor = GridBagConstraints.CENTER;
		JPanel panelBotonesMantenimiento = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		panelBotonesMantenimiento.setBackground(formPanelBgColor);
		JButton btnGuardarMantenimiento = new JButton("Guardar Mantenimiento");
		styleGestionButton(btnGuardarMantenimiento);
		JButton btnCancelarMantenimiento = new JButton("Cancelar");
		styleGestionButton(btnCancelarMantenimiento);
		panelBotonesMantenimiento.add(btnGuardarMantenimiento);
		panelBotonesMantenimiento.add(btnCancelarMantenimiento);
		panelMantenimientoSala.add(panelBotonesMantenimiento, gbcMantenimiento);

		// Añadir todas las vistas al CardLayout
		cardsPanel.add(panelTablaSalasLocal, "Tabla");
		cardsPanel.add(panelFormularioSalaLocal, "Formulario");
		cardsPanel.add(panelMantenimientoSala, "Mantenimiento");
		this.add(cardsPanel, BorderLayout.CENTER);

		// Cargar datos iniciales de la tabla
		cargarSalasEnTabla();

		
		btnAgregar.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Formulario");
			limpiarCamposSala();
			txtNumeroSala.setEditable(false); 
			chkDisponibilidad.setSelected(true); 
			filaSeleccionadaSala = -1; 
		});
		
		btnEditar.addActionListener(e -> {
            filaSeleccionadaSala = tablaSalas.getSelectedRow();
            if (filaSeleccionadaSala != -1) {
                ((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Formulario");
                txtNumeroSala.setText(modeloSalas.getValueAt(filaSeleccionadaSala, 0).toString());
                txtTipoSala.setText(modeloSalas.getValueAt(filaSeleccionadaSala, 1).toString());

                String disponibilidadStr = modeloSalas.getValueAt(filaSeleccionadaSala, 2).toString();
                chkDisponibilidad.setSelected(disponibilidadStr.equalsIgnoreCase("Libre"));

                txtNumeroSala.setEditable(false); 
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una sala para editar.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

		btnEliminar.addActionListener(e -> {
			int fila = tablaSalas.getSelectedRow();
			if (fila != -1) {
				int idSala = (int) modeloSalas.getValueAt(fila, 0); // Obtener el ID de la sala
				int confirm = JOptionPane.showConfirmDialog(this,
						"¿Está seguro de que desea eliminar la sala con ID " + idSala + "?", "Confirmar Eliminación",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					if (dbConnection.eliminarSala(idSala)) {
						JOptionPane.showMessageDialog(this, "Sala eliminada exitosamente.", "Éxito",
								JOptionPane.INFORMATION_MESSAGE);
						cargarSalasEnTabla(); // Recargar la tabla
					} else {
						JOptionPane.showMessageDialog(this, "Error al eliminar la sala.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione una sala para eliminar.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// Action listener para el botón "Mantenimiento"
		btnMantenimiento.addActionListener(e -> {
			filaSeleccionadaSala = tablaSalas.getSelectedRow();
			if (filaSeleccionadaSala != -1) {
				((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Mantenimiento");
				txtNumeroSalaMantenimiento.setText(modeloSalas.getValueAt(filaSeleccionadaSala, 0).toString());
				txtDNIEmpleadoMantenimiento.setText("");
				txtFechaMantenimiento.setValue(null);
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione una sala para gestionar el mantenimiento.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		});

		// Action listener para guardar una sala (desde "Formulario")
		btnGuardarSala.addActionListener(e -> {
			String tipo = txtTipoSala.getText().trim();
			boolean disponibilidad = chkDisponibilidad.isSelected();

			if (tipo.isEmpty()) {
				JOptionPane.showMessageDialog(this, "El campo Tipo es obligatorio.", "Error de Validación",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (filaSeleccionadaSala == -1) { 
				if (dbConnection.agregarSala(tipo, disponibilidad)) {
					JOptionPane.showMessageDialog(this, "Sala agregada exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Error al agregar la sala.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else { 
				int id = (int) modeloSalas.getValueAt(filaSeleccionadaSala, 0);
				if (dbConnection.modificarSala(id, tipo, disponibilidad)) {
					JOptionPane.showMessageDialog(this, "Sala actualizada exitosamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Error al actualizar la sala.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			limpiarCamposSala();
			cargarSalasEnTabla(); 
		});

		// Action listener para cancelar el formulario de sala
		btnCancelarSala.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			limpiarCamposSala();
		});

		// Action listener para guardar el mantenimiento (desde "Mantenimiento")
		btnGuardarMantenimiento.addActionListener(e -> {
			String dniEmpleado = txtDNIEmpleadoMantenimiento.getText().trim();
			Date fechaMantenimiento = null;
			try {

				fechaMantenimiento = (Date) dateFormat.parseObject(txtFechaMantenimiento.getText());
			} catch (ParseException ex) {
				JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error de Validación",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (dniEmpleado.isEmpty()) {
				JOptionPane.showMessageDialog(this, "El DNI de Empleado es obligatorio.", "Error de Validación",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (fechaMantenimiento == null) {
				JOptionPane.showMessageDialog(this, "Debe introducir una fecha válida (YYYY-MM-DD).",
						"Error de Validación", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (filaSeleccionadaSala != -1) {
				int salaId = (int) modeloSalas.getValueAt(filaSeleccionadaSala, 0);
				java.sql.Date sqlDate = new java.sql.Date(fechaMantenimiento.getTime());

				if (dbConnection.agregarTurnoMantenimiento(dniEmpleado, salaId, sqlDate)) {
					JOptionPane.showMessageDialog(this,
							"Mantenimiento registrado para la Sala ID: " + salaId + "\n" + "DNI Empleado: "
									+ dniEmpleado + "\n" + "Fecha: " + dateFormat.format(fechaMantenimiento),
							"Mantenimiento Registrado", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, "Error al registrar el mantenimiento.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			limpiarCamposSala();
			cargarSalasEnTabla(); 
		});

		btnCancelarMantenimiento.addActionListener(e -> {
			((CardLayout) cardsPanel.getLayout()).show(cardsPanel, "Tabla");
			limpiarCamposSala();
		});
	}


	private void cargarSalasEnTabla() {
		modeloSalas.setRowCount(0); 
		List<Sala> salas = dbConnection.obtenerTodasLasSalas();
		for (Sala sala : salas) {
			modeloSalas.addRow(new Object[] { sala.getId(), sala.getTipo(), sala.isDisponibilidad() ? "Libre" : "Ocupa" });
		}
	}

	private void limpiarCamposSala() {
		txtNumeroSala.setText("");
		txtTipoSala.setText("");
		txtNumeroSalaMantenimiento.setText("");
		txtDNIEmpleadoMantenimiento.setText("");
		txtFechaMantenimiento.setValue(null);
	}

	private void styleGestionButton(JButton button) {
		button.setBackground(gestionButtonBgColor);
		button.setForeground(gestionButtonFgColor);
		button.setFont(gestionButtonFont);
		button.setFocusPainted(false);
		button.setBorder(gestionButtonBorder);
		button.setPreferredSize(new Dimension(130, 35));
	}
}