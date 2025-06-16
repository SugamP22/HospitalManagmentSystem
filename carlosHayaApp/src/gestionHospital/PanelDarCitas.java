package gestionHospital;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import clases.DBConnection;
import clases.Turno;

@SuppressWarnings("serial")
class PanelDarCitas extends JPanel {
	
	private DBConnection db;

  // Campos del formulario declarados aquí para poder acceder a ellos
  private JTextField dniMedicoField;
  private JTextField dniPacienteField;
  private JTextField fechaField;
  private JTextField horaInicioField;
  private JTextField horaFinField;

  public PanelDarCitas() {
    setLayout(new BorderLayout()); // Eliminado el espaciado horizontal y vertical para el panel principal
    setBackground(Color.decode("#212f3d")); // Color de fondo general del panel
    setBorder(new EmptyBorder(15, 15, 15, 15)); // Espaciado interno del panel principal

    // 1. Panel para el título (panelLabel)
    JPanel panelLabel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    panelLabel.setBackground(Color.decode("#E3242B")); // Color de fondo rojo para el título
    JLabel formTitle = new JLabel("Introducir Datos de la Cita", SwingConstants.CENTER);
    formTitle.setFont(new Font("Roboto", Font.BOLD, 25));
    formTitle.setForeground(Color.white); // Color del texto del título
    panelLabel.add(formTitle);
    panelLabel.setPreferredSize(new Dimension(panelLabel.getPreferredSize().width, 60)); // Altura de 60px
    add(panelLabel, BorderLayout.NORTH); // Añadir el panel del título en la parte superior

    // Panel para los campos de entrada del formulario (fieldsPanel)
    JPanel fieldsPanel = new JPanel(new GridBagLayout());
    fieldsPanel.setBackground(Color.white); // CAMBIO AQUÍ: Color de fondo blanco para el panel de campos
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8); // Espaciado entre componentes
    gbc.fill = GridBagConstraints.HORIZONTAL; // Los componentes se expanden horizontalmente
    gbc.weightx = 1.0; // Permitir que los campos de texto se expandan

    // Campo para el DNI del Médico
    gbc.gridx = 0;
    gbc.gridy = 0;
    fieldsPanel.add(new JLabel("DNI del Médico:"), gbc);
    dniMedicoField = new JTextField(20);
    gbc.gridx = 1;
    fieldsPanel.add(dniMedicoField, gbc);

    // Campo para el DNI del Paciente
    gbc.gridx = 0;
    gbc.gridy = 1;
    fieldsPanel.add(new JLabel("DNI del Paciente:"), gbc);
    dniPacienteField = new JTextField(20);
    gbc.gridx = 1;
    fieldsPanel.add(dniPacienteField, gbc);

    // Campo para la Fecha
    gbc.gridx = 0;
    gbc.gridy = 2;
    fieldsPanel.add(new JLabel("Fecha (DD-MM-YYYY):"), gbc);
    fechaField = new JTextField(20);
    gbc.gridx = 1;
    fieldsPanel.add(fechaField, gbc);

    // Campo para la Hora de Inicio
    gbc.gridx = 0;
    gbc.gridy = 3;
    fieldsPanel.add(new JLabel("Hora de Inicio (HH:MM):"), gbc);
    horaInicioField = new JTextField(20);
    gbc.gridx = 1;
    fieldsPanel.add(horaInicioField, gbc);

    // Campo para la Hora de Fin
    gbc.gridx = 0;
    gbc.gridy = 4;
    fieldsPanel.add(new JLabel("Hora de Fin (HH:MM):"), gbc);
    horaFinField = new JTextField(20);
    gbc.gridx = 1;
    fieldsPanel.add(horaFinField, gbc);

    add(fieldsPanel, BorderLayout.CENTER); // Añadir el panel de campos al centro

    // Panel para los botones Guardar y Cancelar (buttonPanel)
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    buttonPanel.setBackground(Color.decode("#728C69")); // Color de fondo verde para el panel de botones

    db = new DBConnection();
    
    // Botón Guardar Cita
    JButton saveButton = new JButton("Guardar Cita");
    styleButton(saveButton, Color.decode("#006D77"));
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
    	  try {
        String dniMedico = dniMedicoField.getText();
        String dniPaciente = dniPacienteField.getText();
        String fecha = fechaField.getText();
        String horaInicio = horaInicioField.getText();
        String horaFin = horaFinField.getText();

        if (dniMedico.isEmpty() || dniPaciente.isEmpty() || fecha.isEmpty() || horaInicio.isEmpty()
            || horaFin.isEmpty()) {
          JOptionPane.showMessageDialog(PanelDarCitas.this, "Por favor, complete todos los campos.", "Campos Vacíos",
              JOptionPane.WARNING_MESSAGE);
        }else if(!db.verificarRol(dniMedico).equals("medico")) {
        	JOptionPane.showMessageDialog(PanelDarCitas.this,"El dni no es de un médico", "Error",JOptionPane.ERROR_MESSAGE);
        } else { 
          LocalDate fechaTransformada = transformarFecha(fecha);
          LocalTime horaInicioTransformada = transformarHora(horaInicio);
          LocalTime horaFinTransformada = transformarHora(horaFin);
          
          Turno cita = new Turno(dniMedico,dniPaciente,fechaTransformada,horaInicioTransformada,horaFinTransformada);
          db.asignarTurno(cita);
          JOptionPane.showMessageDialog(PanelDarCitas.this,"Se ha guardado la cita existosamente", "Cita guardada",JOptionPane.INFORMATION_MESSAGE);
          clearFields(); // Limpiar campos después de guardar
        }
        }catch(DateTimeException r) {
        	JOptionPane.showMessageDialog(PanelDarCitas.this,"Introduzca el formato de la fecha y de las horas correctas", "Error",JOptionPane.ERROR_MESSAGE);
        }
      }
      
    });
    buttonPanel.add(saveButton);

    // Botón Borrar
    JButton cancelButton = new JButton("Borrar");
    styleButton(cancelButton, Color.decode("#C08080"));
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(PanelDarCitas.this, "Creación de cita borrado.", "Cita Cancelada",
            JOptionPane.INFORMATION_MESSAGE);
        clearFields(); // Limpiar campos al cancelar
      }
    });
    buttonPanel.add(cancelButton);

    buttonPanel.setPreferredSize(new Dimension(buttonPanel.getPreferredSize().width, 80)); // Altura de 80px
    add(buttonPanel, BorderLayout.SOUTH); // Añadir el panel de botones a la parte inferior
  }

  /**
   * Limpia los campos del formulario.
   */
  private void clearFields() {
    dniMedicoField.setText("");
    dniPacienteField.setText("");
    fechaField.setText("");
    horaInicioField.setText("");
    horaFinField.setText("");
  }

  public LocalTime transformarHora(String horas) {
		horas+=":00";
		int hora = Integer.parseInt(horas.substring(0, horas.indexOf(":")));
		int min = Integer.parseInt(horas.substring(horas.indexOf(":")+1,horas.lastIndexOf(":")));
		int seg = Integer.parseInt(horas.substring(horas.lastIndexOf(":")+1));
		return LocalTime.of(hora, min, seg);
	}

	public LocalDate transformarFecha(String fecha) {
		int dia = Integer.parseInt(fecha.substring(0, fecha.indexOf("-")));
      int mes = Integer.parseInt(fecha.substring(fecha.indexOf("-") + 1, fecha.lastIndexOf("-")));
      int anio = Integer.parseInt(fecha.substring(fecha.lastIndexOf("-") + 1));
		return LocalDate.of(anio, mes, dia);
		
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