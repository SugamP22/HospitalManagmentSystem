package gestionHospital;

import javax.swing.table.DefaultTableModel;

public class Sesion {
	public static String usuarioLogueado;
	public static DefaultTableModel modelo;

	public static String getUsuarioLogueado() {
		return usuarioLogueado;
	}

	public static void setUsuarioLogueado(String usuarioLogueado) {
		Sesion.usuarioLogueado = usuarioLogueado;
	}

	public static DefaultTableModel getModelo() {
		return modelo;
	}

	public static void setModelo(DefaultTableModel modelo) {
		Sesion.modelo = modelo;
	} 
	
}
