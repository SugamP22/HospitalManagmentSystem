package clases;

public class Paciente extends Usuario {
	private String contacto;
	private String obraSocial;
	private boolean alta;
	private int salaID;

	public Paciente(String nombre, String apellido, String dni, String rol, String contacto, String obraSocial,
			boolean alta, int salaID) {
		super(nombre, apellido, dni, rol);
		this.contacto = contacto;
		this.obraSocial = obraSocial;
		this.alta = alta;
		this.salaID = salaID;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getObraSocial() {
		return obraSocial;
	}

	public void setObraSocial(String obraSocial) {
		this.obraSocial = obraSocial;
	}

	public boolean isAlta() {
		return alta;
	}

	public void setAlta(boolean alta) {
		this.alta = alta;
	}

	public int getSalaID() {
		return salaID;
	}

	public void setSalaID(int salaID) {
		this.salaID = salaID;
	}

}