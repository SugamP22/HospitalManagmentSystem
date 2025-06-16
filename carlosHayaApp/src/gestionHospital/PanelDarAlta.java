package gestionHospital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import clases.DBConnection;
import clases.Paciente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


@SuppressWarnings("serial")
class PanelDarAlta extends JPanel {
	private DBConnection db;
    
    private final Color accentColor = Color.decode("#006D77"); // Color de acento para botones, etc.
    private final Color headerTextColor = Color.WHITE; // Color de texto para cabeceras y títulos
    private final Color borderColor = Color.LIGHT_GRAY; // Color para bordes de tablas

    // Colores específicos para este panel
    private final Color titleBackgroundColor = Color.RED; // Fondo rojo para el título
    private final Color tableHeaderBackgroundColor = Color.decode("#34495e"); // Fondo gris azulado para la cabecera de la tabla

    private JTable tablaPacientes;
    private DefaultTableModel modeloPacientes;
    private JButton btnDarAlta;

    public PanelDarAlta() {
    	db= new DBConnection();
    	
        setLayout(new BorderLayout(0, 0)); // Sin espaciado entre componentes principales
       
        // --- 1. Panel para el título (NORTE) ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titlePanel.setBackground(titleBackgroundColor); // Fondo rojo
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Padding vertical
        
        JLabel title = new JLabel("Dar de Alta a Paciente", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28)); // Fuente más grande para el título
        title.setForeground(headerTextColor); // Texto blanco
        titlePanel.add(title);
        
        add(titlePanel, BorderLayout.NORTH);

        // --- 2. Panel para la tabla de pacientes (CENTRO) ---
        String[] columnasPacientes = {"DNI", "Nombre", "Apellidos", "Sala"};
        modeloPacientes = new DefaultTableModel(columnasPacientes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas de la tabla no son editables
            }
        };
        Sesion.setModelo(modeloPacientes);
        
        tablaPacientes = new JTable(modeloPacientes);
        styleTable(tablaPacientes, modeloPacientes); // Aplica estilos a la tabla y su cabecera
        
        JScrollPane scrollPacientes = new JScrollPane(tablaPacientes);
        scrollPacientes.setBorder(new LineBorder(borderColor, 1)); // Borde para el scrollpane
        add(scrollPacientes, BorderLayout.CENTER);

        // --- 3. Panel para el botón "Dar de Alta" (SUR) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Padding vertical

        btnDarAlta = new JButton("Dar de Alta");
        styleButton(btnDarAlta, accentColor, headerTextColor, 18); // Estilo para el botón
        buttonPanel.add(btnDarAlta);
        
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        btnDarAlta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tablaPacientes.getSelectedRow(); // Obtener la fila seleccionada
                if (selectedRow == -1) {
                    // Si no hay fila seleccionada
                    JOptionPane.showMessageDialog(PanelDarAlta.this, 
                                                  "Por favor, selecciona un paciente de la lista para dar de alta.", 
                                                  "Paciente no seleccionado", 
                                                  JOptionPane.WARNING_MESSAGE);
                } else {
                    // Si hay una fila seleccionada
                    String dni = (String) modeloPacientes.getValueAt(selectedRow, 0);
                    String nombre = (String) modeloPacientes.getValueAt(selectedRow, 1);
                    String apellidos = (String) modeloPacientes.getValueAt(selectedRow, 2);

                    darAltaPaciente(dni, nombre, apellidos);
                    
                   
                    
                   loadPacientes();  }
            }
        });

        // Carga inicial de datos (simulada)
        loadPacientes();
    }

    // --- Métodos de Lógica (Simulada) ---

    private void loadPacientes() {
        modeloPacientes.setRowCount(0); // Limpia la tabla
        
        ArrayList<Paciente> pacientes = (ArrayList<Paciente>) db.mostrarPacientesDeBaja();
        for(Paciente p : pacientes) {
        	if(p.getSalaID()==0) {
        		modeloPacientes.addRow(new Object[] {p.getDni(),p.getNombre(),p.getApellido(),"sin habitación asignada"});
        	}else {
        		modeloPacientes.addRow(new Object[] {p.getDni(),p.getNombre(),p.getApellido(),p.getSalaID()});
        	}
        	
        }
        adjustColumnWidths(tablaPacientes); // Ajusta el ancho de las columnas
    }

    private void darAltaPaciente(String dni, String nombre, String apellidos) {
    	if(db.darAlta(dni)) {
    		JOptionPane.showMessageDialog(
    	            this,
    	            "El paciente " + nombre + " " + apellidos + " con DNI " + dni + " ha sido dado de alta correctamente.",
    	            "Paciente Dado de Alta",
    	            JOptionPane.INFORMATION_MESSAGE
    	        );
    		if(db.actualizarDisponibilidad2(dni)) {
    			System.out.println("Disponibilidad actualizada");
    		}
    		if(db.actualizarPacienteSala(dni)) {
    			System.out.println("Sala de persona actualizada");
    		}
    	}else {
    		JOptionPane.showMessageDialog(
    	            this,
    	            "Error al dar de alta al paciente",
    	            "ERROR",
    	            JOptionPane.ERROR_MESSAGE
    	        );
    	}
        
    }

    // --- Métodos de Estilo Reutilizados ---
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
        table.setSelectionForeground(headerTextColor); // Texto de selección blanco
        table.setFillsViewportHeight(true);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 15));
        
        tableHeader.setBackground(tableHeaderBackgroundColor); // Fondo distinto para la cabecera
        tableHeader.setForeground(headerTextColor); // Texto blanco en la cabecera
        
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
}
