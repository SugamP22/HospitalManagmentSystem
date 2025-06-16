package gestionHospital;

import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import clases.DBConnection;
import clases.Receta;

public class PanelRecetarMedicacion extends JPanel {

    private final Color primaryBackgroundColor = Color.decode("#2C3E50");
    private final Color accentColor = Color.decode("#3498DB");
    private final Color textColor = Color.WHITE; // Usado para etiquetas y el título
    private final Color inputFieldBackgroundColor = Color.WHITE;
    private final Color inputFieldTextColor = Color.BLACK;
    private final Color inputFieldBorderColor = Color.decode("#5D6D7E");

    private JTextField textFieldPaciente, textFieldMedicamento, textFieldFecha;
    
    private DBConnection db;

    public PanelRecetarMedicacion() {
        setLayout(new BorderLayout(10, 10));
        setBackground(primaryBackgroundColor);
        setPreferredSize(new Dimension(800, 600));
        initComponents();
        db = new DBConnection();
    }

    private void initComponents() {
        // Panel para el título
        JPanel titlePanel = new JPanel();
        // Establecer el color de fondo a rojo, como solicitaste.
        titlePanel.setBackground(Color.RED); 
        titlePanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titulo = new JLabel("Recetar Medicación");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        // Mantener el color de texto en blanco para que contraste bien con el rojo.
        titulo.setForeground(textColor); 
        titlePanel.add(titulo);
        add(titlePanel, BorderLayout.NORTH);

        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(primaryBackgroundColor);
        formularioPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblPaciente = new JLabel("ID Paciente:");
        lblPaciente.setForeground(textColor);
        lblPaciente.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        formularioPanel.add(lblPaciente, gbc);

        textFieldPaciente = new JTextField(20);
        textFieldPaciente.setBackground(inputFieldBackgroundColor);
        textFieldPaciente.setForeground(inputFieldTextColor);
        textFieldPaciente.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textFieldPaciente.setCaretColor(inputFieldTextColor);
        textFieldPaciente.setBorder(new LineBorder(inputFieldBorderColor, 1, true));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formularioPanel.add(textFieldPaciente, gbc);

        JLabel lblMedicamento = new JLabel("Medicamento:");
        lblMedicamento.setForeground(textColor);
        lblMedicamento.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        formularioPanel.add(lblMedicamento, gbc);

        textFieldMedicamento = new JTextField(20);
        textFieldMedicamento.setBackground(inputFieldBackgroundColor);
        textFieldMedicamento.setForeground(inputFieldTextColor);
        textFieldMedicamento.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textFieldMedicamento.setCaretColor(inputFieldTextColor);
        textFieldMedicamento.setBorder(new LineBorder(inputFieldBorderColor, 1, true));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formularioPanel.add(textFieldMedicamento, gbc);

        JLabel lblFecha = new JLabel("Fecha (DD-MM-YYYY):");
        lblFecha.setForeground(textColor);
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        formularioPanel.add(lblFecha, gbc);

        textFieldFecha = new JTextField(20);
        textFieldFecha.setBackground(inputFieldBackgroundColor);
        textFieldFecha.setForeground(inputFieldTextColor);
        textFieldFecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textFieldFecha.setCaretColor(inputFieldTextColor);
        textFieldFecha.setBorder(new LineBorder(inputFieldBorderColor, 1, true));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formularioPanel.add(textFieldFecha, gbc);
        
        JButton btnGuardar = new JButton("Guardar Receta");
        btnGuardar.setBackground(accentColor);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(accentColor.darker(), 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));

        btnGuardar.addActionListener(e -> guardarReceta());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(25, 5, 8, 5);
        formularioPanel.add(btnGuardar, gbc);

        add(formularioPanel, BorderLayout.CENTER);
    }

    private void guardarReceta() {
        try {
            String pacienteID = textFieldPaciente.getText().trim();
            String medicamento = textFieldMedicamento.getText().trim();
            String fecha = textFieldFecha.getText().trim();

            if (pacienteID.isEmpty() || medicamento.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                if (!fecha.matches("\\d{2}-\\d{2}-\\d{4}")) {
                    JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use DD-MM-YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int dia = Integer.parseInt(fecha.substring(0, 2));
                int mes = Integer.parseInt(fecha.substring(3, 5));
                int anio = Integer.parseInt(fecha.substring(6, 10));
                
                LocalDate fechaTransformada = LocalDate.of(anio, mes, dia);

                Receta receta = new Receta(pacienteID, Sesion.getUsuarioLogueado(), medicamento, fechaTransformada);
                if(db.registrarReceta(receta)) {
                    JOptionPane.showMessageDialog(this, "Receta guardada exitosamente.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    textFieldPaciente.setText("");
                    textFieldMedicamento.setText("");
                    textFieldFecha.setText("");
                  //Recarga la tabla de PanelVerHistorialMedico
                    recargarTablaHistorial();
                }else {
                    JOptionPane.showMessageDialog(this, "Error al guardar la receta en la base de datos.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en el formato numérico de la fecha. Asegúrese de que sea DD-MM-YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeException e) {
            JOptionPane.showMessageDialog(this, "La fecha introducida no es válida (ej. 31-02-2023 no existe).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al guardar la receta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

	public void recargarTablaHistorial() {

		Sesion.getModelo().setRowCount(0);
		ArrayList<Object[][]> resultados = db.mostrarHistorialPaciente(Sesion.getUsuarioLogueado());
	        
	    for (Object[][] filaArray : resultados) {
	    	Sesion.getModelo().addRow(filaArray[0]);
	    }

    
    }
}