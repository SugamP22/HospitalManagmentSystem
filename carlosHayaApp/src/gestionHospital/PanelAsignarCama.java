package gestionHospital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import clases.DBConnection;
import clases.Paciente;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
class PanelAsignarCama extends JPanel {
	private DBConnection db;
	
    // Colores consistentes con el resto de la aplicación
    private final Color primaryBackgroundColor = Color.decode("#ECF0F1"); 
    private final Color accentColor = Color.decode("#006D77"); 
    private final Color borderColor = Color.LIGHT_GRAY; 
    private final Color headerTextColor = Color.WHITE; 
    
    // Colores específicos para los títulos de los paneles
    private final Color titleBackgroundColor = Color.RED; 
    
    // Color para el fondo del mainPanel (el panel derecho)
    private final Color rightPanelBackgroundColor = Color.decode("#212f3d"); 

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Componentes de la Vista de Pacientes
    private JTextField txtBuscarPaciente;
    private JButton btnBuscarPaciente;
    private JButton btnAsignarCamaPaciente;
    private JTable tablaPacientes;
    private DefaultTableModel modeloPacientes;
    private String pacienteSeleccionadoDNI;

    // Componentes de la Vista de Camas
    private JTable tablaCamas;
    private DefaultTableModel modeloCamas;
    private JButton btnConfirmarAsignacion;
    private JButton btnVolverPaciente;
    private JLabel titleCamasLabel;

    public PanelAsignarCama() {
    	db = new DBConnection();
        setLayout(new BorderLayout());
        setBackground(primaryBackgroundColor); 
        setBorder(new EmptyBorder(0, 0, 0, 0)); 

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(rightPanelBackgroundColor); 

        JPanel patientListView = createPatientListView();
        JPanel bedSelectionView = createBedSelectionView();

        mainPanel.add(patientListView, "PatientList");
        mainPanel.add(bedSelectionView, "BedSelection");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "PatientList");
    }

    private JPanel createPatientListView() {
        JPanel panel = new JPanel(new BorderLayout(0, 0)); 
        panel.setBackground(rightPanelBackgroundColor); 
        panel.setBorder(new EmptyBorder(0, 0, 0, 0)); 

        // --- 1. Panel para el título (NORTE) ---
        JPanel titlePatientPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
        titlePatientPanel.setBackground(titleBackgroundColor); 
        titlePatientPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        
        JLabel title = new JLabel("Asignar Cama a Paciente", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(headerTextColor); 
        titlePatientPanel.add(title);
        
        panel.add(titlePatientPanel, BorderLayout.NORTH);

        // --- 2. Panel para búsqueda y lista (CENTRO) ---
        JPanel centerContentPanel = new JPanel(new BorderLayout(0, 0)); 
        centerContentPanel.setBackground(rightPanelBackgroundColor); 
        centerContentPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); 

        // --- 2.1 Panel de controles de búsqueda (dentro de CENTERContentPanel.NORTH) ---
        JPanel topControlsPanel = new JPanel(new BorderLayout(15, 0)); 
        topControlsPanel.setBackground(rightPanelBackgroundColor); 
        topControlsPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); 
        
        txtBuscarPaciente = new JTextField();
        txtBuscarPaciente.setFont(new Font("Arial", Font.PLAIN, 16));
        txtBuscarPaciente.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderColor, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        // Ajustamos el tamaño preferido para que se estire más
        txtBuscarPaciente.setPreferredSize(new Dimension(Integer.MAX_VALUE, txtBuscarPaciente.getPreferredSize().height));
        topControlsPanel.add(txtBuscarPaciente, BorderLayout.CENTER);

        btnBuscarPaciente = new JButton("Buscar");
        styleButton(btnBuscarPaciente, accentColor, Color.WHITE, 16);
        // Ajustamos el tamaño preferido del botón si es necesario, para que no sea excesivamente grande
        // o para darle un ancho fijo si se quiere, pero Integer.MAX_VALUE en el textfield lo gestionará.
        // btnBuscarPaciente.setPreferredSize(new Dimension(100, btnBuscarPaciente.getPreferredSize().height)); // Ejemplo: ancho fijo
        topControlsPanel.add(btnBuscarPaciente, BorderLayout.EAST);
        
        centerContentPanel.add(topControlsPanel, BorderLayout.NORTH); 

        // --- 2.2 Panel para la tabla (dentro de CENTERContentPanel.CENTER) ---
        String[] columnasPacientes = {"DNI", "Nombre", "Apellido", "Sala Asignada"};
        modeloPacientes = new DefaultTableModel(columnasPacientes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPacientes = new JTable(modeloPacientes);
        styleTable(tablaPacientes, modeloPacientes);
        
        JScrollPane scrollPacientes = new JScrollPane(tablaPacientes);
        scrollPacientes.setBorder(new LineBorder(borderColor, 1)); 
        centerContentPanel.add(scrollPacientes, BorderLayout.CENTER); 

        panel.add(centerContentPanel, BorderLayout.CENTER); 

        // --- 3. Botón Asignar Cama (SUR) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
        buttonPanel.setBackground(rightPanelBackgroundColor); 
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        
        btnAsignarCamaPaciente = new JButton("Asignar Cama Seleccionada");
        styleButton(btnAsignarCamaPaciente, accentColor, Color.WHITE, 16);
        buttonPanel.add(btnAsignarCamaPaciente);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Listeners de la Vista de Pacientes ---
        btnBuscarPaciente.addActionListener(e -> searchAndFilterPatients(txtBuscarPaciente.getText().trim()));
        
        tablaPacientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (tablaPacientes.getSelectedRow() != -1) {
                        int selectedRow = tablaPacientes.convertRowIndexToModel(tablaPacientes.getSelectedRow());
                        pacienteSeleccionadoDNI = (String) modeloPacientes.getValueAt(selectedRow, 0);
                    } else {
                        pacienteSeleccionadoDNI = null;
                    }
                }
            }
        });

        btnAsignarCamaPaciente.addActionListener(e -> {
            if (tablaPacientes.getSelectedRow() != -1) { 
                int selectedRow = tablaPacientes.convertRowIndexToModel(tablaPacientes.getSelectedRow());
                pacienteSeleccionadoDNI = (String) modeloPacientes.getValueAt(selectedRow, 0);

                cardLayout.show(mainPanel, "BedSelection");
                loadAvailableBeds();
                tablaCamas.clearSelection();
            } else {
                JOptionPane.showMessageDialog(PanelAsignarCama.this, 
                                              "Por favor, selecciona un paciente de la lista antes de asignar una cama.", 
                                              "Paciente no seleccionado", 
                                              JOptionPane.WARNING_MESSAGE);
            }
        });

        loadAllPatients();

        return panel;
    }

    private JPanel createBedSelectionView() {
        JPanel panel = new JPanel(new BorderLayout(0, 0)); 
        panel.setBackground(rightPanelBackgroundColor); 
        panel.setBorder(new EmptyBorder(0, 0, 0, 0)); 

        // --- 1. Panel para el título de Camas (NORTE) ---
        JPanel titleBedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
        titleBedPanel.setBackground(titleBackgroundColor); 
        titleBedPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        
        titleCamasLabel = new JLabel("Seleccionar Cama Disponible", SwingConstants.CENTER);
        titleCamasLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleCamasLabel.setForeground(headerTextColor); 
        titleBedPanel.add(titleCamasLabel);
        
        panel.add(titleBedPanel, BorderLayout.NORTH);

        // --- 2. Panel para la tabla de camas (CENTRO) ---
        String[] columnasCamas = {"ID Cama", "Tipo", "Estado"};
        modeloCamas = new DefaultTableModel(columnasCamas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCamas = new JTable(modeloCamas);
        styleTable(tablaCamas, modeloCamas);

        JScrollPane scrollCamas = new JScrollPane(tablaCamas);
        scrollCamas.setBorder(new LineBorder(borderColor, 1)); 
        panel.add(scrollCamas, BorderLayout.CENTER);

        // --- 3. Panel de botones de acción (SUR) ---
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); 
        actionButtonsPanel.setBackground(rightPanelBackgroundColor); 
        actionButtonsPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        
        btnConfirmarAsignacion = new JButton("Confirmar Asignación");
        styleButton(btnConfirmarAsignacion, accentColor, Color.WHITE, 16);
        actionButtonsPanel.add(btnConfirmarAsignacion);

        btnVolverPaciente = new JButton("Volver a Pacientes");
        styleButton(btnVolverPaciente, Color.GRAY, Color.WHITE, 16);
        actionButtonsPanel.add(btnVolverPaciente);

        panel.add(actionButtonsPanel, BorderLayout.SOUTH);

        // --- Listeners de la Vista de Camas ---
        tablaCamas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // No hace nada para el estado del botón, solo permite la selección visual
            }
        });

        btnConfirmarAsignacion.addActionListener(e -> {
            int selectedRow = tablaCamas.convertRowIndexToModel(tablaCamas.getSelectedRow());
            if (selectedRow != -1) {
                String idCama = modeloCamas.getValueAt(selectedRow, 0).toString();
                assignBedToPatient(pacienteSeleccionadoDNI, idCama);
                cardLayout.show(mainPanel, "PatientList");
                loadAllPatients(); 
                txtBuscarPaciente.setText(""); 
                pacienteSeleccionadoDNI = null; 
                tablaPacientes.clearSelection(); 
            } else {
                JOptionPane.showMessageDialog(PanelAsignarCama.this, 
                                              "Por favor, selecciona una cama de la lista para asignar.", 
                                              "Cama no seleccionada", 
                                              JOptionPane.WARNING_MESSAGE);
            }
        });

        btnVolverPaciente.addActionListener(e -> {
            cardLayout.show(mainPanel, "PatientList");
            txtBuscarPaciente.setText("");
            pacienteSeleccionadoDNI = null;
            tablaPacientes.clearSelection(); 
        });

        return panel;
    }

    // --- Métodos de Carga y Lógica (SIMULADA - NO BACKEND) ---
    private void loadAllPatients() {
        modeloPacientes.setRowCount(0);
        ArrayList<Paciente> pacientes = (ArrayList<Paciente>) db.mostrarPacientesSinCama();
        for(Paciente p : pacientes) {
        	if(p.getSalaID()==0) {
        		modeloPacientes.addRow(new Object[] {p.getDni(),p.getNombre(),p.getApellido(),"sin habiación asignada"});
        	}else {
        		modeloPacientes.addRow(new Object[] {p.getDni(),p.getNombre(),p.getApellido(),p.getSalaID()});
        	}
        }
        
        adjustColumnWidths(tablaPacientes);
    }

    private void searchAndFilterPatients(String query) {
        modeloPacientes.setRowCount(0);
        if (query.isEmpty()) {
            loadAllPatients();
            return;
        }
        ArrayList<Paciente> pacientes = (ArrayList<Paciente>) db.mostrarPacientesSinCama();
        for(Paciente p : pacientes) {
            String dni = p.getDni();
            String nombre = p.getNombre();
            String apellido = p.getApellido();
            int sala = p.getSalaID();
            
            if (dni.toLowerCase().contains(query.toLowerCase()) ||
                nombre.toLowerCase().contains(query.toLowerCase()) ||
                apellido.toLowerCase().contains(query.toLowerCase())) {
            	if(sala==0) {
            		modeloPacientes.addRow(new Object[] {dni,nombre,apellido,"sin habitación asignada"});
            	}else {
            		modeloPacientes.addRow(new Object[] {dni,nombre,apellido,sala});
            	}
            }
        }

        adjustColumnWidths(tablaPacientes);
    }

    private void loadAvailableBeds() {
        modeloCamas.setRowCount(0);
        ArrayList<Integer> camas = (ArrayList<Integer>) db.mostrarCama();
        for(Integer c : camas) {
        	modeloCamas.addRow(new Object[]{c, "Habitación", "Disponible"});
        }
        adjustColumnWidths(tablaCamas);
    }

    private void assignBedToPatient(String patientDNI, String bedID) {
    	int id_sala = Integer.parseInt(bedID);
    	if(db.asignarSalaPaciente(patientDNI, id_sala)) {
    		JOptionPane.showMessageDialog(
    	            this,
    	            "Cama " + bedID + " asignada al paciente con DNI: " + patientDNI + ".",
    	            "Asignación Exitosa",
    	            JOptionPane.INFORMATION_MESSAGE
    	        );
    		if(db.actualizarDisponibilidad1(bedID)) {
    			System.out.println("Disponibilidad actualizada");
    		}
    		//Recarga la información de la tabla en PanelDarAlta
    		recargarTablaDarAlta();
    		
    	}else {
    		JOptionPane.showMessageDialog(this, "Ha habido un error al asignar la cama al paciente","Error",JOptionPane.ERROR_MESSAGE);
    	}
        
    }

    // --- Métodos de Estilo ---
    private void styleButton(JButton button, Color bgColor, Color fgColor, int fontSize) {
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor.darker(), 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
    }

    private void styleTable(JTable table, DefaultTableModel model) {
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(borderColor);
        table.setSelectionBackground(accentColor.brighter());
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 15));
        tableHeader.setBackground(accentColor);
        tableHeader.setForeground(headerTextColor);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(true);
    }

    private void adjustColumnWidths(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            table.getColumnModel().getColumn(column).setPreferredWidth(120);
            int maxWidth = 0;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                maxWidth = Math.max(c.getPreferredSize().width + table.getIntercellSpacing().width, maxWidth);
            }
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, table.getColumnModel().getColumn(column).getHeaderValue(), false, false, 0, column);
            maxWidth = Math.max(maxWidth, headerComp.getPreferredSize().width + table.getIntercellSpacing().width);

            table.getColumnModel().getColumn(column).setPreferredWidth(maxWidth + 10);
        }
    }
    public void recargarTablaDarAlta() {

		Sesion.getModelo().setRowCount(0);
		ArrayList<Paciente> pacientes = (ArrayList<Paciente>) db.mostrarPacientesDeBaja();
	        
		for(Paciente p : pacientes) {
        	if(p.getSalaID()==0) {
        		Sesion.getModelo().addRow(new Object[] {p.getDni(),p.getNombre(),p.getApellido(),"sin habitación asignada"});
        	}else {
        		Sesion.getModelo().addRow(new Object[] {p.getDni(),p.getNombre(),p.getApellido(),p.getSalaID()});
        	}
        	
        }

    
    }
}
