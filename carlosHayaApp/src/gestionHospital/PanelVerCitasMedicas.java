package gestionHospital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

import clases.DBConnection;
import clases.Turno;

public class PanelVerCitasMedicas extends JPanel {
	private DBConnection db;

	private final Color headerBackgroundColor = Color.decode("#2C3E50");
	private final Color headerTextColor = Color.WHITE;
	private final Color titlePanelColor = Color.RED;
	private final Color titleTextColor = Color.WHITE;
	private final Color panelBackgroundColor = Color.WHITE;

	public PanelVerCitasMedicas() {
		db = new DBConnection();

		setLayout(new BorderLayout());
		setBackground(panelBackgroundColor);

		JPanel titleContainerPanel = new JPanel();
		titleContainerPanel.setBackground(titlePanelColor);
		titleContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		titleContainerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel titulo = new JLabel("Listado de Citas Médicas", SwingConstants.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 24));
		titulo.setForeground(titleTextColor);
		titleContainerPanel.add(titulo);
		add(titleContainerPanel, BorderLayout.NORTH);

		String[] columnas = { "DNI Paciente", "Día", "Hora Comienzo", "Hora Final" };

		DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
		JTable tabla = new JTable(modelo);
		JScrollPane scrollPane = new JScrollPane(tabla);

		DefaultTableCellRenderer customHeaderRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				label.setBackground(headerBackgroundColor);
				label.setForeground(headerTextColor);
				label.setFont(new Font("Arial", Font.BOLD, 14));
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setOpaque(true); // Asegúrate de que el componente sea opaco

				return label;
			}
		};

		for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
			tabla.getColumnModel().getColumn(i).setHeaderRenderer(customHeaderRenderer);
		}

		tabla.setRowHeight(25);
		tabla.setFont(new Font("Arial", Font.PLAIN, 12));
		tabla.setGridColor(Color.LIGHT_GRAY);
		tabla.setFillsViewportHeight(true);

		cargarDatosCitas(modelo);

		add(scrollPane, BorderLayout.CENTER);
	}

	private void cargarDatosCitas(DefaultTableModel modelo) {
		modelo.setRowCount(0);
		ArrayList<Turno> citas = (ArrayList<Turno>) db.obtenerCitasMedicos(Sesion.getUsuarioLogueado());
		for (Turno t : citas) {
			modelo.addRow(new Object[] { t.getPacienteDni(), t.getDia(), t.getHoraInicio(), t.getHoraFin() });
		}
	}
}