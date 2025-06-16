package clases;

public class Sala {
	private int id;
	private String tipo; // habitacion, quirófano, consultorio
	private boolean disponibilidad;

	public Sala(int id, String tipo, boolean disponibilidad) {
		this.id = id;
		this.tipo = tipo;
		this.disponibilidad = disponibilidad;
	}

	public boolean isDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(boolean disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
