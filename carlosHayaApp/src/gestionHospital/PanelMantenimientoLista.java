package gestionHospital;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import clases.DBConnection;
import clases.TurnoMantenimientoInfo;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class PanelMantenimientoLista extends JPanel {

	private final Color displayPanelBg;
	private final Color subPanelTitleBgColor;
	private JTable salasTable;
	private DefaultTableModel tableModel;
	private DBConnection dbConnection; // Instancia de DBConnection

	public PanelMantenimientoLista(Color displayPanelBg, Color subPanelTitleBgColor) {
		this.displayPanelBg = displayPanelBg;
		this.subPanelTitleBgColor = subPanelTitleBgColor;

		setLayout(new BorderLayout());
		setBackground(displayPanelBg);

		dbConnection = new DBConnection();

		JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titleWrapper.setBackground(this.subPanelTitleBgColor);
		titleWrapper.setBorder(new EmptyBorder(15, 0, 15, 0));

		JLabel titulo = new JLabel("Estado de Limpieza - Salas", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 22));
		titulo.setForeground(Color.WHITE);
		titleWrapper.add(titulo);

		add(titleWrapper, BorderLayout.NORTH);

		String[] headers = { "Número Sala", "Tipo", "Limpia", "Fecha de Limpieza" };
		tableModel = new DefaultTableModel(headers, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 2) {
					return Boolean.class;
				}
				return super.getColumnClass(columnIndex);
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		salasTable = new JTable(tableModel);
		salasTable.setFont(new Font("Arial", Font.PLAIN, 16));
		salasTable.setRowHeight(25);
		salasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		salasTable.setBackground(displayPanelBg);

		// Estilo de los encabezados de la tabla
		salasTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		salasTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
		salasTable.getTableHeader().setForeground(Color.DARK_GRAY);

		// Cargar datos desde la base de datos al inicializar
		cargaTurnosMantenimiento();

		JScrollPane scrollPane = new JScrollPane(salasTable);
		scrollPane.setBorder(new EmptyBorder(10, 20, 20, 20));
		add(scrollPane, BorderLayout.CENTER);
	}

	// METODO DE CARGAR DE DATOS POR EL DNI DE USUARIO, CARAGARA SOLO DATOS DE DNI
	// CORESTPONDIENTE
	private void cargaTurnosMantenimiento() {
		tableModel.setRowCount(0);

		String empleadoDni = Sesion.getUsuarioLogueado();
		if (empleadoDni != null && !empleadoDni.isEmpty()) {
			List<TurnoMantenimientoInfo> turnos = dbConnection.obtenerTurnosMantenimientoPorDNI(empleadoDni);
			for (TurnoMantenimientoInfo turno : turnos) {
				tableModel.addRow(new Object[] { "Sala " + turno.getSalaId(), turno.getTipoSala(), turno.isLimpia(),
						turno.getFechaMantenimiento().toString() });
			}
		} else {
			JOptionPane.showMessageDialog(this,
					"No se pudo obtener el DNI del usuario logueado. Asegúrate de que Sesion.usuarioLogueado esté configurado.",
					"Error de Sesión", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void marcasLimpiezaSala() {
		int selectedRow = salasTable.getSelectedRow();
		if (selectedRow != -1) {
			int salaId = Integer.parseInt(((String) tableModel.getValueAt(selectedRow, 0)).replace("Sala ", ""));
			boolean isLimpia = (boolean) tableModel.getValueAt(selectedRow, 2);

			if (!isLimpia) {
				boolean updated = dbConnection.actualizarEstadoLimpiezaSala(salaId, true);
				if (updated) {
					tableModel.setValueAt(true, selectedRow, 2);
					JOptionPane.showMessageDialog(this, "Sala " + salaId + " marcada como limpia en la base de datos.",
							"Limpieza Completada", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this,
							"Error al actualizar la sala " + salaId + " en la base de datos.", "Error de Base de Datos",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "La sala " + salaId + " ya está marcada como limpia.",
						"Información", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Por favor, selecciona una sala para marcar como limpia.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
}
