package gestionHospital;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import clases.DBConnection;
import clases.Diagnostico;
// 'gestionHospital.Sesion' no necesita importación explícita si está en el mismo paquete.

public class PanelRegistrarDiagnostico extends JPanel {

    private final Color primaryBackgroundColor = Color.decode("#212f3d");
    private final Color accentColor = Color.decode("#006D77");
    private final Color textColor = Color.WHITE;
    private final Color inputFieldBackgroundColor = Color.WHITE;
    private final Color inputFieldTextColor = Color.BLACK;
    private final Color titlePanelColor = Color.RED;

    private JTextField textFieldPaciente, textFieldFecha;
    private JTextArea textAreaDescripcion; // Se mantiene el nombre del atributo
    private DBConnection db;
    
    public PanelRegistrarDiagnostico() {
        setLayout(new BorderLayout(10, 10));
        setBackground(primaryBackgroundColor);
        setPreferredSize(new Dimension(600, 500));
        initComponents();
    }

    private void initComponents() {
        // Panel para el título
        JPanel titleContainerPanel = new JPanel();
        titleContainerPanel.setBackground(titlePanelColor); // Fondo rojo
        titleContainerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        titleContainerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titulo = new JLabel("Registrar Diagnóstico");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(textColor);
        titleContainerPanel.add(titulo);
        add(titleContainerPanel, BorderLayout.NORTH);

        // Panel de formulario con GridBagLayout
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(primaryBackgroundColor);
        formularioPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ID Paciente (Fila 0)
        JLabel lblPaciente = new JLabel("ID Paciente:");
        lblPaciente.setForeground(textColor);
        lblPaciente.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        formularioPanel.add(lblPaciente, gbc);

        textFieldPaciente = new JTextField(20);
        textFieldPaciente.setBackground(inputFieldBackgroundColor);
        textFieldPaciente.setForeground(inputFieldTextColor);
        textFieldPaciente.setFont(new Font("Arial", Font.PLAIN, 16));
        textFieldPaciente.setCaretColor(inputFieldTextColor);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formularioPanel.add(textFieldPaciente, gbc);
        
        // Fecha (Fila 1)
        JLabel lblFecha = new JLabel("Fecha (DD-MM-YYYY):");
        lblFecha.setForeground(textColor);
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1; // Ahora en la fila 1
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        formularioPanel.add(lblFecha, gbc);

        textFieldFecha = new JTextField(20);
        textFieldFecha.setBackground(inputFieldBackgroundColor);
        textFieldFecha.setForeground(inputFieldTextColor);
        textFieldFecha.setFont(new Font("Arial", Font.PLAIN, 16));
        textFieldFecha.setCaretColor(inputFieldTextColor);
        gbc.gridx = 1; gbc.gridy = 1; // Ahora en la fila 1
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formularioPanel.add(textFieldFecha, gbc);

        // Descripción (Fila 2 - ahora después de la fecha)
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setForeground(textColor);
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2; // Ahora en la fila 2
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.weighty = 0;
        formularioPanel.add(lblDescripcion, gbc);

        textAreaDescripcion = new JTextArea(5, 20); // Se mantiene el nombre
        textAreaDescripcion.setLineWrap(true);
        textAreaDescripcion.setWrapStyleWord(true);
        textAreaDescripcion.setBackground(inputFieldBackgroundColor);
        textAreaDescripcion.setForeground(inputFieldTextColor);
        textAreaDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        textAreaDescripcion.setCaretColor(inputFieldTextColor);
        JScrollPane scrollDescripcion = new JScrollPane(textAreaDescripcion);
        scrollDescripcion.setPreferredSize(new Dimension(250, 100));
        gbc.gridx = 1; gbc.gridy = 2; // Ahora en la fila 2
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        formularioPanel.add(scrollDescripcion, gbc);
        
        db = new DBConnection(); // Conexión con base de datos (tu backend, sin cambios)
        
        // Botón de guardar (Fila 3)
        JButton btnGuardar = new JButton("Guardar Diagnóstico");
        btnGuardar.setBackground(accentColor);
        btnGuardar.setForeground(textColor);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 16));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor.darker(), 1),
                new EmptyBorder(10, 25, 10, 25)
        ));
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDiagnostico(); // Llamada al método de backend
            }

            private void guardarDiagnostico() {
                try {
                    String idPaciente = textFieldPaciente.getText().trim();
                    String fecha = textFieldFecha.getText().trim();
                    String descripcion = textAreaDescripcion.getText().trim();
                    
                    if (idPaciente.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!fecha.matches("\\d{2}-\\d{2}-\\d{4}")) {
                        JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto. Use DD-MM-YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    int dia = Integer.parseInt(fecha.substring(0, 2));
                    int mes = Integer.parseInt(fecha.substring(3, 5));
                    int anio = Integer.parseInt(fecha.substring(6, 10));
                    
                    LocalDate fechaTransformada = LocalDate.of(anio, mes, dia);

                    Diagnostico d = new Diagnostico(idPaciente, Sesion.getUsuarioLogueado(), descripcion, fechaTransformada);
                    if(db.agregarDiagnostico(d)) {
                        JOptionPane.showMessageDialog(null, "Diagnóstico guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        textFieldPaciente.setText("");
                        textFieldFecha.setText("");
                        textAreaDescripcion.setText("");
                      //Recarga la tabla de PanelVerHistorialMedico
                        recargarTablaHistorial();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al guardar el diagnóstico en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                } catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Error en el formato numérico de la fecha. Asegúrese de que sea DD-MM-YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch(DateTimeException e) {
                    JOptionPane.showMessageDialog(null, "La fecha introducida no es válida (ej. 31-02-2023 no existe).", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado al guardar el diagnóstico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 3; // Ahora en la fila 3
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(25, 5, 8, 5);
        formularioPanel.add(btnGuardar, gbc);
        
        add(formularioPanel, BorderLayout.CENTER);
    }
    
    public void recargarTablaHistorial() {

		Sesion.getModelo().setRowCount(0);
		ArrayList<Object[][]> resultados = db.mostrarHistorialPaciente(Sesion.getUsuarioLogueado());
	        
	    for (Object[][] filaArray : resultados) {
	    	Sesion.getModelo().addRow(filaArray[0]);
	    }

    
    }
}