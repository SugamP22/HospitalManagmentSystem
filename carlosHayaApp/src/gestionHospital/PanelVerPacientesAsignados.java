package gestionHospital;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import clases.DBConnection;

public class PanelVerPacientesAsignados extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private final Color primaryBackgroundColor = Color.decode("#212f3d");
    private final Color titlePanelColor = Color.RED;
    private final Color titleTextColor = Color.WHITE;
    private final Color headerBackgroundColor = Color.decode("#2C3E50");
    private final Color headerTextColor = Color.WHITE;

    private JTable tablaPacientes;
    private DBConnection db;
    private DefaultTableModel modelo;
    
    public PanelVerPacientesAsignados() {
        setLayout(new BorderLayout(10, 10));
        setBackground(primaryBackgroundColor);
        setPreferredSize(new Dimension(800, 600)); 
        
        db = new DBConnection(); 

        initComponents();
    }

    private void initComponents() {
        JPanel titleContainerPanel = new JPanel();
        titleContainerPanel.setBackground(titlePanelColor);
        titleContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        titleContainerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titulo = new JLabel("Pacientes Asignados", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(titleTextColor);
        titleContainerPanel.add(titulo);
        add(titleContainerPanel, BorderLayout.NORTH);

        String[] columnas = { "ID", "Nombre", "Apellido", "Contacto","Obra social", "Sala" };
        
        modelo = new DefaultTableModel( columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cargaDatos();

        tablaPacientes = new JTable(modelo);
        tablaPacientes.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaPacientes.setRowHeight(25);
        
        DefaultTableCellRenderer customHeaderRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                label.setBackground(headerBackgroundColor);
                label.setForeground(headerTextColor);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                
                return label;
            }
        };

        for (int i = 0; i < tablaPacientes.getColumnModel().getColumnCount(); i++) {
            tablaPacientes.getColumnModel().getColumn(i).setHeaderRenderer(customHeaderRenderer);
        }
        
        tablaPacientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void cargaDatos() {
        modelo.setRowCount(0);

        ArrayList<Object[][]> resultados = db.pacientesAsignados(Sesion.getUsuarioLogueado());

        for (Object[][] filaArray : resultados) {
            modelo.addRow(filaArray[0]);
        }
    }
}