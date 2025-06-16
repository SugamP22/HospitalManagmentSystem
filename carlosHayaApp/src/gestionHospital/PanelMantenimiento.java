package gestionHospital;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder; // Needed for consistent padding

@SuppressWarnings("serial")
public class PanelMantenimiento extends JPanel {
	private final PanelImagen panelImagen;

	// Colores consistentes con la aplicación
	Color colorbg = Color.decode("#212f3d"); // Fondo general del panel principal y menú izquierdo
	Color colorButton = Color.decode("#006D77"); // Color de los botones del menú izquierdo
	Color panelAccentColor = Color.decode("#F4D35E"); // Color de acento para el título del panel (amarillo)
	Color displayPanelBg = Color.decode("#ECF0F1"); // Fondo del panel derecho donde se muestran los contenidos

	// Color rojo para el fondo de los títulos de los subpaneles "Salas" y "Habitaciones"
	private final Color subPanelTitleBgColor = Color.RED; // Defined a new color for the sub-panel titles

	private CardLayout cardLayout = new CardLayout();
	private JPanel infoPanel; // El panel derecho que contendrá los diferentes subpaneles

	// Declaración de los subpaneles específicos de Mantenimiento
	private PanelMantenimientoLista panelMantenimiento; // Now an instance of the new class
	private JPanel panelTerminarLimpieza; // Re-added

	public PanelMantenimiento(PanelImagen panelImagen) {
		this.panelImagen = panelImagen;
		setLayout(new BorderLayout()); // Layout principal del PanelMantenimiento
		setBackground(colorbg); // Fondo del PanelMantenimiento
		setPreferredSize(new Dimension(800, 600)); // Adjusted to a more standard size

		initComponents(); // Centralized construction into a single method
	}

	private void initComponents() {
		// --- Main wrapper panel for the content (similar to wrapperPanel in other panels) ---
		JPanel mainWrapperPanel = new JPanel(new BorderLayout());
		mainWrapperPanel.setBackground(colorbg); // Background of the main panel

		// --- Title Panel (Top - BorderLayout.NORTH) ---
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(colorbg); // Dark background #212f3d for the main title panel
		titlePanel.setBorder(new EmptyBorder(20, 20, 10, 20)); // Inner padding
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel titleLabel = new JLabel("PERSONAL DE MANTENIMIENTO", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 35)); // Larger font for the title
		titleLabel.setForeground(panelAccentColor); // Yellow accent color #F4D35E
		titleLabel.setPreferredSize(new Dimension(800, 100)); // Preferred height for the title
		titlePanel.add(titleLabel);

		// --- Central Content Panel (Left Menu + Right Display - BorderLayout.CENTER) ---
		JPanel contentPanel = new JPanel(new BorderLayout(10, 10)); // Spacing between menu and display
		contentPanel.setBackground(colorbg); // Dark background #212f3d
		contentPanel.setBorder(new EmptyBorder(0, 10, 10, 10)); // Padding for the content

		// Option panel (left sidebar menu - BorderLayout.WEST)
		// Changed back to 3 rows for 3 buttons
		JPanel optionPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		optionPanel.setBackground(colorbg); // Dark background #212f3d
		optionPanel.setPreferredSize(new Dimension(230, 550)); // Preferred width for buttons
		optionPanel.setBorder(new EmptyBorder(0, 20, 0, 0)); // 20px left padding

		// Content display panel (right - BorderLayout.CENTER of contentPanel)
		infoPanel = new JPanel(cardLayout);
		infoPanel.setBackground(displayPanelBg); // Light gray background #ECF0F1

		// Initialize the specific Maintenance sub-panels
		// Now instantiate PanelMantenimientoLista
		panelMantenimiento = new PanelMantenimientoLista(displayPanelBg, subPanelTitleBgColor);
		panelTerminarLimpieza = createPanelTerminarLimpieza(); // Re-added

		// Add the panels to the CardLayout with their new names
		infoPanel.add(panelMantenimiento, "Mantenimiento");
		infoPanel.add(panelTerminarLimpieza, "Terminar limpieza"); // Re-added

		// Updated button labels - now three buttons again
		String[] buttonLabels = { "Mantenimiento", "Terminar limpieza", "Cerrar Sesión" };

		for (String buttonLabel : buttonLabels) {
			JButton button = new JButton(buttonLabel);
			// Apply common styles
			stylePanelButton(button);
			button.setForeground(Color.white); // Default text color for all buttons

			if (buttonLabel.equals("Cerrar Sesión")) {
				button.setBackground(Color.decode("#C08080")); // Original color of the Logout button
				button.addActionListener(e -> {
					button.setBackground(Color.decode("#FF6347")); // Color when pressed
					panelImagen.cambiarPanel(new PanelLogin(panelImagen));
				});
			} else if (buttonLabel.equals("Mantenimiento")) {
				// The "Mantenimiento" button is disabled as it's the default view
				button.setEnabled(false);
				button.setBackground(colorButton); // Default color for disabled button
			} else if (buttonLabel.equals("Terminar limpieza")) {
				button.setBackground(Color.decode("#4CAF50")); // Green color for "Terminar limpieza"
				// Add action listener to call markSelectedRoomClean()
				button.addActionListener(e -> {
					panelMantenimiento.marcasLimpiezaSala();
				});
			}
			optionPanel.add(button);
		}

		// Assemble the contentPanel
		contentPanel.add(optionPanel, BorderLayout.WEST);
		contentPanel.add(infoPanel, BorderLayout.CENTER);

		// Assemble the mainWrapperPanel
		mainWrapperPanel.add(titlePanel, BorderLayout.NORTH);
		mainWrapperPanel.add(contentPanel, BorderLayout.CENTER);

		// Add the mainWrapperPanel to PanelMantenimiento
		this.add(mainWrapperPanel, BorderLayout.CENTER);

		// Show the "Mantenimiento" panel by default
		cardLayout.show(infoPanel, "Mantenimiento");

		revalidate();
		repaint();
	}

	private JPanel createPanelTerminarLimpieza() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(displayPanelBg); 

		
		JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titleWrapper.setBackground(subPanelTitleBgColor); 
		titleWrapper.setBorder(new EmptyBorder(15, 0, 15, 0)); 
		JLabel titulo = new JLabel("Terminar Limpieza", SwingConstants.CENTER); 
		titulo.setFont(new Font("Arial", Font.BOLD, 22));
		titulo.setForeground(Color.WHITE); 
		titleWrapper.add(titulo);

		panel.add(titleWrapper, BorderLayout.NORTH); 

		return panel;
	}

	private void stylePanelButton(JButton button) {
		button.setFont(new Font("Arial", Font.BOLD, 17));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), 
				new EmptyBorder(10, 20, 10, 20) 
		));
	}
}
