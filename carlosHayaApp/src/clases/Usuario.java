package clases;

public class Usuario {
	protected String nombre;
	protected String apellido;
	protected String dni;
	protected String rol;

	public Usuario( String nombre, String apellido, String dni, String rol) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.rol = rol;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

}