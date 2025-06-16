package gestionHospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class PanelMedico extends JPanel {
  Color colorbg = Color.decode("#212f3d");
  Color colorButton = Color.decode("#006D77");
  private Border border = BorderFactory.createLineBorder(Color.black, 1);

  private final PanelImagen panelImagen;
  private JPanel panelMedio;
  private JPanel panelMenu;
  private JPanel panelDisplay;
  private JPanel panelTitulo;

  private PanelVerPacientesAsignados panelVerPacientesAsignados;
  private PanelVerHistorialMedico panelVerHistorialMedico;
  private PanelRegistrarDiagnostico panelRegistrarDiagnostico;
  private PanelRecetarMedicacion panelRecetarMedicacion;
  private PanelVerCitasMedicas panelVerCitasMedicas;

  public PanelMedico(PanelImagen panelImagen) {
    this.panelImagen = panelImagen;
    this.setBackground(colorbg);
    this.setPreferredSize(new Dimension(1000, 600));
    this.setLayout(new BorderLayout());
    this.setBorder(new EmptyBorder(20, 20, 20, 20));

    panelTitle();
    panelMenuYdisplay();

    mostrarPanel(panelVerPacientesAsignados);
  }

  private void panelMenuYdisplay() {
    panelMedio = new JPanel(new BorderLayout());
    panelIzquierda();
    panelDerecha();
    panelMedio.setBackground(colorbg);
    this.add(panelMedio, BorderLayout.CENTER);
  }

  private void panelIzquierda() {
    panelMenu = new JPanel(new GridLayout(6, 1, 45, 10));
    panelMenu.setPreferredSize(new Dimension(200, 500));
    panelMenu.setBackground(colorbg);
    panelMenu.setBorder(new EmptyBorder(0, 0, 0, 10));
    panelMedio.add(panelMenu, BorderLayout.WEST);

    String[] buttonLabels = {
        "Ver pacientes asignados",
        "Historial medico",
        "Registrar diagnostico",
        "Recetar medicacion",
        "Ver citas médicas",
        "Cerrar Sesión"
    };

    for (int i = 0; i < buttonLabels.length; i++) {
      JButton button = new JButton(buttonLabels[i]);
      stylePanelButton(button);
      panelMenu.add(button);

      final String label = buttonLabels[i];

      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (label.equals("Ver pacientes asignados")) {
            mostrarPanel(panelVerPacientesAsignados);
          } else if (label.equals("Historial medico")) {
            mostrarPanel(panelVerHistorialMedico);
          } else if (label.equals("Registrar diagnostico")) {
            mostrarPanel(panelRegistrarDiagnostico);
          } else if (label.equals("Recetar medicacion")) {
            mostrarPanel(panelRecetarMedicacion);
          } else if (label.equals("Ver citas médicas")) {
            mostrarPanel(panelVerCitasMedicas);
          } else if (label.equals("Cerrar Sesión")) {
            button.setBackground(Color.decode("#FF6347"));
            panelImagen.cambiarPanel(new PanelLogin(panelImagen));
          }
        }
      });

      if (buttonLabels[i].equals("Cerrar Sesión")) {
        button.setBackground(Color.decode("#C08080"));
        button.setForeground(Color.white);
      }
    }
  }

  private void panelDerecha() {
    panelDisplay = new JPanel(new BorderLayout());
    panelDisplay.setBackground(Color.decode("#ECF0F1"));
    panelMedio.add(panelDisplay, BorderLayout.CENTER);

    panelVerPacientesAsignados = new PanelVerPacientesAsignados();
    panelVerHistorialMedico = new PanelVerHistorialMedico();
    panelRegistrarDiagnostico = new PanelRegistrarDiagnostico();
    panelRecetarMedicacion = new PanelRecetarMedicacion();
    panelVerCitasMedicas = new PanelVerCitasMedicas();
  }

  private void panelTitle() {
    panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
    panelTitulo.setBackground(colorbg);
    JLabel title = new JLabel("MEDICO");
    title.setForeground(Color.decode("#F4D35E"));
    title.setFont(new Font("Roboto", Font.BOLD, 35));
    panelTitulo.add(title);
    this.add(panelTitulo, BorderLayout.NORTH);
  }

  private void stylePanelButton(JButton button) {
    button.setBackground(colorButton);
    button.setForeground(Color.white);
    button.setFont(new Font("Arial", Font.BOLD, 15));
    button.setFocusPainted(false);
    button.setBorder(border);
    button.setPreferredSize(new Dimension(180, 40));
  }

  private void mostrarPanel(JPanel panel) {
    panelDisplay.removeAll();
    panelDisplay.add(panel, BorderLayout.CENTER);
    panelDisplay.revalidate();
    panelDisplay.repaint();
  }
}