package clases;

import java.time.LocalDate;

public class Diagnostico {
	private String pacienteId;
	private String medicoId;
	private String descripcion;
	private LocalDate fecha; // yyyy-MM-dd

	public Diagnostico( String pacienteId, String medicoId, String descripcion, LocalDate fecha) {
		this.pacienteId = pacienteId;
		this.medicoId = medicoId;
		this.descripcion = descripcion;
		this.fecha = fecha;
	}

	public String getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(String pacienteId) {
		this.pacienteId = pacienteId;
	}

	public String getMedicoId() {
		return medicoId;
	}

	public void setMedicoId(String medicoId) {
		this.medicoId = medicoId;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

}