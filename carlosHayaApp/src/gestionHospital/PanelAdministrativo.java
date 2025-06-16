package gestionHospital;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class PanelAdministrativo extends JPanel {
  // Colores y estilos definidos, siguiendo el diseño del PanelAdmin original
  Color colorbg = Color.decode("#212f3d"); // Fondo general del panel principal y menú izquierdo
  Color colorButton = Color.decode("#006D77"); // Color de los botones del menú izquierdo
  private Border border = BorderFactory.createLineBorder(Color.black, 1); // Borde de los botones del menú

  @SuppressWarnings("unused")
  private final PanelImagen panelImagen; // Referencia al panel de imagen para cambiar de vista
  private JPanel panelMedio; // Panel que contiene el menú izquierdo y el área de visualización
  private JPanel panelMenu; // Panel para los botones del menú izquierdo
  private JPanel panelDisplay; // Panel donde se muestran las funcionalidades de gestión
  private JPanel panelTitulo; // Panel para el título superior

  // Paneles para cada funcionalidad específica del administrativo no
  // administrador
  private PanelConsultarDatosHospital panelConsultarDatosHospital;
  private PanelDarCitas panelDarCitas;

  public PanelAdministrativo(PanelImagen panelImagen) {
    this.panelImagen = panelImagen;
    this.setBackground(colorbg); // Establece el color de fondo del panel principal
    this.setPreferredSize(new Dimension(900, 600)); // Tamaño preferido del panel
    this.setLayout(new BorderLayout()); // Usa BorderLayout para organizar los subpaneles
    this.setBorder(new EmptyBorder(20, 20, 20, 20)); // Borde interior para espaciado

    panelTitle(); // Inicializa y añade el panel del título
    panelMenuYdisplay(); // Inicializa y organiza el menú y el área de visualización

    // Muestra el panel de consultar datos del hospital por defecto al iniciar
    mostrarPanel(panelConsultarDatosHospital);
  }

  /**
   * Configura el panel central que divide el menú izquierdo y el área de
   * visualización.
   */
  private void panelMenuYdisplay() {
    panelMedio = new JPanel(new BorderLayout()); // Panel intermedio con BorderLayout
    panelIzquierda(); // Configura el menú izquierdo
    panelDerecho(); // Configura el área de visualización derecha
    panelMedio.setBackground(colorbg); // Establece el color de fondo del panel medio
    this.add(panelMedio, BorderLayout.CENTER); // Añade el panel medio al centro del PanelAdministrativo
  }

  /**
   * Configura el panel del menú izquierdo con botones de navegación para las
   * funcionalidades del administrativo no administrador.
   */
  private void panelIzquierda() {
    String[] buttonLabels = { "Consultar Datos", "Dar Citas", "Cerrar Sesión" }; // Definición
                                                                                 // de las
                                                                                 // etiquetas
                                                                                 // de los
                                                                                 // botones

    // Se ha modificado el GridLayout para que el número de filas coincida con el
    // número de botones.
    panelMenu = new JPanel(new GridLayout(buttonLabels.length, 1, 45, 10)); // GridLayout con filas dinámicas
    panelMenu.setPreferredSize(new Dimension(200, 0)); // Define el ancho preferido, altura flexible
    panelMenu.setBackground(colorbg); // Color de fondo del menú
    panelMenu.setBorder(new EmptyBorder(0, 0, 0, 10)); // Borde derecho para espaciado
    panelMedio.add(panelMenu, BorderLayout.WEST); // Añade el menú a la izquierda del panel medio

    for (int i = 0; i < buttonLabels.length; i++) {
      JButton button = new JButton(buttonLabels[i]);
      stylePanelButton(button); // Aplica el estilo común a los botones del menú
      panelMenu.add(button);

      final String label = buttonLabels[i]; // Variable final para usar en el ActionListener

      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          // Cambia el panel visible en el área de visualización según el botón clicado
          if (label.equals("Consultar Datos")) {
            mostrarPanel(panelConsultarDatosHospital);
          } else if (label.equals("Dar Citas")) {
            mostrarPanel(panelDarCitas);
          } else if (label.equals("Cerrar Sesión")) {
            button.setBackground(Color.decode("#FF6347")); // Color especial para "Cerrar Sesión" al hacer
                                                           // clic
            panelImagen.cambiarPanel(new PanelLogin(panelImagen)); // Cambia al panel de login
          }
        }
      });

      // Establece un color fijo para el botón "Cerrar Sesión"
      if (buttonLabels[i].equals("Cerrar Sesión")) {
        button.setBackground(Color.decode("#C08080"));
        button.setForeground(Color.white);
      }
    }
  }

  /**
   * Configura el panel derecho donde se muestran las funcionalidades de gestión.
   */
  private void panelDerecho() {
    panelDisplay = new JPanel(new BorderLayout()); // Panel de visualización con BorderLayout
    panelDisplay.setBackground(Color.red); // Color de fondo del panel de visualización
    panelMedio.add(panelDisplay, BorderLayout.CENTER); // Añade el panel de visualización al centro del panel medio

    // Inicializa las instancias de los paneles de funcionalidad para el
    // administrativo
    panelConsultarDatosHospital = new PanelConsultarDatosHospital();
    panelDarCitas = new PanelDarCitas();
  }

  /**
   * Configura el panel del título superior.
   */
  private void panelTitle() {
    panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel de título con FlowLayout
    panelTitulo.setBackground(colorbg); // Color de fondo del título
    JLabel title = new JLabel("ADMINISTRATIVO"); // Texto del título
    title.setForeground(Color.decode("#F4D35E")); // Color del texto del título
    title.setFont(new Font("Roboto", Font.BOLD, 35)); // Fuente del título
    panelTitulo.add(title); // Añade el título al panel
    this.add(panelTitulo, BorderLayout.NORTH); // Añade el panel de título a la parte superior del
                                               // PanelAdministrativo
  }

  private void stylePanelButton(JButton button) {
    button.setBackground(colorButton); // Color de fondo del botón
    button.setForeground(Color.white); // Color de texto del botón
    button.setFont(new Font("Arial", Font.BOLD, 15)); // Fuente del botón
    button.setFocusPainted(false); // Deshabilita el pintado del foco
    button.setBorder(border); // Borde del botón
    button.setPreferredSize(new Dimension(180, Short.MAX_VALUE)); // Establece un ancho preferido pero permite que
                                                                  // la altura se ajuste
  }

  private void mostrarPanel(JPanel panel) {
    panelDisplay.removeAll(); // Elimina todos los componentes actuales del panelDisplay
    panelDisplay.add(panel, BorderLayout.CENTER); // Añade el nuevo panel al centro
    panelDisplay.revalidate(); // Revalida el contenedor para recalcular el layout
    panelDisplay.repaint(); // Repinta el contenedor para mostrar los cambios
  }
}
