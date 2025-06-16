package clases;
import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
	private int id;
	private String medicoDni;
	private String PacienteDni;
	private LocalDate dia; // yyyy-MM-dd
	private LocalTime horaInicio; // HH:mm
	private LocalTime horaFin; // HH:mm

	public Turno(int id, String medicoDni, String pacienteDni, LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
		this.id = id;
		this.medicoDni = medicoDni;
		PacienteDni = pacienteDni;
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}
	
	public Turno( String medicoDni, String pacienteDni, LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
		this.medicoDni = medicoDni;
		PacienteDni = pacienteDni;
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMedicoDni() {
		return medicoDni;
	}

	public void setMedicoDni(String medicoDni) {
		this.medicoDni = medicoDni;
	}

	public String getPacienteDni() {
		return PacienteDni;
	}

	public void setPacienteDni(String pacienteDni) {
		PacienteDni = pacienteDni;
	}

	public LocalDate getDia() {
		return dia;
	}

	public void setDia(LocalDate dia) {
		this.dia = dia;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

}