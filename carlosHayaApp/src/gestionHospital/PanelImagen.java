package gestionHospital;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public final class PanelImagen extends JPanel {
	private Image img;
	private final App app;

	public PanelImagen(App app) {
		this.app = app;
		this.setSize(app.getSize());
		this.setLayout(new GridBagLayout());
		ponerImagen();
		PanelLogin loginPanel = new PanelLogin(this);
		posicion(loginPanel); // Añadimos el PanelLogin al PanelImagen
	}

	// IMP: setSize se usan cuando no se especifica un layout pero si estas dando un
	// layout siempre usar un setPreferredSize!!
	// Añadir el panel al centro de la pantalla
	public void posicion(JPanel panel) {
		GridBagConstraints gbc = new GridBagConstraints();
		add(panel, gbc);
	}

	// Cambiar entre paneles
	public void cambiarPanel(JPanel nuevoPanel) {
		this.removeAll(); // Eliminar el contenido anterior
		this.revalidate();
		this.repaint();
		posicion(nuevoPanel);// Asegurarse de que se vuelve a dibujar el panel
		this.revalidate();
		this.repaint();
	}

	// Método para poner la imagen de fondo
	private void ponerImagen() {
		img = new ImageIcon("Imagen/Hospital_Carlos_Haya.jpg").getImage();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // Llama al método de la clase padre para limpiar la pantalla
		if (img != null) {
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // Dibuja la imagen de fondo
		}
	}
}