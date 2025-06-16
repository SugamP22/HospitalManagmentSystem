package gestionHospital;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class PanelEnfermero extends JPanel {
    private final PanelImagen panelImagen;

    // Colores consistentes con la aplicación (replicados del PanelMedico/Admin)
    Color colorbg = Color.decode("#212f3d"); // Fondo general del panel principal y menú izquierdo
    Color colorButton = Color.decode("#006D77"); // Color de los botones del menú izquierdo
    Color panelAccentColor = Color.decode("#F4D35E"); // Color de acento para el título del panel (amarillo)
    Color displayPanelBg = Color.decode("#ECF0F1"); // Fondo del panel derecho donde se muestran los contenidos

    private CardLayout cardLayout = new CardLayout();
    private JPanel infoPanel; // El panel derecho que contendrá los diferentes paneles del enfermero

    // Declaración de los paneles internos específicos del enfermero
    private PanelAsignarCama panelAsignarCama;
    private PanelDarAlta panelDarAlta;
   

    public PanelEnfermero(PanelImagen panelImagen) {
        this.panelImagen = panelImagen;
        setLayout(new BorderLayout()); // Layout principal del PanelEnfermero
        setBackground(colorbg); // Fondo del PanelEnfermero
        setPreferredSize(new Dimension(1000, 600)); // Tamaño preferido
        initComponents();
    }

    private void initComponents() {
        // Panel envolvente principal para el contenido (similar a mainWrapperPanel en PanelMedico)
        JPanel mainWrapperPanel = new JPanel(new BorderLayout());
        mainWrapperPanel.setBackground(colorbg); // Fondo del panel principal

        // --- Panel del Título (Parte superior) ---
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(colorbg); // Fondo oscuro #212f3d
        titlePanel.setBorder(new EmptyBorder(20, 20, 10, 20)); // Padding interno
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("ENFERMERO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        titleLabel.setForeground(panelAccentColor); // Color amarillo #F4D35E
        titleLabel.setPreferredSize(new Dimension(800, 100)); // Altura preferida para el título
        titlePanel.add(titleLabel);

        // --- Panel de Contenido Central (Menú Izquierdo + Display Derecho) ---
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10)); // Espaciado entre menú y display
        contentPanel.setBackground(colorbg); // Fondo oscuro #212f3d
        contentPanel.setBorder(new EmptyBorder(0, 10, 10, 10)); // Padding para el contenido

        // Panel de opciones (menú lateral izquierdo)
        JPanel optionPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // 4 filas (para 4 botones), 1 columna, espaciado de 10px
        optionPanel.setBackground(colorbg); // Fondo oscuro #212f3d
        optionPanel.setPreferredSize(new Dimension(230, 550)); // Ancho preferido para los botones
        optionPanel.setBorder(new EmptyBorder(0, 20, 0, 0)); // 20px de padding a la izquierda

        // Panel de visualización de contenido (derecha)
        infoPanel = new JPanel(cardLayout);
        infoPanel.setBackground(displayPanelBg); // Fondo gris muy claro #ECF0F1

        // Inicializar los paneles específicos del enfermero
        panelAsignarCama = new PanelAsignarCama();
        panelDarAlta = new PanelDarAlta();

        // Añadir los paneles al CardLayout
        infoPanel.add(panelAsignarCama, "AsignarCama");
        infoPanel.add(panelDarAlta, "DarAlta");

        String[] buttonLabels = {
            "Asignar Cama",
            "Dar Alta",
            "Cerrar Sesión"
        };

        for (String buttonLabel : buttonLabels) {
            JButton button = new JButton(buttonLabel);
            // Reutiliza el método de estilo, el último parámetro indica si es el botón de cerrar sesión
            stylePanelButton(button, buttonLabel.equals("Cerrar Sesión")); 
            
            if (buttonLabel.equals("Cerrar Sesión")) {
                button.addActionListener(e -> {
                    button.setBackground(Color.decode("#FF6347")); // Color al presionar (rojo anaranjado)
                    panelImagen.cambiarPanel(new PanelLogin(panelImagen));
                });
            } else {
                button.addActionListener(e -> switchPanel(buttonLabel)); // Cambia el panel
            }
            optionPanel.add(button);
        }

        // Ensamblar el contentPanel
        contentPanel.add(optionPanel, BorderLayout.WEST);
        contentPanel.add(infoPanel, BorderLayout.CENTER);

        // Ensamblar el mainWrapperPanel
        mainWrapperPanel.add(titlePanel, BorderLayout.NORTH);
        mainWrapperPanel.add(contentPanel, BorderLayout.CENTER);

        // Añadir el mainWrapperPanel al PanelEnfermero
        this.add(mainWrapperPanel, BorderLayout.CENTER);

        // Asegúrate de que el primer panel se muestre al inicio
        cardLayout.show(infoPanel, "AsignarCama");
        
        revalidate();
        repaint();
    }

    private void switchPanel(String buttonLabel) {
        String panelName = "";
        switch (buttonLabel) {
            case "Asignar Cama":
                panelName = "AsignarCama";
                break;
            case "Dar Alta":
                panelName = "DarAlta";
                break;
            default:
                // Manejar un caso por defecto o un error si el nombre no coincide
                panelName = "DefaultPanel"; 
                break;
        }
        cardLayout.show(infoPanel, panelName);
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    // Método de estilo para botones, unificado y consistente
    private void stylePanelButton(JButton button, boolean isLogoutButton) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            new EmptyBorder(10, 20, 10, 20) // Padding interno
        ));

        if (isLogoutButton) {
            button.setBackground(Color.decode("#C08080")); // Color para "Cerrar Sesión"
            button.setForeground(Color.white);
        } else {
            button.setBackground(colorButton); // Color para botones normales
            button.setForeground(Color.white);
        }
    }
}