package gestionHospital;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class App extends JFrame {

    public App() {
        propiedades();
        crearImagenPanel();
    }

    // Crear el panel de imagen y pasar la misma clase como parámetro
    private void crearImagenPanel() {
        PanelImagen panelImagen = new PanelImagen(this);
        add(panelImagen);
    }

    // Establecer propiedades del frame
    private void propiedades() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}