package clases;

import java.time.LocalDate;

public class Receta {
	private String pacienteId;
	private String medicoId;
	private String medicamentos; // lista como texto simple (ej: "Paracetamol, Ibuprofeno")
	private LocalDate fecha; 

	public Receta(String pacienteId, String medicoId, String medicamentos, LocalDate fecha) {
		this.pacienteId = pacienteId;
		this.medicoId = medicoId;
		this.medicamentos = medicamentos;
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

	public String getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(String medicamentos) {
		this.medicamentos = medicamentos;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

}