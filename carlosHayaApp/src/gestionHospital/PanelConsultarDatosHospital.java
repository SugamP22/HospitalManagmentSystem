package gestionHospital;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import clases.DBConnection;
import clases.Empleado;
import clases.Paciente;
import clases.Sala;
import clases.Turno;

@SuppressWarnings("serial")
class PanelConsultarDatosHospital extends JPanel {
  private JComboBox<String> dataTypeComboBox;
  private JTable dataTable;
  private DefaultTableModel tableModel;
  private JLabel titleLabel;
  private JPanel panelLabel;
  private JPanel panelTable;
  private JPanel panelFilter;
  private DBConnection db;

  public PanelConsultarDatosHospital() {
	//Se inicializa la conexión con la base de datos
	    db = new DBConnection(); 
	  
    setLayout(new BorderLayout()); // Eliminado el espaciado horizontal y vertical
    setBackground(Color.decode("#212f3d")); // Color de fondo general del panel
    setBorder(new EmptyBorder(15, 15, 15, 15)); // Más espaciado interno

    // 1. Panel para el título (panelLabel)
    panelLabel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    panelLabel.setBackground(Color.decode("#E3242B")); // Color de fondo para el título
    titleLabel = new JLabel("Consulta de Datos del Hospital", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Roboto", Font.BOLD, 25));
    titleLabel.setForeground(Color.white); // Color del texto del título
    panelLabel.add(titleLabel);
    panelLabel.setPreferredSize(new Dimension(panelLabel.getPreferredSize().width, 60)); // Altura de 60px
    add(panelLabel, BorderLayout.NORTH); // Añadir el panel del título al NORTH

    // 2. Panel para la tabla (panelTable)
    panelTable = new JPanel(new BorderLayout());
    panelTable.setBackground(Color.decode("#212f3d")); // Color de fondo de la tabla a colorbg
    tableModel = new DefaultTableModel();
    dataTable = new JTable(tableModel);
    dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
    dataTable.setRowHeight(25);
    dataTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
    dataTable.getTableHeader().setBackground(Color.decode("#006D77"));
    dataTable.getTableHeader().setForeground(Color.white);
    dataTable.setFillsViewportHeight(true); // La tabla llena la altura del JScrollPane

    JScrollPane scrollPane = new JScrollPane(dataTable);
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1)); // Borde para el scroll
    panelTable.add(scrollPane, BorderLayout.CENTER); // Añadir el scrollPane al panelTable
    add(panelTable, BorderLayout.CENTER); // Añadir el panelTable al CENTER (consumirá el espacio restante)

    // 3. Panel para los controles de filtro (panelFilter)
    panelFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    panelFilter.setBackground(Color.decode("#728C69")); // Color de fondo para el filtro
    JLabel selectLabel = new JLabel("Seleccionar Tipo de Dato:");
    selectLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    selectLabel.setForeground(Color.white); // Color de texto para el filtro
    panelFilter.add(selectLabel);

    String[] dataTypes = { "Empleados", "Pacientes", "Salas", "Citas" };
    dataTypeComboBox = new JComboBox<>(dataTypes);
    dataTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
    dataTypeComboBox.setBackground(Color.decode("#ECF0F1"));
    panelFilter.add(dataTypeComboBox);

    JButton loadButton = new JButton("Cargar Datos");
    styleButton(loadButton, Color.decode("#006D77")); // Aplicar estilo consistente
    panelFilter.add(loadButton);

    panelFilter.setPreferredSize(new Dimension(panelFilter.getPreferredSize().width, 80)); // Altura de 80px
    add(panelFilter, BorderLayout.SOUTH); // Añadir el panelFilter al SOUTH

    
    
    // Listener para el botón de cargar datos
    loadButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loadData(dataTypeComboBox.getSelectedItem().toString());
      }
    });

    // Cargar datos por defecto al iniciar (ej. Empleados)
    loadData("Empleados");
  }

  private void loadData(String dataType) {
    tableModel.setRowCount(0); // Limpiar datos existentes
    tableModel.setColumnCount(0); // Limpiar columnas existentes

    // Actualizar el texto del título según el tipo de dato cargado
    titleLabel.setText("Consulta de Datos de " + dataType);
    
  //Se inicializa la conexión con la base de datos
    db = new DBConnection();

    switch (dataType) {
      case "Empleados":
    	
        tableModel.setColumnIdentifiers(new String[] { "DNI Empleado", "Nombre", "Apellido", "Rol", "ID Sala" });
        // Carga las filas de datos de los empleados
        cargaDatosEmpleados();
        
        break;
      case "Pacientes":
        tableModel.setColumnIdentifiers(new String[] { "DNI Paciente", "Nombre", "Apellido", "Contacto", "Obra social", "ID Sala" });
        // Carga las filas de datos de los pacientes
        cargaDatosPacientes();
        break;
      case "Salas":
        tableModel.setColumnIdentifiers(new String[] { "ID Sala", "Tipo",  "Disponibilidad" });
        // Carga las filas de datos de las salas
        cargaDatosSala();
        break;
      case "Citas":
        tableModel.setColumnIdentifiers(new String[] { "ID Cita", "Paciente", "Médico", "Fecha", "Hora_Inicio", "Hora_Fin" });
        // Carga las filas de datos de los turnos
        cargaDatosTurno();
        break;
    }
  }

  private void cargaDatosSala() {
	ArrayList<Sala> salas = (ArrayList<Sala>) db.obtenerTodasLasSalas();
	for(Sala s : salas) {
		if(s.isDisponibilidad()) {
			tableModel.addRow(new Object[] {s.getId(), s.getTipo(), "disponible"});
		}else {
			tableModel.addRow(new Object[] {s.getId(), s.getTipo(), "no disponible"});
		}
	}
	
}

private void cargaDatosTurno() {
	  ArrayList<Turno> turnos = (ArrayList<Turno>) db.obtenerTodosLosTurnos();
	  for(Turno t : turnos) {
		  tableModel.addRow(new Object[] {t.getId(),t.getPacienteDni(),t.getMedicoDni(),t.getDia(),t.getHoraInicio(),t.getHoraFin()});
	  }
	
}

private void cargaDatosPacientes() {
	  ArrayList<Paciente> pacientes = (ArrayList<Paciente>) db.obtenerTodosLosPacientes();
	  for(Paciente p : pacientes) {
		  tableModel.addRow(new Object[] {p.getDni(),p.getNombre(),p.getApellido(),p.getContacto(),p.getObraSocial(),p.getSalaID() == 0 ? "Libre" : String.valueOf(p.getSalaID())});
	  }
	
}

private void cargaDatosEmpleados() {
	  ArrayList<Empleado> empleados = (ArrayList<Empleado>) db.obtenerTodosLosEmpleados();
	  for(Empleado e : empleados) {
		  tableModel.addRow(new Object[] {e.getDni(),e.getNombre(),e.getApellido(),e.getRol(),e.getSalaId() == 0 ? "Sin asignar" : String.valueOf(e.getSalaId())});
	  }
	  
}

private void styleButton(JButton button, Color bgColor) {
    button.setBackground(bgColor);
    button.setForeground(Color.white);
    button.setFont(new Font("Arial", Font.BOLD, 14));
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    button.setPreferredSize(new Dimension(150, 35));
  }
}
