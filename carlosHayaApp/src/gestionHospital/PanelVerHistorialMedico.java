package gestionHospital;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import clases.DBConnection;

public class PanelVerHistorialMedico extends JPanel {

    private final Color primaryBackgroundColor = Color.decode("#212f3d");
    private final Color accentColor = Color.decode("#006D77");
    private final Color textColor = Color.WHITE;
    private final Color titlePanelColor = Color.RED;
    private final Color headerBackgroundColor = Color.decode("#2C3E50");
    private final Color headerTextColor = Color.WHITE;

    private JTable tablaHistorial;
    private DefaultTableModel modelo;
    private JTextField textFieldID;
    private TableRowSorter<DefaultTableModel> sorter;
    private static DBConnection db;

    public PanelVerHistorialMedico() {
        db = new DBConnection(); 

        setLayout(new BorderLayout(10, 10));
        setBackground(primaryBackgroundColor);
        setPreferredSize(new Dimension(1000, 700));
        initComponents();
        
        
    }

    private void initComponents() {
        JPanel titleContainerPanel = new JPanel();
        titleContainerPanel.setBackground(titlePanelColor);
        titleContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        titleContainerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titulo = new JLabel("Historial Médico de Pacientes", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(textColor);
        titleContainerPanel.add(titulo);
        add(titleContainerPanel, BorderLayout.NORTH);

        JPanel filtroPanel = new JPanel();
        filtroPanel.setBackground(primaryBackgroundColor);
        filtroPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        JLabel lblID = new JLabel("ID Paciente:");
        lblID.setForeground(textColor);
        lblID.setFont(new Font("Arial", Font.PLAIN, 16));
        filtroPanel.add(lblID);

        textFieldID = new JTextField(15);
        textFieldID.setFont(new Font("Arial", Font.PLAIN, 16));
        textFieldID.setBackground(Color.WHITE);
        textFieldID.setForeground(Color.BLACK);
        filtroPanel.add(textFieldID);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(accentColor);
        btnBuscar.setForeground(textColor);
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 16));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        filtroPanel.add(btnBuscar);

        add(filtroPanel, BorderLayout.SOUTH);

        String[] columnas = { "ID", "Nombre", "Apellido", "Contacto", "Obra social", "Medicamentos","Fecha receta","Descripción diagnostico","Fecha diagnóstico" };

        modelo = new DefaultTableModel(columnas,0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        
        
        //Carga la tabla de datos
        cargarDatos();
        
        Sesion.setModelo(modelo);
        
        tablaHistorial = new JTable(modelo);
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaHistorial.setRowHeight(25);
        
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

        for (int i = 0; i < tablaHistorial.getColumnModel().getColumnCount(); i++) {
            tablaHistorial.getColumnModel().getColumn(i).setHeaderRenderer(customHeaderRenderer);
        }
        
        tablaHistorial.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        sorter = new TableRowSorter<>(modelo);
        tablaHistorial.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        add(scrollPane, BorderLayout.CENTER);
        
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarTabla();
            }
        });
        
    }

    private void cargarDatos() {
		modelo.setRowCount(0);
		ArrayList<Object[][]> resultados = db.mostrarHistorialPaciente(Sesion.getUsuarioLogueado());
	        
	    for (Object[][] filaArray : resultados) {
	            modelo.addRow(filaArray[0]);
	    }

    }

    private void filtrarTabla() {
        String idPaciente = textFieldID.getText().trim();
        if (idPaciente.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("^" + idPaciente + "$", 0));
        }
    }
}